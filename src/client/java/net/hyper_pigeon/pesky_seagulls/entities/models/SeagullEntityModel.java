package net.hyper_pigeon.pesky_seagulls.entities.models;

import net.hyper_pigeon.pesky_seagulls.entities.animation.SeagullAnimations;
import net.hyper_pigeon.pesky_seagulls.entities.state.SeagullEntityRenderState;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.util.math.MathHelper;

// Made with Blockbench 4.9.2
// Exported for Minecraft version 1.17+ for Yarn

public class SeagullEntityModel extends EntityModel<SeagullEntityRenderState> {
    public final ModelPart headAndNeck;
    public final ModelPart beak;

    public SeagullEntityModel(ModelPart root) {
        super(root);
        ModelPart seagull = root.getChild("seagull");
        this.headAndNeck = seagull.getChild("body").getChild("headAndNeck");
        this.beak = headAndNeck.getChild("beak");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData seagull = modelPartData.addChild("seagull", ModelPartBuilder.create(), ModelTransform.origin(-1.0F, 20.0F, 0.0F));

        ModelPartData body = seagull.addChild("body", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, -3.1416F, 0.0F, 3.1416F));

        ModelPartData mainbody = body.addChild("mainbody", ModelPartBuilder.create().uv(0, 19).cuboid(-3.0F, -8.0F, -6.0F, 8.0F, 6.0F, 10.0F, new Dilation(0.0F))
                .uv(34, 40).cuboid(-1.0F, -9.0F, -3.5F, 4.0F, 2.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, 0.0F, 5.0F, 0.0873F, 0.0F, 0.0F));

        ModelPartData back_r1 = mainbody.addChild("back_r1", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -2.0F, -7.0F, 6.0F, 7.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, -5.0F, -3.0F, -0.0436F, 0.0F, 0.0F));

        ModelPartData headAndNeck = body.addChild("headAndNeck", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -7.0F, 0.0F, 2.0F, 8.0F, 3.0F, new Dilation(0.0F))
                .uv(24, 0).cuboid(-2.0F, -9.0F, -0.25F, 4.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.origin(-1.0F, -5.0F, 4.25F));

        ModelPartData beak = headAndNeck.addChild("beak", ModelPartBuilder.create(), ModelTransform.origin(0.0F, -7.5F, 3.75F));

        ModelPartData bottombeak_r1 = beak.addChild("bottombeak_r1", ModelPartBuilder.create().uv(0, 19).cuboid(-1.0F, -0.5F, -1.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.5F, 1.0F, 0.0F, 0.0453F, 0.0F, 0.0F));

        ModelPartData topbeak_r1 = beak.addChild("topbeak_r1", ModelPartBuilder.create().uv(24, 7).cuboid(-1.0F, -0.5F, -1.0F, 2.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.25F, 0.0F, -0.0821F, 0.0F, 0.0F));

        ModelPartData tail = body.addChild("tail", ModelPartBuilder.create().uv(40, 24).cuboid(-2.0F, -5.0F, -2.0F, 6.0F, 4.0F, 5.0F, new Dilation(0.0F))
                .uv(36, 0).cuboid(-1.0F, -2.5F, -5.0F, 4.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.origin(-2.0F, 0.0F, -7.0F));

        ModelPartData legs = body.addChild("legs", ModelPartBuilder.create(), ModelTransform.origin(-1.0F, 0.0F, 1.0F));

        ModelPartData LeftWholeLeg = legs.addChild("LeftWholeLeg", ModelPartBuilder.create().uv(4, 24).cuboid(-2.0F, 0.5F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
                .uv(6, -1).cuboid(-2.0F, 4.5F, -0.5F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.origin(0.75F, -0.5F, -0.5F));

        ModelPartData rightWholeLeg = legs.addChild("rightWholeLeg", ModelPartBuilder.create().uv(4, -1).cuboid(1.0F, 4.5F, -0.5F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 24).cuboid(1.0F, 0.5F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.origin(-0.75F, -0.5F, -0.5F));

        ModelPartData leftWing = body.addChild("leftWing", ModelPartBuilder.create(), ModelTransform.origin(-5.0F, -6.0F, 4.0F));

        ModelPartData upperLeftWing = leftWing.addChild("upperLeftWing", ModelPartBuilder.create().uv(26, 8).cuboid(-1.0F, 0.0F, -5.0F, 2.0F, 5.0F, 11.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, 0.0F, -5.25F));

        ModelPartData lowerLeftWing = leftWing.addChild("lowerLeftWing", ModelPartBuilder.create().uv(12, 37).cuboid(0.0F, 0.0F, -6.0F, 1.0F, 2.0F, 10.0F, new Dilation(0.0F)), ModelTransform.origin(-1.5F, 3.0F, -6.0F));

        ModelPartData rightWing = body.addChild("rightWing", ModelPartBuilder.create(), ModelTransform.origin(3.0F, -6.0F, 4.0F));

        ModelPartData upperRightWing2 = rightWing.addChild("upperRightWing2", ModelPartBuilder.create().uv(25, 24).cuboid(-1.0F, 0.0F, -5.0F, 2.0F, 5.0F, 11.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, 0.0F, -5.25F));

        ModelPartData lowerRightWing = rightWing.addChild("lowerRightWing", ModelPartBuilder.create().uv(0, 35).cuboid(-1.0F, 0.0F, -6.0F, 1.0F, 2.0F, 10.0F, new Dilation(0.0F)), ModelTransform.origin(1.5F, 3.0F, -6.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(SeagullEntityRenderState state) {
        resetTransforms();
        this.setHeadAngles(state.relativeHeadYaw, state.pitch);
        float speedMult = (Math.min((float)state.velocitySquared * 100.0F, 8F))*2;

        this.animateWalking(SeagullAnimations.WALKING, state.limbSwingAnimationProgress, state.limbSwingAmplitude, 2f, 2.5f);
        this.animate(state.flyingAnimationState, SeagullAnimations.FLY, state.age);
        this.animate(state.walkingAnimationState, SeagullAnimations.WALKING, state.age, speedMult);
        this.animate(state.floatingAnimationState, SeagullAnimations.FLOAT, state.age);
    }

    private void setHeadAngles(float headYaw, float headPitch) {
        // Thanks to Tutorials By Kaupenjoe for this bit
        headYaw = MathHelper.clamp(headYaw, -30.0F, 30.0F);
        headPitch = MathHelper.clamp(headPitch, -30.0F, 30.0F);

        this.headAndNeck.yaw = headYaw * 0.017453292F;
        this.headAndNeck.pitch = headPitch * -0.017453292F;
    }
}