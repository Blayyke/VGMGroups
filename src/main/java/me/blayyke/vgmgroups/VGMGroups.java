package me.blayyke.vgmgroups;

import com.google.common.reflect.TypeToken;
import me.blayyke.vgmgroups.command.Command;
import me.blayyke.vgmgroups.command.groups.CommandGroup;
import me.blayyke.vgmgroups.listener.DamageListener;
import me.blayyke.vgmgroups.manager.ConfigManager;
import me.blayyke.vgmgroups.manager.DataManager;
import me.blayyke.vgmgroups.manager.GroupManager;
import me.blayyke.vgmgroups.relationship.GroupRelationship;
import me.blayyke.vgmgroups.relationship.Relationship;
import me.blayyke.vgmgroups.serializer.GroupRelationshipSerializer;
import me.blayyke.vgmgroups.serializer.GroupSerializer;
import me.blayyke.vgmgroups.serializer.RelationshipSerializer;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.game.state.*;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

@Plugin(id = Refs.ID, name = Refs.NAME, authors = "Blayyke", description = Refs.DESCRIPTION, version = Refs.VERSION)
public class VGMGroups {
    @Inject
    private Logger logger;

    @Inject
    private static PluginContainer pluginContainer;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private ConfigurationLoader<CommentedConfigurationNode> loader;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private Path path;

    @Listener
    public void preInit(GamePreInitializationEvent event) throws IOException {
        logger.info("GamePreInit");
        ConfigManager.getInstance().setConfigPath(path);
        ConfigManager.getInstance().setLoader(loader);
        ConfigManager.getInstance().loadConfig();

        loadCommands();

        DataManager.getInstance().load();
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(Group.class), new GroupSerializer());
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(Relationship.class), new RelationshipSerializer());
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(GroupRelationship.class), new GroupRelationshipSerializer());

        Sponge.getEventManager().registerListeners(this, new DamageListener());
    }

    private void loadCommands() {
        loadCommand(new CommandGroup());
    }

    private void loadCommand(Command command) {
        Sponge.getCommandManager().register(this, command.getSpec(), command.getAliases());
        logger.info("Registered command " + command.getAliases());
    }

    @Listener
    public void init(GameInitializationEvent event) {
        logger.info("GameInit");
    }

    @Listener
    public void postInit(GamePostInitializationEvent event) {
        logger.info("GameInit");
    }

    @Listener
    public void serverAboutToStart(GameAboutToStartServerEvent event) {
        logger.info("ServerAboutToStart");
    }

    @Listener
    public void serverStarting(GameStartingServerEvent event) {
        logger.info("ServerStarting");
    }

    @Listener
    public void serverStart(GameStartedServerEvent event) {
        logger.info("ServerStart");
    }

    @Listener
    public void playerMove(MoveEntityEvent entityEvent, @First Player player) {
        Location<World> location = player.getLocation();
        Optional<Chunk> chunkOpt = location.getExtent().getChunk(location.getChunkPosition());
        if (!chunkOpt.isPresent()) return;
        Chunk chunk = chunkOpt.get();
//        chunk.loca
    }

    @Listener
    public void serverStop(GameStoppingServerEvent event) throws IOException {
        GroupManager.getInstance().saveGroups();
    }

    public static PluginContainer getPluginContainer() {
        return pluginContainer;
    }
}