package me.blayyke.vgmgroups.manager;

import me.blayyke.vgmgroups.VGMGroups;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {
    private static ConfigManager instance;
    private ConfigurationNode rootNode;
    private Path configPath;
    private ConfigurationLoader<CommentedConfigurationNode> loader;

    public void loadConfig() throws IOException {
        if (Files.notExists(configPath))
            VGMGroups.getPluginContainer().getAsset("default.conf").get().copyToFile(configPath);
        rootNode = loader.load();
    }

    public ConfigurationNode getNode(String key) {
        return rootNode.getNode(key);
    }

    public static ConfigManager getInstance() {
        if (instance == null) instance = new ConfigManager();
        return instance;
    }

    public void setConfigPath(Path configPath) {
        this.configPath = configPath;
    }

    public void setLoader(ConfigurationLoader<CommentedConfigurationNode> loader) {
        this.loader = loader;
    }
}