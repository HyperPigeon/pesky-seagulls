package net.hyper_pigeon.pesky_seagulls.entities.state;

import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.entity.AnimationState;

public class SeagullEntityRenderState extends LivingEntityRenderState {
    public final AnimationState walkingAnimationState = new AnimationState();
    public final AnimationState flyingAnimationState = new AnimationState();
    public final AnimationState floatingAnimationState = new AnimationState();
    public final ItemRenderState heldItem = new ItemRenderState();
    public double velocitySquared;
}
