package me.blayyke.vgmgroups;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.collect.Lists;
import me.blayyke.vgmgroups.manager.GroupManager;
import me.blayyke.vgmgroups.relationship.GroupRelationship;
import me.blayyke.vgmgroups.relationship.Relationship;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Group {
    private final UUID uuid;
    private UUID ownerUUID;
    private UUID homeWorldUUID;
    private List<UUID> memberUUIDs;
    private List<UUID> invitedUUIDs;

    private String name;
    private String description;

    private List<GroupRelationship> relationships;

    private List<ChunkLocation> land;
    private Vector3d home;

    public Group(UUID ownerUUID, String name) {
        this(GroupManager.getInstance().createNewUUID(), ownerUUID, Lists.newArrayList(ownerUUID), name, null, new ArrayList<>(), new ArrayList<>(), null, null, new ArrayList<>());
    }

    public Group(UUID uuid, UUID ownerUUID, List<UUID> memberUUIDs, String name, String description, List<GroupRelationship> relationships, List<ChunkLocation> land, Vector3d home, UUID homeWorldUUID, List<UUID> invitedUUIDs) {
        this.uuid = uuid;
        this.ownerUUID = ownerUUID;
        this.homeWorldUUID = homeWorldUUID;
        this.memberUUIDs = memberUUIDs;
        this.invitedUUIDs = invitedUUIDs;

        this.name = name;
        this.description = description;

        this.relationships = relationships;

        this.land = land;
        this.home = home;
    }

    public UUID getUUID() {
        return uuid;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<UUID> getMemberUUIDs() {
        return memberUUIDs;
    }

    public List<GroupRelationship> getRelationships() {
        return relationships;
    }

    public List<ChunkLocation> getLand() {
        return land;
    }

    public Vector3d getHome() {
        return home;
    }

    public UUID getHomeWorldUUID() {
        return homeWorldUUID;
    }

    public Optional<World> getHomeWorld() {
        return Sponge.getServer().getWorld(getHomeWorldUUID());
    }

    private void setRelationship(Group target, Relationship relationship) {
        removeRelationshipIfPresent(target);
        relationships.add(new GroupRelationship(this, target, relationship));
    }

    private Optional<GroupRelationship> getRelationshipWith(Group target) {
        return relationships.stream().filter(groupRelationship -> groupRelationship.getTargetGroup().equals(target)).findFirst();
    }

    private void removeRelationshipIfPresent(Group target) {
        Optional<GroupRelationship> prevRelationship = getRelationshipWith(target);
        prevRelationship.ifPresent(groupRelationship -> relationships.remove(groupRelationship));
    }

    public void addMember(Player player) {
        memberUUIDs.add(player.getUniqueId());
    }

    public void removeMember(Player player) {
        memberUUIDs.remove(player.getUniqueId());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Group)) return false;
        Group group = (Group) obj;
        return group.getUUID().equals(uuid);
    }

    public boolean isInGroup(Player target) {
        return memberUUIDs.contains(target.getUniqueId());
    }

    public void addInvited(Player target) {
        invitedUUIDs.add(target.getUniqueId());
    }

    public List<UUID> getInvitedUUIDs() {
        return invitedUUIDs;
    }
}