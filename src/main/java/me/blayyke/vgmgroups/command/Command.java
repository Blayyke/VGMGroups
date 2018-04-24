package me.blayyke.vgmgroups.command;

import me.blayyke.vgmgroups.Group;
import me.blayyke.vgmgroups.VGMGroups;
import me.blayyke.vgmgroups.manager.GroupManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public abstract class Command implements CommandExecutor {
    @Nonnull
    private final VGMGroups plugin;
    @Nonnull
    private final List<String> aliases;
    @Nonnull
    private final Text description;

    protected Command(@Nonnull final VGMGroups plugin,
                      @Nonnull final List<String> aliases,
                      @Nonnull final Text description) {
        if (aliases.isEmpty()) throw new IllegalArgumentException("aliases may not be empty");

        this.plugin = plugin;
        this.aliases = aliases;
        this.description = description;
    }

    protected Player playersOnly(CommandSource src) throws CommandException {
        if (!(src instanceof Player)) throw new CommandException(Text.of("Only players can run this command!"));
        return (Player) src;
    }

    protected Group requireGroup(Player player) throws CommandException {
        Optional<Group> playerGroup = GroupManager.getInstance().getPlayerGroup(player);
        if (!playerGroup.isPresent()) error(Text.of(TextColors.RED, "You need to be in a group to use this command!"));
        return playerGroup.get();
    }

    protected void error(Text text) throws CommandException {
        throw new CommandException(text);
    }

    @Nonnull
    protected VGMGroups getPlugin() {
        return this.plugin;
    }

    @Nonnull
    List<String> getAliases() {
        return this.aliases;
    }

    @Nonnull
    private Text getDescription() {
        return this.description;
    }

    @Nonnull
    protected abstract String getPermission();

    @Nonnull
    protected Optional<CommandElement[]> getArguments() {
        return Optional.empty();
    }

    CommandSpec getCommandSpec() {
        final CommandSpec.Builder builder = CommandSpec.builder()
                .description(getDescription())
                .permission(getPermission());

        this.getArguments().ifPresent(builder::arguments);

        if (this instanceof ParentCommand) {
            for (final Command child : ((ParentCommand) this).getChildren()) {
                builder.child(child.getCommandSpec(), child.getAliases());
            }
        }

        if (!(this instanceof CommandContainer)) {
            builder.executor(this);
        }

        return builder.build();
    }
}