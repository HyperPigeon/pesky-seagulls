package net.hyper_pigeon.pesky_seagulls.entities.renderers;

import net.hyper_pigeon.pesky_seagulls.PeskySeagulls;
import net.hyper_pigeon.pesky_seagulls.entities.ModelLayers;
import net.hyper_pigeon.pesky_seagulls.entities.SeagullEntity;
import net.hyper_pigeon.pesky_seagulls.entities.models.SeagullEntityModel;
import net.hyper_pigeon.pesky_seagulls.entities.state.SeagullEntityRenderState;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.util.Identifier;

public class SeagullEntityRenderer extends MobEntityRenderer<SeagullEntity, SeagullEntityRenderState, SeagullEntityModel> {

    private static final Identifier SEAGULL_TEXTURE = Identifier.of(PeskySeagulls.MOD_ID, "textures/entity/seagull.png");

    private final ItemModelManager itemModelManager;

    public SeagullEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new SeagullEntityModel(context.getPart(ModelLayers.SEAGULL)),0.6F);
        this.itemModelManager = context.getItemModelManager();
        this.addFeature(new SeagullHeldItemFeatureRenderer(this));
    }

    @Override
    public SeagullEntityRenderState createRenderState() {
        return new SeagullEntityRenderState();
    }

    @Override
    public void updateRenderState(SeagullEntity entity, SeagullEntityRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        state.walkingAnimationState.copyFrom(entity.walkingAnimationState);
        state.flyingAnimationState.copyFrom(entity.flyingAnimationState);
        state.floatingAnimationState.copyFrom(entity.floatingAnimationState);
        state.velocitySquared = entity.getVelocity().lengthSquared();
        this.itemModelManager.updateForLivingEntity(state.heldItem, entity.getEquippedStack(EquipmentSlot.MAINHAND), ItemDisplayContext.GROUND, entity);
    }

    @Override
    public Identifier getTexture(SeagullEntityRenderState state) {
        return SEAGULL_TEXTURE;
    }
}
