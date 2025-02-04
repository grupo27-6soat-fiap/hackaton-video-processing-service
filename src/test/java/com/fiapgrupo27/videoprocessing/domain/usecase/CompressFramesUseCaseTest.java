package com.fiapgrupo27.videoprocessing.domain.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompressFramesUseCaseTest {

    private CompressFramesUseCase compressFramesUseCase;
    private String outputDir;
    private String videoPath;

    @BeforeEach
    void setUp() throws IOException {
        compressFramesUseCase = new CompressFramesUseCase();
        outputDir = "test-output";
        videoPath = "test-video.mp4";

        // Cria diretório de teste e arquivos temporários para simular o cenário
        Files.createDirectories(Paths.get(outputDir));

        // Cria um arquivo de imagem temporário
        Path testFile = Paths.get(outputDir, "test-video_frame1.jpg");
        if (!Files.exists(testFile)) {
            Files.createFile(testFile);
        }
    }

    @Test
    void testExecutar_CriacaoZip() throws IOException {
        // Executa o método a ser testado
        String zipFilePath = compressFramesUseCase.executar(outputDir, videoPath);

        // Verifica se o arquivo zip foi criado
        Path zipFile = Paths.get(zipFilePath);
        assertTrue(Files.exists(zipFile), "O arquivo zip não foi criado.");
        assertTrue(Files.isRegularFile(zipFile), "O arquivo zip não é um arquivo regular.");
    }

    @Test
    void testExecutar_SemArquivosParaCompactar() throws IOException {
        // Simula a ausência de arquivos JPG correspondentes
        Files.delete(Paths.get(outputDir, "test-video_frame1.jpg"));
        
        // Executa o método a ser testado e verifica a exceção
        IOException thrown = assertThrows(IOException.class, () -> {
            compressFramesUseCase.executar(outputDir, videoPath);
        });
        // Verifica se a mensagem de erro é a esperada
        assertEquals("Nenhum arquivo .jpg encontrado para compactar.", thrown.getMessage());
    }

    @Test
    void testRemoverFrames_RemocaoComSucesso() throws IOException {
        // Executa o método a ser testado
        compressFramesUseCase.removerFrames(outputDir, videoPath);

        // Verifica se o arquivo de imagem foi removido
        Path frameFile = Paths.get(outputDir, "test-video_frame1.jpg");
        assertFalse(Files.exists(frameFile), "O arquivo de imagem não foi removido.");
    }

    @Test
    void testRemoverFrames_SemArquivosParaRemover() throws IOException {
        // Simula a ausência de arquivos JPG correspondentes
        Files.delete(Paths.get(outputDir, "test-video_frame1.jpg"));
        
        // Executa o método a ser testado e verifica a exceção
        IOException thrown = assertThrows(IOException.class, () -> {
            compressFramesUseCase.removerFrames(outputDir, videoPath);
        });
        // Verifica se a mensagem de erro é a esperada
        assertEquals("Nenhum arquivo .jpg encontrado para remover.", thrown.getMessage());
    }

    @Test
    void testRemoverFrames_DiretórioInexistente() {
        // Testa o caso de diretório inexistente
        String invalidDir = "invalid-directory";
        
        // Executa o método a ser testado e verifica a exceção
        IOException thrown = assertThrows(IOException.class, () -> {
            compressFramesUseCase.removerFrames(invalidDir, videoPath);
        });
        // Verifica se a mensagem de erro é a esperada
        assertEquals("Diretório de saída inválido ou não existe: invalid-directory", thrown.getMessage());
    }

    @Test
    void testExecutar_DiretórioInexistente() {
        // Testa o caso de diretório inexistente
        String invalidDir = "invalid-directory";
        
        // Executa o método a ser testado e verifica a exceção
        IOException thrown = assertThrows(IOException.class, () -> {
            compressFramesUseCase.executar(invalidDir, videoPath);
        });
        // Verifica se a mensagem de erro é a esperada
        assertEquals("Diretório de saída inválido ou não existe: invalid-directory", thrown.getMessage());
    }
}
