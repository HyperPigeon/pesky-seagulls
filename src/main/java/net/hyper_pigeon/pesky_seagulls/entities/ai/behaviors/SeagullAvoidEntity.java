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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class SeagullAvoidEntity<E extends PathAwareEntity> extends ExtendedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryModuleState>> MEMORY_REQUIREMENTS = ObjectArrayList.of(Pair.of(MemoryModuleType.VISIBLE_MOBS, MemoryModuleState.VALUE_PRESENT));

    protected Predicate<LivingEntity> avoidingPredicate = target -> false;
    protected float noCloserThanSqr = 9f;
    protected float stopAvoidingAfterSqr = 49f;
    protected float speedModifier = 1;

    private Path runPath = null;

    public SeagullAvoidEntity() {
        noTimeout();
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryModuleState>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    /**
     * Set the minimum distance the target entity should be allowed to come before the entity starts retreating.
     * @param blocks The distance, in blocks
     * @return this
     */
    public SeagullAvoidEntity<E> noCloserThan(float blocks) {
        this.noCloserThanSqr = blocks * blocks;
        return this;
    }

    /**
     * Set the maximum distance the target entity should be before the entity stops retreating.
     * @param blocks The distance, in blocks
     * @return this
     */
    public SeagullAvoidEntity<E> stopCaringAfter(float blocks) {
        this.stopAvoidingAfterSqr = blocks * blocks;

        return this;
    }

    /**
     * Sets the predicate for entities to avoid.
     * @param predicate The predicate
     * @return this
     */
    public SeagullAvoidEntity<E> avoiding(Predicate<LivingEntity> predicate) {
        this.avoidingPredicate = predicate;

        return this;
    }

    /**
     * Set the movespeed modifier for when the entity is running away.
     * @param mod The speed multiplier modifier
     * @return this
     */
    public SeagullAvoidEntity<E> speedModifier(float mod) {
        this.speedModifier = mod;

        return this;
    }

    @Override
    protected boolean shouldRun(ServerWorld level, E entity) {
        Optional<LivingEntity> target = BrainUtils.getMemory(entity, MemoryModuleType.VISIBLE_MOBS).findFirst(this.avoidingPredicate);

        if (target.isEmpty())
            return false;

        LivingEntity avoidingEntity = target.get();
        double distToTarget = avoidingEntity.squaredDistanceTo(entity);

        if (distToTarget > this.noCloserThanSqr) {
            return false;
        }

        Vec3d runPos = SeagullAvoidEntity.findFrom(entity, avoidingEntity.getPos(), 16, 7,5,0);

        if (runPos == null || avoidingEntity.squaredDistanceTo(runPos.x, runPos.y, runPos.z) < distToTarget) {
            return false;
        }

        this.runPath = entity.getNavigation().findPathTo(runPos.x, runPos.y, runPos.z, 0);

        return this.runPath != null;
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


    @Override
    protected boolean shouldKeepRunning(E entity) {
        return !this.runPath.isFinished();
    }

    @Override
    protected void start(E entity) {
        System.out.println("Fleeing players...");
        entity.getNavigation().startMovingAlong(this.runPath, this.speedModifier);
    }

    @Override
    protected void stop(E entity) {
        this.runPath = null;

        entity.getNavigation().setSpeed(1);
    }
}
