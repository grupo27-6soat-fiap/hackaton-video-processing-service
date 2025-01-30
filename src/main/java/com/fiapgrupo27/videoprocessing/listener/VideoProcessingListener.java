package com.fiapgrupo27.videoprocessing.listener;

import com.fiapgrupo27.videoprocessing.repository.SolicitacaoArquivoRepository;
import com.fiapgrupo27.videoprocessing.service.VideoProcessingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class VideoProcessingListener {

    private final VideoProcessingService videoProcessingService;
    private final SolicitacaoArquivoRepository arquivoRepository;
    private final ObjectMapper objectMapper;


    public VideoProcessingListener(VideoProcessingService videoProcessingService, SolicitacaoArquivoRepository arquivoRepository, ObjectMapper objectMapper) {
        this.videoProcessingService = videoProcessingService;
        this.arquivoRepository = arquivoRepository;
        this.objectMapper = objectMapper;

    }

    @RabbitListener(queues = "video-processing-queue")
    public void receberMensagem(Map<String, Object> mensagem) {
        try{
            // Certifique-se de que os tipos correspondem ao que foi enviado
            Integer idSolicitacao = (Integer) mensagem.get("idSolicitacao");
            String nomeArquivo = (String) mensagem.get("nomeArquivo");
            byte[] conteudoArquivo = (byte[]) mensagem.get("conteudoArquivo");
            String idArquivo = String.valueOf(mensagem.get("idArquivo"));

            videoProcessingService.processarVideo(idSolicitacao.toString(), nomeArquivo, conteudoArquivo, idArquivo);

        }catch (ClassCastException e) {
            throw new RuntimeException("Erro ao processar a mensagem: tipo de dado inesperado", e);
        }


    }
}
