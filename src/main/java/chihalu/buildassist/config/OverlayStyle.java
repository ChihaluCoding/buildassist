package chihalu.buildassist.config;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;

/**
 * Defines overlay appearance presets by combining particle offsets.
 */
public enum OverlayStyle {
        SINGLE_POINT("text.buildassist.config.overlay_style.single", ParticleTypes.END_ROD,
                        new Offset(0.0, 0.0, 0.0)),
        DOUBLE_VERTICAL("text.buildassist.config.overlay_style.double_vertical", ParticleTypes.SOUL_FIRE_FLAME,
                        new Offset(0.0, 0.0, 0.0), new Offset(0.0, 0.35, 0.0)),
        CROSS_PLANE("text.buildassist.config.overlay_style.cross_plane", ParticleTypes.FLAME,
                        new Offset(0.0, 0.0, 0.0), new Offset(0.25, 0.0, 0.0),
                        new Offset(-0.25, 0.0, 0.0), new Offset(0.0, 0.0, 0.25), new Offset(0.0, 0.0, -0.25)),
        SQUARE_RING("text.buildassist.config.overlay_style.square_ring", ParticleTypes.HAPPY_VILLAGER,
                        new Offset(0.0, 0.0, 0.0), new Offset(0.2, 0.0, 0.2), new Offset(0.2, 0.0, -0.2),
                        new Offset(-0.2, 0.0, 0.2), new Offset(-0.2, 0.0, -0.2)),
        COLUMN("text.buildassist.config.overlay_style.column", ParticleTypes.END_ROD,
                        new Offset(0.0, 0.0, 0.0), new Offset(0.0, 0.3, 0.0), new Offset(0.0, 0.6, 0.0),
                        new Offset(0.0, 0.9, 0.0));

        private final String translationKey;
        private final ParticleEffect baseEffect;
        private final Offset[] offsets;

        OverlayStyle(String translationKey, ParticleEffect baseEffect, Offset... offsets) {
                this.translationKey = translationKey;
                this.baseEffect = baseEffect;
                this.offsets = offsets;
        }

        public Text getLabel() {
                return Text.translatable(this.translationKey);
        }

        public ParticleEffect getBaseEffect() {
                return baseEffect;
        }

        public Offset[] getOffsets() {
                return offsets;
        }

        public OverlayStyle next() {
                int idx = (this.ordinal() + 1) % values().length;
                return values()[idx];
        }

        public record Offset(double x, double y, double z) {
        }
}
