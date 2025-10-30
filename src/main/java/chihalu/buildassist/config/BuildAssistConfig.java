package chihalu.buildassist.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import net.fabricmc.loader.api.FabricLoader;

/**
 * Handles persistence of user configurable settings for BuildAssist.
 */
public final class BuildAssistConfig {
        public static final int OVERLAY_RANGE_MIN = 1;
        public static final int OVERLAY_RANGE_MAX = 200;
        public static final int HIGHLIGHT_INTERVAL_MIN = 1;
        public static final int HIGHLIGHT_INTERVAL_MAX = 200;

        private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
        private static final String CONFIG_FILE_NAME = "buildassist.json";
        private static final BuildAssistConfig INSTANCE = new BuildAssistConfig();

        private DisplayLocation displayLocation = DisplayLocation.ACTION_BAR;
        private int overlayRange = 50;
        private boolean overlayAlwaysVisible = false;
        private OverlayStyle overlayStyle = OverlayStyle.SINGLE_POINT;
        private int overlayHighlightInterval = 10;
        private Boolean highlightIncludeOrigin = Boolean.TRUE;
        private OverlayHighlightColor overlayHighlightColor = OverlayHighlightColor.RED;

        private BuildAssistConfig() {
        }

        public static void load() {
                Path configPath = resolveConfigPath();
                if (!Files.exists(configPath)) {
                        save(); // writes defaults
                        return;
                }

                try (Reader reader = new BufferedReader(Files.newBufferedReader(configPath))) {
                        BuildAssistConfig loaded = GSON.fromJson(reader, BuildAssistConfig.class);
                        if (loaded != null) {
                                if (loaded.displayLocation != null) {
                                        INSTANCE.displayLocation = loaded.displayLocation;
                                }
                                if (loaded.overlayRange >= OVERLAY_RANGE_MIN) {
                                        INSTANCE.overlayRange = clampRange(loaded.overlayRange);
                                }
                                INSTANCE.overlayAlwaysVisible = loaded.overlayAlwaysVisible;
                                if (loaded.overlayStyle != null) {
                                        INSTANCE.overlayStyle = loaded.overlayStyle;
                                }
                                if (loaded.overlayHighlightInterval >= HIGHLIGHT_INTERVAL_MIN) {
                                        INSTANCE.overlayHighlightInterval = clampInterval(loaded.overlayHighlightInterval);
                                }
                                if (loaded.highlightIncludeOrigin != null) {
                                        INSTANCE.highlightIncludeOrigin = loaded.highlightIncludeOrigin;
                                }
                                if (loaded.overlayHighlightColor != null) {
                                        INSTANCE.overlayHighlightColor = loaded.overlayHighlightColor;
                                }
                        }
                } catch (IOException | JsonSyntaxException ex) {
                        BuildAssistConfig.save(); // reset to defaults if load fails
                }
        }

        public static void save() {
                Path configPath = resolveConfigPath();
                try {
                        Files.createDirectories(configPath.getParent());
                } catch (IOException ignored) {
                        return;
                }

                try (Writer writer = new BufferedWriter(Files.newBufferedWriter(configPath))) {
                        GSON.toJson(INSTANCE, writer);
                } catch (IOException ignored) {
                        // Swallow - failing to save should not crash the game.
                }
        }

        public static DisplayLocation getDisplayLocation() {
                return INSTANCE.displayLocation;
        }

        public static void setDisplayLocation(DisplayLocation location) {
                INSTANCE.displayLocation = Objects.requireNonNull(location, "location");
        }

        public static int getOverlayRange() {
                return INSTANCE.overlayRange;
        }

        public static void setOverlayRange(int range) {
                INSTANCE.overlayRange = clampRange(range);
        }

        public static boolean isOverlayAlwaysVisible() {
                return INSTANCE.overlayAlwaysVisible;
        }

        public static void setOverlayAlwaysVisible(boolean visible) {
                INSTANCE.overlayAlwaysVisible = visible;
        }

        public static OverlayStyle getOverlayStyle() {
                return INSTANCE.overlayStyle;
        }

        public static void setOverlayStyle(OverlayStyle style) {
                INSTANCE.overlayStyle = Objects.requireNonNull(style, "style");
        }

        public static int getOverlayHighlightInterval() {
                return INSTANCE.overlayHighlightInterval;
        }

        public static void setOverlayHighlightInterval(int interval) {
                INSTANCE.overlayHighlightInterval = clampInterval(interval);
        }

        public static boolean isHighlightIncludeOrigin() {
                return Boolean.TRUE.equals(INSTANCE.highlightIncludeOrigin);
        }

        public static void setHighlightIncludeOrigin(boolean includeOrigin) {
                INSTANCE.highlightIncludeOrigin = includeOrigin;
        }

        public static OverlayHighlightColor getOverlayHighlightColor() {
                return INSTANCE.overlayHighlightColor;
        }

        public static void setOverlayHighlightColor(OverlayHighlightColor color) {
                INSTANCE.overlayHighlightColor = Objects.requireNonNull(color, "color");
        }

        private static int clampRange(int range) {
                return Math.max(OVERLAY_RANGE_MIN, Math.min(OVERLAY_RANGE_MAX, range));
        }

        private static int clampInterval(int interval) {
                return Math.max(HIGHLIGHT_INTERVAL_MIN, Math.min(HIGHLIGHT_INTERVAL_MAX, interval));
        }

        private static Path resolveConfigPath() {
                return FabricLoader.getInstance().getConfigDir().resolve(CONFIG_FILE_NAME);
        }
}
