package com.terraformersmc.modmenu.api;

import java.util.function.Function;

import net.minecraft.client.gui.screen.Screen;

@FunctionalInterface
public interface ConfigScreenFactory<S extends Screen> extends Function<Screen, S> {
        @Override
        S apply(Screen screen);
}
