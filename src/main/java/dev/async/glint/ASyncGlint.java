package dev.async.glint;

import dev.async.glint.client.ClientEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = ASyncGlint.MOD_ID,
        name = ASyncGlint.NAME,
        version = ASyncGlint.VERSION,
        clientSideOnly = true,
        acceptableRemoteVersions = "*"
)
public final class ASyncGlint {
    public static final String MOD_ID = "async_glint";
    public static final String NAME = "ASync_glint";
    public static final String VERSION = "1.0.2";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ASyncGlintConfig.load(event.getSuggestedConfigurationFile());
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
    }
}
