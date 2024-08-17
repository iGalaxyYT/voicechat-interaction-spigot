package dev.igalaxy.voicechatinteraction;

import de.maxhenkel.voicechat.api.BukkitVoicechatService;
import dev.igalaxy.voicechatinteraction.config.ServerConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.GameEvent;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;

public final class VoiceChatInteraction extends JavaPlugin {

    public static final String PLUGIN_ID = "voicechat_interaction";
    public static final Logger LOGGER = LogManager.getLogger(PLUGIN_ID);
    public static ServerConfig SERVER_CONFIG;
    public static Server SERVER;
    public static GameEvent VOICE_GAME_EVENT;
    public static VoiceChatInteraction INSTANCE;

    @Nullable
    public static VoiceChatInteractionPlugin voicechatPlugin;

    @Override
    public void onEnable() {
        SERVER = getServer();
        VOICE_GAME_EVENT = GameEvent.EAT;
        INSTANCE = this;

        FileConfiguration config = this.getConfig();
        config.addDefault("group_interaction", false);
        config.addDefault("whisper_interaction", false);
        config.addDefault("sneak_interaction", false);
        config.addDefault("minimum_activation_threshold", -50);
        config.addDefault("default_interaction_toggle", true);
        config.options().copyDefaults(true);
        saveConfig();

        SERVER_CONFIG = new ServerConfig(config);

        BukkitVoicechatService service = getServer().getServicesManager().load(BukkitVoicechatService.class);
        if (service != null) {
            voicechatPlugin = new VoiceChatInteractionPlugin();
            service.registerPlugin(voicechatPlugin);
            LOGGER.info("Successfully registered voicechat_interaction plugin");
        } else {
            LOGGER.info("Failed to register voicechat_interaction plugin");
        }

        this.getCommand("voicechat_interaction").setExecutor(new VoiceChatInteractionCommand());
    }

    @Override
    public void onDisable() {
        if (voicechatPlugin != null) {
            getServer().getServicesManager().unregister(voicechatPlugin);
            LOGGER.info("Successfully unregistered voicechat_interaction plugin");
        }
    }
}
