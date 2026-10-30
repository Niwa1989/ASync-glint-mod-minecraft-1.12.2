package dev.async.glint.client;

import dev.async.glint.ASyncGlintConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public final class GlintRenderHook {
    private static final ResourceLocation GLINT =
            new ResourceLocation("textures/misc/enchanted_item_glint.png");
    private static final double MAX_DISTANCE_SQ = 64.0D * 64.0D;

    private GlintRenderHook() {
    }

    public static void renderOptimized(RenderLivingBase<?> renderer, EntityLivingBase entity,
                                       ModelBase model, float limbSwing, float limbSwingAmount,
                                       float partialTicks, float ageInTicks, float netHeadYaw,
                                       float headPitch, float scale) {
        if (!ASyncGlintConfig.isEnabled()) {
            return;
        }

        Minecraft minecraft = Minecraft.getMinecraft();
        Entity view = minecraft.getRenderViewEntity();
        if (view != null && view != entity && entity.getDistanceSq(view) > MAX_DISTANCE_SQ) {
            return;
        }

        float time = entity.ticksExisted + partialTicks;
        renderer.bindTexture(GLINT);
        minecraft.entityRenderer.setupFogColor(true);
        GlStateManager.enableBlend();
        GlStateManager.depthFunc(GL11.GL_EQUAL);
        GlStateManager.depthMask(false);
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(
                GlStateManager.SourceFactor.SRC_COLOR,
                GlStateManager.DestFactor.ONE
        );
        applyColor(time);
        GlStateManager.matrixMode(GL11.GL_TEXTURE);
        GlStateManager.loadIdentity();
        GlStateManager.scale(0.33333334F, 0.33333334F, 0.33333334F);
        GlStateManager.rotate(30.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.translate(0.0F, time * 0.001F * 20.0F, 0.0F);
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GlStateManager.blendFunc(
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
        );

        GlStateManager.matrixMode(GL11.GL_TEXTURE);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.enableLighting();
        GlStateManager.depthMask(true);
        GlStateManager.depthFunc(GL11.GL_LEQUAL);
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.entityRenderer.setupFogColor(false);
    }

    private static void applyColor(float time) {
        float red;
        float green;
        float blue;
        switch (ASyncGlintConfig.color) {
            case "cyan":
                red = 0.15F; green = 0.80F; blue = 1.00F;
                break;
            case "white":
                red = 0.68F; green = 0.68F; blue = 0.68F;
                break;
            case "gold":
                red = 0.95F; green = 0.62F; blue = 0.12F;
                break;
            case "red":
                red = 0.95F; green = 0.18F; blue = 0.18F;
                break;
            case "green":
                red = 0.18F; green = 0.82F; blue = 0.28F;
                break;
            case "purple":
                red = 0.68F; green = 0.25F; blue = 0.95F;
                break;
            case "rainbow":
                int rgb = java.awt.Color.HSBtoRGB((time * 0.012F) % 1.0F, 0.78F, 0.95F);
                red = ((rgb >> 16) & 255) / 255.0F;
                green = ((rgb >> 8) & 255) / 255.0F;
                blue = (rgb & 255) / 255.0F;
                break;
            case "blue":
            default:
                red = 0.20F; green = 0.45F; blue = 0.95F;
                break;
        }
        float opacity = ASyncGlintConfig.opacityFactor();
        GlStateManager.color(red * opacity, green * opacity, blue * opacity, opacity);
    }
}
