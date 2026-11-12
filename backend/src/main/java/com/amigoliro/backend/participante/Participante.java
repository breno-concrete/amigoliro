package com.amigoliro.backend.participante;

import com.amigoliro.backend.evento.Evento;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participante {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "evento_id")
    private Evento evento;

    private String tokenAcessoHash;

    private String nome;

    private String nomeNormalizado;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amigo_id")
    private Participante amigo;

    private String listaDeDesejos;

    private Instant visualizouEm;

    private Instant acessoRefeitoEm;
}
