package net.minecraft.registry;

import java.util.Objects;

/**
 * Simple registry key representation used for offline compilation.
 */
public final class RegistryKey<T> {
        private final String value;

        private RegistryKey(String value) {
                this.value = value;
        }

        public static <T> RegistryKey<T> of(String value) {
                return new RegistryKey<>(value);
        }

        @Override
        public boolean equals(Object obj) {
                if (this == obj) {
                        return true;
                }
                if (obj instanceof RegistryKey<?> other) {
                        return Objects.equals(value, other.value);
                }
                return false;
        }

        @Override
        public int hashCode() {
                return Objects.hash(value);
        }
}
