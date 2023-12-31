package net.hyper_pigeon.pesky_seagulls.entities.ai.behaviors;

import net.minecraft.entity.ai.FuzzyPositions;
import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.NavigationConditions;
import net.minecraft.entity.ai.NoPenaltySolidTargeting;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.WalkTarget;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomHoverTarget;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

public class SetRandomSeagullFlightTarget<E extends PathAwareEntity> extends SetRandomHoverTarget<E> {

    @Nullable
    protected Vec3d getTargetPos(E entity) {
        Vec3d entityFacing = entity.getRotationVec(0);
        Vec3d hoverPos = SetRandomSeagullFlightTarget.find(entity,
                (int) (Math.ceil(this.radius.xzRadius())),
                (int) Math.ceil(this.radius.yRadius()),
                entityFacing.x, entityFacing.z,
                MathHelper.HALF_PI,
                5, 1);

        if (hoverPos != null)
            return hoverPos;

        return NoPenaltySolidTargeting.find(entity, (int) (Math.ceil(this.radius.xzRadius())), (int) Math.ceil(this.radius.yRadius()), 0, entityFacing.x, entityFacing.z, MathHelper.HALF_PI);
    }

    @Override
    protected void start(E entity) {
        Vec3d targetPos = getTargetPos(entity);

        if (!this.positionPredicate.test(entity, targetPos))
            targetPos = null;

        if (targetPos == null) {
            BrainUtils.clearMemory(entity, MemoryModuleType.WALK_TARGET);
        }
        else {
            BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(targetPos, this.speedModifier.apply(entity, targetPos), 1));
        }
    }

    @Nullable
    public static Vec3d find(
            PathAwareEntity entity, int horizontalRange, int verticalRange, double x, double z, float angle, int maxAboveSolid, int minAboveSolid
    ) {
        boolean bl = NavigationConditions.isPositionTargetInRange(entity, horizontalRange);
        return FuzzyPositions.guessBestPathTarget(
                entity,
                () -> {
                    BlockPos blockPos = FuzzyPositions.localFuzz(entity.getRandom(), horizontalRange, verticalRange, 0, x, z, (double)angle);
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
}
