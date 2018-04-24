package me.blayyke.vgmgroups;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import me.blayyke.vgmgroups.enums.Rank;
import me.blayyke.vgmgroups.enums.Relationship;
import me.blayyke.vgmgroups.manager.GroupManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.world.World;

import java.util.*;
import java.util.stream.Collectors;

public class Group {
    private final UUID uuid;
    private final long creationTime;
    private UUID ownerUUID;
    private UUID homeWorldUUID;
    private List<UUID> memberUUIDs;
    private List<UUID> invitedUUIDs;

    private String name;
    private String description;

    private List<GroupRelationship> relationships;
    private List<GroupRank> ranks;

    private List<GroupClaim> claims;
    private Vector3d home;

    private MessageChannel allyChat;
    private MessageChannel truceChat = () -> ImmutableSet.copyOf(Sponge.getGame().getServer().getOnlinePlayers());
    private MessageChannel groupChat = () -> ImmutableSet.copyOf(Sponge.getGame().getServer().getOnlinePlayers());

    public Group(UUID ownerUUID, String name) {
        this(ownerUUID, System.currentTimeMillis(), Lists.newArrayList(ownerUUID), name, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null, null, new ArrayList<>(), GroupManager.getInstance().createNewUUID());
        ranks.add(new GroupRank(ownerUUID, Rank.OWNER));
        System.out.println("Created new group " + name + ". Owner: " + ownerUUID);
    }

    public Group(UUID ownerUUID, long creationTime, List<UUID> memberUUIDs, String name, String description, List<GroupRelationship> relationships, List<GroupRank> ranks, List<GroupClaim> claims, Vector3d home, UUID homeWorldUUID, List<UUID> invitedUUIDs, UUID uuid) {
        this.uuid = uuid;
        this.creationTime = creationTime;
        this.ownerUUID = ownerUUID;
        this.homeWorldUUID = homeWorldUUID;
        this.memberUUIDs = memberUUIDs;
        this.invitedUUIDs = invitedUUIDs;

        this.name = name;
        this.description = description;

        this.relationships = relationships;
        this.ranks = ranks;

        this.claims = claims;
        this.home = home;
    }

    public MessageChannel getAllyChat() {
        return allyChat;
    }

    public MessageChannel getTruceChat() {
        return truceChat;
    }

    public MessageChannel getGroupChat() {
        return groupChat;
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

    public List<GroupClaim> getClaims() {
        return claims;
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

    public void setRank(UUID targetUUID, Rank rank) {
        removeRankIfPresent(targetUUID);
        ranks.add(new GroupRank(targetUUID, rank));
    }

    private void removeRankIfPresent(UUID target) {
        ranks.remove(getRank(target));
    }

    public void setRelationshipWith(Group target, Relationship relationship) {
        removeRelationshipIfPresent(target);
        relationships.add(new GroupRelationship(this, target, relationship));
    }

    public GroupRank getRank(UUID target) {
        return ranks.stream().filter(rank -> rank.getMemberUUID().equals(target)).findFirst().orElseThrow(() -> new IllegalStateException("No rank found for " + target));
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

    public List<User> getMembers() {
        Optional<UserStorageService> userStorage = Sponge.getServiceManager().provide(UserStorageService.class);

        ListIterator<UUID> it = memberUUIDs.listIterator();
        ArrayList<User> users = new ArrayList<>();
        while (it.hasNext()) {
            UUID uuid = it.next();
            Optional<User> user = userStorage.get().get(uuid);
            if (!user.isPresent()) {
                System.err.println("Player not found for UUID " + uuid + "! This should never happen.");
                it.remove();
                continue;
            }
            users.add(user.get());
        }

        return users;
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
        setRank(this.ownerUUID, Rank.RECRUIT); // set the old owner to Recruit.
        setRank(ownerUUID, Rank.OWNER);
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

    public List<Player> getOnlineMembers() {
        List<User> collect = getMembers().stream().filter(User::isOnline).collect(Collectors.toList());
        List<Player> players = new ArrayList<>(collect.size());
        for (User user : collect) players.add(user.getPlayer().get());
        return players;
    }

    public void broadcastMessage(Text text) {
        for (Player player : getOnlineMembers()) player.sendMessage(text);
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void removeInvited(UUID uniqueId) {
        invitedUUIDs.remove(uniqueId);
    }

    public boolean ownsChunk(Vector3i chunkPosition) {
        return getClaims().stream().anyMatch(groupClaim -> groupClaim.getChunkX() == chunkPosition.getX() && groupClaim.getChunkZ() == chunkPosition.getY());
    }
}