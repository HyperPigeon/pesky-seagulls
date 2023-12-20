package net.hyper_pigeon.pesky_seagulls.entities.ai.memory_types;

import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.tslat.smartbrainlib.SBLConstants;

import java.util.function.Supplier;

public class SeagullMemoryTypes {
    public static Supplier<MemoryModuleType<Boolean>> IS_PADDLING;
    public static void init(){
        IS_PADDLING =  SBLConstants.SBL_LOADER.registerMemoryType("is_paddling", null);
    };
}
