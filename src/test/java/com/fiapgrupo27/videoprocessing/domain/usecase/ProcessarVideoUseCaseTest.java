package com.fiapgrupo27.videoprocessing.domain.usecase;

import com.fiapgrupo27.videoprocessing.infrastructure.persistence.SolicitacaoArquivoRepository;
import com.fiapgrupo27.videoprocessing.infrastructure.SolicitacaoServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProcessarVideoUseCaseTest {

    @Mock
    private SalvarArquivoUseCase salvarArquivoUseCase;

    @Mock
    private ExtractFramesUseCase extractFramesUseCase;

    @Mock
    private CompressFramesUseCase compressFramesUseCase;

    @Mock
    private SolicitacaoArquivoRepository arquivoRepository;

    @Mock
    private SolicitacaoServiceClient solicitacaoServiceClient;

    private ProcessarVideoUseCase processarVideoUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        processarVideoUseCase = new ProcessarVideoUseCase(
                salvarArquivoUseCase,
                extractFramesUseCase,
                compressFramesUseCase,
                arquivoRepository,
                solicitacaoServiceClient
        );
    }

    @Test
    void testExecutar_Sucesso() throws Exception {
        // Dados de entrada
        String idSolicitacao = "1";
        String nomeArquivo = "video.mp4";
        byte[] conteudoArquivo = new byte[10];
        String idArquivo = "100";

        // Configuração dos mocks para fluxo de sucesso:
        String[] resultadoSalva = {"/path/to/video.mp4", "/output/dir", "baseName"};
        when(salvarArquivoUseCase.executar(eq(nomeArquivo), eq(conteudoArquivo)))
                .thenReturn(resultadoSalva);
        when(compressFramesUseCase.executar(eq("/output/dir"), eq("/path/to/video.mp4")))
                .thenReturn("/path/to/compressed.zip");

        // Execução
        assertDoesNotThrow(() ->
                processarVideoUseCase.executar(idSolicitacao, nomeArquivo, conteudoArquivo, idArquivo)
        );

        // Verificações:
        verify(salvarArquivoUseCase, times(1)).executar(eq(nomeArquivo), eq(conteudoArquivo));
        verify(extractFramesUseCase, times(1))
                .executar(eq("/path/to/video.mp4"), eq("/output/dir"), eq("baseName"));
        verify(solicitacaoServiceClient, times(1))
                .atualizarStatusSolicitacao(eq(Long.valueOf(idSolicitacao)), eq(Long.valueOf(idArquivo)), eq("CONCLUIDO"));
        verify(compressFramesUseCase, times(1))
                .executar(eq("/output/dir"), eq("/path/to/video.mp4"));
        verify(compressFramesUseCase, times(1))
                .removerFrames(eq("/output/dir"), eq("/path/to/video.mp4"));
    }

    @Test
    void testExecutar_FalhaAoSalvarArquivo() throws Exception {
        String idSolicitacao = "1";
        String nomeArquivo = "video.mp4";
        byte[] conteudoArquivo = new byte[10];
        String idArquivo = "100";

        // Simula falha no salvamento do arquivo
        when(salvarArquivoUseCase.executar(anyString(), any(byte[].class)))
                .thenThrow(new RuntimeException("Erro ao salvar arquivo"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                processarVideoUseCase.executar(idSolicitacao, nomeArquivo, conteudoArquivo, idArquivo)
        );
        assertTrue(exception.getMessage().contains("Erro ao processar vídeo"));

        // Verifica que os métodos subsequentes NÃO foram chamados
        verify(extractFramesUseCase, never()).executar(anyString(), anyString(), anyString());
        verify(solicitacaoServiceClient, never()).atualizarStatusSolicitacao(anyLong(), anyLong(), anyString());
        verify(compressFramesUseCase, never()).executar(anyString(), anyString());
        verify(compressFramesUseCase, never()).removerFrames(anyString(), anyString());
    }

    @Test
    void testExecutar_FalhaNaExtracao() throws Exception {
        String idSolicitacao = "1";
        String nomeArquivo = "video.mp4";
        byte[] conteudoArquivo = new byte[10];
        String idArquivo = "100";

        String[] resultadoSalva = {"/path/to/video.mp4", "/output/dir", "baseName"};
        when(salvarArquivoUseCase.executar(eq(nomeArquivo), eq(conteudoArquivo)))
                .thenReturn(resultadoSalva);

        // Simula falha na extração dos frames
        doThrow(new RuntimeException("Erro ao extrair frames"))
                .when(extractFramesUseCase)
                .executar(eq("/path/to/video.mp4"), eq("/output/dir"), eq("baseName"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                processarVideoUseCase.executar(idSolicitacao, nomeArquivo, conteudoArquivo, idArquivo)
        );
        assertTrue(exception.getMessage().contains("Erro ao processar vídeo"));

        // Verifica que a atualização do status e as etapas subsequentes NÃO foram chamadas
        verify(solicitacaoServiceClient, never()).atualizarStatusSolicitacao(anyLong(), anyLong(), anyString());
        verify(compressFramesUseCase, never()).executar(anyString(), anyString());
        verify(compressFramesUseCase, never()).removerFrames(anyString(), anyString());
    }

    @Test
    void testExecutar_FalhaNaCompressao() throws Exception {
        String idSolicitacao = "1";
        String nomeArquivo = "video.mp4";
        byte[] conteudoArquivo = new byte[10];
        String idArquivo = "100";

        String[] resultadoSalva = {"/path/to/video.mp4", "/output/dir", "baseName"};
        when(salvarArquivoUseCase.executar(eq(nomeArquivo), eq(conteudoArquivo)))
                .thenReturn(resultadoSalva);

        // Simula falha na compressão
        doThrow(new RuntimeException("Erro na compressão"))
                .when(compressFramesUseCase).executar(eq("/output/dir"), eq("/path/to/video.mp4"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                processarVideoUseCase.executar(idSolicitacao, nomeArquivo, conteudoArquivo, idArquivo)
        );
        assertTrue(exception.getMessage().contains("Erro ao processar vídeo"));

        // Verifica que o salvamento e a extração foram realizados
        verify(salvarArquivoUseCase, times(1)).executar(eq(nomeArquivo), eq(conteudoArquivo));
        verify(extractFramesUseCase, times(1))
                .executar(eq("/path/to/video.mp4"), eq("/output/dir"), eq("baseName"));
        // A atualização do status para "CONCLUIDO" é realizada antes da compressão
        verify(solicitacaoServiceClient, times(1))
                .atualizarStatusSolicitacao(eq(Long.valueOf(idSolicitacao)), eq(Long.valueOf(idArquivo)), eq("CONCLUIDO"));
        // Compressão foi chamada e falhou
        verify(compressFramesUseCase, times(1)).executar(eq("/output/dir"), eq("/path/to/video.mp4"));
        // Como a compressão falhou, removerFrames NÃO deve ser chamado
        verify(compressFramesUseCase, never()).removerFrames(anyString(), anyString());
    }
}
