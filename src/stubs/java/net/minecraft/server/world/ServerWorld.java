package net.minecraft.server.world;

import net.minecraft.block.BlockState;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Minimal server world stub for offline compilation.
 */
public class ServerWorld extends World {
        private final MinecraftServer server;

        public ServerWorld(boolean client, RegistryKey<World> registryKey, MinecraftServer server) {
                super(client, registryKey);
                this.server = server;
        }

        @Override
        public MinecraftServer getServer() {
                return server;
        }

        @Override
        public BlockState getBlockState(BlockPos pos) {
                return new BlockState();
        }

        public void setBlockState(BlockPos pos, BlockState state) {
                // no-op
        }
}
