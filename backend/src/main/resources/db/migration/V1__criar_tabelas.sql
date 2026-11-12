CREATE TABLE evento (
                        id                      UUID          PRIMARY KEY,
                        codigo_publico          VARCHAR(32)   NOT NULL,
                        token_admin_hash        VARCHAR(64)   NOT NULL,
                        nome                    VARCHAR(100)  NOT NULL,
                        data                    DATE          NOT NULL,
                        valor_min               NUMERIC(10,2),
                        valor_max               NUMERIC(10,2),
                        modo                    VARCHAR(20)   NOT NULL,
                        permite_auto_inscricao  BOOLEAN       NOT NULL DEFAULT FALSE,
                        status                  VARCHAR(20)   NOT NULL,
                        vezes_sorteado          INTEGER       NOT NULL DEFAULT 0,
                        sorteado_em             TIMESTAMPTZ,

                        CONSTRAINT uk_evento_codigo_publico   UNIQUE (codigo_publico),
                        CONSTRAINT uk_evento_token_admin_hash UNIQUE (token_admin_hash),
                        CONSTRAINT ck_evento_valores CHECK (valor_min >= 0 AND valor_max >= 0 AND valor_min <= valor_max),
                        CONSTRAINT ck_evento_modo    CHECK (modo IN ('CLASSICO', 'AMIGO_DA_ONCA')),
                        CONSTRAINT ck_evento_status  CHECK (status IN ('ABERTO', 'SORTEADO'))
);


CREATE TABLE participante (
    id UUID PRIMARY KEY,
    evento_id UUID NOT NULL ,
    token_acesso_hash VARCHAR(64),
    nome VARCHAR(100) NOT NULL,
    nome_normalizado VARCHAR(100) NOT NULL,
    amigo_id UUID,
    lista_de_desejos VARCHAR(500),
    visualizou_em TIMESTAMPTZ,
    acesso_refeito_em TIMESTAMPTZ,


    CONSTRAINT uk_participante_nome_normalizado UNIQUE (evento_id, nome_normalizado),
    CONSTRAINT uk_participante_amigo_id UNIQUE (amigo_id),
    CONSTRAINT uk_participante_token_acesso_hash UNIQUE (token_acesso_hash),

    CONSTRAINT fk_participante_evento FOREIGN KEY (evento_id) REFERENCES evento(id) ON DELETE CASCADE,
    CONSTRAINT fk_participante_amigo FOREIGN KEY (amigo_id) REFERENCES participante(id)
);

CREATE TABLE exclusao (
    id UUID PRIMARY KEY,
    evento_id UUID NOT NULL ,
    participante_a_id UUID NOT NULL ,
    participante_b_id UUID NOT NULL ,


    CONSTRAINT fk_exclusao_evento FOREIGN KEY (evento_id) references evento(id) ON DELETE CASCADE,
    CONSTRAINT fk_exclusao_participante_a FOREIGN KEY (participante_a_id) REFERENCES participante(id) ON DELETE CASCADE,
    CONSTRAINT fk_exclusao_participante_b FOREIGN KEY (participante_b_id) REFERENCES participante(id) ON DELETE CASCADE,

    CONSTRAINT uk_exclusao_participantes UNIQUE (participante_a_id, participante_b_id),

    CONSTRAINT ck_exclusao_participante_a CHECK ( participante_a_id < participante_b_id)

);
