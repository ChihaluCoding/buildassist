package net.minecraft.world;

import net.minecraft.registry.RegistryKey;

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
}
