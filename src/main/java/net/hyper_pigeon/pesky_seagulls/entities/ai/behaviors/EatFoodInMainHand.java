package net.hyper_pigeon.pesky_seagulls.entities.ai.behaviors;

import com.mojang.datafixers.util.Pair;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;

public class EatFoodInMainHand<E extends PathAwareEntity> extends ExtendedBehaviour<E> {

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryModuleState>> getMemoryRequirements() {
        return List.of();
    }

    public boolean hasFood(E entity) {
        return !entity.getMainHandStack().isEmpty() && entity.getMainHandStack().contains(DataComponentTypes.FOOD);
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
            ItemStack remainder = entity.getMainHandStack().finishUsing(entity.getEntityWorld(), entity);
            entity.setStackInHand(Hand.MAIN_HAND, remainder);
            entity.heal(5.0F);
        }
    }
}
