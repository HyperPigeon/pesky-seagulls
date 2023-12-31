package net.hyper_pigeon.pesky_seagulls.entities;

import net.hyper_pigeon.pesky_seagulls.PeskySeagulls;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class Sounds {
    public static final Identifier SEAGULL_CRY_1_ID = new Identifier(PeskySeagulls.MOD_ID, "seagull_cry_1");
    public static final SoundEvent SEAGULL_CRY_1 = SoundEvent.of(SEAGULL_CRY_1_ID);
}
