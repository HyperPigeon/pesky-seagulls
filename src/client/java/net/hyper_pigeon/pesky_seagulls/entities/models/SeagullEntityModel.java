package net.hyper_pigeon.pesky_seagulls.entities.models;

import net.hyper_pigeon.pesky_seagulls.entities.SeagullEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;

public class SeagullEntityModel<T extends SeagullEntity> extends SinglePartEntityModel<T> {
    public SeagullEntityModel(ModelPart part) {
    }

    @Override
    public ModelPart getPart() {
        return null;
    }

    @Override
    public void setAngles(SeagullEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }
}
