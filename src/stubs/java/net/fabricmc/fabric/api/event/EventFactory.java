package net.fabricmc.fabric.api.event;

import java.util.function.Function;

public final class EventFactory {
        private EventFactory() {
        }

        public static <T> Event<T> createArrayBacked(Class<T> type, Function<T[], T> factory) {
                return null;
        }
}
