package me.blayyke.vgmgroups.manager;

import me.blayyke.vgmgroups.Refs;

import java.io.File;

public class DataManager {
    private static DataManager instance;
    private File dataDir = new File(Refs.NAME);
    private File groupsDir = new File(Refs.NAME);

    public void load() {
        dataDir.mkdir();
        groupsDir.mkdir();
    }

    public File getGroupsDir() {
        if (!groupsDir.exists()) groupsDir.mkdir();
        return groupsDir;
    }

    public static DataManager getInstance() {
        if (instance == null) instance = new DataManager();
        return instance;
    }
}