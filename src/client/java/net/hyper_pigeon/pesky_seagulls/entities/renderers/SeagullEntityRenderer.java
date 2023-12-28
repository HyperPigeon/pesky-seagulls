package net.hyper_pigeon.pesky_seagulls.entities.renderers;

import net.hyper_pigeon.pesky_seagulls.entities.SeagullEntity;
import net.hyper_pigeon.pesky_seagulls.entities.models.SeagullEntityModel;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class SeagullEntityRenderer extends MobEntityRenderer<SeagullEntity, SeagullEntityModel<SeagullEntity>> {

    private static final Identifier SEAGULL_TEXTURE = new Identifier("textures/entity/seagull.png");

    public static final EntityModelLayer SEAGULL =
            new EntityModelLayer(new Identifier("pesky_seagulls", "seagull"), "main"); // TODO constant string

    public SeagullEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new SeagullEntityModel<>(context.getPart(SEAGULL)),0.3F);
    }


    @Override
    public Identifier getTexture(SeagullEntity entity) {
        return SEAGULL_TEXTURE;
    }

    @Override
    public void render(SeagullEntity mobEntity, float f, float g, MatrixStack matrixStack,
                       VertexConsumerProvider vertexConsumerProvider, int i) {
        if(mobEntity.isBaby()) {
            matrixStack.scale(0.5f, 0.5f, 0.5f);
        } else {
            matrixStack.scale(1f, 1f, 1f);
        }

        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
