package net.hyper_pigeon.pesky_seagulls.entities.renderers;

import net.hyper_pigeon.pesky_seagulls.entities.models.SeagullEntityModel;
import net.hyper_pigeon.pesky_seagulls.entities.state.SeagullEntityRenderState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class SeagullHeldItemFeatureRenderer extends FeatureRenderer<SeagullEntityRenderState, SeagullEntityModel> {
    public SeagullHeldItemFeatureRenderer(FeatureRendererContext<SeagullEntityRenderState, SeagullEntityModel> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrixStack,
                       VertexConsumerProvider vertexConsumerProvider,
                       int light,
                       SeagullEntityRenderState state,
                       float limbAngle,
                       float limbDistance) {

        matrixStack.push();
        matrixStack.translate(0,1.0F, -0.3F);
        float yaw = state.relativeHeadYaw;
        float pitch = state.pitch;
        yaw = MathHelper.clamp(yaw, -30.0F, 30.0F);
        pitch = MathHelper.clamp(pitch, -30.0F, 30.0F);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pitch));
        matrixStack.translate(0f, -0.45f, -0.5f); // Move to be in mouth
        // Make item sideways and at the same angle as head
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(100.0f));
        matrixStack.scale(0.7F,0.7F,0.7F);
        state.heldItem.render(matrixStack, vertexConsumerProvider, light, OverlayTexture.DEFAULT_UV);
        matrixStack.pop();
    }
}
