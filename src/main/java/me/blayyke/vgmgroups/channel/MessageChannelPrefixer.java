package me.blayyke.vgmgroups.channel;

import me.blayyke.vgmgroups.Group;
import me.blayyke.vgmgroups.GroupRelationship;
import me.blayyke.vgmgroups.VGMGroups;
import me.blayyke.vgmgroups.manager.GroupManager;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.chat.ChatType;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class MessageChannelPrefixer implements MessageChannel {
    @Override
    public Collection<MessageReceiver> getMembers() {
        return Collections.emptyList();
    }

    private TextColor neutralColour = TextColors.GRAY;
    private TextColor allyColour = TextColors.DARK_PURPLE;
    private TextColor truceColour = TextColors.LIGHT_PURPLE;
    private TextColor enemyColour = TextColors.RED;
    private TextColor sameColour = TextColors.GREEN;

    @Override
    public Optional<Text> transformMessage(@Nullable Object senderObj, MessageReceiver recipient, Text original, ChatType type) {
        // we don't want non-players
        if (!(senderObj instanceof Player) || !(recipient instanceof Player)) return Optional.of(original);
        Player playerRecipient = (Player) recipient;
        Player sender = (Player) senderObj;

        Optional<Group> senderGroupOpt = GroupManager.getInstance().getPlayerGroup(sender);
        // sender has no group, therefore we needn't prefix the message.
        if (!senderGroupOpt.isPresent()) return Optional.of(original);
        Group senderGroup = senderGroupOpt.get();

        Optional<Group> receiverGroupOpt = GroupManager.getInstance().getPlayerGroup(playerRecipient);
        TextColor prefixColour;


        // if receiver is not part of a group they are considered neutral.
        if (!receiverGroupOpt.isPresent()) prefixColour = neutralColour;
        else if (receiverGroupOpt.get().equals(senderGroup)) prefixColour = sameColour;
        else {
            Group receiverGroup = receiverGroupOpt.get();
            Optional<GroupRelationship> relationshipWith = receiverGroup.getRelationshipWith(senderGroup);

            // if receiver has no relationship with gorup they are considered neutral.
            if (!relationshipWith.isPresent()) prefixColour = neutralColour;
            else {
                // receiver and sender must have a group relationship.
                GroupRelationship groupRelationship = relationshipWith.get();
                switch (groupRelationship.getRelationship()) {
                    case ALLY:
                        prefixColour = allyColour;
                        break;
                    case TRUCE:
                        prefixColour = truceColour;
                        break;
                    case ENEMY:
                        prefixColour = enemyColour;
                        break;
                    default:
                        VGMGroups.getLogger().warn("Reached default statement on chat colour application:: " + groupRelationship.getRelationship());
                        return Optional.of(original);
                }
            }
        }
        // colour is set now, proceed with message

        Text prefix = Text.of(prefixColour, "[" + senderGroupOpt.get().getName() + "]");
        Text finalText = Text.join(prefix, original);
        return Optional.of(finalText);
    }
}