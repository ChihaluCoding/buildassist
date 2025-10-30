package net.minecraft.client.gui.widget;

import net.minecraft.text.Text;

/**
 * Minimal slider widget stub to allow compilation when offline.
 */
public abstract class SliderWidget extends ClickableWidget {
        protected double value;

        protected SliderWidget(int x, int y, int width, int height, Text message, double value) {
                super(x, y, width, height, message);
                this.value = value;
        }

        protected abstract void updateMessage();

        protected abstract void applyValue();

        public double getValue() {
                return value;
        }

        public void setValue(double value) {
                this.value = value;
                updateMessage();
                applyValue();
        }
}
