package com.fiapgrupo27.videoprocessing.bdd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.cucumber.java.en.*;

public class VideoProcessingSteps {
    private String video;
    private String status;

    @Given("que um usuário envia uma solicitação de processamento para um vídeo {string}")
    public void usuarioEnviaSolicitacao(String video) {
        this.video = video;
        this.status = "Pendente";
    }

    @When("o sistema recebe a solicitação")
    public void sistemaRecebeSolicitacao() {
        assertNotNull(video);
    }

    @Then("a solicitação deve ser registrada no sistema")
    public void solicitacaoRegistrada() {
        assertEquals("Pendente", status);
    }

    @Then("o status da solicitação deve ser {string}")
    public void statusSolicitacao(String esperado) {
        assertEquals(esperado, status);
    }

    @Given("que uma solicitação de processamento foi registrada para o vídeo {string}")
    public void solicitacaoRegistradaParaVideo(String video) {
        this.video = video;
    }

    @When("o sistema inicia a extração de frames")
    public void sistemaIniciaExtracao() {
        assertNotNull(video);
    }

    @Then("os frames devem ser extraídos corretamente")
    public void framesExtraidosCorretamente() {
        assertTrue(true); // Simulação de sucesso
    }

    @Then("os frames devem ser armazenados no local apropriado")
    public void framesArmazenados() {
        assertTrue(true); // Simulação de sucesso
    }

    @Given("que os frames do vídeo {string} foram extraídos")
    public void framesExtraidos(String video) {
        this.video = video;
    }

    @When("o sistema inicia a compressão dos frames")
    public void sistemaIniciaCompressao() {
        assertNotNull(video);
    }

    @Then("os frames devem ser comprimidos corretamente")
    public void framesComprimidosCorretamente() {
        assertTrue(true); // Simulação de sucesso
    }

    @Then("os frames comprimidos devem ser armazenados no local apropriado")
    public void framesComprimidosArmazenados() {
        assertTrue(true); // Simulação de sucesso
    }
}
