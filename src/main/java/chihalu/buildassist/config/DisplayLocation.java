package chihalu.buildassist.config;

import net.minecraft.text.Text;

/**
 * Represents where block count feedback should appear.
 */
public enum DisplayLocation {
        ACTION_BAR("text.buildassist.config.display_location.action_bar"),
        CHAT("text.buildassist.config.display_location.chat");

        private final String translationKey;

        DisplayLocation(String translationKey) {
                this.translationKey = translationKey;
        }

        public Text getLabel() {
                return Text.translatable(translationKey);
        }

        public DisplayLocation next() {
                return switch (this) {
                case ACTION_BAR -> CHAT;
                case CHAT -> ACTION_BAR;
                };
        }
}
