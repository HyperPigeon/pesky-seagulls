package net.hyper_pigeon.pesky_seagulls.entities;

import net.hyper_pigeon.pesky_seagulls.PeskySeagulls;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class ModelLayers {
    public static final EntityModelLayer SEAGULL =
            new EntityModelLayer(new Identifier(PeskySeagulls.MOD_ID, "seagull"), "main");
}
