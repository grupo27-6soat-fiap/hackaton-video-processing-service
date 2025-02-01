package com.fiapgrupo27.videoprocessing.domain.usecase;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class ExtractFramesUseCase {
    public void executar(String videoPath, String outputDir, String baseName) throws IOException, InterruptedException {
//        String command = String.format("ffmpeg -i %s -vf fps=1/1 %s/%s_%%04d.jpg", videoPath, outputDir, baseName);
//        Process process = Runtime.getRuntime().exec(command);
//        int exitCode = process.waitFor();
//        if (exitCode != 0) {
//            throw new RuntimeException("Erro ao extrair quadros.");
//        }


        String command = String.format(
                // "ffmpeg -i %s -vf fps=1/1 %s/frame_%%04d.jpg",
                "ffmpeg -i %s -vf fps=1/2 %s/%s_%%04d.jpg",
                videoPath, outputDir, baseName
        );

        Process process = Runtime.getRuntime().exec(command);

        // Consome a saída padrão em uma thread separada
        Thread stdOutThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("STDOUT: " + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Consome a saída de erro em uma thread separada
        Thread stdErrThread = new Thread(() -> {
            try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = errorReader.readLine()) != null) {
                    System.err.println("STDERR: " + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        stdOutThread.start();
        stdErrThread.start();

        // Aguarda o término do processo
        int exitCode = process.waitFor();
        stdOutThread.join();
        stdErrThread.join();

        if (exitCode != 0) {
            throw new RuntimeException("Erro ao extrair quadros. Verifique o log para mais detalhes.");
        }
    }
}
