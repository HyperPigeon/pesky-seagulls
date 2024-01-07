package net.hyper_pigeon.pesky_seagulls.entities.renderers;

import net.hyper_pigeon.pesky_seagulls.entities.SeagullEntity;
import net.hyper_pigeon.pesky_seagulls.entities.models.SeagullEntityModel;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class SeagullHeldItemFeatureRenderer extends FeatureRenderer<SeagullEntity, SeagullEntityModel<SeagullEntity>> {
    private final HeldItemRenderer heldItemRenderer;
    public SeagullHeldItemFeatureRenderer(FeatureRendererContext<SeagullEntity, SeagullEntityModel<SeagullEntity>> context, HeldItemRenderer heldItemRenderer) {
        super(context);
        this.heldItemRenderer = heldItemRenderer;
    }

    @Override
    public void render(MatrixStack matrixStack,
                       VertexConsumerProvider vertexConsumerProvider,
                       int light,
                       SeagullEntity seagullEntity,
                       float limbAngle,
                       float limbDistance,
                       float tickDelta,
                       float animationProgress,
                       float headYaw,
                       float headPitch) {

        matrixStack.push();
        SeagullEntityModel<SeagullEntity> contextModel = this.getContextModel();
        matrixStack.translate(0,1.0F, -0.3F);
        float yaw = headYaw;
        float pitch = headPitch;
        yaw = MathHelper.clamp(yaw, -30.0F, 30.0F);
        pitch = MathHelper.clamp(pitch, -30.0F, 30.0F);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pitch));
        ItemStack itemStack = seagullEntity.getEquippedStack(EquipmentSlot.MAINHAND);
        matrixStack.translate(0f, -0.45f, -0.5f); // Move to be in mouth
        // Make item sideways and at the same angle as head
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(100.0f));
        matrixStack.scale(0.7F,0.7F,0.7F);
        this.heldItemRenderer.renderItem(seagullEntity, itemStack, ModelTransformationMode.GROUND, false, matrixStack, vertexConsumerProvider, light);
        matrixStack.pop();
    }
}
