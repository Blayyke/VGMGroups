package me.blayyke.vgmgroups;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.collect.Lists;
import me.blayyke.vgmgroups.enums.Rank;
import me.blayyke.vgmgroups.enums.Relationship;
import me.blayyke.vgmgroups.manager.GroupManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

import java.util.*;

public class Group {
    private final UUID uuid;
    private UUID ownerUUID;
    private UUID homeWorldUUID;
    private List<UUID> memberUUIDs;
    private List<UUID> invitedUUIDs;

    private String name;
    private String description;

    private List<GroupRelationship> relationships;
    private List<GroupRank> ranks;

    private List<ChunkLocation> land;
    private Vector3d home;

    public Group(UUID ownerUUID, String name) {
        this(GroupManager.getInstance().createNewUUID(), ownerUUID, Lists.newArrayList(ownerUUID), name, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null, null, new ArrayList<>());
    }

    public Group(UUID uuid, UUID ownerUUID, List<UUID> memberUUIDs, String name, String description, List<GroupRelationship> relationships, List<GroupRank> ranks, List<ChunkLocation> land, Vector3d home, UUID homeWorldUUID, List<UUID> invitedUUIDs) {
        this.uuid = uuid;
        this.ownerUUID = ownerUUID;
        this.homeWorldUUID = homeWorldUUID;
        this.memberUUIDs = memberUUIDs;
        this.invitedUUIDs = invitedUUIDs;

        this.name = name;
        this.description = description;

        this.relationships = relationships;
        this.ranks = ranks;

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

    public List<GroupRank> getRanks() {
        return ranks;
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

    public void setRank(Player target, Rank rank) {
        removeRankIfPresent(target);
        ranks.add(new GroupRank(this, target.getUniqueId(), rank));
    }

    private void removeRankIfPresent(Player target) {
        Optional<GroupRank> prevRank = getRank(target);
        prevRank.ifPresent(rank -> ranks.remove(rank));
    }

    public void setRelationshipWith(Group target, Relationship relationship) {
        removeRelationshipIfPresent(target);
        relationships.add(new GroupRelationship(this, target, relationship));
    }

    public Optional<GroupRank> getRank(Player target) {
        return ranks.stream().filter(rank -> rank.getMemberUUID().equals(target.getUniqueId())).findFirst();
    }

    public Optional<GroupRelationship> getRelationshipWith(Group target) {
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

    public List<Player> getMembers() {
        ListIterator<UUID> it = memberUUIDs.listIterator();
        ArrayList<Player> players = new ArrayList<>();
        while (it.hasNext()) {
            UUID uuid = it.next();
            Optional<Player> player = Sponge.getServer().getPlayer(uuid);
            if (!player.isPresent()) {
                System.err.println("Player not found for UUID " + uuid + "! This should never happen.");
                it.remove();
                continue;
            }
            players.add(player.get());
        }

        return players;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public void setName(String name) throws CommandException {
        if (GroupManager.getInstance().isGroupNameTaken(name))
            throw new CommandException(Text.of("That name is already taken by another group!"));
        this.name = name;
    }

    public void setOwner(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    public Player getOwner() {
        Optional<Player> playerOpt = Sponge.getServer().getPlayer(getOwnerUUID());
        return playerOpt.orElseThrow(() -> new RuntimeException("Could not find leader from UUID " + uuid + "!"));
    }

    public void playerJoin(Player player) {
        memberUUIDs.add(player.getUniqueId());
    }

    public boolean isInvited(UUID uniqueId) {
        return invitedUUIDs.contains(uniqueId);
    }
}