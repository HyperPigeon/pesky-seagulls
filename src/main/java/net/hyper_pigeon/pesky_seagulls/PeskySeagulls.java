package net.hyper_pigeon.pesky_seagulls;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.hyper_pigeon.pesky_seagulls.entities.SeagullEntity;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PeskySeagulls implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("pesky_seagulls");

	public static final EntityType<SeagullEntity> SEAGULL_ENTITY = Registry.register(
			Registries.ENTITY_TYPE, new Identifier("pesky_seagulls", "seagull"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, SeagullEntity::new).dimensions(EntityDimensions.fixed(0.5F, 0.9F)).build()
	);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
		FabricDefaultAttributeRegistry.register(SEAGULL_ENTITY, SeagullEntity.createSeagullAttributes());
	}
}