package org.smirnovav.program;

import java.util.List;

public class Scenarios {

    public static void cipher(String sourceFolderPath, String resultFolderPath) {
        String createdFile = IOUtils.createData(sourceFolderPath);
        IOUtils.writeFile(createdFile, resultFolderPath);
    }

    public static void cipher(String sourceFolderPath, String resultFolderPath, char symbol) {
        String createdFile = IOUtils.createData(sourceFolderPath);
        String fileWithSymbols = IOUtils.addSymbols(createdFile, symbol);
        IOUtils.writeFile(fileWithSymbols, resultFolderPath);
    }

    public static void cipher(String sourceFolderPath, String resultFolderPath, char symbol, int shift) {

        String createdFile = IOUtils.createData(sourceFolderPath);
        String cipheredFile = IOUtils.dataCipher(createdFile, shift);
        String fileWithSymbols = IOUtils.addSymbols(cipheredFile, symbol);
        IOUtils.writeFile(fileWithSymbols, resultFolderPath);
    }

    public static void decipher(String sourceFilePath, String resultFolderPath) {
        String file = IOUtils.readOneFileContent(sourceFilePath);
        List<String> directoriesPaths = IOUtils.getDirectoriesPaths(file);
        IOUtils.createFolderStructure(resultFolderPath, directoriesPaths);
        List<ProjectFile> projectFiles = IOUtils.getProjectFiles(file);
        IOUtils.writeProjectFiles(resultFolderPath, projectFiles);
    }

    public static void decipher(String sourceFilePath, String resultFolderPath, char symbol) {
        String sourceFile = IOUtils.readOneFileContent(sourceFilePath);
        String file = IOUtils.deleteSymbols(sourceFile, symbol);
        List<String> directoriesPaths = IOUtils.getDirectoriesPaths(file);
        IOUtils.createFolderStructure(resultFolderPath, directoriesPaths);
        List<ProjectFile> projectFiles = IOUtils.getProjectFiles(file);
        IOUtils.writeProjectFiles(resultFolderPath, projectFiles);
    }


}
