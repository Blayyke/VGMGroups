package me.blayyke.vgmgroups;

import me.blayyke.vgmgroups.enums.Relationship;

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
        GroupRelationship relationship = (GroupRelationship) obj;
        return relationship.getGroup().equals(getGroup()) && relationship.getTargetGroup().equals(getTargetGroup()) && this.relationship == relationship.getRelationship();
    }
}