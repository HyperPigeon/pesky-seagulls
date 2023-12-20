package net.hyper_pigeon.pesky_seagulls.entities.ai.behaviors;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.hyper_pigeon.pesky_seagulls.entities.ai.memory_types.SeagullMemoryTypes;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.WalkTarget;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Unit;
import net.minecraft.util.math.Vec3d;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SetRandomSeagullPaddleTarget<E extends PathAwareEntity> extends SetRandomWalkTarget<E> {

    private static final List<Pair<MemoryModuleType<?>, MemoryModuleState>> MEMORY_REQUIREMENTS =
            ObjectArrayList.of(Pair.of(MemoryModuleType.WALK_TARGET, MemoryModuleState.VALUE_ABSENT),
                    Pair.of(MemoryModuleType.IS_IN_WATER, MemoryModuleState.VALUE_PRESENT),
                    Pair.of(SeagullMemoryTypes.IS_PADDLING.get(), MemoryModuleState.REGISTERED));


    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryModuleState>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean shouldRun(ServerWorld level, E entity) {
        return BrainUtils.getMemory(entity, MemoryModuleType.IS_IN_WATER) == Unit.INSTANCE;
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
            BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(targetPos, this.speedModifier.apply(entity, targetPos), 0));
        }
        BrainUtils.setMemory(entity, SeagullMemoryTypes.IS_PADDLING.get(), Boolean.TRUE);
    }

    @Override
    protected void stop(E entity) {
        super.start(entity);
        BrainUtils.setMemory(entity, SeagullMemoryTypes.IS_PADDLING.get(), Boolean.FALSE);
    }

    @Nullable
    protected Vec3d getTargetPos(E entity) {
        return NoPenaltyTargeting.find(entity, (int) this.radius.xzRadius(), (int)this.radius.yRadius());
    }


}
