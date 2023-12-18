package net.hyper_pigeon.pesky_seagulls.entities.renderers;

import net.hyper_pigeon.pesky_seagulls.entities.SeagullEntity;
import net.hyper_pigeon.pesky_seagulls.entities.models.SeagullEntityModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;

public class SeagullEntityRenderer extends MobEntityRenderer<SeagullEntity, SeagullEntityModel<SeagullEntity>> {

    private static final Identifier BLUE_TEXTURE = new Identifier("textures/entity/parrot/parrot_blue.png");

    public SeagullEntityRenderer(EntityRendererFactory.Context context) {
        //Use parrot model as placeholder
        super(context, new SeagullEntityModel(context.getPart(EntityModelLayers.PARROT)), 0.3F);
    }


    @Override
    //using blue parrot texture as placeholder
    public Identifier getTexture(SeagullEntity entity) {
        return BLUE_TEXTURE;
    }
}
