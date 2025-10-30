package chihalu.buildassist.client.gui;

import chihalu.buildassist.config.BuildAssistConfig;
import chihalu.buildassist.config.DisplayLocation;
import chihalu.buildassist.config.OverlayHighlightColor;
import chihalu.buildassist.config.OverlayStyle;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class BuildAssistConfigScreen extends Screen {
        private static final int BUTTON_WIDTH = 220;
        private static final int BUTTON_HEIGHT = 20;
        private static final int OVERLAY_RANGE_MIN = BuildAssistConfig.OVERLAY_RANGE_MIN;
        private static final int OVERLAY_RANGE_MAX = BuildAssistConfig.OVERLAY_RANGE_MAX;
        private static final int HIGHLIGHT_INTERVAL_MIN = BuildAssistConfig.HIGHLIGHT_INTERVAL_MIN;
        private static final int HIGHLIGHT_INTERVAL_MAX = BuildAssistConfig.HIGHLIGHT_INTERVAL_MAX;

        private final Screen parent;
        private ButtonWidget displayLocationButton;
        private ButtonWidget overlayVisibilityButton;
        private ButtonWidget highlightColorButton;
        private ButtonWidget overlayStyleButton;
        private OverlayRangeSlider overlayRangeSlider;
        private HighlightIntervalSlider highlightIntervalSlider;
        private ButtonWidget highlightOriginButton;

        public BuildAssistConfigScreen(Screen parent) {
                super(Text.translatable("text.buildassist.config.title"));
                this.parent = parent;
        }

        @Override
        protected void init() {
                int centerX = this.width / 2;
                int verticalSpacing = BUTTON_HEIGHT + 6;
                int totalRows = 7;
                int top = this.height / 2 - (totalRows * verticalSpacing) / 2;
                int currentY = top;

                this.displayLocationButton = ButtonWidget.builder(currentDisplayLabel(), button -> {
                        DisplayLocation next = BuildAssistConfig.getDisplayLocation().next();
                        BuildAssistConfig.setDisplayLocation(next);
                        BuildAssistConfig.save();
                        button.setMessage(currentDisplayLabel());
                }).dimensions(centerX - BUTTON_WIDTH / 2, currentY, BUTTON_WIDTH, BUTTON_HEIGHT).build();

                this.addDrawableChild(this.displayLocationButton);

                currentY += verticalSpacing;

                this.overlayVisibilityButton = ButtonWidget.builder(currentOverlayVisibilityLabel(), button -> {
                        boolean newValue = !BuildAssistConfig.isOverlayAlwaysVisible();
                        BuildAssistConfig.setOverlayAlwaysVisible(newValue);
                        BuildAssistConfig.save();
                        button.setMessage(currentOverlayVisibilityLabel());
                }).dimensions(centerX - BUTTON_WIDTH / 2, currentY, BUTTON_WIDTH, BUTTON_HEIGHT).build();
                this.addDrawableChild(this.overlayVisibilityButton);

                currentY += verticalSpacing;

                this.highlightColorButton = ButtonWidget.builder(currentHighlightColorLabel(), button -> {
                        OverlayHighlightColor next = BuildAssistConfig.getOverlayHighlightColor().next();
                        BuildAssistConfig.setOverlayHighlightColor(next);
                        BuildAssistConfig.save();
                        button.setMessage(currentHighlightColorLabel());
                }).dimensions(centerX - BUTTON_WIDTH / 2, currentY, BUTTON_WIDTH, BUTTON_HEIGHT).build();
                this.addDrawableChild(this.highlightColorButton);

                currentY += verticalSpacing;

                this.overlayStyleButton = ButtonWidget.builder(currentOverlayStyleLabel(), button -> {
                        OverlayStyle next = BuildAssistConfig.getOverlayStyle().next();
                        BuildAssistConfig.setOverlayStyle(next);
                        BuildAssistConfig.save();
                        button.setMessage(currentOverlayStyleLabel());
                        this.highlightColorButton.setMessage(currentHighlightColorLabel());
                }).dimensions(centerX - BUTTON_WIDTH / 2, currentY, BUTTON_WIDTH, BUTTON_HEIGHT).build();
                this.addDrawableChild(this.overlayStyleButton);

                currentY += verticalSpacing;

                this.overlayRangeSlider = new OverlayRangeSlider(centerX - BUTTON_WIDTH / 2, currentY, BUTTON_WIDTH,
                                BUTTON_HEIGHT);
                this.addDrawableChild(this.overlayRangeSlider);

                currentY += verticalSpacing;

                this.highlightIntervalSlider = new HighlightIntervalSlider(centerX - BUTTON_WIDTH / 2, currentY,
                                BUTTON_WIDTH, BUTTON_HEIGHT);
                this.addDrawableChild(this.highlightIntervalSlider);

                currentY += verticalSpacing;

                this.highlightOriginButton = ButtonWidget.builder(currentHighlightOriginLabel(), button -> {
                        boolean newValue = !BuildAssistConfig.isHighlightIncludeOrigin();
                        BuildAssistConfig.setHighlightIncludeOrigin(newValue);
                        BuildAssistConfig.save();
                        button.setMessage(currentHighlightOriginLabel());
                }).dimensions(centerX - BUTTON_WIDTH / 2, currentY, BUTTON_WIDTH, BUTTON_HEIGHT).build();
                this.addDrawableChild(this.highlightOriginButton);

                this.addDrawableChild(ButtonWidget.builder(Text.translatable("gui.done"), button -> this.close())
                                .dimensions(centerX - BUTTON_WIDTH / 2, top + verticalSpacing * totalRows + 8, BUTTON_WIDTH,
                                                BUTTON_HEIGHT)
                                .build());
        }

        private Text currentDisplayLabel() {
                return Text.translatable("text.buildassist.config.display_location",
                                BuildAssistConfig.getDisplayLocation().getLabel());
        }

        private Text currentHighlightColorLabel() {
                return Text.translatable("text.buildassist.config.overlay_highlight_color",
                                BuildAssistConfig.getOverlayHighlightColor().getLabel());
        }

        private Text currentOverlayStyleLabel() {
                return Text.translatable("text.buildassist.config.overlay_style",
                                BuildAssistConfig.getOverlayStyle().getLabel());
        }

        private Text currentOverlayVisibilityLabel() {
                String stateKey = BuildAssistConfig.isOverlayAlwaysVisible()
                                ? "text.buildassist.config.overlay_visibility.always"
                                : "text.buildassist.config.overlay_visibility.hoe_only";
                return Text.translatable("text.buildassist.config.overlay_visibility",
                                Text.translatable(stateKey));
        }

        private Text currentHighlightOriginLabel() {
                String key = BuildAssistConfig.isHighlightIncludeOrigin()
                                ? "text.buildassist.config.overlay_highlight_origin.include"
                                : "text.buildassist.config.overlay_highlight_origin.exclude";
                return Text.translatable(key);
        }

        @Override
        public void close() {
                if (this.client != null) {
                        this.client.setScreen(this.parent);
                }
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
                context.fill(0, 0, this.width, this.height, 0xB0000000);
                super.render(context, mouseX, mouseY, delta);
                context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2,
                                this.height / 2 - BUTTON_HEIGHT - 20, 0xFFFFFF);
        }

        private final class OverlayRangeSlider extends SliderWidget {
                OverlayRangeSlider(int x, int y, int width, int height) {
                        super(x, y, width, height,
                                        Text.translatable("text.buildassist.config.overlay_range",
                                                        BuildAssistConfig.getOverlayRange()),
                                        normalize(BuildAssistConfig.getOverlayRange()));
                        updateMessage();
                }

                private int currentRange() {
                        int range = (int) Math.round(this.value * (OVERLAY_RANGE_MAX - OVERLAY_RANGE_MIN))
                                        + OVERLAY_RANGE_MIN;
                        return Math.max(OVERLAY_RANGE_MIN, Math.min(OVERLAY_RANGE_MAX, range));
                }

                @Override
                protected void updateMessage() {
                        this.setMessage(Text.translatable("text.buildassist.config.overlay_range", currentRange()));
                }

                @Override
                protected void applyValue() {
                        BuildAssistConfig.setOverlayRange(currentRange());
                        BuildAssistConfig.save();
                }

                private static double normalize(int range) {
                        int clamped = Math.max(OVERLAY_RANGE_MIN, Math.min(OVERLAY_RANGE_MAX, range));
                        return (double) (clamped - OVERLAY_RANGE_MIN) / (OVERLAY_RANGE_MAX - OVERLAY_RANGE_MIN);
                }
        }

        private final class HighlightIntervalSlider extends SliderWidget {
                HighlightIntervalSlider(int x, int y, int width, int height) {
                        super(x, y, width, height,
                                        Text.translatable("text.buildassist.config.overlay_highlight_interval",
                                                        BuildAssistConfig.getOverlayHighlightInterval()),
                                        normalize(BuildAssistConfig.getOverlayHighlightInterval()));
                        updateMessage();
                }

                private int currentInterval() {
                        int interval = (int) Math.round(this.value * (HIGHLIGHT_INTERVAL_MAX - HIGHLIGHT_INTERVAL_MIN))
                                        + HIGHLIGHT_INTERVAL_MIN;
                        return Math.max(HIGHLIGHT_INTERVAL_MIN, Math.min(HIGHLIGHT_INTERVAL_MAX, interval));
                }

                @Override
                protected void updateMessage() {
                        this.setMessage(Text.translatable("text.buildassist.config.overlay_highlight_interval",
                                        currentInterval()));
                }

                @Override
                protected void applyValue() {
                        BuildAssistConfig.setOverlayHighlightInterval(currentInterval());
                        BuildAssistConfig.save();
                }

                private static double normalize(int interval) {
                        int clamped = Math.max(HIGHLIGHT_INTERVAL_MIN, Math.min(HIGHLIGHT_INTERVAL_MAX, interval));
                        return (double) (clamped - HIGHLIGHT_INTERVAL_MIN)
                                        / (HIGHLIGHT_INTERVAL_MAX - HIGHLIGHT_INTERVAL_MIN);
                }
        }
}
