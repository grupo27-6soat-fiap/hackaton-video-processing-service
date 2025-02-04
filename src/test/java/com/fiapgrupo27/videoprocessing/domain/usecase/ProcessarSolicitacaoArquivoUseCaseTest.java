package com.fiapgrupo27.videoprocessing.domain.usecase;

import com.fiapgrupo27.videoprocessing.domain.entity.SolicitacaoArquivo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

class ProcessarSolicitacaoArquivoUseCaseTest {

    private ProcessarSolicitacaoArquivoUseCase useCase;
    private SolicitacaoArquivo solicitacaoMock;

    @BeforeEach
    void setUp() {
        useCase = new ProcessarSolicitacaoArquivoUseCase();
        solicitacaoMock = mock(SolicitacaoArquivo.class);
    }

    @Test
    void testExecutar_QuandoNaoProcessado() {
        when(solicitacaoMock.isProcessado()).thenReturn(false);

        useCase.executar(solicitacaoMock);

        verify(solicitacaoMock).atualizarStatus("PROCESSANDO");
        verify(solicitacaoMock).atualizarStatus("PROCESSADO");
    }

    @Test
    void testExecutar_QuandoJaProcessado() {
        when(solicitacaoMock.isProcessado()).thenReturn(true);

        useCase.executar(solicitacaoMock);

        verify(solicitacaoMock, never()).atualizarStatus(anyString());
    }
}
