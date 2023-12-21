package net.hyper_pigeon.pesky_seagulls.entities.ai.behaviors;

import net.hyper_pigeon.pesky_seagulls.entities.SeagullEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.server.world.ServerWorld;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;

public class SetRandomSeagullWalkTarget<E extends PathAwareEntity> extends SetRandomWalkTarget<E> {

    @Override
    protected boolean shouldRun(ServerWorld level, E entity) {
        return entity.isOnGround() || entity.isInFluid();
    }

    @Override
    protected void start(E entity) {
        if(entity instanceof SeagullEntity seagullEntity) {
            seagullEntity.swapNavigation(false);
        }
        super.start(entity);
    }

}
