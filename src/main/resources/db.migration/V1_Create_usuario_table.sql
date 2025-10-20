-- V1 - Criação da tabela usuario (MySQL 8)
-- Estrutura baseada na entidade Usuario e no embutido Endereco

CREATE TABLE IF NOT EXISTS usuario (
  id BIGINT NOT NULL AUTO_INCREMENT,
  `E-mail` VARCHAR(255) NOT NULL,
  `Nome` VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  data_cadastro DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  ultima_atualizacao DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  -- Campos do endereço embutido
  rua VARCHAR(255) NOT NULL,
  numero VARCHAR(50) NOT NULL,
  cidade VARCHAR(255) NOT NULL,
  cep VARCHAR(8) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


