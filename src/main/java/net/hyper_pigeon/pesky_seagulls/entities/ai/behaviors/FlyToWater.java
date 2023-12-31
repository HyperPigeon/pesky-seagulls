package net.hyper_pigeon.pesky_seagulls.entities.ai.behaviors;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.NoPenaltySolidTargeting;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomFlyingTarget;
import org.jetbrains.annotations.Nullable;

public class FlyToWater<E extends PathAwareEntity> extends SetRandomFlyingTarget<E> {
    @Nullable
    @Override
    protected Vec3d getTargetPos(E entity) {
        BlockPos waterPos = locateClosestWater(entity.getWorld(), entity,  (int)(Math.ceil(this.radius.xzRadius())), (int)Math.ceil(this.radius.yRadius()));
        Vec3d entityFacing = entity.getRotationVec(0);

        if (waterPos != null)
            return new Vec3d(waterPos.getX(),waterPos.getY(),waterPos.getZ());

        return NoPenaltySolidTargeting.find(entity, (int)(Math.ceil(this.radius.xzRadius())), (int)Math.ceil(this.radius.yRadius()), this.verticalWeight.applyAsInt(entity), entityFacing.x, entityFacing.z, MathHelper.HALF_PI);
    }

    @Nullable
    protected BlockPos locateClosestWater(BlockView world, Entity entity, int rangeX , int rangeY) {
        BlockPos blockPos = entity.getBlockPos();
        if (!world.getBlockState(blockPos).getCollisionShape(world, blockPos).isEmpty()) {
            return null;
        }
        return BlockPos.findClosest(entity.getBlockPos(), rangeX, rangeY, pos -> world.getFluidState((BlockPos)pos).isIn(FluidTags.WATER)).orElse(null);
    }
}
