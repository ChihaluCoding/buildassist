package org.slf4j;

/**
 * Minimal SLF4J Logger used for offline builds.
 */
public interface Logger {
        default void info(String message) {
        }
}
