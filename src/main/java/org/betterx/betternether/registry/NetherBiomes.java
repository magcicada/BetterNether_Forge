package org.betterx.betternether.registry;

import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeRegistry;
import org.betterx.bclib.api.v2.levelgen.biomes.BiomeAPI;
import org.betterx.bclib.interfaces.NumericProvider;
import org.betterx.betternether.BN;
import org.betterx.betternether.BetterNether;
import org.betterx.betternether.world.NetherBiome;
import org.betterx.betternether.world.biomes.providers.NetherGrasslandsNumericProvider;
import org.betterx.betternether.world.biomes.providers.NetherMushroomForestEdgeNumericProvider;

import net.minecraft.core.Registry;

public class NetherBiomes {
    private static boolean REGISTERED = false;

    public static void register() {
        if (REGISTERED) {
            return;
        }
        REGISTERED = true;
        BCLBiomeRegistry.registerBiomeCodec(BN.id("biome"), NetherBiome.KEY_CODEC);
        BiomeAPI.registerNetherBiomeModification((biomeID, biome) -> {
            if (!biomeID.getNamespace().equals(BetterNether.MOD_ID)) {
                NetherEntities.modifyNonBNBiome(biomeID, biome);
                NetherFeatures.modifyNonBNBiome(biomeID, biome);
            }
        });
        BiomeAPI.onFinishingNetherBiomeTags((biomeID, biome) -> {
            if (!biomeID.getNamespace().equals(BetterNether.MOD_ID)) {
                NetherStructures.addNonBNBiomeTags(biomeID, biome);
            }
        });
        registerNumericProviders();
    }

    private static void registerNumericProviders() {
        registerNumericProvider(
                BetterNether.makeID("nether_grasslands"),
                NetherGrasslandsNumericProvider.CODEC
        );
        registerNumericProvider(
                BetterNether.makeID("nether_mushroom_forrest_edge"),
                NetherMushroomForestEdgeNumericProvider.CODEC
        );
    }

    private static void registerNumericProvider(
            net.minecraft.resources.ResourceLocation id,
            com.mojang.serialization.Codec<? extends NumericProvider> codec
    ) {
        if (!NumericProvider.NUMERIC_PROVIDER.containsKey(id)) {
            Registry.register(
                    NumericProvider.NUMERIC_PROVIDER,
                    id,
                    codec
            );
        }
    }
}
