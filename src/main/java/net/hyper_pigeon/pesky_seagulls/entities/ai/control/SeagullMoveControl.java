package net.hyper_pigeon.pesky_seagulls.entities.ai.control;

import net.hyper_pigeon.pesky_seagulls.entities.SeagullEntity;
import net.minecraft.entity.ai.control.AquaticMoveControl;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.math.MathHelper;

public class SeagullMoveControl extends MoveControl {


    private SeagullEntity seagullEntity;
    private FlightMoveControl seagullFlightControl;
    private AquaticMoveControl seagullPaddleControl;


    private final int maxPitchChange;
    private final int maxYawChange;
    private final boolean noGravity;
    private final float speedInWater;


    public SeagullMoveControl(SeagullEntity seagullEntity, int maxPitchChange, int maxYawChange, float speedInWater, boolean noGravity) {
        super(seagullEntity);
        this.seagullEntity = seagullEntity;
        this.seagullFlightControl = new FlightMoveControl(seagullEntity, maxPitchChange, noGravity);
        this.seagullPaddleControl = new AquaticMoveControl(seagullEntity, maxPitchChange, maxYawChange, speedInWater,  1, true);
        this.maxPitchChange = maxPitchChange;
        this.maxYawChange = maxYawChange;
        this.speedInWater = speedInWater;
        this.noGravity = noGravity;
    }

    public void tick() {
        if (this.state == State.MOVE_TO) {
            this.state = State.WAIT;
            this.entity.setNoGravity(true);
            double d = this.targetX - this.entity.getX();
            double e = this.targetY - this.entity.getY();
            double f = this.targetZ - this.entity.getZ();
            double g = d * d + e * e + f * f;
            if (g < 2.5000003E-7F) {
                this.entity.setUpwardSpeed(0.0F);
                this.entity.setForwardSpeed(0.0F);
                return;
            }

            float h = (float)(MathHelper.atan2(f, d) * 180.0F / (float)Math.PI) - 90.0F;
            this.entity.setYaw(this.wrapDegrees(this.entity.getYaw(), h, 90.0F));
            float i;
            if (this.entity.isOnGround()) {
                i = (float)(this.speed * this.entity.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
            } else {
                i = (float)(this.speed * this.entity.getAttributeValue(EntityAttributes.GENERIC_FLYING_SPEED));
            }

            this.entity.setMovementSpeed(i);
            double j = Math.sqrt(d * d + f * f);
            if (Math.abs(e) > 1.0E-5F || Math.abs(j) > 1.0E-5F) {
                float k = (float)(-(MathHelper.atan2(e, j) * 180.0F / (float)Math.PI));
                this.entity.setPitch(this.wrapDegrees(this.entity.getPitch(), k, (float)this.maxPitchChange));
                this.entity.setUpwardSpeed(e > 0.0 ? i : -i);
            }
        } else {
            if (!this.noGravity) {
                this.entity.setNoGravity(false);
            }
            this.entity.setUpwardSpeed(0.0F);
            this.entity.setForwardSpeed(0.0F);
        }
    }
}
