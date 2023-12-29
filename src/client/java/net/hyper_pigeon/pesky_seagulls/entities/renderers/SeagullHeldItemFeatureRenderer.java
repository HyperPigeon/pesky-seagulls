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
import net.minecraft.util.math.RotationAxis;

public class SeagullHeldItemFeatureRenderer extends FeatureRenderer<SeagullEntity, SeagullEntityModel<SeagullEntity>> {
    private final HeldItemRenderer heldItemRenderer;
    public SeagullHeldItemFeatureRenderer(FeatureRendererContext<SeagullEntity, SeagullEntityModel<SeagullEntity>> context, HeldItemRenderer heldItemRenderer) {
        super(context);
        this.heldItemRenderer = heldItemRenderer;
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, SeagullEntity seagullEntity, float f, float g, float h, float j, float k, float l) {
        matrixStack.push();
        SeagullEntityModel<SeagullEntity> contextModel = this.getContextModel();
        matrixStack.translate(contextModel.beak.pivotX / 16.0f, contextModel.beak.pivotY / 16.0f, contextModel.beak.pivotZ / 16.0f);
        //matrixStack.multiply(RotationAxis.POSITIVE_Z.rotation(h));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(k));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(l));
        matrixStack.translate(0f, 1f, -1f);
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0f));
        ItemStack itemStack = seagullEntity.getEquippedStack(EquipmentSlot.MAINHAND);
        this.heldItemRenderer.renderItem(seagullEntity, itemStack, ModelTransformationMode.GROUND, false, matrixStack, vertexConsumerProvider, i);
        matrixStack.pop();
    }
}
