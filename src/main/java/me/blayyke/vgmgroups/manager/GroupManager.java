package me.blayyke.vgmgroups.manager;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.io.Files;
import com.google.common.reflect.TypeToken;
import me.blayyke.vgmgroups.Group;
import me.blayyke.vgmgroups.VGMGroups;
import me.blayyke.vgmgroups.enums.Relationship;
import me.blayyke.vgmgroups.event.GroupCreateEvent;
import me.blayyke.vgmgroups.event.GroupDeleteEvent;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.world.World;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GroupManager {
    private final String fileExtension = "vgroup";

    private static GroupManager instance;
    private List<Group> groups;

    private GroupManager() {
    }

    public void updateRelationship(Group group, Group targetGroup, Relationship relationship) {
        group.setRelationshipWith(targetGroup, relationship);
        targetGroup.setRelationshipWith(group, relationship);
    }

    public Optional<Group> getGroupByUUID(UUID uuid) {
        return groups.stream().filter(g -> g.getUUID().equals(uuid)).findFirst();
    }

    public Optional<Group> getGroupByName(String name) {
        return groups.stream().filter(g -> g.getName().equalsIgnoreCase(name)).findFirst();
    }

    public void loadGroups() throws IOException, ObjectMappingException {
        VGMGroups.getLogger().info("Attempting to load groups from disk");
        groups = new ArrayList<>();

        File[] files = DataManager.getInstance().getGroupsDir().listFiles();
        if (files == null) {
            VGMGroups.getLogger().info("Groups dir has no children files, no groups exist.");
            return;
        }

        for (File file : files) {
            if (!Files.getFileExtension(file.getName()).equalsIgnoreCase(fileExtension)) {
                VGMGroups.getLogger().info(String.format("File %s does not match extension %s skipping file.", file.getName(), fileExtension));
                continue;
            }

            HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setFile(file).build();
            ConfigurationNode rootNode = loader.load();

            Group group = rootNode.getValue(TypeToken.of(Group.class));
            if (group == null) VGMGroups.getLogger().error("GROUP IS NULL!");
            groups.add(group);
        }
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void deleteGroup(Group group) {
        EventContext context = EventContext.builder()
                .add(EventContextKeys.PLAYER, group.getOwner())
                .build();
        Cause cause = Cause.builder()
                .append(group.getOwner())
                .append(group)
                .append(VGMGroups.getPlugin())
                .build(context);
        if (Sponge.getEventManager().post(new GroupDeleteEvent(group.getOwner(), group, cause))) return;
        groups.remove(group);
        getGroupFile(group).delete();
        VGMGroups.getLogger().info("Deleted group " + group.getUUID());
    }

    private File getGroupFile(Group group) {
        File groupsDir = DataManager.getInstance().getGroupsDir();
        return new File(groupsDir, group.getUUID().toString() + "." + fileExtension);
    }

    public Optional<Group> getPlayerGroup(Player player) {
        return groups.stream().filter(group -> group.isInGroup(player)).findFirst();
    }

    public void saveGroups() throws IOException, ObjectMappingException {
        for (Group group : groups) saveGroup(group);
    }

    private void saveGroup(Group group) throws IOException, ObjectMappingException {
        File groupFile = getGroupFile(group);
        groupFile.createNewFile();

        HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setFile(groupFile).build();
        ConfigurationNode rootNode = loader.load();

        rootNode.setValue(TypeToken.of(Group.class), group);
        loader.save(rootNode);
    }

    public static GroupManager getInstance() {
        if (instance == null) instance = new GroupManager();
        return instance;
    }

    public void createNewGroup(Player player, String name) {
        Group group = new Group(player.getUniqueId(), name);

        EventContext context = EventContext.builder()
                .add(EventContextKeys.PLAYER, player)
                .build();
        Cause cause = Cause.builder()
                .append(player)
                .append(group)
                .append(VGMGroups.getPlugin())
                .build(context);
        if (Sponge.getEventManager().post(new GroupCreateEvent(player, group, cause))) return;

        try {
            saveGroup(group);
        } catch (IOException | ObjectMappingException e) {
            throw new RuntimeException("Failed to save group " + group.getName(), e);
        }
        groups.add(group);
    }

    public UUID createNewUUID() {
        UUID uuid = UUID.randomUUID();
        for (Group group : groups)
            if (uuid.equals(group.getUUID()))
                uuid = createNewUUID();

        return uuid;
    }

    public boolean isGroupNameTaken(String name) {
        return groups.stream().anyMatch(g -> g.getName().equalsIgnoreCase(name));
    }

    public Group getGroupForChunk(World world, Vector3i chunkPosition) {
        return groups.stream().filter(group -> group.ownsChunk(world, chunkPosition)).findFirst().orElse(null);
    }
}