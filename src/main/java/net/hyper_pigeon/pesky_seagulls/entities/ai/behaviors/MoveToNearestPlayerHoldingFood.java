package net.hyper_pigeon.pesky_seagulls.entities.ai.behaviors;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.entity.ai.brain.EntityLookTarget;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.WalkTarget;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

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
        PlayerEntity player = BrainUtils.getMemory(entity, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER);
        return player != null
                && (player.getMainHandStack().isFood() || player.getOffHandStack().isFood());
        // TODO changing targets
    }

    @Override
    protected void start(E entity) {
        PlayerEntity player = BrainUtils.getMemory(entity, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER); // targetable so won't steal in creative mode
        this.targetPlayer = player;

        EntityLookTarget entityLookTarget = new EntityLookTarget(player, true);
        WalkTarget walkTarget = new WalkTarget(new EntityLookTarget(player, false), this.speedModifier.apply(entity,this.targetPlayer.getPos()), 0);

        BrainUtils.clearMemory(entity, MemoryModuleType.WALK_TARGET);
        BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, walkTarget);

        BrainUtils.clearMemory(entity, MemoryModuleType.LOOK_TARGET);
        BrainUtils.setMemory(entity, MemoryModuleType.LOOK_TARGET, entityLookTarget);
    }

    protected boolean shouldKeepRunning(E entity) {
        return BrainUtils.hasMemory(entity, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER) && this.targetPlayer != null && !this.targetPlayer.isRemoved();
    }
}
