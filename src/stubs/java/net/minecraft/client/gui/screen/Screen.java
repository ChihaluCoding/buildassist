package net.minecraft.client.gui.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class Screen {
        protected final Text title;
        protected MinecraftClient client;
        protected int width;
        protected int height;
        protected final TextRenderer textRenderer = new TextRenderer();

        protected Screen(Text title) {
                this.title = title;
        }

        protected void init() {
        }

        protected <T> T addDrawableChild(T widget) {
                return widget;
        }

        public void close() {
        }

        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        }

        public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        }
}
