package edu.ccrm.util;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class RecursiveUtil {

    public static long calculateTotalSize(Path path) throws IOException {
        if (Files.isRegularFile(path)) {
            return Files.size(path);
        }

        long totalSize = 0;
        try (var stream = Files.list(path)) {
            List<Path> entries = stream.toList();
            for (Path entry : entries) {
                totalSize += calculateTotalSize(entry);
            }
        }
        return totalSize;
    }

    public static List<Path> findFilesByExtension(Path path, String extension) throws IOException {
        List<Path> result = new ArrayList<>();
        findFilesByExtensionRecursive(path, extension, result, 0);
        return result;
    }

    private static void findFilesByExtensionRecursive(Path path, String extension,
                                                      List<Path> result, int depth) throws IOException {
        if (Files.isDirectory(path)) {
            try (var stream = Files.list(path)) {
                List<Path> entries = stream.toList();
                for (Path entry : entries) {
                    findFilesByExtensionRecursive(entry, extension, result, depth + 1);
                }
            }
        } else if (path.toString().toLowerCase().endsWith(extension.toLowerCase())) {
            result.add(path);
        }
    }

    public static void printDirectoryTree(Path path) throws IOException {
        printDirectoryTreeRecursive(path, 0);
    }

    private static void printDirectoryTreeRecursive(Path path, int depth) throws IOException {
        String indent = "  ".repeat(depth);

        if (Files.isDirectory(path)) {
            System.out.println(indent + "📁 " + path.getFileName());
            try (var stream = Files.list(path)) {
                List<Path> entries = stream.sorted().toList();
                for (Path entry : entries) {
                    printDirectoryTreeRecursive(entry, depth + 1);
                }
            }
        } else {
            try {
                long size = Files.size(path);
                System.out.println(indent + "📄 " + path.getFileName() + " (" + size + " bytes)");
            } catch (IOException e) {
                System.out.println(indent + "📄 " + path.getFileName() + " (Error reading size)");
            }
        }
    }
}