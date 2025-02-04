package com.fiapgrupo27.videoprocessing.domain.usecase;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class CompressFramesUseCase {

    public String executar(String outputDir, String videoPath) throws IOException {
        // Verifica se o diretório existe
        Path outputDirPath = Paths.get(outputDir);
        if (!Files.exists(outputDirPath) || !Files.isDirectory(outputDirPath)) {
            throw new IOException("Diretório de saída inválido ou não existe: " + outputDir);
        }

        String baseName = new File(videoPath).getName().replace(".mp4", "");
        String zipFilePath = outputDir + File.separator + baseName + ".zip";

        // Filtra os arquivos JPG que correspondem ao nome base do vídeo
        List<Path> jpgFiles = Files.list(outputDirPath)
                .filter(path -> path.toString().endsWith(".jpg") && path.getFileName().toString().contains(baseName))
                .collect(Collectors.toList());

        // Verifica se há arquivos JPG
        if (jpgFiles.isEmpty()) {
            throw new IOException("Nenhum arquivo .jpg encontrado para compactar.");
        }

        // Criação do arquivo zip
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFilePath))) {
            for (Path path : jpgFiles) {
                try (FileInputStream fis = new FileInputStream(path.toFile())) {
                    ZipEntry zipEntry = new ZipEntry(path.getFileName().toString());
                    zipOut.putNextEntry(zipEntry);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) >= 0) {
                        zipOut.write(buffer, 0, length);
                    }
                    zipOut.closeEntry(); // Certifique-se de fechar a entrada do zip para cada arquivo
                }
            }
        }

        return zipFilePath;
    }

    public void removerFrames(String outputDir, String videoPath) throws IOException {
        // Verifica se o diretório existe
        Path outputDirPath = Paths.get(outputDir);
        if (!Files.exists(outputDirPath) || !Files.isDirectory(outputDirPath)) {
            throw new IOException("Diretório de saída inválido ou não existe: " + outputDir);
        }

        String baseName = new File(videoPath).getName().replace(".mp4", "");

        // Filtra os arquivos JPG que correspondem ao nome base do vídeo
        List<Path> jpgFiles = Files.list(outputDirPath)
                .filter(path -> path.toString().endsWith(".jpg") && path.getFileName().toString().contains(baseName))
                .collect(Collectors.toList());

        // Verifica se há arquivos JPG
        if (jpgFiles.isEmpty()) {
            throw new IOException("Nenhum arquivo .jpg encontrado para remover.");
        }

        // Remove os arquivos JPG
        for (Path path : jpgFiles) {
            Files.delete(path);
        }
    }
}
