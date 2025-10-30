package net.minecraft.world;

import net.minecraft.block.BlockState;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

/**
 * Simplified world implementation for offline builds.
 */
public class World {
        private final boolean client;
        private final RegistryKey<World> registryKey;

        public World(boolean client, RegistryKey<World> registryKey) {
                this.client = client;
                this.registryKey = registryKey;
        }

        public boolean isClient() {
                return client;
        }

        public RegistryKey<World> getRegistryKey() {
                return registryKey;
        }

        public BlockState getBlockState(BlockPos pos) {
                return new BlockState();
        }

        public MinecraftServer getServer() {
                return new MinecraftServer();
        }
}
