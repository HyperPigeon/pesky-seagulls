package net.hyper_pigeon.pesky_seagulls.entities.ai.behaviors;

import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.server.world.ServerWorld;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;

public class SetRandomSeagullWalkTarget<E extends PathAwareEntity> extends SetRandomWalkTarget<E> {

    @Override
    protected boolean shouldRun(ServerWorld level, E entity) {
        return entity.isOnGround() || entity.isInFluid();
    }

}
