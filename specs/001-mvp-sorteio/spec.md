# Feature Specification: MVP do Sorteio de Amigo Oculto

**Feature Branch**: `001-mvp-sorteio`

**Created**: 2026-11-08

**Status**: Draft

**Input**: User description: "Site de sorteio de amigo oculto, mobile-first, aberto a qualquer grupo e ocasião, com visual low-poly em 3D. O organizador cria o evento, cadastra os nomes (ou deixa a galera se inscrever em grupos grandes, como empresas) e manda um único link do evento no grupo. Cada pessoa escolhe o próprio nome antes do sorteio, e depois do sorteio abre o mesmo link para ver quem tirou. Precisa estar pronto para a temporada de dezembro."

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Sorteio de ponta a ponta para um grupo pequeno (Priority: P1)

O organizador cria um evento, digita os nomes dos participantes e manda um único link no grupo do WhatsApp. Cada participante abre o link e toca no próprio nome, que fica vinculado ao seu aparelho. Quando todos confirmaram, o organizador faz o sorteio. Cada participante abre o mesmo link e vê, só no próprio aparelho, quem tirou.

**Why this priority**: É o produto em si. Sem esse fluxo não existe amigo oculto, e ele sozinho já resolve o caso mais comum de dezembro: amigos e família.

**Independent Test**: Criar um evento com 5 nomes, confirmar cada nome em um navegador diferente, sortear e verificar que cada navegador mostra um amigo diferente, ninguém tira a si mesmo, e o organizador não consegue ver nenhum resultado.

**Acceptance Scenarios**:

1. **Dado** que o organizador está na página inicial, **Quando** ele informa nome e data do evento e confirma, **Então** o sistema cria o evento e mostra o link do evento para compartilhar e o link de administração para guardar.
2. **Dado** um evento criado, **Quando** o organizador adiciona os nomes dos participantes, **Então** os nomes aparecem na lista do evento como "aguardando confirmação".
3. **Dado** um evento antes do sorteio, **Quando** um participante abre o link do evento e toca em um nome ainda não confirmado, **Então** o sistema vincula aquele nome ao aparelho dele e mostra "Pronto, agora é só esperar o sorteio".
4. **Dado** que um participante já confirmou um nome neste aparelho, **Quando** ele abre o link do evento de novo, **Então** o sistema reconhece quem ele é e não oferece a lista de nomes.
5. **Dado** um evento com pelo menos 3 participantes, **Quando** o organizador toca em "Sortear" e confirma, **Então** o sistema realiza o sorteio e passa o evento para o estado "sorteado".
6. **Dado** um evento sorteado, **Quando** um participante identificado abre o link do evento, **Então** o sistema o leva direto à revelação, que mostra apenas o nome da pessoa que ele tirou, junto com nome, data e faixa de valor do evento.
7. **Dado** um evento sorteado, **Quando** o organizador abre o link de administração, **Então** ele vê o status de cada participante, mas nenhum resultado do sorteio.

---

### User Story 2 - Exclusões e faixa de valor (Priority: P2)

O organizador define pares de pessoas que não podem se tirar (casais, irmãos, quem mora junto) e uma faixa de valor sugerida para os presentes. O sorteio respeita todas as exclusões, e se não houver combinação possível, o sistema avisa sem sortear.

**Why this priority**: Exclusões são o pedido mais comum em grupos de família e casais. Sem elas, parte dos grupos refaria o sorteio por fora do produto.

**Independent Test**: Criar um evento com 6 participantes e 2 exclusões, sortear repetidas vezes e verificar que nenhuma exclusão é violada. Depois, criar exclusões que tornem o sorteio impossível e verificar que o sistema recusa com mensagem clara.

**Acceptance Scenarios**:

1. **Dado** um evento antes do sorteio, **Quando** o organizador cria uma exclusão entre Carla e Diego, **Então** o sistema registra que Carla não pode tirar Diego e Diego não pode tirar Carla.
2. **Dado** um evento com exclusões, **Quando** o sorteio é realizado, **Então** nenhum participante tira alguém com quem tem exclusão.
3. **Dado** exclusões que impedem qualquer sorteio válido, **Quando** o organizador toca em "Sortear", **Então** o sistema informa que não existe combinação possível, sugere remover exclusões e não altera o evento.
4. **Dado** um evento com faixa de valor definida, **Quando** um participante vê seu resultado, **Então** a faixa de valor aparece junto ao nome sorteado.

---

### User Story 3 - Painel do organizador e recuperação de acesso (Priority: P2)

O organizador acompanha quem já confirmou o nome e quem já viu o resultado. Ele corrige problemas: libera um nome escolhido por engano antes do sorteio e gera um link individual para quem perdeu o acesso ou não tem celular. O participante cujo acesso foi refeito é avisado.

**Why this priority**: Em grupos reais sempre alguém escolhe o nome errado, troca de celular ou não sabe usar link. Sem recuperação, o evento trava e o organizador desiste do produto.

**Independent Test**: Confirmar um nome no navegador errado, liberar esse nome pelo painel e confirmá-lo no navegador certo. Depois do sorteio, gerar um link individual para um participante, abrir em outro navegador e verificar o aviso de acesso refeito e a invalidação do acesso anterior.

**Acceptance Scenarios**:

1. **Dado** um evento antes do sorteio, **Quando** o organizador abre o painel, **Então** ele vê cada participante marcado como "confirmado" ou "aguardando".
2. **Dado** um evento sorteado, **Quando** o organizador abre o painel, **Então** ele vê cada participante marcado como "já viu" ou "ainda não viu".
3. **Dado** um nome confirmado antes do sorteio, **Quando** o organizador libera esse nome, **Então** o vínculo com o aparelho anterior é desfeito e o nome volta a ficar disponível no link do evento.
4. **Dado** qualquer participante, **Quando** o organizador gera um link individual para ele, **Então** o sistema invalida o acesso anterior desse participante e oferece o botão de enviar o novo link pelo WhatsApp.
5. **Dado** que o acesso de um participante foi refeito pelo organizador, **Quando** ele abre o novo link, **Então** o sistema mostra um aviso informando que o acesso foi refeito pelo organizador e quando isso aconteceu.
6. **Dado** um evento sorteado, **Quando** alguém abre o link do evento sem estar identificado, **Então** o sistema não oferece a lista de nomes e orienta a pessoa a pedir um link individual ao organizador.

---

### User Story 4 - Lista de desejos (Priority: P2)

Cada participante escreve o que gostaria de ganhar, o que não quer e seus tamanhos. Quem tirou essa pessoa vê a lista junto com o resultado.

**Why this priority**: Resolve a dúvida mais comum depois do sorteio ("o que eu compro?") e dá um motivo para os participantes voltarem ao site antes do evento.

**Independent Test**: Um participante preenche a lista de desejos; o participante que o tirou abre o próprio resultado e vê a lista; o organizador e os demais participantes não a veem.

**Acceptance Scenarios**:

1. **Dado** um participante identificado, **Quando** ele escreve e salva sua lista de desejos, **Então** o sistema guarda a lista e a mostra para ele editar quando quiser.
2. **Dado** um evento sorteado, **Quando** um participante vê seu resultado, **Então** a lista de desejos da pessoa que ele tirou aparece logo abaixo do nome, ou uma mensagem dizendo que ela ainda não escreveu a lista.
3. **Dado** um participante que atualizou a lista depois do sorteio, **Quando** quem o tirou abre o resultado de novo, **Então** vê a versão atualizada.

---

### User Story 5 - Revelação em 3D low-poly (Priority: P2)

A tela de revelação tem uma animação 3D low-poly: uma caixa de presente que treme a cada toque e se abre no terceiro, revelando o nome. Em aparelhos sem suporte a 3D ou com redução de movimento ativada, a revelação acontece com uma animação 2D simples.

**Why this priority**: É o diferencial do produto frente aos concorrentes e o momento que as pessoas compartilham. Fica atrás das histórias 1 a 4 porque a revelação em texto já entrega o valor principal.

**Independent Test**: Abrir a revelação em um celular Android intermediário e verificar a animação fluida; abrir com WebGL desativado e com redução de movimento ativada e verificar o fallback 2D em ambos os casos.

**Acceptance Scenarios**:

1. **Dado** um aparelho com suporte a 3D, **Quando** o participante entra na tela de revelação, **Então** vê a caixa de presente 3D e a instrução "toque para abrir".
2. **Dado** a caixa de presente na tela, **Quando** o participante toca nela três vezes, **Então** a caixa treme mais forte a cada toque e se abre no terceiro, revelando o nome com efeito de partículas.
3. **Dado** um aparelho sem suporte a WebGL ou com redução de movimento ativada, **Quando** o participante entra na tela de revelação, **Então** o nome é revelado com uma animação 2D de cartão virando.
4. **Dado** um participante que já viu o resultado, **Quando** ele volta à tela de revelação, **Então** pode ver o nome de novo direto ou repetir a animação.

---

### User Story 6 - Auto-inscrição para grupos grandes (Priority: P3)

Em grupos grandes, como empresas, o organizador liga a opção "deixar a galera se adicionar". Quem abre o link do evento antes do sorteio pode digitar o próprio nome, que entra na lista já confirmado no aparelho dele.

**Why this priority**: Abre o público de empresas e grupos grandes, onde digitar todos os nomes é inviável. Fica em P3 porque o público principal de dezembro são grupos pequenos.

**Independent Test**: Criar um evento com auto-inscrição ligada, inscrever 30 pessoas por navegadores diferentes, sortear e verificar que todas recebem resultado válido.

**Acceptance Scenarios**:

1. **Dado** um evento com auto-inscrição ligada e antes do sorteio, **Quando** um visitante abre o link, **Então** vê a lista de nomes existentes e a opção "Não estou na lista, quero entrar".
2. **Dado** um visitante usando a auto-inscrição, **Quando** ele informa um nome que ainda não existe no evento, **Então** o sistema adiciona o participante já confirmado no aparelho dele.
3. **Dado** um visitante usando a auto-inscrição, **Quando** ele informa um nome que já existe no evento, **Então** o sistema recusa e pede para ele escolher o nome na lista ou usar um nome diferenciado, como "Maria Clara" em vez de "Maria".
4. **Dado** um evento com auto-inscrição desligada, **Quando** um visitante abre o link, **Então** não vê a opção de se adicionar.

---

### User Story 7 - Modo Amigo da Onça (Priority: P3)

Ao criar o evento, o organizador escolhe entre o modo Clássico e o modo Amigo da Onça, em que a graça é dar o presente mais engraçado ou inútil. O modo muda os textos do evento e a animação da revelação.

**Why this priority**: Dá personalidade e assunto para o produto ser compartilhado, mas não altera a mecânica do sorteio.

**Independent Test**: Criar um evento em cada modo e verificar que textos e animação de revelação mudam, enquanto as regras do sorteio permanecem iguais.

**Acceptance Scenarios**:

1. **Dado** a tela de criação de evento, **Quando** o organizador escolhe o modo Amigo da Onça, **Então** o evento é criado com esse modo e os textos exibidos aos participantes refletem a proposta do modo.
2. **Dado** um evento no modo Amigo da Onça, **Quando** um participante abre a revelação, **Então** a caixa de presente se abre com uma variação cômica da animação, e o fallback 2D também tem sua variação.

---

### Edge Cases

- **Menos de 3 participantes:** o botão de sortear fica desabilitado, com a explicação de que são necessárias pelo menos 3 pessoas.
- **Mais de 200 participantes:** o sistema recusa novas adições e informa o limite.
- **Sorteio impossível por exclusões:** o sistema recusa, explica e não altera o evento (User Story 2).
- **Nome já confirmado por outra pessoa:** o sistema informa que o nome já foi escolhido e orienta a falar com o organizador, que pode liberá-lo antes do sorteio.
- **Aparelho tenta confirmar um segundo nome pelo link do evento:** o sistema recusa. Um aparelho confirma no máximo uma identidade pelo link do evento.
- **Responsável com dependentes sem celular** (criança, idoso): o organizador gera links individuais para os dependentes. Um link individual abre a identidade vinculada a ele mesmo em um aparelho que já tem outra identidade no evento.
- **Participante não confirmado no momento do sorteio:** o organizador é avisado antes de confirmar o sorteio. Após o sorteio, esse participante só acessa o resultado por link individual.
- **Participante limpou os dados do navegador ou trocou de celular:** perde o acesso e recupera por link individual gerado pelo organizador (User Story 3).
- **Organizador perde o link de administração:** o link fica salvo na lista "Meus eventos" do aparelho em que o evento foi criado. Fora desse aparelho, não há recuperação, e isso é informado com destaque no momento da criação.
- **Organizador quer refazer o sorteio:** é permitido após confirmação explícita. O sorteio anterior é descartado, e todo participante que abrir o resultado vê o aviso "o sorteio foi refeito em [data e hora]".
- **Alterar participantes ou exclusões após o sorteio:** bloqueado. Para mudar, o organizador refaz o sorteio, o que reabre a edição.
- **Link do evento chega a um estranho antes do sorteio:** o estranho só consegue confirmar nomes ainda não confirmados. O painel mostra quem confirmou, e o organizador libera qualquer confirmação indevida.
- **Tentativas repetidas de adivinhar links:** os endpoints públicos limitam a quantidade de requisições por origem.
- **Evento depois da data:** continua acessível até ser apagado, 60 dias após a data do evento.
- **Nomes com acento ou maiúsculas diferentes** ("José" e "jose"): são considerados o mesmo nome para efeito de duplicidade.

## Requirements *(mandatory)*

### Functional Requirements

**Evento**

- **FR-001**: Organizadores DEVEM poder criar um evento sem conta, informando nome (obrigatório), data (obrigatória, no futuro), faixa de valor (opcional, mínimo e máximo em reais), modo (Clássico ou Amigo da Onça; padrão Clássico) e a opção de auto-inscrição (padrão desligada).
- **FR-002**: Ao criar um evento, o sistema DEVE gerar um link público do evento e um link de administração, ambos distintos e não adivinháveis.
- **FR-003**: O sistema DEVE exibir o link de administração com destaque, instrução para guardá-lo, botões de copiar e de enviar para si mesmo pelo WhatsApp, e DEVE salvá-lo na lista "Meus eventos" do aparelho.
- **FR-004**: O organizador DEVE poder editar nome, data e faixa de valor do evento a qualquer momento, e DEVE poder apagar o evento a qualquer momento.

**Participantes**

- **FR-005**: O organizador DEVE poder adicionar e remover participantes antes do sorteio.
- **FR-006**: O sistema DEVE rejeitar nomes duplicados no mesmo evento, desconsiderando maiúsculas, minúsculas e acentos.
- **FR-007**: Um evento DEVE ter entre 3 e 200 participantes para ser sorteado.
- **FR-008**: Com a auto-inscrição ligada, visitantes do link do evento DEVEM poder se adicionar antes do sorteio, já confirmados no próprio aparelho.

**Identidade e acesso**

- **FR-009**: Antes do sorteio, visitantes do link do evento DEVEM poder escolher um nome ainda não confirmado, vinculando aquela identidade ao aparelho.
- **FR-010**: Um aparelho DEVE confirmar no máximo uma identidade por evento pelo link do evento.
- **FR-011**: Um nome confirmado NÃO DEVE poder ser escolhido por outro aparelho; o sistema DEVE orientar a pessoa a procurar o organizador.
- **FR-012**: O organizador DEVE poder liberar um nome confirmado antes do sorteio, desfazendo o vínculo com o aparelho anterior.
- **FR-013**: Após o sorteio, o link do evento NÃO DEVE oferecer escolha de nome; participantes não identificados DEVEM ser orientados a pedir um link individual ao organizador.
- **FR-014**: O organizador DEVE poder gerar um link individual para qualquer participante a qualquer momento. Gerar um link individual DEVE invalidar o acesso anterior desse participante.
- **FR-015**: Um link individual DEVE abrir a identidade vinculada a ele, mesmo em um aparelho que já tenha outra identidade no mesmo evento. Nesse aparelho, o link do evento continua abrindo a identidade confirmada por ele, e cada link individual abre apenas a própria identidade.
- **FR-016**: Quando o acesso de um participante for refeito pelo organizador, o sistema DEVE mostrar a esse participante um aviso com a data e a hora em que isso aconteceu.

**Exclusões e sorteio**

- **FR-017**: O organizador DEVE poder criar e remover exclusões entre dois participantes antes do sorteio. Toda exclusão é mútua.
- **FR-018**: O sorteio DEVE garantir que ninguém tira a si mesmo, que cada participante dá e recebe exatamente um presente, e que todas as exclusões são respeitadas.
- **FR-019**: Quando nenhuma combinação válida existir, o sistema DEVE informar o organizador, sugerir a remoção de exclusões e NÃO DEVE alterar o evento.
- **FR-020**: O sorteio DEVE usar fonte de aleatoriedade criptograficamente segura.
- **FR-021**: Antes de sortear, o sistema DEVE pedir confirmação ao organizador e informar quantos participantes ainda não confirmaram o nome.
- **FR-022**: Após o sorteio, participantes e exclusões DEVEM ficar bloqueados para edição.
- **FR-023**: O organizador DEVE poder refazer o sorteio após confirmação explícita. Refazer DEVE descartar o resultado anterior, reabrir a edição de participantes e exclusões até o novo sorteio, e fazer todo participante ver o aviso "o sorteio foi refeito em [data e hora]" ao abrir o resultado.

**Revelação**

- **FR-024**: Um participante identificado DEVE ver apenas o nome de quem tirou, o nome, a data e a faixa de valor do evento, e a lista de desejos da pessoa sorteada.
- **FR-025**: O sistema DEVE registrar a primeira visualização do resultado de cada participante.
- **FR-026**: A revelação DEVE usar animação 3D low-poly quando o aparelho suportar, e animação 2D quando o aparelho não suportar WebGL ou a pessoa tiver redução de movimento ativada.
- **FR-027**: O modo Amigo da Onça DEVE ter textos próprios e variações próprias das animações 3D e 2D da revelação.
- **FR-028**: Ao final da revelação, o sistema DEVE exibir um convite para criar o amigo oculto de outro grupo.

**Lista de desejos**

- **FR-029**: Um participante identificado DEVE poder criar e editar sua lista de desejos em texto livre de até 500 caracteres, antes ou depois do sorteio.
- **FR-030**: A lista de desejos DEVE ser visível apenas para o próprio participante e para quem o tirou. O organizador NÃO DEVE ver listas de desejos de outros participantes.

**Painel do organizador**

- **FR-031**: O painel DEVE mostrar, para cada participante, se ele confirmou o nome e se já viu o resultado.
- **FR-032**: Nenhuma tela ou resposta acessível pelo link de administração DEVE conter o resultado do sorteio ou a lista de desejos de qualquer participante.

**Compartilhamento**

- **FR-033**: O sistema DEVE oferecer o compartilhamento do link do evento pelo WhatsApp com mensagem pronta, e pelo compartilhamento nativo do aparelho quando disponível.
- **FR-034**: Após o sorteio, o sistema DEVE oferecer ao organizador uma mensagem pronta para avisar o grupo de que o sorteio saiu.
- **FR-035**: Cada link individual DEVE ter um botão de envio pelo WhatsApp com mensagem pronta.

**Dados e proteção**

- **FR-036**: O sistema NÃO DEVE coletar telefone, e-mail ou qualquer documento de participantes ou organizadores.
- **FR-037**: O sistema DEVE apagar automaticamente todos os dados de um evento 60 dias após a data do evento.
- **FR-038**: O sistema DEVE limitar a quantidade de requisições aos endpoints públicos por origem.
- **FR-039**: O sistema DEVE disponibilizar uma política de privacidade em linguagem simples, acessível em todas as telas.

### Key Entities

- **Evento**: um amigo oculto específico. Tem nome, data, faixa de valor, modo, se permite auto-inscrição, estado (aberto ou sorteado), quantas vezes foi sorteado, o código do link público e a credencial de administração.
- **Participante**: uma pessoa dentro de um evento. Tem nome, se está confirmado, a credencial de acesso vinculada ao seu aparelho ou link individual, a data em que o acesso foi refeito (se foi), a pessoa que tirou, a data da primeira visualização e a lista de desejos. Pertence a um único evento.
- **Exclusão**: um par de participantes do mesmo evento que não podem se tirar mutuamente.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Um organizador cria um evento com 10 nomes e compartilha o link em até 2 minutos usando apenas o celular.
- **SC-002**: Após o sorteio, um participante identificado vê quem tirou em até 3 toques a partir do link do evento.
- **SC-003**: Em 10.000 sorteios automatizados com quantidades de participantes e exclusões aleatórias, 0 violações das regras do FR-018, e 100% dos casos impossíveis são detectados sem alterar o evento.
- **SC-004**: 0 telas ou respostas acessíveis pelo link de administração expõem resultados ou listas de desejos, verificado por testes automatizados de todas as rotas de administração.
- **SC-005**: A página inicial tem nota de desempenho 90 ou mais no Lighthouse em perfil mobile, com maior conteúdo visível carregado em até 2,5 segundos.
- **SC-006**: A revelação 3D roda a pelo menos 50 quadros por segundo em um celular Android intermediário, e 100% dos aparelhos sem suporte a WebGL recebem a revelação 2D.
- **SC-007**: O produto está publicamente disponível até 25/11/2026.
- **SC-008**: Pelo menos 20 eventos reais, com 5 ou mais participantes cada, são sorteados até 25/12/2026, incluindo pelo menos 1 evento com 30 ou mais participantes.

## Assumptions

- O organizador conhece os participantes e os alcança por um grupo de WhatsApp ou mensagens individuais.
- Participantes usam celulares com navegadores modernos (as duas versões mais recentes de Chrome para Android e Safari para iOS).
- A identidade de um participante fica vinculada ao navegador do aparelho; usar outro navegador ou limpar os dados equivale a perder o acesso, que é recuperável pelo organizador.
- A interface é apenas em português do Brasil.
- O produto é gratuito no MVP, sem login, pagamento ou anúncios.

### Fora de Escopo

- Pergunta anônima, dicas anônimas, votação de presentes, modo festa na TV e jogo de adivinhação.
- Skins por ocasião, mascote animado e card de compartilhamento para stories.
- Setores de empresa, exclusão por grupos, exportação de lista e histórico entre anos.
- Modos amigo chocolate, amigo ladrão, amigo temático e amigo de duplas.
- Envio automático por bot de WhatsApp, e-mail ou SMS.
- Login, contas de organizador, planos pagos e recuperação do link de administração fora do aparelho de criação.
