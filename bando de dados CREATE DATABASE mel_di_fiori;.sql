 Criar banco de dados
CREATE DATABASE IF NOT EXISTS j_stor;
USE j_stor;

Criar usuário para a aplicação (opcional)
CREATE USER IF NOT EXISTS 'j_stor_user'@'localhost' IDENTIFIED BY 'j_stor_password';
GRANT ALL PRIVILEGES ON j_stor.* TO 'j_stor_user'@'localhost';
FLUSH PRIVILEGES;

-- A tabela será criada automaticamente pelo Hibernate com a configuração hbm2ddl.auto=update
-- Mas aqui está a estrutura esperada:
/*
CREATE TABLE produtos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    identificacao VARCHAR(255) UNIQUE NOT NULL,
    localizacao VARCHAR(255) NOT NULL,
    tipo VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    dataInstalacao DATE NOT NULL,
    numeroQuadros INT NOT NULL,
    observacoes VARCHAR(2000),
    preco DOUBLE NOT NULL,
    cor VARCHAR(255) NOT NULL,
    marca VARCHAR(255)
);
*/

-- Inserir alguns dados de exemplo
INSERT INTO produtos (identificacao, localizacao, tipo, status, dataInstalacao, numeroQuadros, observacoes, preco, cor, marca) VALUES
('CAM001', 'Camiseta Básica Preta', 'Camisetas', 'M', '2024-01-15', 50, 'Camiseta 100% algodão', 29.90, 'Preto', 'Marca A'),
('CAL002', 'Calça Jeans Skinny', 'Calças', 'G', '2024-01-20', 30, 'Calça jeans elastano', 89.90, 'Azul', 'Marca B'),
('CAS003', 'Casaco Inverno', 'Casacos', 'GG', '2024-02-01', 20, 'Casaco quente para inverno', 149.90, 'Cinza', 'Marca C'),
('VES004', 'Vestido Floral', 'Vestidos', 'P', '2024-02-10', 15, 'Vestido verão floral', 79.90, 'Rosa', 'Marca D'),
('ACE005', 'Cinto Couro', 'Acessórios', 'Único', '2024-02-15', 40, 'Cinto genuíno couro', 45.90, 'Marrom', 'Marca E');