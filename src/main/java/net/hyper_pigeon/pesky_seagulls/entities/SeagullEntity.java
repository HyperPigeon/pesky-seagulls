package net.hyper_pigeon.pesky_seagulls.entities;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.hyper_pigeon.pesky_seagulls.entities.ai.behaviors.SetRandomSeagullFlightTarget;
import net.hyper_pigeon.pesky_seagulls.entities.ai.behaviors.SetRandomSeagullWalkTarget;
import net.hyper_pigeon.pesky_seagulls.entities.ai.control.SeagullMoveControl;
import net.hyper_pigeon.pesky_seagulls.entities.ai.memory_types.SeagullMemoryTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.control.YawAdjustingLookControl;
import net.minecraft.entity.ai.pathing.*;
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
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FloatToSurfaceOfFluid;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.GenericAttackTargetSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.NearbyBlocksSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.*;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SeagullEntity extends AnimalEntity implements SmartBrainOwner<SeagullEntity> {

    private boolean paddling;

    public SeagullEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 16.0F);
        this.setPathfindingPenalty(PathNodeType.COCOA, -1.0F);
        this.setPathfindingPenalty(PathNodeType.FENCE, -1.0F);
        swapNavigation(false);
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

    public void swapNavigation(boolean isFlying) {
        if(isFlying){
            this.navigation = createNavigation(this.getWorld());
            this.moveControl = new FlightMoveControl(this,20,false);

        }
        else {
            this.navigation = new MobNavigation(this, this.getWorld());
            this.moveControl = new MoveControl(this);
        }
    }

    public void tickMovement(){
        super.tickMovement();
        Vec3d vec3d = this.getVelocity();
        if (!this.isOnGround() && vec3d.y < 0.0) {
            this.setVelocity(vec3d.multiply(1.0, 0.6, 1.0));
        }
    }

    public boolean isPushedByFluids() {
        return false;
    }

    public boolean isClimbing() { return false; }

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


    @Override
    public List<? extends ExtendedSensor<? extends SeagullEntity>> getSensors() {
        return ObjectArrayList.of(
                new NearbyPlayersSensor<>(),
                new NearestItemSensor<>(),
                new HurtBySensor<>(),
                new NearbyBlocksSensor<>(),
                new GenericAttackTargetSensor<>(),
                new InWaterSensor<>()
        );
    }

    @Override
    public BrainActivityGroup<? extends SeagullEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new FloatToSurfaceOfFluid<>(),
                new LookAtTarget<>(),
                new MoveToWalkTarget<>());
    }

    @Override
    public BrainActivityGroup<? extends SeagullEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new OneRandomBehaviour<>(
                        new SetRandomSeagullFlightTarget<>().setRadius(10),
                        new SetRandomSeagullWalkTarget<>().setRadius(5,3).dontAvoidWater(),
                        new Idle<>().runFor(entity -> entity.getRandom().nextBetween(30,60))
                ));
    }

    @Override
    public BrainActivityGroup<? extends SeagullEntity> getFightTasks() {
        return SmartBrainOwner.super.getFightTasks();
    }

}
