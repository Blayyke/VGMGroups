package me.blayyke.vgmgroups;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import me.blayyke.vgmgroups.enums.Channel;
import me.blayyke.vgmgroups.enums.Rank;
import me.blayyke.vgmgroups.enums.Relationship;
import me.blayyke.vgmgroups.event.GroupMemberInviteEvent;
import me.blayyke.vgmgroups.manager.GroupManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.world.Location;
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

    private int maxPowerPerPerson = 10;

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
        this.memberUUIDs = new ArrayList<>(memberUUIDs);
        this.invitedUUIDs = new ArrayList<>(invitedUUIDs);

        this.name = name;
        this.description = description;

        this.relationships = new ArrayList<>(relationships);
        this.ranks = new ArrayList<>(ranks);

        this.claims = new ArrayList<>(claims);
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
        relationships.add(new GroupRelationship(this.uuid, target.uuid, relationship));
    }

    public GroupRank getRank(UUID target) {
        return ranks.stream().filter(rank -> rank.getMemberUUID().equals(target)).findFirst().orElseThrow(() -> new IllegalStateException("No rank found for " + target));
    }

    public Optional<GroupRelationship> getRelationshipWith(Group target) {
        return relationships.stream().filter(groupRelationship -> groupRelationship.getTargetGroupUUID().equals(target.getUUID())).findFirst();
    }

    private void removeRelationshipIfPresent(Group target) {
        Optional<GroupRelationship> prevRelationship = getRelationshipWith(target);
        prevRelationship.ifPresent(groupRelationship -> relationships.remove(groupRelationship));
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

    public void invitePlayer(Player inviter, Player target) {
        EventContext context = EventContext.builder()
                .add(EventContextKeys.PLAYER, inviter)
                .add(EventContextKeys.PLAYER, target)
                .build();
        Cause cause = Cause.builder()
                .append(target).append(inviter)
                .append(this)
                .append(VGMGroups.getPlugin())
                .build(context);
        Sponge.getEventManager().post(new GroupMemberInviteEvent(inviter, this, cause, target));

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
        invitedUUIDs.remove(player.getUniqueId());
        memberUUIDs.add(player.getUniqueId());
    }

    public boolean isInvited(UUID uniqueId) {
        return invitedUUIDs.contains(uniqueId);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public List<Player> getOnlineMembers() {
        List<User> collect = getMembers().stream().filter(User::isOnline).collect(Collectors.toList());
        List<Player> players = new ArrayList<>(collect.size());
        for (User user : collect) players.add(user.getPlayer().get());
        return players;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void revokeInvite(UUID uniqueId) {
        invitedUUIDs.remove(uniqueId);
    }

    public boolean ownsChunk(World world, Vector3i chunkPosition) {
        for (GroupClaim claim : claims)
            if (claim.getWorldUUID().equals(world.getUniqueId()) && claim.getChunkX() == chunkPosition.getX() && claim.getChunkZ() == chunkPosition.getZ())
                return true;
        return false;
    }

    /**
     * Claim a chunk at the provided {@link Location}
     *
     * @param location Location for claim
     * @return Whether or not the claim was successful
     */
    public boolean claimChunk(Location<World> location) {
        if (!canClaimMore()) return false;

        int chunkX = location.getChunkPosition().getX();
        int chunkZ = location.getChunkPosition().getZ();
        claims.add(new GroupClaim(location.getExtent().getUniqueId(), chunkX, chunkZ));

        GroupManager.getInstance().saveGroup(this);
        VGMGroups.getLogger().info(String.format("Group %s claimed chunk %s.", getName(), getClaimString(location)));
        return true;
    }

    private String getClaimString(Location<World> location) {
        return "[x=" + location.getChunkPosition().getX() + ",y=" + location.getChunkPosition().getZ() + ",world=" + location.getExtent().getName() + "]";
    }

    public void unclaimChunk(Location<World> location) {
        Iterator<GroupClaim> iterator = claims.iterator();
        while (iterator.hasNext()) {
            GroupClaim next = iterator.next();
            Vector3i chunkPosition = location.getChunkPosition();
            if (next.getChunkX() == chunkPosition.getX() && next.getChunkZ() == chunkPosition.getZ()) {
                iterator.remove();
                GroupManager.getInstance().saveGroup(this);
                VGMGroups.getLogger().info(String.format("Group %s unclaimed chunk %s.", getName(), getClaimString(location)));
                return;
            }
        }
        // This should be impossible if the chunk was checked properly
        throw new RuntimeException("Didn't find chunk to unclaim?");
    }

    private boolean canClaimMore() {
        return claims.size() < getMaxPower();
    }

    public void setHome(Location<World> location) {
        this.home = location.getPosition();
        this.homeWorldUUID = location.getExtent().getUniqueId();
    }

    public void setChatChannel(Player player, Channel channel) {
        MessageChannel messageChannel = player.getMessageChannel();
        player.setMessageChannel(MessageChannel.combined(getChat(channel), messageChannel));
    }

    private MessageChannel getChat(Channel channel) {
        switch (channel) {
            case TRUCE:
                return getTruceChat();
            case ALLY:
                return getAllyChat();
            case GROUP:
                return getGroupChat();
        }
        return null;
    }

    public int getMaxPower() {
        return memberUUIDs.size() * maxPowerPerPerson;
    }

    public void unclaimAllChunks() {
        claims.clear();
        GroupManager.getInstance().saveGroup(this);

        VGMGroups.getLogger().info(String.format("Group %s unclaimed all chunks.", getName()));
    }
}