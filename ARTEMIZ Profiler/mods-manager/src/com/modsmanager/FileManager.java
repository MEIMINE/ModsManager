package com.modsmanager;

import java.io.*;
import java.nio.file.*;
import java.util.zip.*;

public class FileManager {

    public void exportMods(Path modsDir, String exportPath) {
        Path exportDir = Paths.get(exportPath);
        try {
            Files.createDirectories(exportDir);
            Files.walk(modsDir)
                    .filter(path -> path.toString().endsWith(".jar") || path.toString().endsWith(".jar.disabled"))
                    .forEach(source -> copyFile(source, exportDir.resolve(modsDir.relativize(source))));
            System.out.println("Mods exported successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void importMods(Path modsDir, String zipFilePath) {
        Path tempDir = Paths.get("resources/temp");
        try {
            Files.createDirectories(tempDir);
            unzip(zipFilePath, tempDir.toString());
            Files.walk(tempDir)
                    .filter(path -> path.toString().endsWith(".jar") || path.toString().endsWith(".jar.disabled"))
                    .forEach(source -> copyFile(source, modsDir.resolve(tempDir.relativize(source))));
            cleanUp(tempDir);
            System.out.println("Mods imported successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void copyFile(Path source, Path target) {
        try {
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void unzip(String zipFilePath, String destDir) throws IOException {
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = new File(destDir, zipEntry.getName());
            if (zipEntry.isDirectory()) {
                newFile.mkdirs();
            } else {
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
    }

    private void cleanUp(Path tempDir) throws IOException {
        Files.walk(tempDir)
                .map(Path::toFile)
                .forEach(File::delete);
    }
}
