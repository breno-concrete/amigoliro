package com.amigoliro.backend.participante;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ParticipanteRepository extends JpaRepository<Participante, UUID> {
}
