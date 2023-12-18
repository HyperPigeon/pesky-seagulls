package net.hyper_pigeon.pesky_seagulls.entities.renderers;

import net.hyper_pigeon.pesky_seagulls.entities.SeagullEntity;
import net.hyper_pigeon.pesky_seagulls.entities.models.SeagullEntityModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;

public class SeagullEntityRenderer extends MobEntityRenderer<SeagullEntity, SeagullEntityModel<SeagullEntity>> {
    public SeagullEntityRenderer(EntityRendererFactory.Context context) {
        //Set EntityModelLayer to Parrot as a placeholder
        super(context, new SeagullEntityModel(context.getPart(EntityModelLayers.PARROT)), 0.3F);
    }


    @Override
    public Identifier getTexture(SeagullEntity entity) {
        return null;
    }
}
