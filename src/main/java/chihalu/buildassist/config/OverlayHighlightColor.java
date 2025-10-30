package chihalu.buildassist.config;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;

/**
 * Preset highlight color selections backed by particle effects.
 */
public enum OverlayHighlightColor {
        RED("text.buildassist.config.overlay_highlight_color.red", ParticleTypes.FLAME),
        BLUE("text.buildassist.config.overlay_highlight_color.blue", ParticleTypes.SOUL_FIRE_FLAME),
        GREEN("text.buildassist.config.overlay_highlight_color.green", ParticleTypes.HAPPY_VILLAGER),
        WHITE("text.buildassist.config.overlay_highlight_color.white", ParticleTypes.END_ROD);

        private final String translationKey;
        private final ParticleEffect particleEffect;

        OverlayHighlightColor(String translationKey, ParticleEffect particleEffect) {
                this.translationKey = translationKey;
                this.particleEffect = particleEffect;
        }

        public Text getLabel() {
                return Text.translatable(this.translationKey);
        }

        public ParticleEffect getParticleEffect() {
                return particleEffect;
        }

        public OverlayHighlightColor next() {
                int idx = (this.ordinal() + 1) % values().length;
                return values()[idx];
        }
}
