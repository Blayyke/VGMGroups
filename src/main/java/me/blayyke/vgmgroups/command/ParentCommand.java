package me.blayyke.vgmgroups.command;

import me.blayyke.vgmgroups.VGMGroups;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

public abstract class ParentCommand extends ChildCommand {
    private Set<ChildCommand> children;

    ParentCommand(@Nonnull final VGMGroups plugin,
                  @Nonnull final List<String> aliases,
                  @Nonnull final Text description) {
        super(plugin, aliases, description);

        this.children = null;
    }

    @Nonnull
    Set<ChildCommand> getChildren() {
        if (this.children == null) this.children = registerChildren();
        return this.children;
    }

    @Nonnull
    protected abstract Set<ChildCommand> registerChildren();

    public void register() {
        Sponge.getCommandManager().register(this.getPlugin(), this.getCommandSpec(), this.getAliases());
    }
}