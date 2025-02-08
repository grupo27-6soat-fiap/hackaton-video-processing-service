Feature: Processamento de Vídeo

  Scenario: Receber uma solicitação de processamento de vídeo
    Given que um usuário envia uma solicitação de processamento para um vídeo "video.mp4"
    When o sistema recebe a solicitação
    Then a solicitação deve ser registrada no sistema
    And o status da solicitação deve ser "Pendente"

  Scenario: Extração de frames do vídeo
    Given que uma solicitação de processamento foi registrada para o vídeo "video.mp4"
    When o sistema inicia a extração de frames
    Then os frames devem ser extraídos corretamente
    And os frames devem ser armazenados no local apropriado

  Scenario: Compressão de frames extraídos
    Given que os frames do vídeo "video.mp4" foram extraídos
    When o sistema inicia a compressão dos frames
    Then os frames devem ser comprimidos corretamente
    And os frames comprimidos devem ser armazenados no local apropriado
