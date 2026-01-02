SET FOREIGN_KEY_CHECKS=0;

INSERT INTO users (nome, tipo, created_at)
SELECT s.nome, s.tipo, NOW()
FROM (
    -- BUYERS
    SELECT 'Maria Silva' AS nome, 'BUYER' AS tipo UNION ALL
    SELECT 'Pedro Souza','BUYER' UNION ALL
    SELECT 'Jose Santos','BUYER' UNION ALL
    SELECT 'Ana Oliveira','BUYER' UNION ALL
    SELECT 'Joao Pereira','BUYER' UNION ALL
    SELECT 'Carlos Lima','BUYER' UNION ALL
    SELECT 'Fernanda Gomes','BUYER' UNION ALL
    SELECT 'Paulo Ribeiro','BUYER' UNION ALL
    SELECT 'Lucas Carvalho','BUYER' UNION ALL
    SELECT 'Mariana Almeida','BUYER' UNION ALL
    SELECT 'Rafael Ferreira','BUYER' UNION ALL
    SELECT 'Juliana Rocha','BUYER' UNION ALL
    SELECT 'Bruno Fernandes','BUYER' UNION ALL
    SELECT 'Camila Araujo','BUYER' UNION ALL
    SELECT 'Thiago Moreira','BUYER' UNION ALL
    SELECT 'Beatriz Teixeira','BUYER' UNION ALL
    SELECT 'Ricardo Correia','BUYER' UNION ALL
    SELECT 'Luana Barros','BUYER' UNION ALL
    SELECT 'Andre Costa','BUYER' UNION ALL
    SELECT 'Sofia Martins','BUYER' UNION ALL
    -- SELLERS
    SELECT 'Gabriel Nunes','SELLER' UNION ALL
    SELECT 'Larissa Pinto','SELLER' UNION ALL
    SELECT 'Eduardo Dias','SELLER' UNION ALL
    SELECT 'Aline Melo','SELLER' UNION ALL
    SELECT 'Victor Cardoso','SELLER' UNION ALL
    SELECT 'Leticia Castro','SELLER' UNION ALL
    SELECT 'Diego Rezende','SELLER' UNION ALL
    SELECT 'Isabella Figueiredo','SELLER' UNION ALL
    SELECT 'Marcelo Tavares','SELLER' UNION ALL
    SELECT 'Priscila Brito','SELLER' UNION ALL
    SELECT 'Guilherme Pires','SELLER' UNION ALL
    SELECT 'Tatiana Sales','SELLER' UNION ALL
    SELECT 'Fabio Cunha','SELLER' UNION ALL
    SELECT 'Carolina Novaes','SELLER' UNION ALL
    SELECT 'Danilo Macedo','SELLER' UNION ALL
    SELECT 'Bianca Valente','SELLER' UNION ALL
    SELECT 'Roberto Mota','SELLER' UNION ALL
    SELECT 'Renata Paiva','SELLER' UNION ALL
    SELECT 'Felipe Aragao','SELLER' UNION ALL
    SELECT 'Gabriela Duarte','SELLER'
) s
LEFT JOIN users u ON u.nome = s.nome AND u.tipo = s.tipo
WHERE u.id IS NULL;

INSERT INTO follow (follower_id, seller_id, created_at)
SELECT uf.id AS follower_id, us.id AS seller_id, NOW()
FROM (
    SELECT 'Maria Silva' AS follower_nome, 'Gabriel Nunes' AS seller_nome UNION ALL
    SELECT 'Pedro Souza', 'Larissa Pinto' UNION ALL
    SELECT 'Jose Santos', 'Eduardo Dias' UNION ALL
    SELECT 'Ana Oliveira', 'Aline Melo' UNION ALL
    SELECT 'Joao Pereira', 'Victor Cardoso' UNION ALL
    SELECT 'Carlos Lima', 'Leticia Castro' UNION ALL
    SELECT 'Fernanda Gomes', 'Diego Rezende' UNION ALL
    SELECT 'Paulo Ribeiro', 'Isabella Figueiredo' UNION ALL
    SELECT 'Lucas Carvalho', 'Marcelo Tavares' UNION ALL
    SELECT 'Mariana Almeida', 'Priscila Brito' UNION ALL
    SELECT 'Rafael Ferreira', 'Guilherme Pires' UNION ALL
    SELECT 'Juliana Rocha', 'Tatiana Sales' UNION ALL
    SELECT 'Bruno Fernandes', 'Fabio Cunha' UNION ALL
    SELECT 'Camila Araujo', 'Carolina Novaes' UNION ALL
    SELECT 'Thiago Moreira', 'Danilo Macedo' UNION ALL
    SELECT 'Beatriz Teixeira', 'Bianca Valente' UNION ALL
    SELECT 'Ricardo Correia', 'Roberto Mota' UNION ALL
    SELECT 'Luana Barros', 'Renata Paiva' UNION ALL
    SELECT 'Andre Costa', 'Felipe Aragao' UNION ALL
    SELECT 'Sofia Martins', 'Gabriela Duarte'
) pares
JOIN users uf ON uf.nome = pares.follower_nome AND uf.tipo = 'BUYER'
JOIN users us ON us.nome = pares.seller_nome   AND us.tipo = 'SELLER'
LEFT JOIN follow f ON f.follower_id = uf.id AND f.seller_id = us.id
WHERE f.id IS NULL;

-- Limpeza para evitar erros de duplicidade ao reiniciar o app
DELETE FROM posts;
DELETE FROM products;

-- 1. Inserir Produtos (3 para cada SELLER)
INSERT INTO products (name, type, brand, color, notes, created_at)
SELECT
    CONCAT('Produto ', p.idx, ' de ', u.nome) AS name,
    'HOME_FURNITURE_DECOR' AS type,
    'Meli Brand' AS brand,
    'Padrão' AS color,
    'Nota de teste automatizado' AS notes,
    NOW()
FROM users u
         CROSS JOIN (SELECT 1 AS idx UNION ALL SELECT 2 UNION ALL SELECT 3) p
WHERE u.tipo = 'SELLER';

-- 2. Inserir Posts associando aos produtos criados
INSERT INTO posts (user_id, product_id, category, price, created_at)
SELECT
    v.user_id,
    v.product_id,
    100 AS category,
    (100.00 + v.pos) AS price,
    (CASE WHEN v.pos = 1 THEN DATE_SUB(NOW(), INTERVAL 15 DAY) ELSE NOW() END) - INTERVAL (v.seq - 1) SECOND AS created_at
FROM (
    SELECT
        u.id AS user_id,
        pr.id AS product_id,
        ROW_NUMBER() OVER (PARTITION BY u.id ORDER BY pr.id) AS pos,
        ROW_NUMBER() OVER (ORDER BY u.id, pr.id) AS seq
    FROM users u
    INNER JOIN products pr ON pr.name LIKE CONCAT('%', u.nome, '%')
    WHERE u.tipo = 'SELLER'
) v;

SET FOREIGN_KEY_CHECKS=1;

