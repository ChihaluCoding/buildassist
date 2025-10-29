package net.minecraft.text;

/**
 * Minimal representation of Minecraft's Text component.
 */
public class Text {
        private final String translationKey;
        private final Object[] args;

        private Text(String translationKey, Object[] args) {
                this.translationKey = translationKey;
                this.args = args;
        }

        public static Text translatable(String translationKey, Object... args) {
                return new Text(translationKey, args);
        }

        public String translationKey() {
                return translationKey;
        }

        public Object[] arguments() {
                return args.clone();
        }
}
