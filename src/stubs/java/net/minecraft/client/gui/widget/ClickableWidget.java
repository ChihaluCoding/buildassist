package net.minecraft.client.gui.widget;

import net.minecraft.text.Text;

/**
 * Minimal clickable widget base class for compilation with bundled stubs.
 */
public abstract class ClickableWidget {
        protected int x;
        protected int y;
        protected int width;
        protected int height;
        protected Text message;

        protected ClickableWidget(int x, int y, int width, int height, Text message) {
                this.x = x;
                this.y = y;
                this.width = width;
                this.height = height;
                this.message = message;
        }

        public void setMessage(Text message) {
                this.message = message;
        }

        public Text getMessage() {
                return message;
        }
}
