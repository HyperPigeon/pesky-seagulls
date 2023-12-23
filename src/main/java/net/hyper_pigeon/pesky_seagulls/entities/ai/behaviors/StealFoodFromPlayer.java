package net.hyper_pigeon.pesky_seagulls.entities.ai.behaviors;

import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

public class StealFoodFromPlayer<E extends PathAwareEntity> extends ExtendedBehaviour<E> {
    private PlayerEntity targetPlayer;

    private boolean canStealFromPlayer(PlayerEntity player, E entity) {
        return player != null
                && (player.getMainHandStack().isFood() || player.getOffHandStack().isFood())
                && (entity.squaredDistanceTo(player) <= 1);
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryModuleState>> getMemoryRequirements() {
        return List.of();
    }

    @Override
    protected boolean shouldRun(ServerWorld level, E entity) {
        // TODO targeting players with food even if they aren't nearest
        PlayerEntity player = BrainUtils.getMemory(entity, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER);
        return canStealFromPlayer(player, entity);
    }

    @Override
    protected void start(E entity) {
        PlayerEntity player = BrainUtils.getMemory(entity, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER); // targetable so won't steal in creative mode
        targetPlayer = player;
        if (targetPlayer.getMainHandStack().isFood()) {
            ItemStack stolenFood = player.getMainHandStack();
            ItemStack obtainedFood = stolenFood.copyAndEmpty();
            // TODO add the food to seagull inventory
        } else if (targetPlayer.getOffHandStack().isFood()) {
            ItemStack stolenFood = player.getOffHandStack();
            ItemStack obtainedFood = stolenFood.copyAndEmpty();
        }
    }

    protected boolean shouldKeepRunning(E entity) {
        return canStealFromPlayer(targetPlayer, entity);
    }
}
