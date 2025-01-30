package com.fiapgrupo27.videoprocessing.service;

import com.fiapgrupo27.videoprocessing.infrastructure.SolicitacaoServiceClient;
import com.fiapgrupo27.videoprocessing.repository.SolicitacaoArquivoRepository;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class VideoProcessingService {

    private final SolicitacaoArquivoRepository arquivoRepository;
    private final SolicitacaoServiceClient solicitacaoServiceClient;


    public VideoProcessingService(SolicitacaoArquivoRepository arquivoRepository, SolicitacaoServiceClient solicitacaoServiceClient) {
        this.arquivoRepository = arquivoRepository;
        this.solicitacaoServiceClient = solicitacaoServiceClient;
    }

    public void processarVideo(String idSolicitacao, String nomeArquivo, byte[] conteudoArquivo, String idArquivo) {
        try {

            String[] resultadoSalva = salvarArquivo(nomeArquivo, conteudoArquivo);
            String videoPath = resultadoSalva[0];
            String outputDir = resultadoSalva[1];
            String baseName =  resultadoSalva[2];




//            atualizaStatus(idSolicitacao, idArquivo, "SUCESSO");


            extractFrames(videoPath, outputDir, baseName);
            solicitacaoServiceClient.atualizarStatusSolicitacao(Long.valueOf(idSolicitacao), Long.valueOf(idArquivo), "CONCLUIDO");

            // Compactar quadros
            String compressReturn = compressFrames(outputDir, videoPath);

            // Remover Arquivos pos Compactacao
            removeExtractedFrames(outputDir, videoPath);


        } catch (Exception e) {

            throw new RuntimeException("Erro ao processar vídeo: " + e.getMessage());
        }
    }


    public String[] salvarArquivo( String nomeArquivo, byte[] conteudoArquivo){
        String outputDirPath = "output";

        // Obtém o diretório atual do projeto
        String baseDir = System.getProperty("user.dir");
        String outputDir = baseDir + File.separator + "output";

        // Certifique-se de que o diretório "output" existe
        File outputDirectory = new File(outputDir);
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs(); // Cria o diretório, se necessário
        }

        // Salvar o arquivo no sistema de arquivos
        File arquivo = new File("output/" + nomeArquivo);
        try (FileOutputStream fos = new FileOutputStream(arquivo)) {
            fos.write(conteudoArquivo);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String videoPath = arquivo.getAbsolutePath();
        String originalFileName = new File(videoPath).getName();
        String baseName = originalFileName.substring(0, originalFileName.lastIndexOf('.'));

        return new String[]{videoPath, outputDir, baseName };


    }

    private void extractFrames(String videoPath, String outputDir, String baseName) throws IOException, InterruptedException {


        String command = String.format(
                // "ffmpeg -i %s -vf fps=1/1 %s/frame_%%04d.jpg",
                "ffmpeg -i %s -vf fps=1/1 %s/%s_%%04d.jpg",
                videoPath, outputDir, baseName
        );

        Process process = Runtime.getRuntime().exec(command);

        // Consome a saída padrão em uma thread separada
        Thread stdOutThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("STDOUT: " + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Consome a saída de erro em uma thread separada
        Thread stdErrThread = new Thread(() -> {
            try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = errorReader.readLine()) != null) {
                    System.err.println("STDERR: " + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        stdOutThread.start();
        stdErrThread.start();

        // Aguarda o término do processo
        int exitCode = process.waitFor();
        stdOutThread.join();
        stdErrThread.join();

        if (exitCode != 0) {
            throw new RuntimeException("Erro ao extrair quadros. Verifique o log para mais detalhes.");
        }
    }

    private String compressFrames(String outputDir, String videoPath) throws IOException {


        // Obtém o nome do arquivo original sem o caminho e sem a extensão
        String originalFileName = new File(videoPath).getName();
        String baseName = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
//        String zipFilePath = outputDir + '\\' + baseName + ".zip";
        String zipFilePath = outputDir + File.separator + baseName + ".zip";

        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFilePath))) {
            Files.list(Paths.get(outputDir))
                    .filter(path -> path.toString().endsWith(".jpg") && path.getFileName().toString().contains(baseName))
                    .forEach(path -> {
                        try (FileInputStream fis = new FileInputStream(path.toFile())) {
                            ZipEntry zipEntry = new ZipEntry(path.getFileName().toString());
                            zipOut.putNextEntry(zipEntry);

                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = fis.read(buffer)) >= 0) {
                                zipOut.write(buffer, 0, length);
                            }
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
        }

        return zipFilePath;
    }

    private void removeExtractedFrames(String outputDir, String videoPath) throws IOException {

        String originalFileName = new File(videoPath).getName();
        String baseName = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
        Files.list(Paths.get(outputDir))
                .filter(path -> path.toString().endsWith(".jpg") && path.getFileName().toString().contains(baseName))
                .forEach(path -> {
                    try {
                        Files.delete(path);
                        System.out.println("Arquivo removido: " + path.getFileName());
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });

        System.out.println("Todos os frames extraídos foram removidos.");
    }
}
