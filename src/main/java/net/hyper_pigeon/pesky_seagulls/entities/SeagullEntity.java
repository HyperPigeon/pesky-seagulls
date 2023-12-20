package net.hyper_pigeon.pesky_seagulls.entities;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.hyper_pigeon.pesky_seagulls.entities.ai.behaviors.SetRandomSeagullFlightTarget;
import net.hyper_pigeon.pesky_seagulls.entities.ai.control.SeagullMoveControl;
import net.hyper_pigeon.pesky_seagulls.entities.ai.memory_types.SeagullMemoryTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.control.YawAdjustingLookControl;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.InWaterSensor;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SeagullEntity extends AnimalEntity implements SmartBrainOwner<SeagullEntity> {

    public SeagullEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new SeagullMoveControl(this, 20, 10,0.1F,false);
        this.lookControl = new YawAdjustingLookControl(this, 10);
        this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 0.0F);
        this.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
    }

    public static DefaultAttributeContainer.Builder createSeagullAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 12.0)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 1.0F)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5F);
    }


    protected EntityNavigation createNavigation(World world) {
        BirdNavigation birdNavigation = new BirdNavigation(this, world);
        birdNavigation.setCanPathThroughDoors(true);
        birdNavigation.setCanSwim(true);
        birdNavigation.setCanEnterOpenDoors(true);
        return birdNavigation;
    }

    public void tickMovement(){
        super.tickMovement();
        Vec3d vec3d = this.getVelocity();
        if (!this.isOnGround() && vec3d.y < 0.0) {
            this.setVelocity(vec3d.multiply(1.0, 0.6, 1.0));
        }
    }

    public boolean isClimbing() {
        return false;
    }

    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    protected Brain.Profile<SeagullEntity> createBrainProfile() {
        return new SmartBrainProvider<>(this);
    }

    @Override
    protected void mobTick() {
        tickBrain(this);
    }

    public boolean isPaddling(){
        return BrainUtils.getMemory(this, SeagullMemoryTypes.IS_PADDLING.get()) == Boolean.TRUE;
    }

    @Override
    public List<? extends ExtendedSensor<? extends SeagullEntity>> getSensors() {
        return ObjectArrayList.of(
                new InWaterSensor<>()
        );
    }

    @Override
    public BrainActivityGroup<? extends SeagullEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new LookAtTarget<>(),
                new MoveToWalkTarget<>());
    }

    @Override
    public BrainActivityGroup<? extends SeagullEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new OneRandomBehaviour<>(
                        new SetRandomSeagullFlightTarget<>().setRadius(15,5),
                        new Idle<>().runFor(entity -> entity.getRandom().nextBetween(30,60))
                ));
    }

    @Override
    public BrainActivityGroup<? extends SeagullEntity> getFightTasks() {
        return SmartBrainOwner.super.getFightTasks();
    }

}
