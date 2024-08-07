package com.modsmanager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProfileManager {
    private static final String[] COMMON_PARENT_PATHS = {
            System.getenv("APPDATA"),
            System.getProperty("user.home") + "/Library/Application Support",
            System.getProperty("user.home") + "/.local/share",
            System.getProperty("user.home")
    };

    private static final String[] ATL_SUBDIRS = { "ATLauncher", ".atlauncher" };
    private static final String[] MODRINTH_SUBDIRS = { "com.modrinth.theseus", ".modrinth" };
    private static final String INSTANCES_SUBDIR = "Instances";
    private static final String MODRINTH_PROFILES_SUBDIR = "profiles";
    private static final String MULTIMC_SUBDIR = "instances";

    private List<String> profiles;

    public ProfileManager() {
        refreshProfiles();
    }

    public void refreshProfiles() {
        profiles = new ArrayList<>();
        for (String parentPath : COMMON_PARENT_PATHS) {
            profiles.addAll(getProfilesFromPossiblePaths(parentPath, ATL_SUBDIRS, INSTANCES_SUBDIR));
            profiles.addAll(getProfilesFromPossiblePaths(parentPath, MODRINTH_SUBDIRS, MODRINTH_PROFILES_SUBDIR));
        }
        System.out.println("Detected profiles: " + profiles);
    }

    public String[] getProfiles() {
        return profiles.toArray(new String[0]);
    }

    private List<String> getProfilesFromPossiblePaths(String parentPath, String[] subDirs, String instancesSubDir) {
        List<String> profiles = new ArrayList<>();
        File parent = new File(parentPath);
        if (parent.exists() && parent.isDirectory()) {
            for (File subDir : parent.listFiles()) {
                String subDirName = subDir.getName().toLowerCase(Locale.ROOT);
                for (String expectedSubDir : subDirs) {
                    if (subDirName.equals(expectedSubDir.toLowerCase(Locale.ROOT))) {
                        File instancesPath = new File(subDir, instancesSubDir);
                        System.out.println("Checking path: " + instancesPath.getAbsolutePath());
                        if (instancesPath.exists() && instancesPath.isDirectory()) {
                            for (File file : instancesPath.listFiles()) {
                                if (file.isDirectory()) {
                                    profiles.add(file.getName());
                                }
                            }
                        }
                    }
                }
            }
        }
        return profiles;
    }
}
