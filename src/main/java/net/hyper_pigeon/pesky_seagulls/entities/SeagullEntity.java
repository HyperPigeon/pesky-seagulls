package net.hyper_pigeon.pesky_seagulls.entities;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.hyper_pigeon.pesky_seagulls.entities.ai.behaviors.*;
import net.hyper_pigeon.pesky_seagulls.entities.ai.control.SeagullFlightMoveControl;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.pathing.AmphibiousSwimNavigation;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.AllApplicableBehaviours;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FloatToSurfaceOfFluid;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.GenericAttackTargetSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.NearbyBlocksSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SeagullEntity extends AnimalEntity implements SmartBrainOwner<SeagullEntity> {

    private static final Vec3i ITEM_PICKUP_RANGE_EXPANDER = new Vec3i(1,1,1);

    public AnimationState walkingAnimationState = new AnimationState();
    public AnimationState flyingAnimationState = new AnimationState();

    public SeagullEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 16.0F);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.COCOA, -1.0F);
        this.setPathfindingPenalty(PathNodeType.FENCE, -1.0F);
        swapNavigation(true);
    }

    public boolean isFlapping() {
        return !this.isOnGround();
    }

    public static DefaultAttributeContainer.Builder createSeagullAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 12.0)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 1.5F)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25F)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16F);

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
            this.moveControl = new SeagullFlightMoveControl(this,15,false);
        }
        else {
            this.navigation = new AmphibiousSwimNavigation(this, this.getWorld());
            this.moveControl = new MoveControl(this);
        }
    }

    private void setFlying(){
        if(!(getMoveControl() instanceof FlightMoveControl)) {
            swapNavigation(true);
        }
    }

    private void setGrounded(){
        if(getMoveControl() instanceof FlightMoveControl) {
            swapNavigation(false);
        }
    }

    public void tickMovement(){
        super.tickMovement();
        Vec3d vec3d = this.getVelocity();
        if (!this.isOnGround() && vec3d.y < 0.0) {
            this.setVelocity(vec3d.multiply(1.0, 0.6, 1.0));
        }
        if (this.isTouchingWater()) {
            this.setVelocity(this.getVelocity().add(0.0, 0.010, 0.0));
        }
    }

    public boolean isPushedByFluids() {
        return false;
    }

    public boolean isClimbing() { return false; }

    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    protected Vec3i getItemPickUpRangeExpander() {
        return ITEM_PICKUP_RANGE_EXPANDER;
    }

    public boolean canGather(ItemStack stack) {
        ItemStack itemStack = this.getStackInHand(Hand.MAIN_HAND);
        return stack.getItem().isFood() && itemStack.isEmpty();
    }

    public boolean canPickUpLoot() {
        return true;
    }

    public boolean canEquip(ItemStack stack) {
        EquipmentSlot equipmentSlot = MobEntity.getPreferredEquipmentSlot(stack);
        if (!this.getEquippedStack(equipmentSlot).isEmpty()) {
            return false;
        } else {
            return equipmentSlot == EquipmentSlot.MAINHAND && super.canEquip(stack);
        }
    }


    protected void loot(ItemEntity item) {
        ItemStack itemStack = item.getStack();
        if (this.canPickupItem(itemStack)) {
            this.triggerItemPickedUpByEntityCriteria(item);
            this.equipStack(EquipmentSlot.MAINHAND, itemStack.split(1));
            this.updateDropChances(EquipmentSlot.MAINHAND);
            this.sendPickup(item, itemStack.getCount());
        }
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if(hand == Hand.MAIN_HAND && player.getMainHandStack().isEmpty() && !this.getMainHandStack().isEmpty()) {
            ItemStack itemStack = this.getMainHandStack().copy();
            this.getMainHandStack().decrement(1);
            player.equipStack(EquipmentSlot.MAINHAND, itemStack);
            return ActionResult.SUCCESS;
        }
        return super.interactMob(player, hand);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    public boolean hasFood() {
        return !this.getMainHandStack().isEmpty() && this.getMainHandStack().getItem().isFood();
    }

    protected Brain.Profile<SeagullEntity> createBrainProfile() {
        return new SmartBrainProvider<>(this);
    }

    @Override
    protected void mobTick() {
        tickBrain(this);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getEntityWorld().isClient()) {
            this.setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        flyingAnimationState.setRunning(isFlapping(), this.age);
        walkingAnimationState.setRunning((float)this.getVelocity().horizontalLengthSquared() > 1.0E-5F && !isFlapping(), this.age);
    }

    @Override
    public List<? extends ExtendedSensor<? extends SeagullEntity>> getSensors() {
        return ObjectArrayList.of(
                new NearbyPlayersSensor<>(),
                new NearbyLivingEntitySensor<>(),
                new HurtBySensor<>(),
                new GenericAttackTargetSensor<>(),
                new NearestItemSensor<SeagullEntity>().
                        setRadius(16,16),
                new NearbyBlocksSensor<>(),
                new InWaterSensor<>()
        );
    }

    @Override
    public BrainActivityGroup<? extends SeagullEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                    new FloatToSurfaceOfFluid<>(),
                    new SeagullAvoidEntity<>().avoiding(livingEntity ->  livingEntity instanceof PlayerEntity)
                            .noCloserThan(9).speedModifier(1.5F).whenStarting(pathAwareEntity -> setFlying()).
                            startCondition(pathAwareEntity -> hasFood()), //avoid players when carrying food
                    new LookAtTarget<>().runFor(entity -> entity.getRandom().nextBetween(40, 300)), //look at look target
                    new MoveToWalkTarget<>()//move to walk target
        );
    }

    @Override
    public BrainActivityGroup<? extends SeagullEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                    new EatFoodInMainHand<>().runFor((entity) -> 300),//eat food if in mainhand slot
                    new FirstApplicableBehaviour<>(
                            new SeagullPanic<>().setRadius(10, 3).speedMod((object) -> 1.5F).whenStarting((pathAwareEntity) ->
                                    setFlying()).runFor(entity -> entity.getRandom().nextBetween(100, 300)),
                            new MoveToNearestVisibleWantedItem<>().speedModifier(1.2F).whenStarting(pathAwareEntity -> setFlying()), //set walk target to visible wanted item
                            new AllApplicableBehaviours<>(
                                    new SwoopInOnWalkTarget<>().cooldownFor((pathAwareEntity) -> 70).whenStarting((pathAwareEntity) ->
                                            setFlying()),
                                    new MoveToNearestPlayerHoldingFood<>().speedModifier(1.2F).whenStarting(pathAwareEntity -> setFlying()),
                                    new StealFoodFromPlayer<>()
                            ).cooldownFor((pathAwareEntity) -> {
                                if(hasFood()) {
                                    return 2400;
                                }
                                return 0;
                            })
                    ),
                    new OneRandomBehaviour<>(
                            new SetRandomSeagullFlightTarget<>().setRadius(10).whenStarting((pathAwareEntity) ->
                                    setFlying()),
                            new FlyToWater<>().verticalWeight((pathAwareEntity) -> -1).setRadius(10).whenStarting((pathAwareEntity) ->
                                    setFlying()),
                            new SetRandomSeagullWalkTarget<>().setRadius(5,3).dontAvoidWater().whenStarting((pathAwareEntity) ->
                                    setGrounded()),
                            new Idle<>().runFor(entity -> entity.getRandom().nextBetween(30,60))
                    ).startCondition(pathAwareEntity -> !hasFood() && pathAwareEntity.getNavigation().isIdle())
                );
    }

    @Override
    public BrainActivityGroup<? extends SeagullEntity> getFightTasks() {
        return SmartBrainOwner.super.getFightTasks();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return Sounds.SEAGULL_CRY_1;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_PARROT_DEATH;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_PARROT_HURT;
    }
}
