package com.fiapgrupo27.videoprocessing.domain.usecase;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class ExtractFramesUseCase {

    private final Runtime runtime;

    public ExtractFramesUseCase() {
        this.runtime = Runtime.getRuntime();
    }

    // Construtor para permitir injeção no teste
    public ExtractFramesUseCase(Runtime runtime) {
        this.runtime = runtime;
    }

    public void executar(String videoPath, String outputDir, String baseName) throws IOException, InterruptedException {
        String command = String.format(
                "ffmpeg -i %s -vf fps=1/2 %s/%s_%%04d.jpg",
                videoPath, outputDir, baseName
        );

        Process process = runtime.exec(command);

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

        int exitCode = process.waitFor();
        stdOutThread.join();
        stdErrThread.join();

        if (exitCode != 0) {
            throw new RuntimeException("Erro ao extrair quadros. Verifique o log para mais detalhes.");
        }
    }
}
