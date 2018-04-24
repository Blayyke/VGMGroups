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
    public void preInit(GamePreInitializationEvent event) throws IOException, ObjectMappingException {
        registerTypeSerializers();

        ConfigManager.getInstance().setConfigPath(path);
        ConfigManager.getInstance().setLoader(loader);
        ConfigManager.getInstance().loadConfig();

        registerListeners();
        registerCommands();

        GroupManager.getInstance().loadGroups();
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
    }

    @Listener
    public void postInit(GamePostInitializationEvent event) {
    }

    @Listener
    public void serverAboutToStart(GameAboutToStartServerEvent event) {
    }

    @Listener
    public void serverStarting(GameStartingServerEvent event) {
    }

    @Listener
    public void serverStart(GameStartedServerEvent event) {
    }

    @Listener
    public void playerMove(MoveEntityEvent entityEvent, @First Player player) {
        Location<World> location = player.getLocation();
        Optional<Chunk> chunkOpt = location.getExtent().getChunk(location.getChunkPosition());
        if (!chunkOpt.isPresent()) return;
        //// TODO: 19/04/2018
//        Chunk chunk = chunkOpt.get();
//        chunk.loca
    }

    @Listener
    public void serverStop(GameStoppingServerEvent event) throws IOException, ObjectMappingException {
        GroupManager.getInstance().saveGroups();
    }

    public static PluginContainer getPluginContainer() {
        return pluginContainer;
    }
}