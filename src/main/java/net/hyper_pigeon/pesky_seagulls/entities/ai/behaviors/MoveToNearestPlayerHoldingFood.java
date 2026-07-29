package net.hyper_pigeon.pesky_seagulls.entities.ai.behaviors;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.ai.brain.EntityLookTarget;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.WalkTarget;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtil;

import java.util.List;
import java.util.function.BiFunction;

public class MoveToNearestPlayerHoldingFood<E extends PathAwareEntity> extends ExtendedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryModuleState>> MEMORY_REQUIREMENTS = ObjectArrayList.of(Pair.of(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, MemoryModuleState.VALUE_PRESENT));
    protected BiFunction<E, Vec3d, Float> speedModifier = (entity, targetPos) -> 1f;
    private PlayerEntity targetPlayer;
    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryModuleState>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    public boolean hasFood(E entity) {
        return !entity.getMainHandStack().isEmpty() && entity.getMainHandStack().contains(DataComponentTypes.FOOD);
    }

    /**
     * Set the movespeed modifier for the path when chosen.
     * @param modifier The movespeed modifier/multiplier
     * @return this
     */
    public MoveToNearestPlayerHoldingFood<E> speedModifier(float modifier) {
        return speedModifier((entity, targetPos) -> modifier);
    }

    /**
     * Set the movespeed modifier for the path when chosen.
     * @param function The movespeed modifier/multiplier function
     * @return this
     */
    public MoveToNearestPlayerHoldingFood<E> speedModifier(BiFunction<E, Vec3d, Float> function) {
        this.speedModifier = function;
        return this;
    }

    @Override
    protected boolean shouldRun(ServerWorld level, E entity) {
        PlayerEntity player = BrainUtil.getMemory(entity, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER);
        return !hasFood(entity) && player != null
                && (player.getMainHandStack().contains(DataComponentTypes.FOOD) || player.getOffHandStack().contains(DataComponentTypes.FOOD));
        // TODO changing targets
    }


    @Override
    protected void start(E entity) {
        PlayerEntity player = BrainUtil.getMemory(entity, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER); // targetable so won't steal in creative mode
        if (player == null) {
            return;
        }
        this.targetPlayer = player;

        EntityLookTarget entityLookTarget = new EntityLookTarget(player, true);
        WalkTarget walkTarget = new WalkTarget(new EntityLookTarget(player, false), this.speedModifier.apply(entity,this.targetPlayer.getEntityPos()), 1);

        BrainUtil.clearMemory(entity, MemoryModuleType.WALK_TARGET);
        BrainUtil.setMemory(entity, MemoryModuleType.WALK_TARGET, walkTarget);

        BrainUtil.clearMemory(entity, MemoryModuleType.LOOK_TARGET);
        BrainUtil.setMemory(entity, MemoryModuleType.LOOK_TARGET, entityLookTarget);
    }

//    protected boolean shouldKeepRunning(E entity) {
//        return !hasFood(entity) && BrainUtil.hasMemory(entity, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER) && this.targetPlayer != null && !this.targetPlayer.isRemoved();
//    }
//
//    protected void stop(E entity){
//        //clear the current walk and look target
//        BrainUtil.clearMemory(entity, MemoryModuleType.WALK_TARGET);
//        BrainUtil.clearMemory(entity, MemoryModuleType.LOOK_TARGET);
//    }
}
