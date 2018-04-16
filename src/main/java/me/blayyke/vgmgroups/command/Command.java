package me.blayyke.vgmgroups.command;

import me.blayyke.vgmgroups.Refs;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public abstract class Command implements CommandExecutor {
    private final Text description;
    private String permission;
    private List<String> aliases;
    private Set<Command> children;

    public Command(Text description, String permission, List<String> aliases) {
        this.description = description;
        this.permission = Refs.ID + "." + permission;
        this.aliases = aliases;
    }

    public Text getDescription() {
        return description;
    }

    public String getPermission() {
        return permission;
    }

    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        return runCommand(src, args);
    }

    protected CommandResult runCommand(CommandSource src, CommandContext args) throws CommandException {
        return CommandResult.empty();
    }

    public CommandElement[] getArguments() {
        return new CommandElement[]{GenericArguments.none()};
    }

    public final CommandSpec getSpec() {
        final CommandSpec.Builder builder = CommandSpec.builder()
                .description(this.getDescription())
                .permission(this.getPermission())
                .arguments(this.getArguments());

        if (this instanceof ParentCommand) for (final Command child : ((ParentCommand) this).getChildren())
            builder.child(child.getSpec(), child.getAliases());
        else
            builder.executor(this);

        return builder.build();
    }

    protected Player playersOnly(CommandSource src) throws CommandException {
        if (!(src instanceof Player)) error("Only players may execute this command!");
        return (Player) src;
    }

    protected void error(String s) throws CommandException {
        throw new CommandException(Text.of(TextColors.RED, s));
    }
}