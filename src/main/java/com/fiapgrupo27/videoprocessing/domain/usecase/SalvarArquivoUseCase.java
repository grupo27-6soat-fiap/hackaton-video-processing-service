package com.fiapgrupo27.videoprocessing.domain.usecase;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class SalvarArquivoUseCase {
    public String[] executar(String nomeArquivo, byte[] conteudoArquivo) {
        String outputDir = System.getProperty("user.dir") + File.separator + "output";
        File outputDirectory = new File(outputDir);
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }

        File arquivo = new File(outputDir + File.separator + nomeArquivo);
        try (FileOutputStream fos = new FileOutputStream(arquivo)) {
            fos.write(conteudoArquivo);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar arquivo: " + e.getMessage());
        }

        String videoPath = arquivo.getAbsolutePath();
        String baseName = nomeArquivo.substring(0, nomeArquivo.lastIndexOf('.'));
        return new String[]{videoPath, outputDir, baseName};
    }
}
