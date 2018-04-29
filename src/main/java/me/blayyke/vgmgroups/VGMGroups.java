package me.blayyke.vgmgroups;

import com.google.common.reflect.TypeToken;
import me.blayyke.vgmgroups.command.groups.CommandGroup;
import me.blayyke.vgmgroups.enums.Rank;
import me.blayyke.vgmgroups.enums.Relationship;
import me.blayyke.vgmgroups.listener.ChatListener;
import me.blayyke.vgmgroups.listener.DamageListener;
import me.blayyke.vgmgroups.listener.LandEditListener;
import me.blayyke.vgmgroups.listener.MoveListener;
import me.blayyke.vgmgroups.manager.ConfigManager;
import me.blayyke.vgmgroups.manager.DataManager;
import me.blayyke.vgmgroups.manager.GroupManager;
import me.blayyke.vgmgroups.serializer.*;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.*;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Path;

@Plugin(id = Refs.ID, name = Refs.NAME, authors = "Blayyke", description = Refs.DESCRIPTION, version = Refs.VERSION, dependencies = {@Dependency(id = "placeholderapi")})
public class VGMGroups {
    private static Logger logger = LoggerFactory.getLogger(Refs.NAME);
    private static VGMGroups plugin;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private ConfigurationLoader<CommentedConfigurationNode> loader;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private Path path;

    public static Logger getLogger() {
        return logger;
    }

    @Listener
    public void preInit(GamePreInitializationEvent event) throws IOException {
        plugin = this;
        registerTypeSerializers();

        ConfigManager.getInstance().setConfigPath(path);
        ConfigManager.getInstance().setLoader(loader);
        ConfigManager.getInstance().loadConfig();

        registerListeners();

        DataManager.getInstance().load();
    }

    private void registerListeners() {
        logger.info("Registering listeners");
        Sponge.getEventManager().registerListeners(this, new ChatListener());
        Sponge.getEventManager().registerListeners(this, new DamageListener());
        Sponge.getEventManager().registerListeners(this, new LandEditListener());
        Sponge.getEventManager().registerListeners(this, new MoveListener());
        logger.info("Registered listeners");
    }

    private void registerTypeSerializers() {
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(Group.class), new GroupSerializer());
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(GroupClaim.class), new GroupClaimSerializer());

        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(Relationship.class), new RelationshipSerializer());
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(GroupRelationship.class), new GroupRelationshipSerializer());

        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(Rank.class), new RankSerializer());
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(GroupRank.class), new GroupRankSerializer());
    }

    private void registerCommands() {
        new CommandGroup(this).register();
    }

    @Listener
    public void init(GameInitializationEvent event) {
        registerCommands();
    }

    @Listener
    public void postInit(GamePostInitializationEvent event) {
    }

    @Listener
    public void serverAboutToStart(GameAboutToStartServerEvent event) {
    }

    @Listener
    public void serverStarting(GameStartingServerEvent event) throws IOException, ObjectMappingException {
        GroupManager.getInstance().loadGroups();
    }

    @Listener
    public void serverStart(GameStartedServerEvent event) {
    }

    @Listener
    public void serverStop(GameStoppingServerEvent event) throws IOException, ObjectMappingException {
        GroupManager.getInstance().saveGroups();
    }

    public static VGMGroups getPlugin() {
        return plugin;
    }
}