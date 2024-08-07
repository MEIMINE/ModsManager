package com.modsmanager;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ImportManager {
    private ProfileManager profileManager;
    private FileManager fileManager;

    public ImportManager(ProfileManager profileManager) {
        this.profileManager = profileManager;
        this.fileManager = new FileManager();
    }

    public void importMods(String profile, String zipFilePath) {
        Path modsDir = getProfileModsPath(profile);
        if (modsDir != null) {
            fileManager.importMods(modsDir, zipFilePath);
        } else {
            System.out.println("Profile not found: " + profile);
        }
    }

    private Path getProfileModsPath(String profile) {
        String[] paths = {
                System.getenv("APPDATA"),
                System.getProperty("user.home") + "/Library/Application Support",
                System.getProperty("user.home") + "/.local/share",
                System.getProperty("user.home")
        };

        String[][] subdirs = {
                { "ATLauncher", ".atlauncher" },
                { "com.modrinth.theseus", ".modrinth" }
        };

        String[] instanceSubdirs = { "Instances", "instances" };

        for (String basePath : paths) {
            for (String[] subdir : subdirs) {
                for (String instanceSubdir : instanceSubdirs) {
                    Path profilePath = Paths.get(basePath, subdir[0], instanceSubdir, profile, "mods");
                    if (profilePath.toFile().exists()) {
                        return profilePath;
                    }
                }
            }
        }
        return null;
    }
}
