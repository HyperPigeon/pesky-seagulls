package net.hyper_pigeon.pesky_seagulls.entities.models;

import net.hyper_pigeon.pesky_seagulls.entities.SeagullEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;

public class SeagullEntityModel<T extends SeagullEntity> extends SinglePartEntityModel<T> {

    //copying parrot model as a placeholder

    private static final String FEATHER = "feather";
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart leftWing;
    private final ModelPart rightWing;
    private final ModelPart head;
    private final ModelPart feather;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;


    public SeagullEntityModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.tail = root.getChild("tail");
        this.leftWing = root.getChild("left_wing");
        this.rightWing = root.getChild("right_wing");
        this.head = root.getChild("head");
        this.feather = this.head.getChild("feather");
        this.leftLeg = root.getChild("left_leg");
        this.rightLeg = root.getChild("right_leg");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(
                "body", ModelPartBuilder.create().uv(2, 8).cuboid(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F), ModelTransform.pivot(0.0F, 16.5F, -3.0F)
        );
        modelPartData.addChild(
                "tail", ModelPartBuilder.create().uv(22, 1).cuboid(-1.5F, -1.0F, -1.0F, 3.0F, 4.0F, 1.0F), ModelTransform.pivot(0.0F, 21.07F, 1.16F)
        );
        modelPartData.addChild(
                "left_wing", ModelPartBuilder.create().uv(19, 8).cuboid(-0.5F, 0.0F, -1.5F, 1.0F, 5.0F, 3.0F), ModelTransform.pivot(1.5F, 16.94F, -2.76F)
        );
        modelPartData.addChild(
                "right_wing", ModelPartBuilder.create().uv(19, 8).cuboid(-0.5F, 0.0F, -1.5F, 1.0F, 5.0F, 3.0F), ModelTransform.pivot(-1.5F, 16.94F, -2.76F)
        );
        ModelPartData modelPartData2 = modelPartData.addChild(
                "head", ModelPartBuilder.create().uv(2, 2).cuboid(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F), ModelTransform.pivot(0.0F, 15.69F, -2.76F)
        );
        modelPartData2.addChild(
                "head2", ModelPartBuilder.create().uv(10, 0).cuboid(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F), ModelTransform.pivot(0.0F, -2.0F, -1.0F)
        );
        modelPartData2.addChild(
                "beak1", ModelPartBuilder.create().uv(11, 7).cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F), ModelTransform.pivot(0.0F, -0.5F, -1.5F)
        );
        modelPartData2.addChild(
                "beak2", ModelPartBuilder.create().uv(16, 7).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F), ModelTransform.pivot(0.0F, -1.75F, -2.45F)
        );
        modelPartData2.addChild(
                "feather", ModelPartBuilder.create().uv(2, 18).cuboid(0.0F, -4.0F, -2.0F, 0.0F, 5.0F, 4.0F), ModelTransform.pivot(0.0F, -2.15F, 0.15F)
        );
        ModelPartBuilder modelPartBuilder = ModelPartBuilder.create().uv(14, 18).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F);
        modelPartData.addChild("left_leg", modelPartBuilder, ModelTransform.pivot(1.0F, 22.0F, -1.05F));
        modelPartData.addChild("right_leg", modelPartBuilder, ModelTransform.pivot(-1.0F, 22.0F, -1.05F));
        return TexturedModelData.of(modelData, 32, 32);
    }

    public ModelPart getPart() {
        return this.root;
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }
}
