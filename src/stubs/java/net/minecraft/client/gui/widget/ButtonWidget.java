package net.minecraft.client.gui.widget;

import net.minecraft.text.Text;

/**
 * Simple stub implementation of Minecraft's ButtonWidget for offline builds.
 */
public class ButtonWidget extends ClickableWidget {
        private final PressAction onPress;

        private ButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress) {
                super(x, y, width, height, message);
                this.onPress = onPress;
        }

        public static Builder builder(Text message, PressAction onPress) {
                return new Builder(message, onPress);
        }

        public void press() {
                if (onPress != null) {
                        onPress.onPress(this);
                }
        }

        public interface PressAction {
                void onPress(ButtonWidget button);
        }

        public static class Builder {
                private final Text message;
                private final PressAction onPress;
                private int x;
                private int y;
                private int width = 20;
                private int height = 20;

                public Builder(Text message, PressAction onPress) {
                        this.message = message;
                        this.onPress = onPress;
                }

                public Builder dimensions(int x, int y, int width, int height) {
                        this.x = x;
                        this.y = y;
                        this.width = width;
                        this.height = height;
                        return this;
                }

                public ButtonWidget build() {
                        return new ButtonWidget(x, y, width, height, message, onPress);
                }
        }
}
