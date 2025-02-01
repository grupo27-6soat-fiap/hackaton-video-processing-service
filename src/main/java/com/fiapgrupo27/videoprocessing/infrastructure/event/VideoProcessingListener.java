package com.fiapgrupo27.videoprocessing.listener;

import com.fiapgrupo27.videoprocessing.domain.usecase.ProcessarVideoUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class VideoProcessingListener {

    private final ProcessarVideoUseCase processarVideoUseCase;
    private final ObjectMapper objectMapper;

    public VideoProcessingListener(ProcessarVideoUseCase processarVideoUseCase, ObjectMapper objectMapper) {
        this.processarVideoUseCase = processarVideoUseCase;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "video-processing-queue")
    public void receberMensagem(Map<String, Object> mensagem) {
        try {
            Integer idSolicitacao = (Integer) mensagem.get("idSolicitacao");
            String nomeArquivo = (String) mensagem.get("nomeArquivo");
            byte[] conteudoArquivo = (byte[]) mensagem.get("conteudoArquivo");
            String idArquivo = String.valueOf(mensagem.get("idArquivo"));

            processarVideoUseCase.executar(idSolicitacao.toString(), nomeArquivo, conteudoArquivo, idArquivo);

        } catch (ClassCastException e) {
            throw new RuntimeException("Erro ao processar a mensagem: tipo de dado inesperado", e);
        }
    }
}
