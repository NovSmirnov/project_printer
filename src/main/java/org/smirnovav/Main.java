package org.smirnovav;

import org.smirnovav.program.IOUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        String rootDir = "D:\\Files\\JavaProjects\\moex_api_lib";
        List<Path> paths = IOUtils.listRelativePaths(rootDir);
        for (Path path : paths) {
            System.out.println(path);
        }
    }
}