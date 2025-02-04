package com.fiapgrupo27.videoprocessing.domain.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SolicitacaoArquivoTest {

    private SolicitacaoArquivo solicitacaoArquivo;

    @BeforeEach
    void setUp() {
        solicitacaoArquivo = new SolicitacaoArquivo();
    }

    @Test
    void testGetSetIdArquivo() {
        solicitacaoArquivo.setIdArquivo(1);
        assertEquals(1, solicitacaoArquivo.getIdArquivo());
    }

    @Test
    void testGetSetIdSolicitacao() {
        solicitacaoArquivo.setIdSolicitacao(100);
        assertEquals(100, solicitacaoArquivo.getIdSolicitacao());
    }

    @Test
    void testGetSetIdSolicitante() {
        solicitacaoArquivo.setIdSolicitante(200);
        assertEquals(200, solicitacaoArquivo.getIdSolicitante());
    }

    @Test
    void testGetSetNomeArquivo() {
        solicitacaoArquivo.setNomeArquivo("video.mp4");
        assertEquals("video.mp4", solicitacaoArquivo.getNomeArquivo());
    }

    @Test
    void testGetSetStatus() {
        solicitacaoArquivo.setStatus("EM PROCESSAMENTO");
        assertEquals("EM PROCESSAMENTO", solicitacaoArquivo.getStatus());
    }

    @Test
    void testGetSetDataInclusao() {
        LocalDateTime now = LocalDateTime.now();
        solicitacaoArquivo.setDataInclusao(now);
        assertEquals(now, solicitacaoArquivo.getDataInclusao());
    }

    @Test
    void testIsProcessado_RetornaTrueSeStatusForProcessado() {
        solicitacaoArquivo.setStatus("PROCESSADO");
        assertTrue(solicitacaoArquivo.isProcessado());
    }

    @Test
    void testIsProcessado_RetornaFalseSeStatusNaoForProcessado() {
        solicitacaoArquivo.setStatus("EM PROCESSAMENTO");
        assertFalse(solicitacaoArquivo.isProcessado());
    }

    @Test
    void testAtualizarStatus_AlteraStatusCorretamente() {
        solicitacaoArquivo.atualizarStatus("CONCLUÍDO");
        assertEquals("CONCLUÍDO", solicitacaoArquivo.getStatus());
    }
}
