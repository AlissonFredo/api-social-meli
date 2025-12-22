SET FOREIGN_KEY_CHECKS=0;

INSERT INTO user (nome, tipo, created_at)
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
         LEFT JOIN user u
                   ON u.nome = s.nome AND u.tipo = s.tipo
WHERE u.id IS NULL;

SET FOREIGN_KEY_CHECKS=1;

