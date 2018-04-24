package me.blayyke.vgmgroups.manager;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.io.Files;
import com.google.common.reflect.TypeToken;
import me.blayyke.vgmgroups.Group;
import me.blayyke.vgmgroups.enums.Relationship;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
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
        System.out.println("Loading groups");
        groups = new ArrayList<>();
        System.out.println("List init");
        File[] files = DataManager.getInstance().getGroupsDir().listFiles();
        System.out.println("ListFiles stored");
        if (files == null) {
            System.out.println("ListFiles null");
            return;
        }

        for (File file : files) {
            if (!Files.getFileExtension(file.getName()).equalsIgnoreCase(fileExtension)) {
                System.out.println("File " + file.getName() + " does not match extension " + fileExtension + ". Continuing");
                continue;
            }

            HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setFile(file).build();
            ConfigurationNode rootNode = loader.load();

            Group group = rootNode.getValue(TypeToken.of(Group.class));
            if (group == null) System.out.println("GROUP IS NULL!");
            groups.add(group);
        }
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void deleteGroup(Group group) {
        groups.remove(group);
    }

    public Optional<Group> getPlayerGroup(Player player) {
        return groups.stream().filter(group -> group.isInGroup(player)).findFirst();
    }

    public void saveGroups() throws IOException, ObjectMappingException {
        for (Group group : groups) saveGroup(group);
    }

    private void saveGroup(Group group) throws IOException, ObjectMappingException {
        File groupsDir = DataManager.getInstance().getGroupsDir();
        File groupFile = new File(groupsDir, group.getUUID().toString() + "." + fileExtension);
        groupFile.createNewFile();

        HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setFile(groupFile).build();
        ConfigurationNode rootNode = loader.load();

        System.out.println(group.toString());

        rootNode.setValue(TypeToken.of(Group.class), group);
        loader.save(rootNode);
    }

    public static GroupManager getInstance() {
        if (instance == null) instance = new GroupManager();
        return instance;
    }

    public Group createNewGroup(Player player, String name) {
        Group group = new Group(player.getUniqueId(), name);
        groups.add(group);

        try {
            saveGroup(group);
        } catch (IOException | ObjectMappingException e) {
            throw new RuntimeException("Failed to save group " + group.getName(), e);
        }
        return group;
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

    public void isClaimed(Location<World> location) {

    }
}