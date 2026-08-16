package org.smirnovav.program;

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


}
