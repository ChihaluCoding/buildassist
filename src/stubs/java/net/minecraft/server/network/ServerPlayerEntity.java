package net.minecraft.server.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

/**
 * Minimal representation of a server-side player for compilation only.
 */
public class ServerPlayerEntity extends PlayerEntity {
        private final ServerWorld world;

        public ServerPlayerEntity(ServerWorld world) {
                this.world = world;
        }

        public ServerWorld getWorld() {
                return world;
        }
}
