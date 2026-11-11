package com.amigoliro.backend.evento;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// Só @Getter: sem @Setter (mudança de estado vira método com nome de regra, ex.: sortear())
// e sem @Data (o toString/equals gerado tocaria em tokens e relações LAZY).
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // exigido pelo JPA; o construtor de criação chega no Dia 5
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String codigoPublico;
    private String tokenAdminHash;
    private String nome;
    private LocalDate data;
    private BigDecimal valorMin;
    private BigDecimal valorMax;

    // STRING grava 'CLASSICO', que é o que o CHECK da migração aceita; ORDINAL gravaria 0.
    @Enumerated(EnumType.STRING)
    private Modo modo = Modo.CLASSICO;

    private boolean permiteAutoInscricao;

    // O Hibernate envia todas as colunas no INSERT, então o DEFAULT do banco nunca é usado:
    // o valor inicial precisa vir do Java.
    @Enumerated(EnumType.STRING)
    private StatusEvento status = StatusEvento.ABERTO;

    private int vezesSorteado;
    private Instant sorteadoEm;
}
