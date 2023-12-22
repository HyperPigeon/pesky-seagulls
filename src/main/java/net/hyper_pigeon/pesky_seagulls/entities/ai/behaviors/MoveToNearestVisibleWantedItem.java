package net.hyper_pigeon.pesky_seagulls.entities.ai.behaviors;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.brain.EntityLookTarget;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.WalkTarget;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.function.BiFunction;

public class MoveToNearestVisibleWantedItem<E extends PathAwareEntity> extends ExtendedBehaviour<E> {

    private static final List<Pair<MemoryModuleType<?>, MemoryModuleState>> MEMORY_REQUIREMENTS = ObjectArrayList.of(Pair.of(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleState.VALUE_PRESENT));
    protected BiFunction<E, Vec3d, Float> speedModifier = (entity, targetPos) -> 1f;
    private ItemEntity targetItemEntity;

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryModuleState>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }


    /**
     * Set the movespeed modifier for the path when chosen.
     * @param modifier The movespeed modifier/multiplier
     * @return this
     */
    public MoveToNearestVisibleWantedItem<E> speedModifier(float modifier) {
        return speedModifier((entity, targetPos) -> modifier);
    }

    /**
     * Set the movespeed modifier for the path when chosen.
     * @param function The movespeed modifier/multiplier function
     * @return this
     */
    public MoveToNearestVisibleWantedItem<E> speedModifier(BiFunction<E, Vec3d, Float> function) {
        this.speedModifier = function;

        return this;
    }

    @Override
    protected boolean shouldRun(ServerWorld level, E entity) {
        ItemEntity itemEntity = BrainUtils.getMemory(entity, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM);
        return entity.getMainHandStack().isEmpty() && (this.targetItemEntity == null || entity.squaredDistanceTo(targetItemEntity) > entity.squaredDistanceTo(itemEntity));
    }


    @Override
    protected void start(E entity) {

        ItemEntity itemEntity = BrainUtils.getMemory(entity, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM);
        this.targetItemEntity = itemEntity;

        EntityLookTarget entityLookTarget = new EntityLookTarget(itemEntity, true);
        WalkTarget walkTarget = new WalkTarget(new EntityLookTarget(itemEntity, false), this.speedModifier.apply(entity,this.targetItemEntity.getPos()), 0);

        BrainUtils.clearMemory(entity, MemoryModuleType.WALK_TARGET);
        BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, walkTarget);

        BrainUtils.clearMemory(entity, MemoryModuleType.LOOK_TARGET);
        BrainUtils.setMemory(entity, MemoryModuleType.LOOK_TARGET, entityLookTarget);
    }

    protected boolean shouldKeepRunning(E entity) {
        return BrainUtils.hasMemory(entity, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM) && this.targetItemEntity != null && !this.targetItemEntity.isRemoved();
    }
}
