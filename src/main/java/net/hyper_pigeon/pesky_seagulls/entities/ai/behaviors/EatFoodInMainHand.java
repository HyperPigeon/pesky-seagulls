package net.hyper_pigeon.pesky_seagulls.entities.ai.behaviors;

import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.server.world.ServerWorld;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;

public class EatFoodInMainHand<E extends PathAwareEntity> extends ExtendedBehaviour<E> {

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryModuleState>> getMemoryRequirements() {
        return List.of();
    }

    public boolean hasFood(E entity) {
        return entity.getMainHandStack().isEmpty() && entity.getMainHandStack().getItem().isFood();
    }

    @Override
    protected boolean shouldRun(ServerWorld level, E entity) {
        return hasFood(entity);
    }


    protected boolean shouldKeepRunning(E entity) {
        return hasFood(entity);
    }

    @Override
    protected void stop(E entity) {
        if(hasFood(entity)) {
            entity.eatFood(entity.getWorld(),entity.getMainHandStack());
            entity.heal(5.0F);
        }
    }
}
