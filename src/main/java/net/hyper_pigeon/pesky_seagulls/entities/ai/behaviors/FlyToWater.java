package net.hyper_pigeon.pesky_seagulls.entities.ai.behaviors;

import net.minecraft.entity.ai.NoPenaltySolidTargeting;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomFlyingTarget;
import org.jetbrains.annotations.Nullable;

public class FlyToWater<E extends PathAwareEntity> extends SetRandomFlyingTarget<E> {
    @Nullable
    @Override
    protected Vec3d getTargetPos(E entity) {
        Vec3d waterPos = findNearbyWater(entity);
        Vec3d entityFacing = entity.getRotationVec(0);

        if (waterPos != null)
            return waterPos;

        return NoPenaltySolidTargeting.find(entity, (int)(Math.ceil(this.radius.xzRadius())), (int)Math.ceil(this.radius.yRadius()), this.verticalWeight.applyAsInt(entity), entityFacing.x, entityFacing.z, MathHelper.HALF_PI);
    }


    @Nullable
    protected Vec3d findNearbyWater(E entity) {
        final BlockPos pos = entity.getBlockPos();
        final World level = entity.getWorld();

        return !level.getBlockState(pos).getCollisionShape(level, pos).isEmpty() ? null : BlockPos.findClosest(entity.getBlockPos(), (int)this.radius.xzRadius(), (int)this.radius.yRadius(), checkPos -> level.getFluidState(checkPos).isIn(FluidTags.WATER)).map(Vec3d::ofBottomCenter).orElse(null);
    }
}
