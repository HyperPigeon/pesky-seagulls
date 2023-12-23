package net.hyper_pigeon.pesky_seagulls.entities.ai.behaviors;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.entity.EquipmentSlot;
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
    private static final List<Pair<MemoryModuleType<?>, MemoryModuleState>> MEMORY_REQUIREMENTS = ObjectArrayList.of(Pair.of(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, MemoryModuleState.VALUE_PRESENT));

    private PlayerEntity targetPlayer;

    private boolean canStealFromPlayer(PlayerEntity player, E entity) {
        return player != null
                && (player.getMainHandStack().isFood() || player.getOffHandStack().isFood())
                && (entity.squaredDistanceTo(player) <= 1);
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryModuleState>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean shouldRun(ServerWorld level, E entity) {
        // TODO targeting players with food even if they aren't nearest
        // TODO don't run if they have a full inventory?
        PlayerEntity player = BrainUtils.getMemory(entity, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER);
        return canStealFromPlayer(player, entity);
    }

    private void stealFood(ItemStack stolenFood, PlayerEntity player, E entity) {
        ItemStack obtainedFood = stolenFood.copyWithCount(1);
        stolenFood.setCount(stolenFood.getCount()-1);
        entity.equipStack(EquipmentSlot.MAINHAND, obtainedFood);
        // TODO drop currently held
    }

    @Override
    protected void start(E entity) {
        PlayerEntity player = BrainUtils.getMemory(entity, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER); // targetable so won't steal in creative mode
        if (player == null) {
            return;
        }
        targetPlayer = player;
        ItemStack mainHandStack = targetPlayer.getMainHandStack();
        ItemStack offHandStack;
        if (mainHandStack != null && mainHandStack.isFood()) {
            stealFood(player.getMainHandStack(), player, entity);
        } else if ((offHandStack = targetPlayer.getOffHandStack())!= null
                && offHandStack.isFood()) {
            stealFood(player.getOffHandStack(), player, entity);
        }
    }

    protected boolean shouldKeepRunning(E entity) {
        return canStealFromPlayer(targetPlayer, entity);
    }
}
