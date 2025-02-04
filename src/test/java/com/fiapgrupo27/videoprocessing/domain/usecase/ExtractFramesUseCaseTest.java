package com.fiapgrupo27.videoprocessing.domain.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExtractFramesUseCaseTest {

    private ExtractFramesUseCase extractFramesUseCase;
    private Runtime mockRuntime;
    private Process mockProcess;
    private InputStream mockInputStream;
    private InputStream mockErrorStream;

    @BeforeEach
    void setUp() throws IOException {
        mockRuntime = mock(Runtime.class);
        mockProcess = mock(Process.class);
        mockInputStream = mock(InputStream.class);
        mockErrorStream = mock(InputStream.class);

        when(mockRuntime.exec(anyString())).thenReturn(mockProcess);
        when(mockProcess.getInputStream()).thenReturn(mockInputStream);
        when(mockProcess.getErrorStream()).thenReturn(mockErrorStream);

        extractFramesUseCase = new ExtractFramesUseCase(mockRuntime);
    }

    @Test
    void testExecutar_Sucesso() throws IOException, InterruptedException {
        when(mockProcess.waitFor()).thenReturn(0);

        extractFramesUseCase.executar("test-video.mp4", "outputDir", "baseName");

        verify(mockProcess, times(1)).waitFor();
        verify(mockProcess, times(1)).getInputStream();
        verify(mockProcess, times(1)).getErrorStream();
    }

    @Test
    void testExecutar_FalhaAoExecutarComando() throws IOException, InterruptedException {
        when(mockProcess.waitFor()).thenReturn(1);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            extractFramesUseCase.executar("test-video.mp4", "outputDir", "baseName")
        );

        assertEquals("Erro ao extrair quadros. Verifique o log para mais detalhes.", exception.getMessage());
        verify(mockProcess, times(1)).waitFor();
    }

    @Test
    void testExecutar_ExceptionNoProcesso() throws IOException {
        when(mockRuntime.exec(anyString())).thenThrow(new IOException("Falha ao iniciar o processo"));

        IOException exception = assertThrows(IOException.class, () -> 
            extractFramesUseCase.executar("test-video.mp4", "outputDir", "baseName")
        );

        assertEquals("Falha ao iniciar o processo", exception.getMessage());
    }

    @Test
    void testExecutar_ProcessoInterrompido() throws IOException, InterruptedException {
        when(mockProcess.waitFor()).thenThrow(new InterruptedException("Processo interrompido"));

        InterruptedException exception = assertThrows(InterruptedException.class, () -> 
            extractFramesUseCase.executar("test-video.mp4", "outputDir", "baseName")
        );

        assertEquals("Processo interrompido", exception.getMessage());
        verify(mockProcess, times(1)).waitFor();
    }

    @Test
    void testExecutar_VerificaConsumoDeSaida() throws IOException, InterruptedException {
        when(mockProcess.waitFor()).thenReturn(0);

        extractFramesUseCase.executar("test-video.mp4", "outputDir", "baseName");

        verify(mockProcess, times(1)).getInputStream();
        verify(mockProcess, times(1)).getErrorStream();
    }
}
