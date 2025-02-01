package com.fiapgrupo27.videoprocessing.service;

import com.fiapgrupo27.videoprocessing.domain.usecase.ProcessarVideoUseCase;
import org.springframework.stereotype.Service;

@Service
public class VideoProcessingService {
    private final ProcessarVideoUseCase processarVideoUseCase;

    public VideoProcessingService(ProcessarVideoUseCase processarVideoUseCase) {
        this.processarVideoUseCase = processarVideoUseCase;
    }

    public void processarVideo(String idSolicitacao, String nomeArquivo, byte[] conteudoArquivo, String idArquivo) {
        processarVideoUseCase.executar(idSolicitacao, nomeArquivo, conteudoArquivo, idArquivo);
    }
}
