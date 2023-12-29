package net.hyper_pigeon.pesky_seagulls.entities.renderers;

import net.hyper_pigeon.pesky_seagulls.PeskySeagulls;
import net.hyper_pigeon.pesky_seagulls.entities.ModelLayers;
import net.hyper_pigeon.pesky_seagulls.entities.SeagullEntity;
import net.hyper_pigeon.pesky_seagulls.entities.models.SeagullEntityModel;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class SeagullEntityRenderer extends MobEntityRenderer<SeagullEntity, SeagullEntityModel<SeagullEntity>> {

    private static final Identifier SEAGULL_TEXTURE = new Identifier(PeskySeagulls.MOD_ID, "textures/entity/seagull.png");

    public SeagullEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new SeagullEntityModel<>(context.getPart(ModelLayers.SEAGULL)),0.6F);
    }


    @Override
    public Identifier getTexture(SeagullEntity entity) {
        return SEAGULL_TEXTURE;
    }

    @Override
    public void render(SeagullEntity mobEntity, float f, float g, MatrixStack matrixStack,
                       VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
