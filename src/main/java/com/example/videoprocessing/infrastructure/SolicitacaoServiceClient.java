package com.example.videoprocessing.infrastructure;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SolicitacaoServiceClient {

    private final RestTemplate restTemplate;

    public SolicitacaoServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void atualizarStatusSolicitacao(Long idSolicitacao, Long idArquivo, String status) {
        String url = String.format("http://localhost:8080/api/solicitacoes/%d/arquivos/%d/status?status=%s", idSolicitacao, idArquivo, status);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    HttpEntity.EMPTY,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Status atualizado com sucesso!");
            } else {
                System.err.println("Falha ao atualizar status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("Erro ao chamar o serviço de atualização de status: " + e.getMessage());
        }
    }
}
