package net.hyper_pigeon.pesky_seagulls.entities.ai.control;

import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.MathHelper;

public class SeagullFlightMoveControl extends FlightMoveControl {

    public SeagullFlightMoveControl(MobEntity entity, int maxPitchChange, boolean noGravity) {
        super(entity, maxPitchChange, noGravity);
    }

    @Override
    public void tick() {
        super.tick();
        this.entity.setUpwardSpeed((float) MathHelper.clamp(this.entity.upwardSpeed,-this.entity.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED),this.entity.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED)));
    }
}
