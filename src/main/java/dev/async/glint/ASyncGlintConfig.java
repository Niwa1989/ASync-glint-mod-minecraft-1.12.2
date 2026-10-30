package dev.async.glint;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public final class ASyncGlintConfig {
    public static final String[] COLOR_IDS = new String[] {
            "blue", "cyan", "white", "gold", "red", "green", "purple", "rainbow", "off"
    };

    private static Configuration configuration;

    public static String color = "blue";
    public static int opacity = 100;

    private ASyncGlintConfig() {
    }

    public static void load(File file) {
        configuration = new Configuration(file);
        configuration.load();
        color = normalizeColor(configuration.getString(
                "color", "glint", "blue",
                "Armor enchantment glint color/style.", COLOR_IDS
        ));
        opacity = clamp(configuration.getInt(
                "opacity", "glint", 100, 25, 100,
                "Armor enchantment glint opacity/intensity. Every integer from 25 to 100 is supported."
        ));
        writeValues();
        save();
    }

    public static void cycleColor() {
        for (int index = 0; index < COLOR_IDS.length; index++) {
            if (COLOR_IDS[index].equals(color)) {
                color = COLOR_IDS[(index + 1) % COLOR_IDS.length];
                writeValues();
                save();
                return;
            }
        }
        color = COLOR_IDS[0];
        writeValues();
        save();
    }

    public static void setOpacity(int value, boolean persist) {
        opacity = clamp(value);
        if (persist) {
            writeValues();
            save();
        }
    }

    public static float opacityFactor() {
        return opacity / 100.0F;
    }

    public static boolean isEnabled() {
        return !"off".equals(color);
    }

    private static String normalizeColor(String value) {
        if (value != null) {
            for (String id : COLOR_IDS) {
                if (id.equalsIgnoreCase(value)) {
                    return id;
                }
            }
        }
        return COLOR_IDS[0];
    }

    private static int clamp(int value) {
        return Math.max(25, Math.min(100, value));
    }

    private static void writeValues() {
        if (configuration != null) {
            configuration.get("glint", "color", "blue").set(color);
            configuration.get("glint", "opacity", 100).set(opacity);
        }
    }

    private static void save() {
        if (configuration != null && configuration.hasChanged()) {
            configuration.save();
        }
    }
}
