package com.fiapgrupo27.videoprocessing.domain.usecase;

import com.fiapgrupo27.videoprocessing.infrastructure.persistence.SolicitacaoArquivoRepository;
import com.fiapgrupo27.videoprocessing.infrastructure.SolicitacaoServiceClient;
import org.springframework.stereotype.Component;

@Component
public class ProcessarVideoUseCase {
    private final SalvarArquivoUseCase salvarArquivoUseCase;
    private final ExtractFramesUseCase extractFramesUseCase;
    private final CompressFramesUseCase compressFramesUseCase;
    private final SolicitacaoArquivoRepository arquivoRepository;
    private final SolicitacaoServiceClient solicitacaoServiceClient;

    public ProcessarVideoUseCase(
            SalvarArquivoUseCase salvarArquivoUseCase,
            ExtractFramesUseCase extractFramesUseCase,
            CompressFramesUseCase compressFramesUseCase,
            SolicitacaoArquivoRepository arquivoRepository,
            SolicitacaoServiceClient solicitacaoServiceClient) {
        this.salvarArquivoUseCase = salvarArquivoUseCase;
        this.extractFramesUseCase = extractFramesUseCase;
        this.compressFramesUseCase = compressFramesUseCase;
        this.arquivoRepository = arquivoRepository;
        this.solicitacaoServiceClient = solicitacaoServiceClient;
    }

    public void executar(String idSolicitacao, String nomeArquivo, byte[] conteudoArquivo, String idArquivo) {
        try {
            String[] resultadoSalva = salvarArquivoUseCase.executar(nomeArquivo, conteudoArquivo);
            String videoPath = resultadoSalva[0];
            String outputDir = resultadoSalva[1];
            String baseName = resultadoSalva[2];

            extractFramesUseCase.executar(videoPath, outputDir, baseName);
            solicitacaoServiceClient.atualizarStatusSolicitacao(Long.valueOf(idSolicitacao), Long.valueOf(idArquivo), "CONCLUIDO");

            String compressReturn = compressFramesUseCase.executar(outputDir, videoPath);
            compressFramesUseCase.removerFrames(outputDir, videoPath);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar vídeo: " + e.getMessage());
        }
    }
}