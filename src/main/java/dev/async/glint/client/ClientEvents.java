package dev.async.glint.client;

import dev.async.glint.ASyncGlintConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCustomizeSkin;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class ClientEvents {
    private static final int COLOR_BUTTON = 852201;
    private static final int OPACITY_SLIDER = 852202;

    @SubscribeEvent
    public void onGuiInit(GuiScreenEvent.InitGuiEvent.Post event) {
        if (!(event.getGui() instanceof GuiCustomizeSkin)
                || Loader.isModLoaded("asynctactical")) {
            return;
        }

        GuiButton doneButton = null;
        for (GuiButton button : event.getButtonList()) {
            if (button.id == 200) {
                doneButton = button;
                break;
            }
        }

        int buttonY = event.getGui().height / 6 + 96;
        if (doneButton != null) {
            buttonY = doneButton.y;
            doneButton.y += 48;
        }

        event.getButtonList().add(new GuiButton(
                COLOR_BUTTON,
                event.getGui().width / 2 - 100,
                buttonY,
                200,
                20,
                colorButtonLabel()
        ));
        event.getButtonList().add(new OpacitySlider(
                OPACITY_SLIDER,
                event.getGui().width / 2 - 100,
                buttonY + 24,
                ASyncGlintConfig.opacity
        ));
    }

    @SubscribeEvent
    public void onGuiAction(GuiScreenEvent.ActionPerformedEvent.Post event) {
        if (event.getGui() instanceof GuiCustomizeSkin
                && event.getButton().id == COLOR_BUTTON) {
            ASyncGlintConfig.cycleColor();
            event.getButton().displayString = colorButtonLabel();
        }
    }

    private static String colorButtonLabel() {
        String nameKey = "async_glint.color." + ASyncGlintConfig.color;
        String name = I18n.format(nameKey);
        if (nameKey.equals(name)) {
            name = fallbackColorName(ASyncGlintConfig.color);
        }
        String labelKey = "async_glint.gui.color";
        String label = I18n.format(labelKey, name);
        return label.startsWith(labelKey) ? "Glint Color: " + name : label;
    }

    private static String fallbackColorName(String id) {
        switch (id) {
            case "cyan": return "Cyan";
            case "white": return "White";
            case "gold": return "Gold";
            case "red": return "Red";
            case "green": return "Green";
            case "purple": return "Purple";
            case "rainbow": return "Rainbow";
            case "off": return "Off";
            default: return "Blue";
        }
    }

    private static final class OpacitySlider extends GuiSlider {
        private OpacitySlider(int id, int x, int y, int currentValue) {
            super(
                    id, x, y, 200, 20,
                    opacityPrefix(), "%",
                    25.0D, 100.0D, currentValue,
                    false, true,
                    slider -> ASyncGlintConfig.setOpacity(slider.getValueInt(), false)
            );
        }

        @Override
        public void mouseReleased(int mouseX, int mouseY) {
            super.mouseReleased(mouseX, mouseY);
            ASyncGlintConfig.setOpacity(getValueInt(), true);
        }

        private static String opacityPrefix() {
            String key = "async_glint.gui.opacity";
            String translated = I18n.format(key, "");
            if (translated.startsWith(key)) {
                return "Glint Opacity: ";
            }
            int marker = translated.indexOf('%');
            return marker >= 0 ? translated.substring(0, marker) : translated;
        }
    }
}
