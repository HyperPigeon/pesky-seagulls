package net.hyper_pigeon.pesky_seagulls;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.hyper_pigeon.pesky_seagulls.entities.SeagullEntity;
import net.hyper_pigeon.pesky_seagulls.entities.Sounds;
import net.hyper_pigeon.pesky_seagulls.entities.ai.memory_types.SeagullMemoryTypes;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PeskySeagulls implements ModInitializer {
	public static final String MOD_ID = "pesky_seagulls";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final EntityType<SeagullEntity> SEAGULL_ENTITY = Registry.register(
			Registries.ENTITY_TYPE, new Identifier(MOD_ID, "seagull"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, SeagullEntity::new)
					.dimensions(EntityDimensions.fixed(1F, 1F))
					.build()
	);

	public static final TagKey<Biome> SEAGULL_SPAWN_BIOMES = TagKey.of(RegistryKeys.BIOME, new Identifier(MOD_ID, "seagull_spawn_biomes"));


	@Override
	public void onInitialize() {
		LOGGER.info("Spawning pesky seagulls...");
		SeagullMemoryTypes.init();
		SpawnRestriction.register(SEAGULL_ENTITY,SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, PeskySeagulls::isValidNaturalSpawn);
		BiomeModifications.addSpawn(BiomeSelectors.tag(SEAGULL_SPAWN_BIOMES), SpawnGroup.CREATURE, SEAGULL_ENTITY, 20, 3, 5);
		FabricDefaultAttributeRegistry.register(SEAGULL_ENTITY, SeagullEntity.createSeagullAttributes());
		Registry.register(Registries.SOUND_EVENT, Sounds.SEAGULL_CRY_1_ID, Sounds.SEAGULL_CRY_1);
	}

	public static boolean isValidNaturalSpawn(EntityType<? extends AnimalEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
		boolean bl = SpawnReason.isTrialSpawner(spawnReason) ||world.getBaseLightLevel(pos, 0) > 8;
		return (world.getBlockState(pos.down()).isIn(BlockTags.ANIMALS_SPAWNABLE_ON) || world.getBlockState(pos.down()).getBlock().equals(Blocks.SAND)) && bl;
	}


}