package com.amigoliro.backend.exclusao;

import com.amigoliro.backend.evento.Evento;
import com.amigoliro.backend.participante.Participante;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Exclusao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "evento_id")
    private Evento evento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "participante_a_id")
    private Participante participanteA;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "participante_b_id")
    private Participante participanteB;
}
