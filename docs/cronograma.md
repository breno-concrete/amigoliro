# Cronograma: do dia 1 ao lançamento

**Início:** dom 08/11/2026 · **Lançamento:** qua 25/11/2026 (SC-007) · **18 dias**

Premissa: cerca de 4 horas por dia. Se for menos, use a ordem de corte no fim deste arquivo.

## Estratégia em 4 frases

1. **Backend primeiro** (dias 1–10), porque é onde você tem base (Java) e onde moram as regras não negociáveis: sorteio e sigilo.
2. **Deploy cedo** (dia 10), com o backend "quase vazio" já em produção. O dia do lançamento não pode ser o primeiro deploy.
3. **Frontend funcional em 2D** (dias 11–14). No fim do dia 14 o produto já funciona de ponta a ponta, sem 3D.
4. **3D e polimento por último** (dias 15–17), porque a constituição diz que o 3D é enfeite (princípio IV).

## Marcos

| Dia | Data | Marco |
|---|---|---|
| 4 | qua 11/11 | Sorteio provado por testes (10.000 execuções, 0 violações) |
| 9 | seg 16/11 | API completa das histórias 1 a 4, com testes de sigilo |
| 10 | ter 17/11 | Backend em produção com HTTPS |
| 14 | sáb 21/11 | **Produto funcionando de ponta a ponta no celular (2D)** |
| 17 | ter 24/11 | Evento real testado com amigos |
| 18 | qua 25/11 | **Lançamento** |

---

## Semana 1: fundação e coração do sistema

### Dia 1 · dom 08/11 · Repositório e Spring Boot rodando
- [ ] Primeiro commit e repositório no GitHub
- [ ] Gerar o projeto no start.spring.io dentro de `backend/` (Spring Web, Spring Data JPA, PostgreSQL Driver, Flyway, Validation)
- [ ] `docker-compose.yml` com PostgreSQL local
- [ ] App sobe e conecta no banco; um endpoint `GET /api/ping` responde
- **Você aprende:** o que o Spring Boot faz por você, pom.xml e dependências, `application.properties`, Docker Compose.
- **Pronto quando:** `./mvnw spring-boot:run` sobe sem erro e o ping responde no navegador.

### Dia 2 · seg 09/11 · Banco de dados e CI
- [ ] Migração Flyway `V1__criar_tabelas.sql` com as 3 tabelas e **todas** as restrições do `data-model.md`
- [ ] Entidades JPA `Evento`, `Participante`, `Exclusao` e enums `Modo`, `StatusEvento`
- [ ] Repositories (`EventoRepository` etc.)
- [ ] GitHub Actions rodando `./mvnw verify` em todo PR; proteção da `main` (merge só com CI verde)
- [ ] Primeira branch no padrão `001-mvp-sorteio` e primeiro PR
- **Você aprende:** por que migração em vez de `ddl-auto`, `@Entity`/`@Id`/`@ManyToOne`, UUID como chave, CI.
- **Pronto quando:** o CI fica verde num PR e as tabelas aparecem no banco.

### Dia 3 · ter 10/11 · Algoritmo do sorteio com TDD (parte 1)
- [ ] Classe `Sorteador` em Java puro (sem Spring, sem banco): recebe participantes e exclusões, devolve pares
- [ ] Testes escritos **antes**: ninguém tira a si mesmo; cada um dá e recebe uma vez; exclusões respeitadas
- [ ] Implementação com `SecureRandom` (FR-020)
- **Você aprende:** TDD (vermelho → verde → refatorar), JUnit 5, AssertJ, por que separar regra de negócio do framework.

### Dia 4 · qua 11/11 · Sorteio (parte 2): casos impossíveis e testes em massa
- [ ] Detecção de combinação impossível sem alterar nada (FR-019) com exceção própria
- [ ] Teste de propriedade: 10.000 execuções com sementes, tamanhos e exclusões aleatórias (SC-003)
- [ ] Casos de borda: 3 participantes, 200 participantes, exclusões que travam
- **Você aprende:** backtracking, teste de propriedade, sementes em testes.
- **Marco:** o coração do produto está provado.

### Dia 5 · qui 12/11 · Tokens e criar evento
- [ ] `TokenService`: token com 128+ bits de `SecureRandom` em Base64 URL, e hash SHA-256 para guardar no banco
- [ ] `POST /api/eventos` (FR-001, FR-002): DTO com Bean Validation (`@NotBlank`, `@Future`), devolve link público e link de admin
- [ ] `@RestControllerAdvice` com erros em linguagem simples (princípio III)
- **Você aprende:** por que guardar só o hash (igual senha), DTO vs entidade, camadas Controller → Service → Repository.

### Dia 6 · sex 13/11 · Painel do organizador e participantes
- [ ] Autenticação do admin pelo token (header), sem Spring Security completo
- [ ] `GET` do painel: participantes com status confirmado/aguardando e já viu/não viu (FR-031), **sem resultado**
- [ ] Adicionar/remover participante, nome normalizado sem acento, limite de 200 (FR-005, FR-006, FR-007)
- [ ] Editar e apagar evento (FR-004)
- **Você aprende:** `Normalizer` do Java, tratar violação de UNIQUE do banco, status HTTP certos (400, 404, 409).

### Dia 7 · sáb 14/11 · Identidade do participante
- [ ] `GET /api/eventos/{codigo}`: dados públicos + nomes disponíveis (só antes do sorteio, FR-013)
- [ ] Confirmar nome → devolve token de acesso (FR-009 a FR-011)
- [ ] Liberar nome pelo painel (FR-012)
- [ ] Gerar link individual, que invalida o anterior e grava `acessoRefeitoEm` (FR-014 a FR-016)
- **Você aprende:** como o "aparelho" vira só um token guardado no navegador; idempotência.

---

## Semana 2: API completa, produção e frontend

### Dia 8 · dom 15/11 · Exclusões, sorteio e revelação na API
- [ ] Criar/remover exclusão, guardando A < B (FR-017)
- [ ] Sortear com aviso de não confirmados (FR-021), bloqueio de edição (FR-022), refazer (FR-023)
- [ ] `GET` do resultado do participante: amigo, dados do evento, lista de desejos do amigo, avisos; grava `visualizouEm` (FR-024, FR-025)
- [ ] Editar a própria lista de desejos, até 500 caracteres (FR-029)
- **Você aprende:** `@Transactional` (sorteio é tudo ou nada), máquina de estados.

### Dia 9 · seg 16/11 · Sigilo e segurança (princípio I)
- [ ] Testes de integração com Testcontainers (Postgres de verdade) para **toda** rota de admin: a resposta não contém amigo nem lista de desejos (SC-004)
- [ ] Revisar logs: nenhum token, par ou lista
- [ ] Limite de requisições nos endpoints públicos (FR-038)
- [ ] CORS restrito ao domínio do frontend
- [ ] Job `@Scheduled` que apaga eventos 60 dias após a data (FR-037)
- **Você aprende:** `@SpringBootTest` + MockMvc, Testcontainers, filtros HTTP.
- **Marco:** API das histórias 1 a 4 completa.

### Dia 10 · ter 17/11 · Backend em produção
- [ ] `Dockerfile` multi-stage do backend
- [ ] Banco gerenciado (sugestão: Neon) e backend hospedado (sugestão: Render), com HTTPS
- [ ] Variáveis de ambiente para senha do banco e domínio do CORS
- [ ] Deploy automático da `main` pelo GitHub Actions
- **Você aprende:** imagem Docker, variáveis de ambiente em produção, por que deploy cedo.
- **Marco:** `https://.../api/ping` responde na internet.

### Dia 11 · qua 18/11 · Frontend: base e tela de criar evento
- [ ] Vite + React + TypeScript + Tailwind em `frontend/`
- [ ] Rotas: `/`, `/e/:codigo`, `/admin/:token`, `/p/:token`, `/privacidade`
- [ ] Cliente da API (um arquivo com uma função por endpoint)
- [ ] Tela inicial com formulário de criar evento, desenhada em 360px
- [ ] Site estático publicado (sugestão: Cloudflare Pages)
- **Você aprende:** componente, estado, formulário controlado, mobile-first com Tailwind.

### Dia 12 · qui 19/11 · Frontend: evento criado e painel
- [ ] Tela pós-criação: link de admin em destaque, copiar, WhatsApp, "Meus eventos" no `localStorage` (FR-003)
- [ ] Painel: adicionar/remover participantes, exclusões, status de cada um
- [ ] Compartilhar link do evento pelo WhatsApp e compartilhamento nativo (FR-033)

### Dia 13 · sex 20/11 · Frontend: link do evento
- [ ] Escolher o próprio nome e guardar o token no aparelho
- [ ] "Pronto, agora é só esperar o sorteio"; reconhecer quem já confirmou
- [ ] Depois do sorteio, sem identidade: orientar a pedir link individual
- [ ] Link individual com aviso de acesso refeito
- [ ] Lista de desejos: escrever e editar

### Dia 14 · sáb 21/11 · Frontend: sorteio e revelação 2D
- [ ] Botão sortear com confirmação e contagem de não confirmados; refazer sorteio
- [ ] Mensagem pronta para avisar o grupo (FR-034); WhatsApp nos links individuais (FR-035)
- [ ] Revelação 2D (cartão virando, CSS) com nome, evento, faixa de valor e lista de desejos
- [ ] Convite final para criar outro amigo oculto (FR-028)
- **Marco: produto completo de ponta a ponta no celular.** A partir daqui, tudo é melhoria.

---

## Semana 3: diferencial, polimento e lançamento

### Dia 15 · dom 22/11 · Revelação 3D
- [ ] React Three Fiber carregado só na tela de revelação (`React.lazy`)
- [ ] Caixa de presente low-poly feita com geometrias simples (sem modelo pesado)
- [ ] Treme a cada toque, abre no terceiro, partículas simples
- [ ] Fallback 2D quando não há WebGL ou com `prefers-reduced-motion` (FR-026)
- [ ] Renderização pausada quando nada se move (`frameloop="demand"`)

### Dia 16 · seg 23/11 · Polimento mobile e privacidade
- [ ] Testar tudo num Android físico: alvos de 44px, uma mão, mensagens de erro claras
- [ ] Lighthouse mobile ≥ 90 na home (SC-005)
- [ ] Página de política de privacidade em linguagem simples, link em todas as telas (FR-039)

### Dia 17 · ter 24/11 · Ensaio geral
- [ ] Evento real com 5+ amigos em celulares diferentes
- [ ] Corrigir o que eles encontrarem
- [ ] Checklist final da constituição: sigilo, logs, HTTPS, CORS, CI verde

### Dia 18 · qua 25/11 · Lançamento
- [ ] Smoke test em produção (criar, confirmar, sortear, revelar)
- [ ] Divulgar nos grupos
- [ ] Acompanhar erros nos logs do servidor

---

## Ordem de corte se atrasar

A constituição manda cortar escopo, nunca sigilo nem testes do sorteio. Corte nesta ordem:

1. **US7 Modo Amigo da Onça** (P3): fora do cronograma desde já; entra só se sobrar tempo.
2. **US6 Auto-inscrição** (P3): fora do cronograma desde já; entra só se sobrar tempo.
3. **US5 Revelação 3D**: a revelação 2D já entrega o valor. O 3D pode sair numa versão depois do lançamento.
4. **Partículas e animações extras** dentro do 3D.

**Nunca cortar:** testes do sorteio (dias 3–4), testes de sigilo (dia 9), HTTPS.

## Se sobrar tempo (depois do dia 14 ou após o lançamento)

- US6 Auto-inscrição: a API reaproveita o "confirmar nome" (≈ 1 dia).
- US7 Modo Amigo da Onça: textos e uma variação da animação (≈ 1 dia).
