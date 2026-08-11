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
            dataBuilder.append(simpleFileStr).append("---").append("\n");
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



}
