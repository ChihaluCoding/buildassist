package net.minecraft.particle;

/**
 * Provides particle constants for offline compilation.
 */
public final class ParticleTypes {
        public static final ParticleEffect END_ROD = new SimpleParticleEffect();
        public static final ParticleEffect FLAME = new SimpleParticleEffect();
        public static final ParticleEffect SOUL_FIRE_FLAME = new SimpleParticleEffect();
        public static final ParticleEffect HAPPY_VILLAGER = new SimpleParticleEffect();

        private ParticleTypes() {
        }

        private static final class SimpleParticleEffect implements ParticleEffect {
        }
}
