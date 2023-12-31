package net.hyper_pigeon.pesky_seagulls.entities.ai.behaviors;

import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.WalkTarget;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.util.BrainUtils;

public class SetRandomSeagullWalkTarget<E extends PathAwareEntity> extends SetRandomWalkTarget<E> {

    @Override
    protected boolean shouldRun(ServerWorld level, E entity) {
        return entity.isOnGround() || entity.isInFluid();
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

}
