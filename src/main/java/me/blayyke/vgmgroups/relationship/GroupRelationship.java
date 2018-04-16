package me.blayyke.vgmgroups.relationship;

import me.blayyke.vgmgroups.Group;

public class GroupRelationship {
    private Group group;
    private Group targetGroup;
    private Relationship relationship;

    public GroupRelationship(Group group, Group targetGroup, Relationship relationship) {
        this.group = group;
        this.targetGroup = targetGroup;
        this.relationship = relationship;
    }

    public Group getGroup() {
        return group;
    }

    public Group getTargetGroup() {
        return targetGroup;
    }

    public Relationship getRelationship() {
        return relationship;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GroupRelationship)) return false;
        GroupRelationship group = (GroupRelationship) obj;
        return group.getGroup().equals(getGroup()) && group.getTargetGroup().equals(getTargetGroup()) && relationship == group.getRelationship();
    }
}