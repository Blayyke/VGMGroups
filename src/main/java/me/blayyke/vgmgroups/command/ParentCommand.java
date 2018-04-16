package me.blayyke.vgmgroups.command;

import org.spongepowered.api.text.Text;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public abstract class ParentCommand extends Command {
    private Set<Command> children;

    public ParentCommand(Text description, String permission, List<String> aliases) {
        super(description, permission, aliases);
    }

    protected Set<Command> registerChildren() {
        return Collections.emptySet();
    }

    public Set<Command> getChildren() {
        if (this.children == null) this.children = this.registerChildren();
        return this.children;
    }
}