package net.hyper_pigeon.pesky_seagulls.entities.models;

import net.hyper_pigeon.pesky_seagulls.entities.SeagullEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;

// Made with Blockbench 4.9.2
// Exported for Minecraft version 1.17+ for Yarn

public class SeagullEntityModel<T extends SeagullEntity> extends SinglePartEntityModel<T> {
    private final ModelPart seagull;

    public SeagullEntityModel(ModelPart root) {
        this.seagull = root.getChild("seagull");
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData body = modelPartData.addChild("seagull", ModelPartBuilder.create().uv(0, 0).cuboid(0.0F, -12.0F, 0.25F, 2.0F, 8.0F, 3.0F, new Dilation(0.0F))
                .uv(24, 0).cuboid(-1.0F, -14.0F, 0.0F, 4.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.0F, 20.0F, 4.0F));

        ModelPartData mainbody = body.addChild("mainbody", ModelPartBuilder.create().uv(0, 19).cuboid(-3.0F, -8.0F, -6.0F, 8.0F, 6.0F, 10.0F, new Dilation(0.0F))
                .uv(34, 40).cuboid(-1.0F, -9.0F, -3.5F, 4.0F, 2.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 1.0F, 0.0873F, 0.0F, 0.0F));

        ModelPartData cube_r1 = mainbody.addChild("cube_r1", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -2.0F, -7.0F, 6.0F, 7.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, -5.0F, -3.0F, -0.0436F, 0.0F, 0.0F));

        ModelPartData beak = body.addChild("beak", ModelPartBuilder.create(), ModelTransform.pivot(1.0F, -12.5F, 4.0F));

        ModelPartData cube_r2 = beak.addChild("cube_r2", ModelPartBuilder.create().uv(0, 19).cuboid(-1.0F, -0.5F, -1.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.5F, 1.0F, 0.0F, 0.0453F, 0.0F, 0.0F));

        ModelPartData cube_r3 = beak.addChild("cube_r3", ModelPartBuilder.create().uv(24, 7).cuboid(-1.0F, -0.5F, -1.0F, 2.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.25F, 0.0F, -0.0821F, 0.0F, 0.0F));

        ModelPartData tail = body.addChild("tail", ModelPartBuilder.create().uv(40, 24).cuboid(-2.0F, -5.0F, -2.0F, 6.0F, 4.0F, 5.0F, new Dilation(0.0F))
                .uv(36, 0).cuboid(-1.0F, -2.5F, -5.0F, 4.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, -11.0F));

        ModelPartData legs = body.addChild("legs", ModelPartBuilder.create(), ModelTransform.pivot(1.0F, 0.0F, -3.0F));

        ModelPartData bone3 = legs.addChild("bone3", ModelPartBuilder.create().uv(4, 24).cuboid(0.0F, -3.0F, -1.0F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
                .uv(6, -1).cuboid(0.0F, 1.0F, -1.0F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.25F, 3.0F, 0.0F));

        ModelPartData bone4 = legs.addChild("bone4", ModelPartBuilder.create().uv(4, -1).cuboid(1.5F, 1.0F, -1.0F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 24).cuboid(1.5F, -3.0F, -1.0F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.25F, 3.0F, 0.0F));

        ModelPartData leftwing = body.addChild("leftwing", ModelPartBuilder.create(), ModelTransform.pivot(-3.0F, -6.0F, 0.0F));

        ModelPartData bone = leftwing.addChild("bone", ModelPartBuilder.create().uv(26, 8).cuboid(-1.0F, 0.0F, -5.0F, 2.0F, 5.0F, 11.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, -5.25F));

        ModelPartData bone2 = leftwing.addChild("bone2", ModelPartBuilder.create().uv(12, 37).cuboid(0.0F, 0.0F, -6.0F, 1.0F, 2.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.5F, 3.0F, -6.0F));

        ModelPartData rightwing = body.addChild("rightwing", ModelPartBuilder.create().uv(25, 24).cuboid(-1.0F, 0.0F, -6.25F, 2.0F, 5.0F, 11.0F, new Dilation(0.0F))
                .uv(0, 35).cuboid(0.5F, 3.0F, -8.0F, 1.0F, 2.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(5.0F, -6.0F, -4.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }
    @Override
    public void setAngles(SeagullEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }
    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        seagull.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart getPart() {
        return seagull;
    }
}