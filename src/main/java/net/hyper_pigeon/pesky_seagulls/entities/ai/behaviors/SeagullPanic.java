package net.hyper_pigeon.pesky_seagulls.entities.ai.behaviors;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.FuzzyPositions;
import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.NavigationConditions;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Panic;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

public class SeagullPanic<E extends PathAwareEntity> extends Panic<E> {

    @Override
    protected boolean shouldRun(ServerWorld level, E entity) {
        if (!this.shouldPanicPredicate.test(entity, BrainUtils.getMemory(entity, MemoryModuleType.HURT_BY))) {
            return false;
        }

        setPanicTarget(entity);

        return this.targetPos != null;
    }

    protected void setPanicTarget(E entity) {
        if (entity.isOnFire()) {
            this.targetPos = findNearbyWater(entity);
        }

        if (this.targetPos == null) {
            final DamageSource lastDamage = BrainUtils.getMemory(entity, MemoryModuleType.HURT_BY);

            if (lastDamage != null && lastDamage.getAttacker() instanceof LivingEntity attacker)
                this.targetPos = SeagullPanic.findFrom(entity, attacker.getPos(), (int)this.radius.xzRadius(), (int)this.radius.yRadius(), 8, 0);

            if (this.targetPos == null)
                this.targetPos = NoPenaltyTargeting.find(entity, (int)this.radius.xzRadius(), (int)this.radius.yRadius());
        }
    }


    @Nullable
    protected Vec3d findNearbyWater(E entity) {
        final BlockPos pos = entity.getBlockPos();
        final World level = entity.getWorld();

        return !level.getBlockState(pos).getCollisionShape(level, pos).isEmpty() ? null : BlockPos.findClosest(entity.getBlockPos(), (int)this.radius.xzRadius(), (int)this.radius.yRadius(), checkPos -> level.getFluidState(checkPos).isIn(FluidTags.WATER)).map(Vec3d::ofBottomCenter).orElse(null);
    }

    @Nullable
    public static Vec3d findFrom(
            PathAwareEntity entity, Vec3d start, int horizontalRange, int verticalRange, int maxAboveSolid, int minAboveSolid
    ) {
        Vec3d vec3d = entity.getPos().subtract(start);
        boolean bl = NavigationConditions.isPositionTargetInRange(entity, horizontalRange);
        return FuzzyPositions.guessBestPathTarget(
                entity,
                () -> {
                    BlockPos blockPos = FuzzyPositions.localFuzz(entity.getRandom(), horizontalRange, verticalRange, 0, vec3d.x, vec3d.z, (double) MathHelper.HALF_PI);
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
                                    pos -> NavigationConditions.isSolidAt(entity, pos) || NavigationConditions.isWaterAt(entity,pos)
                            );
                            return  !NavigationConditions.hasPathfindingPenalty(entity, blockPos2)
                                    ? blockPos2
                                    : null;
                        }
                    }
                }
        );
    }
}
