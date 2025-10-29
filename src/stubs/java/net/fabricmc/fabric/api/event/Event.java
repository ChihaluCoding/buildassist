package net.fabricmc.fabric.api.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Extremely small event bus stub that stores listeners but does not invoke them.
 */
public final class Event<T> {
        private final List<T> listeners = new ArrayList<>();

        public void register(T listener) {
                listeners.add(Objects.requireNonNull(listener, "listener"));
        }

        public List<T> listeners() {
                return List.copyOf(listeners);
        }
}
