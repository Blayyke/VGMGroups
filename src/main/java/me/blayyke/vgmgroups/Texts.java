package me.blayyke.vgmgroups;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.format.TextColors;

public class Texts {
    // permission messages
    public static final TextSendable OFFICER_ONLY = new TextSendable(true, "Only officer or above can do this!");
    public static final TextSendable OWNER_ONLY = new TextSendable(true, "Only the group owner can do this!");

    public static final TextSendable CANNOT_TARGET_SELF = new TextSendable(true, "You cannot use this command on either yourself or your group.");
    public static final TextSendable PLAYER_NO_GROUP = new TextSendable(true, "That player is not in a group.");
    public static final TextSendable ALREADY_IN_GROUP = new TextSendable(true, "You are already in a group!");
    public static final TextSendable INPUT_NOT_FOUND = new TextSendable(true, "Nothing was found with your input.");
    public static final TextSendable OWNER_CANNOT_RUN = new TextSendable(true, "As you are the owner of the group, you cannot do this.");

    // major group events
    public static final TextSendable GROUP_CREATED = new TextSendable(false, "Successfully created group %s. Set the description with /g desc <description>");
    public static final TextSendable GROUP_DISBANDED = new TextSendable(false, "Your group has been disbanded.");
    public static final TextSendable GROUP_LEFT = new TextSendable(false, "You have left your group.");
    public static final TextSendable BROADCAST_GROUP_LEFT = new TextSendable(false, "%s have left your group!");
    public static final TextSendable OTHER_GROUP_CREATED = new TextSendable(false, "%s has created the group %s.");

    // joining
    public static final TextSendable GROUP_NOT_INVITED = new TextSendable(true, "You are not invited to this group.");
    public static final TextSendable GROUP_JOINED = new TextSendable(false, "Successfully joined the group %s.");
    public static final TextSendable GROUP_OTHER_JOINED = new TextSendable(false, "%s has joined the group.");

    // description
    public static final TextSendable DESCRIPTION_UPDATE = new TextSendable(false, "Your groups description has been changed to %s.");

    // name
    public static final TextSendable NAME_UPDATED = new TextSendable(false, "Your groups name has been changed to %s.");
    public static final TextSendable INVALID_NAME = new TextSendable(true, "That name is invalid. Group names may only contain letters, numbers and underscores (_).");
    public static final TextSendable NAME_TOO_LONG = new TextSendable(true, "That name is too long. Max length: %s.");
    public static final TextSendable NAME_TOO_SHORT = new TextSendable(true, "That name is too short. Min length: %s.");

    // home
    public static final TextSendable CANNOT_TELEPORT_HOME = new TextSendable(true, "Could not teleport you to your group home safely.");
    public static final TextSendable TELEPORT_HOME = new TextSendable(false, "You have been teleported to your group home.");
    public static final TextSendable ABOUT_TO_TELEPORT_HOME = new TextSendable(false, "You will be teleported to your group home in %s seconds.");
    public static final TextSendable HOME_SET = new TextSendable(false, "The group home has been updated. Position: X=%s, Y=%s, Z=%s in world %s.");
    public static final TextSendable NO_HOME_SET = new TextSendable(true, "Your group has no home set.");
    public static final TextSendable ALREADY_TELEPORTING = new TextSendable(true, "You have already initiated a teleport!");

    // ranks
    public static final TextSendable CANNOT_CHANGE_OWNER_RANK = new TextSendable(true, "Cannot change rank for owner!");
    public static final TextSendable RANK_UPDATE_SEND = new TextSendable(false, "Set %s's rank to %s.");
    public static final TextSendable RANK_UPDATE_RECEIVE = new TextSendable(false, "Your rank was set to %s.");
    public static final TextSendable RANK_VIEW = new TextSendable(false, "%s's rank is currently %s.");

    // relationships
    public static final TextSendable RELATIONSHIP_UPDATE = new TextSendable(false, "Your group has changed your relationship with %s. It is now %s.");
    public static final TextSendable RELATIONSHIP_UPDATE_RECEIVE = new TextSendable(false, "Your with %s has changed to be %s.");
    public static final TextSendable RELATIONSHIP_VIEW = new TextSendable(false, "Your relationship with %s is currently %s.");

    // invitations
    public static final TextSendable INVITATION_SENT = new TextSendable(false, "%s has been invited to your group.");
    public static final TextSendable INVITATION_REVOKED = new TextSendable(false, "%s has had their group invitation revoked.");
    public static final TextSendable INVITATION_RECEIVED = new TextSendable(false, "You have been invited to %s by %s.");

    // leader
    public static final TextSendable LEADER_RECEIVE = new TextSendable(false, "%s has set you as the new group leader.");
    public static final TextSendable LEADER_SET = new TextSendable(false, "%s is the new group leader.");

    // channel
    public static final TextSendable CHANNEL_UPDATED = new TextSendable(false, "Your chat channel has been set to %s.");

    // chunks
    public static final TextSendable CLAIMED_SELF = new TextSendable(true, "Your group has already claimed this chunk.");
    public static final TextSendable CLAIMED_OTHER = new TextSendable(true, "This chunk is already owned by %s.");
    public static final TextSendable CLAIM_SUCCESS = new TextSendable(false, "You now own the chunk at %s, %s.");
    public static final TextSendable CLAIM_FAILURE = new TextSendable(true, "Could not claim this area as you have reached your claim limit!");
    public static final TextSendable CHUNK_NOT_OWNED = new TextSendable(true, "You do not own this chunk.");

    public static class TextSendable {
        private final Text text;
        private final boolean error;

        /**
         * Create an {@link TextSendable} object with provided message.
         *
         * @param error   Whether or not to colour this text red or green.
         * @param message The message to be sent on use.
         */
        TextSendable(boolean error, String message) {
            this.error = error;
            this.text = Text.of(error ? TextColors.RED : TextColors.GREEN, message);
        }

        /**
         * Send this message to an {@link MessageReceiver}.
         *
         * @param receiver The receiver to send the message to.
         */
        public void send(MessageReceiver receiver) {
            receiver.sendMessage(text);
        }

        /**
         * Send this message after formatting to an {@link MessageReceiver}.
         *
         * @param receiver The receiver to send the message to.
         * @param vars     The objects to format the string with.
         */
        public void sendWithVars(MessageReceiver receiver, Object... vars) {
            String formatted = String.format(text.toPlain(), vars);
            receiver.sendMessage(error ? Text.of(TextColors.RED, formatted) : Text.of(formatted));
        }

        /**
         * Send this message after formatting to all {@link org.spongepowered.api.entity.living.player.Player}'s in a {@link Group}.
         *
         * @param group The group to send the message to.
         * @param vars  The objects to format the string with.
         */
        public void broadcastWithVars(Group group, Object... vars) {
            group.getOnlineMembers().forEach(player -> sendWithVars(player, vars));
        }

        /**
         * Send this message to all {@link org.spongepowered.api.entity.living.player.Player}'s in a {@link Group}.
         *
         * @param group The group to send the message to.
         */
        public void broadcast(Group group) {
            group.getOnlineMembers().forEach(this::send);
        }

        /**
         * Send this message to every {@link org.spongepowered.api.entity.living.player.Player} currently online.
         */
        public void globalBroadcast() {
            Sponge.getServer().getBroadcastChannel().send(text);
        }

        /**
         * Send this message after formatting to every {@link org.spongepowered.api.entity.living.player.Player} currently online.
         */
        public void globalBroadcastWithVars(Object... vars) {
            String formatted = String.format(text.toPlain(), vars);
            Sponge.getServer().getBroadcastChannel().send(error ? Text.of(TextColors.RED, formatted) : Text.of(formatted));
        }
    }
}