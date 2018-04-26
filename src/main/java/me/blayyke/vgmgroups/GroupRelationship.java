package me.blayyke.vgmgroups;

import me.blayyke.vgmgroups.enums.Relationship;

import java.util.UUID;

public class GroupRelationship {
    private UUID groupUUID;
    private UUID targetGroupUUID;
    private Relationship relationship;

    public GroupRelationship(UUID groupUUID, UUID targetGroupUUID, Relationship relationship) {
        this.groupUUID = groupUUID;
        this.targetGroupUUID = targetGroupUUID;
        this.relationship = relationship;
    }

    public UUID getGroupUUID() {
        return groupUUID;
    }

    public UUID getTargetGroupUUID() {
        return targetGroupUUID;
    }

    public Relationship getRelationship() {
        return relationship;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GroupRelationship)) return false;
        GroupRelationship relationship = (GroupRelationship) obj;
        return relationship.getGroupUUID().equals(getGroupUUID()) && relationship.getTargetGroupUUID().equals(getTargetGroupUUID()) && this.relationship == relationship.getRelationship();
    }
}