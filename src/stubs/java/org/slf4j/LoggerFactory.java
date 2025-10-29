package org.slf4j;

/**
 * Very small LoggerFactory that provides no-op loggers for offline compilation.
 */
public final class LoggerFactory {
        private LoggerFactory() {
        }

        private static final Logger LOGGER = new Logger() {
        };

        public static Logger getLogger(String name) {
                return LOGGER;
        }
}
