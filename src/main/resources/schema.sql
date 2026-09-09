CREATE TABLE IF NOT EXISTS usuario  (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         nick VARCHAR(50) NOT NULL UNIQUE,
                         email VARCHAR(150) NOT NULL UNIQUE,
                         senha VARCHAR(255) NOT NULL
);


-- =====================================================
-- NOTICIA
-- =====================================================

CREATE TABLE IF NOT EXISTS noticia  (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        titulo VARCHAR(200) NOT NULL,
    descricao TEXT NOT NULL,
    data TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    autorid INT NOT NULL,
    categoria VARCHAR(50) NOT NULL DEFAULT 'geral',

    CONSTRAINT fknoticia_autor
    FOREIGN KEY (autorid)
    REFERENCES usuario(id)
    );


-- =====================================================
-- COMENTARIO
-- =====================================================

CREATE TABLE IF NOT EXISTS comentario  (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            idUsuario INT NOT NULL,
                            idNoticia INT NOT NULL,
                            conteudo TEXT NOT NULL,

                            CONSTRAINT fkcomentario_usuario
                                FOREIGN KEY (idUsuario)
                                    REFERENCES usuario(id),

                            CONSTRAINT fk_comentario_noticia
                                FOREIGN KEY (idNoticia)
                                    REFERENCES noticia(id)
);


-- =====================================================
-- CURTIDAS DA NOTICIA
-- =====================================================

CREATE TABLE IF NOT EXISTS curtida_noticia  (
                                 idUsuario INT NOT NULL,
                                 idNoticia INT NOT NULL,
                                 data TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                 PRIMARY KEY (idUsuario, idNoticia),

                                 CONSTRAINT fk_curtida_noticia_usuario
                                     FOREIGN KEY (idUsuario)
                                         REFERENCES usuario(id),

                                 CONSTRAINT fk_curtida_noticia_noticia
                                     FOREIGN KEY (idNoticia)
                                         REFERENCES noticia(id)
);


-- =====================================================
-- CURTIDAS DO COMENTARIO
-- =====================================================

CREATE TABLE IF NOT EXISTS  curtida_comentario (
                                    idUsuario INT NOT NULL,
                                    idComentario INT NOT NULL,
                                    data TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                    PRIMARY KEY (idUsuario, idComentario),

                                    CONSTRAINT fk_curtida_comentario_usuario
                                        FOREIGN KEY (idUsuario)
                                            REFERENCES usuario(id),

                                    CONSTRAINT fk_curtida_comentario_comentario
                                        FOREIGN KEY (idComentario)
                                            REFERENCES comentario(id)
);