package me.blayyke.vgmgroups.manager;

import me.blayyke.vgmgroups.Group;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.api.entity.living.player.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GroupManager {
    private static GroupManager instance;
    private File groupsDir;
    private List<Group> groups;

    private GroupManager() {
        loadGroups();
    }

    public Optional<Group> getGroup(UUID uuid) {
        return groups.stream().filter(g -> g.getUUID().equals(uuid)).findFirst();
    }

    void loadGroups() {
        groups = new ArrayList<>();
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

    public void saveGroups() throws IOException {
        File groupsDir = DataManager.getInstance().getGroupsDir();

        for (Group group : groups) {
            File groupFile = new File(groupsDir, group.getUUID().toString());
            groupFile.createNewFile();

            HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setPath(groupFile.toPath()).build();
            ConfigurationNode rootNode = loader.load();

            rootNode.setValue(group);

            loader.save(rootNode);
        }
    }

    public static GroupManager getInstance() {
        if (instance == null) instance = new GroupManager();
        return instance;
    }

    public Group createNewGroup(Player player, String name) {
        Group group = new Group(player.getUniqueId(), name);
        groups.add(group);

        return group;
    }

    public UUID createNewUUID() {
        UUID uuid = UUID.randomUUID();
        for (Group group : groups)
            if (uuid.equals(group.getUUID()))
                uuid = createNewUUID();

        return uuid;
    }
}