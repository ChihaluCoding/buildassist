package net.minecraft.client;

import net.minecraft.client.gui.screen.Screen;

public class MinecraftClient {
        private Screen currentScreen;

        public void setScreen(Screen screen) {
                this.currentScreen = screen;
        }

        public Screen currentScreen() {
                return currentScreen;
        }
}
