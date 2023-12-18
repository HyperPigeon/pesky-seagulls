package net.hyper_pigeon.pesky_seagulls;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.hyper_pigeon.pesky_seagulls.entities.renderers.SeagullEntityRenderer;

public class PeskySeagullsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(PeskySeagulls.SEAGULL_ENTITY, ((context) -> new SeagullEntityRenderer(context)));
       // EntityModelLayerRegistry.registerModelLayer(SeagullEntityModel.LAYER_LOCATION, SeagullEntityModel::getTexturedModelData);
    }
}
