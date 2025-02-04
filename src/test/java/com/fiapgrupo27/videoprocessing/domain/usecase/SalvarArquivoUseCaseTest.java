package com.fiapgrupo27.videoprocessing.domain.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class SalvarArquivoUseCaseTest {

    private SalvarArquivoUseCase salvarArquivoUseCase;
    private String outputDir;
    private String nomeArquivo;
    private byte[] conteudoArquivo;

    @BeforeEach
    void setUp() {
        salvarArquivoUseCase = new SalvarArquivoUseCase();
        outputDir = System.getProperty("user.dir") + File.separator + "output";
        nomeArquivo = "teste-video.mp4";
        conteudoArquivo = "conteudo do arquivo".getBytes();
    }

    @Test
    void testExecutar_Sucesso() throws IOException {
        // Executa o método a ser testado
        String[] resultado = salvarArquivoUseCase.executar(nomeArquivo, conteudoArquivo);

        // Verifica se os caminhos retornados são válidos
        String videoPath = resultado[0];
        String outputDirPath = resultado[1];
        String baseName = resultado[2];

        assertNotNull(videoPath);
        assertNotNull(outputDirPath);
        assertNotNull(baseName);
        assertTrue(Files.exists(Paths.get(videoPath)), "O arquivo salvo não foi encontrado.");
        assertEquals("teste-video", baseName, "O nome base do arquivo está incorreto.");

        // Limpeza do arquivo criado durante o teste
        Files.deleteIfExists(Paths.get(videoPath));
    }

    @Test
    void testExecutar_ErroAoSalvarArquivo() {
        // Simula um nome de arquivo inválido para forçar erro
        String nomeInvalido = "\\/?:*\"<>|";

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            salvarArquivoUseCase.executar(nomeInvalido, conteudoArquivo);
        });
        
        assertTrue(thrown.getMessage().contains("Erro ao salvar arquivo"));
    }
}
