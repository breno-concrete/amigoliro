package com.amigoliro.backend.sorteio;

import java.security.SecureRandom;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// O Sorteador é Java puro (sem @Component); é aqui que o Spring aprende a criá-lo.
// No app, sempre SecureRandom (FR-020). O Random com semente fica só nos testes.
@Configuration
public class SorteioConfig {

    @Bean
    Sorteador sorteador() {
        return new Sorteador(new SecureRandom());
    }
}
