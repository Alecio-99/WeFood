-- V2 - Índices e constraints adicionais

-- Unique no e-mail (coluna nomeada como `E-mail` na entidade)
ALTER TABLE usuario
  ADD CONSTRAINT uk_usuario_email UNIQUE (`E-mail`);

-- Índices auxiliares para busca e filtros
CREATE INDEX idx_usuario_nome ON usuario (`Nome`);
CREATE INDEX idx_usuario_cidade ON usuario (cidade);


