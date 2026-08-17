package org.smirnovav.program;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class IOUtils {


    public static String readSingleFile(String filePath) {
        List<String> lines = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            lines = br.lines().toList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder builder = new StringBuilder();
        for (String line : lines) {
            builder.append(line).append("\n");
        }
        return builder.toString();
    }

    public static String readOneFileContent(String filePath) {
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(Paths.get(filePath), Charset.forName("windows-1251"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder builder = new StringBuilder();
        for (String line : lines) {
            builder.append(line).append("\n");
        }
        return builder.toString();
    }

    public static void writeFile(String content, String filePath) {
        Path path = Paths.get(filePath);
        try {
            Files.writeString(path, content, Charset.forName("windows-1251"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Path> listRelativePaths(String rootPathStr) {
        Path rootPath = Paths.get(rootPathStr).toAbsolutePath().normalize();

        if (!Files.isDirectory(rootPath)) {
            throw new IllegalArgumentException("Указанный путь не является директорией: " + rootPath);
        }

        List<Path> result = new ArrayList<>();

        // Проходим по дереву файлов, включая саму директорию и всё содержимое
        try (Stream<Path> stream = Files.walk(rootPath)) {
            stream.forEach(path -> {
                // Преобразуем абсолютный путь в относительный относительно rootPath
                Path relative = rootPath.relativize(path);
                result.add(relative);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static List<Path> getOnlyDirectories(String rootPathStr) {
        List<Path> result = new ArrayList<>();
        for (Path path : listRelativePaths(rootPathStr)) {
            Path absPath = Paths.get(rootPathStr + "\\" + path.toString());
            if (Files.isDirectory(absPath)) {
                result.add(path);
            }
        }
        return result;
    }

    public static List<Path> getOnlyFiles(String rootPathStr) {
        List<Path> result = new ArrayList<>();
        for (Path path : listRelativePaths(rootPathStr)) {
            Path absPath = Paths.get(rootPathStr + "\\" + path.toString());
            if (Files.isRegularFile(absPath)) {
                result.add(path);
            }
        }
        return result;
    }

    public static String createData(String rootPathStr) {
        List<Path> directories = getOnlyDirectories(rootPathStr);
        List<Path> files = getOnlyFiles(rootPathStr);
        StringBuilder dirBuilder = new StringBuilder();
        StringBuilder dataBuilder = new StringBuilder();
        for (Path dirPath : directories) {
            dirBuilder.append(dirPath.toString()).append("\n");
        }
        dirBuilder.append("+++").append("\n");
        for (Path filePath : files) {
            dataBuilder.append(filePath.toString()).append("\n");
            dataBuilder.append("???").append("\n");
            String simpleFileStr = readSingleFile(rootPathStr + "\\" + filePath.toString());
            dataBuilder.append(simpleFileStr).append("__________________________________________________").append("\n");
        }
        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append(dirBuilder).append(dataBuilder);
        return resultBuilder.toString();
    }

    public static String addSymbols(String content, char symbol) {
        List<String> lines = content.lines().toList();
        StringBuilder builder = new StringBuilder();
        for (String line : lines) {
            builder.append(symbol).append(line).append("\n");
        }
        return builder.toString();
    }

    public static String deleteSymbols(String content, char symbol) {
        List<String> lines = content.lines().toList();
        StringBuilder builder = new StringBuilder();
        for (String line : lines) {
            String newLine = line.substring(1);
            builder.append(newLine).append("\n");
        }
        return builder.toString();
    }

    public static String oneLineCipher(String line, int shift) {
        if (line == null || line.isEmpty()) {
            return line;
        }
        shift = ((shift % 256) + 256) % 256;
        StringBuilder result = new StringBuilder();

        for (char c : line.toCharArray()) {
            char shifted = (char) ((shift + c) % 0x10000);
            result.append(shifted);
        }
        return result.toString();
    }

    public static String dataCipher(String content, int shift) {
        List<String> lines = content.lines().toList();
        StringBuilder builder = new StringBuilder();
        for (String line : lines) {
            builder.append(oneLineCipher(line, shift)).append("\n");
        }
        return builder.toString();
    }

    public static String oneLineDecipher(String line, int shift) {
        if (line == null || line.isEmpty()) {
            return line;
        }

        shift = ((shift % 256) + 256) % 256;
        StringBuilder result = new StringBuilder(line.length());
        for (char c : line.toCharArray()) {
            int code = c - shift;
            if (code < 0) {
                code += 0x10000;
            }
            char deciphered = (char) code;
            result.append(deciphered);
        }
        return result.toString();
    }

    public static String dataDecipher(String content, int shift) {
        List<String> lines = content.lines().toList();
        StringBuilder builder = new StringBuilder();
        for (String line : lines) {
            builder.append(oneLineDecipher(line, shift)).append("\n");
        }
        return builder.toString();
    }

    public static void createFolderStructure(String rootPath, List<String> relativePaths) {
        Path root = Paths.get(rootPath).toAbsolutePath();

        try {
            Files.createDirectories(root);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String relativePath : relativePaths) {
            if (relativePath == null || relativePath.isBlank()) {
                continue;
            }

            Path fullPath = root.resolve(relativePath).normalize();

            try {
                Files.createDirectories(fullPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static List<String> getDirectoriesPaths(String content) {
        String[] pathsStr = content.split("\\+\\+\\+", 2);
        return pathsStr[0].lines().toList();
    }

    public static List<ProjectFile> getProjectFiles(String content) {
        String[] splitedFile = content.split("\\+\\+\\+", 2);
        String[] filesStr = splitedFile[1].split("__________________________________________________");
        List<ProjectFile> projectFiles = new ArrayList<>();
        for (int i = 0; i < filesStr.length - 1; i++) {
            String[] fileData = filesStr[i].split("\\?\\?\\?");
            projectFiles.add(new ProjectFile(fileData[0].replace("\n", ""), fileData[1]));
        }
        return projectFiles;
    }

    public static void writeProjectFiles(String rootPath, List<ProjectFile> files) {
        for (ProjectFile file : files) {
            writeFile(file.getContent(), rootPath + "\\" + file.getFilePath());
        }
    }



}
