package me.blayyke.vgmgroups.command;

import me.blayyke.vgmgroups.VGMGroups;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class CommandContainer extends ParentCommand {
    protected CommandContainer(@Nonnull final VGMGroups plugin,
                               @Nonnull final List<String> aliases,
                               @Nonnull final Text description) {
        super(plugin, aliases, description);
    }

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull final CommandSource src, @Nonnull final CommandContext args) throws CommandException {
        throw new CommandException(Text.of("CommandContainers are not meant to be registered as executors."));
    }
}