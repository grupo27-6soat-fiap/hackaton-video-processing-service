package com.fiapgrupo27.videoprocessing.domain.usecase;

import com.fiapgrupo27.videoprocessing.domain.entity.SolicitacaoArquivo;

public class ProcessarSolicitacaoArquivoUseCase {
    public void executar(SolicitacaoArquivo solicitacao) {
        if (!solicitacao.isProcessado()) {
            solicitacao.atualizarStatus("PROCESSANDO");
            // Lógica de processamento aqui
            solicitacao.atualizarStatus("PROCESSADO");
        }
    }
}