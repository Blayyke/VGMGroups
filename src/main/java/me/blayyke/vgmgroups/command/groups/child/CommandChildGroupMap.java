package me.blayyke.vgmgroups.command.groups.child;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.collect.Lists;
import me.blayyke.vgmgroups.Group;
import me.blayyke.vgmgroups.GroupRelationship;
import me.blayyke.vgmgroups.VGMGroups;
import me.blayyke.vgmgroups.command.Command;
import me.blayyke.vgmgroups.enums.Relationship;
import me.blayyke.vgmgroups.manager.GroupManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandChildGroupMap extends Command {
    public CommandChildGroupMap(VGMGroups plugin) {
        super(plugin, Lists.newArrayList("map"), Text.of("View nearby group claims."));
    }

    @Nonnull
    @Override
    protected String getPermission() {
        return "map";
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Player player = playersOnly(src);
        World world = player.getWorld();

        Text notCapturedMark = Text.of(TextColors.GRAY, "█");
        Text factionMark = Text.of(TextColors.GREEN, "█");
        Text allianceMark = Text.of(TextColors.AQUA, "█");
        Text enemyMark = Text.of(TextColors.RED, "█");
        Text truceMark = Text.of(TextColors.LIGHT_PURPLE, "█");
        Text normalFactionMark = Text.of(TextColors.WHITE, "█");
        Text playerLocationMark = Text.of(TextColors.GOLD, "█");

        Vector3i playerPosition = player.getLocation().getChunkPosition();

        List<Text> map = new ArrayList<>();
        StringBuilder normalFactions = new StringBuilder();
        StringBuilder allyFactions = new StringBuilder();
        StringBuilder enemyFactions = new StringBuilder();
        StringBuilder truceFactions = new StringBuilder();
        String playerFaction = "";

        //Map resolution
        int mapWidth = 20;
        int mapHeight = 8;

        //Half map resolution + 1 (for player column/row in the center)
        //Needs to be an odd number so the map will have equal distance to the left and right.
        int halfMapWidth = mapWidth / 2;
        int halfMapHeight = mapHeight / 2;

        for (int row = -halfMapHeight; row <= halfMapHeight; row++) {
            Text.Builder textBuilder = Text.builder();

            for (int column = -halfMapWidth; column <= halfMapWidth; column++) {
                if (row == 0 && column == 0) {
                    //TODO: Faction that player is standing at is not showed in the list.
                    textBuilder.append(playerLocationMark);
                    continue;
                }

                Vector3i chunk = playerPosition.add(column, 0, row);
                Group chunkGroup = GroupManager.getInstance().getGroupForChunk(world, chunk);
                if (chunkGroup != null) {
                    Optional<Group> playerGroupOpt = GroupManager.getInstance().getPlayerGroup(player);

                    if (playerGroupOpt.isPresent()) {
                        Group group = playerGroupOpt.get();
                        if (chunkGroup.equals(group)) {
                            textBuilder.append(factionMark);
                            playerFaction = group.getName();
                        } else {
                            Optional<GroupRelationship> relationshipWith = group.getRelationshipWith(chunkGroup);
                            if (relationshipWith.isPresent()) {
                                Relationship r = relationshipWith.get().getRelationship();
                                switch (r) {
                                    case ALLY:
                                        textBuilder.append(allianceMark);
                                        if (!allyFactions.toString().contains(chunkGroup.getName()))
                                            allyFactions.append(chunkGroup.getName()).append(", ");
                                        break;
                                    case ENEMY:
                                        textBuilder.append(enemyMark);
                                        if (!enemyFactions.toString().contains(chunkGroup.getName()))
                                            enemyFactions.append(chunkGroup.getName()).append(", ");
                                        break;
                                    case TRUCE:
                                        textBuilder.append(truceMark);
                                        if (!truceFactions.toString().contains(chunkGroup.getName()))
                                            truceFactions.append(chunkGroup.getName()).append(", ");
                                        break;
                                    case NEUTRAL:
                                        break;
                                }
                            }
                        }
                    } else {
                        switch (chunkGroup.getName()) {
                            case "SafeZone":
                                textBuilder.append(Text.of(TextColors.AQUA, "+"));
                                break;
                            case "WarZone":
                                textBuilder.append(Text.of(TextColors.DARK_RED, "#"));
                                break;
                            default:
                                textBuilder.append(normalFactionMark);
                                break;
                        }
                        if (!normalFactions.toString().contains(chunkGroup.getName())) {
                            normalFactions.append(chunkGroup.getName()).append(", ");
                        }
                    }
                } else textBuilder.append(notCapturedMark);
            }
            map.add(textBuilder.build());
        }

        String playerPositionClaim = "none";

        if (GroupManager.getInstance().getGroupForChunk(world, playerPosition) != null) {
            playerPositionClaim = GroupManager.getInstance().getGroupForChunk(world, playerPosition).getName();
        }

        //Print map
        player.sendMessage(Text.of(TextColors.GREEN, "======Group Map======"));
        map.stream().map(Text::of).forEach(player::sendMessage);
        player.sendMessage(Text.of(TextColors.GREEN, "====================="));

        //Print factions on map
        if (!playerFaction.isEmpty())
            player.sendMessage(Text.of(TextColors.GREEN,
                    "Your faction: ", TextColors.GREEN, playerFaction));
        if (normalFactions.length() > 0)
            player.sendMessage(Text.of(TextColors.WHITE,
                    "Factions: ", TextColors.RESET, normalFactions.substring(0, normalFactions.length() - 2)));
        if (allyFactions.length() > 0)
            player.sendMessage(Text.of(TextColors.AQUA,
                    "Allies: " + allyFactions.substring(0, allyFactions.length() - 2)));
        if (enemyFactions.length() > 0)
            player.sendMessage(Text.of(TextColors.RED,
                    "Enemies: " + enemyFactions.substring(0, enemyFactions.length() - 2)));
        if (truceFactions.length() > 0)
            player.sendMessage(Text.of(TextColors.LIGHT_PURPLE,
                    "Truce: " + truceFactions.substring(0, enemyFactions.length() - 2)));

        player.sendMessage(Text.of("Currently standing at: ", TextColors.GOLD, playerPosition.toString(), TextColors.WHITE, " which is claimed by ", TextColors.GOLD, playerPositionClaim));
        return CommandResult.success();
    }
}