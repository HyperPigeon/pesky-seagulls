package net.hyper_pigeon.pesky_seagulls.entities.ai.behaviors;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.FuzzyPositions;
import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.NavigationConditions;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SwoopInOnWalkTarget<E extends PathAwareEntity> extends ExtendedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryModuleState>> MEMORY_REQUIREMENTS = ObjectArrayList.of(Pair.of(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, MemoryModuleState.VALUE_PRESENT),Pair.of(MemoryModuleType.WALK_TARGET, MemoryModuleState.VALUE_ABSENT));
    protected float speedModifier = 1;
    private PlayerEntity targetPlayer;
    private Path flyPath = null;

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryModuleState>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    public boolean hasFood(E entity) {
        return !entity.getMainHandStack().isEmpty() && entity.getMainHandStack().getItem().isFood();
    }

    public static boolean isSkyVisible(ServerWorld world, LivingEntity entity, BlockPos pos) {
        return world.isSkyVisible(pos) && (double)world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, pos).getY() <= entity.getY();
    }

    @Override
    protected boolean shouldRun(ServerWorld level, E entity) {
        PlayerEntity player = BrainUtils.getMemory(entity, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER);
        return !hasFood(entity)
                && player != null
                && (player.getMainHandStack().isFood() || player.getOffHandStack().isFood())
                && (player.getY() + 3 > entity.getY())
                && (entity.squaredDistanceTo(player) >= 12)
                && (isSkyVisible(level, entity, entity.getBlockPos()))
                && (isSkyVisible(level, player, player.getBlockPos()));
    }

    @Override
    protected void start(E entity) {
        PlayerEntity player = BrainUtils.getMemory(entity, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER); // targetable so won't steal in creative mode
        if (player == null) {
            return;
        }
        this.targetPlayer = player;

        Vec3d swoopPos = new Vec3d(MathHelper.lerp(0.10 + entity.getRandom().nextDouble() * (0.4),entity.getX(), this.targetPlayer.getX()),
                this.targetPlayer.getY()+3,
                MathHelper.lerp(0.10 + entity.getRandom().nextDouble() * (0.4),entity.getZ(), this.targetPlayer.getZ()));

        this.flyPath = entity.getNavigation().findPathTo(swoopPos.x, swoopPos.y, swoopPos.z, 0);

        entity.getNavigation().startMovingAlong(this.flyPath, this.speedModifier);

    }


    @Override
    protected boolean shouldKeepRunning(E entity) {
        return this.flyPath != null && !this.flyPath.isFinished() && !entity.getNavigation().isIdle();
    }

    @Override
    protected void stop(E entity) {
        this.flyPath = null;
        entity.getNavigation().setSpeed(1);
    }


    @Nullable
    public static Vec3d find(
            PathAwareEntity entity, int horizontalRange, int heightIncrease, double x, double z, float angle, int maxAboveSolid, int minAboveSolid
    ) {
        boolean bl = NavigationConditions.isPositionTargetInRange(entity, horizontalRange);
        return FuzzyPositions.guessBestPathTarget(
                entity,
                () -> {
                    BlockPos blockPos = SwoopInOnWalkTarget.localFuzz(entity.getRandom(), horizontalRange, heightIncrease, 0, x, z, (double)angle);
                    if (blockPos == null) {
                        return null;
                    } else {
                        BlockPos blockPos2 = FuzzyTargeting.towardTarget(entity, horizontalRange, bl, blockPos);
                        if (blockPos2 == null) {
                            return null;
                        } else {
                            blockPos2 = FuzzyPositions.upWhile(
                                    blockPos2,
                                    entity.getRandom().nextInt(maxAboveSolid - minAboveSolid + 1) + minAboveSolid,
                                    entity.getWorld().getTopY(),
                                    pos -> NavigationConditions.isSolidAt(entity, pos) || NavigationConditions.isWaterAt(entity, pos)
                            );
                            return  !NavigationConditions.hasPathfindingPenalty(entity, blockPos2)
                                    ? blockPos2
                                    : null;
                        }
                    }
                }
        );
    }


    @Nullable
    public static BlockPos localFuzz(Random random, int horizontalRange, int goalHeight, int startHeight, double directionX, double directionZ, double angleRange) {
        double d = MathHelper.atan2(directionZ, directionX) - 1.5707963705062866;
        double e = d + (double)(2.0f * random.nextFloat() - 1.0f) * angleRange;
        double f = Math.sqrt(random.nextDouble()) * (double)MathHelper.SQUARE_ROOT_OF_TWO * (double)horizontalRange;
        double g = -f * Math.sin(e);
        double h = f * Math.cos(e);
        if (Math.abs(g) > (double)horizontalRange || Math.abs(h) > (double)horizontalRange) {
            return null;
        }
        int i = goalHeight + startHeight;
        return BlockPos.ofFloored(g, i, h);
    }





}
