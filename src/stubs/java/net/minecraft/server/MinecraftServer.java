package net.minecraft.server;

/**
 * Minimal Minecraft server stub for offline compilation.
 */
public class MinecraftServer {
        public void execute(Runnable runnable) {
                if (runnable != null) {
                        runnable.run();
                }
        }
}
