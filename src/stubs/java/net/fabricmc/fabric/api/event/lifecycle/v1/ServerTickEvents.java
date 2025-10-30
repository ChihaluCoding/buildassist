package net.fabricmc.fabric.api.event.lifecycle.v1;

import net.minecraft.server.world.ServerWorld;

/**
 * Minimal lifecycle event stubs for server tick callbacks.
 */
public final class ServerTickEvents {
        public static final EventHolder<EndWorldTick> END_WORLD_TICK = new EventHolder<>();

        private ServerTickEvents() {
        }

        public interface EndWorldTick {
                void onEndTick(ServerWorld world);
        }

        public static final class EventHolder<T> {
                public void register(T callback) {
                        // no-op
                }
        }
}
