package me.blayyke.vgmgroups.command.groups.child.home;

import com.google.common.collect.Lists;
import me.blayyke.vgmgroups.Group;
import me.blayyke.vgmgroups.Texts;
import me.blayyke.vgmgroups.VGMGroups;
import me.blayyke.vgmgroups.command.Command;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CommandChildGroupHome extends Command {
    private static final long DELAY = 10;
    private List<UUID> waitingForTeleport = new ArrayList<>();

    public CommandChildGroupHome(@Nonnull VGMGroups plugin) {
        super(plugin, Lists.newArrayList("home"), Text.of("Go to your groups home."));
    }

    @Nonnull
    @Override
    protected String getPermission() {
        return "home";
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Player player = playersOnly(src);
        Group group = requireGroup(player);
        Location<World> location = group.getHomeWorld().get().getLocation(group.getHome());

        if (waitingForTeleport.contains(player.getUniqueId())) {
            Texts.ALREADY_TELEPORTING.send(player);
            return CommandResult.empty();
        }

        waitingForTeleport.add(player.getUniqueId());
        Texts.ABOUT_TO_TELEPORT_HOME.sendWithVars(player, DELAY);
        Sponge.getScheduler().createSyncExecutor(getPlugin()).schedule(() -> {
            if (!player.setLocationSafely(location)) {
                Texts.CANNOT_TELEPORT_HOME.send(player);
                return;
            }
            waitingForTeleport.remove(player.getUniqueId());
            Texts.TELEPORT_HOME.send(player);
        }, DELAY, TimeUnit.SECONDS);
        return CommandResult.success();
    }
}