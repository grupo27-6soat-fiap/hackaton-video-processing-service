package com.fiapgrupo27.videoprocessing.domain.usecase;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class CompressFramesUseCase {
    public String executar(String outputDir, String videoPath) throws IOException {
        String baseName = new File(videoPath).getName().replace(".mp4", "");
        String zipFilePath = outputDir + File.separator + baseName + ".zip";

        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFilePath))) {
            Files.list(Paths.get(outputDir))
                    .filter(path -> path.toString().endsWith(".jpg") && path.getFileName().toString().contains(baseName))
                    .forEach(path -> {
                        try (FileInputStream fis = new FileInputStream(path.toFile())) {
                            ZipEntry zipEntry = new ZipEntry(path.getFileName().toString());
                            zipOut.putNextEntry(zipEntry);
                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = fis.read(buffer)) >= 0) {
                                zipOut.write(buffer, 0, length);
                            }
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
        }
        return zipFilePath;
    }

    public void removerFrames(String outputDir, String videoPath) throws IOException {
        String baseName = new File(videoPath).getName().replace(".mp4", "");
        Files.list(Paths.get(outputDir))
                .filter(path -> path.toString().endsWith(".jpg") && path.getFileName().toString().contains(baseName))
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });
    }
}
