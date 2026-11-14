# Pong em Java SE - FATEC Osasco

Tecnicas de Programacao, avaliacao final em grupo. Entrega: 28/11/2026, as 17h30.

Integrantes: [NOME 1], [NOME 2], [NOME 3], [NOME 4]. Ajustar a lista ao grupo real.

Implementado e validado com Microsoft OpenJDK 21.0.13.0.2, Windows 11. Apenas biblioteca padrao Java SE, com Swing/AWT. Todos os fontes do jogo estao na raiz, no pacote padrao. A pasta preexistente `Exemplos` pertence a outro exercicio e nao participa da compilacao.

## Compilacao e execucao

Abra um terminal na pasta deste README, com um JDK completo no PATH:

```powershell
javac *.java
java Main
```

O campo tem area util de 800 x 600 pixels. Na tela inicial, selecione a dificuldade com D e pressione ESPACO para iniciar. Fechar a janela encerra o processo.

Para verificar todos os avisos do compilador:

```powershell
javac -Xlint:all -Werror *.java
```

Os fontes Java usam texto ASCII, evitando depender da codificacao padrao do terminal.

### Gravacao local e alternativa de compilacao

Na primeira verificacao, o compilador nao conseguiu gravar arquivos `.class` diretamente nesta pasta do OneDrive. Em nova tentativa, a compilacao na propria pasta funcionou com `javac -Xlint:all -Werror *.java`, sem avisos, e os 133 testes passaram usando os arquivos locais. A causa da falha inicial nao foi determinada. Se a restricao voltar a ocorrer, a alternativa abaixo tambem foi verificada:

```powershell
$pongBuild = Join-Path $env:TEMP 'fatec-pong-build'
New-Item -ItemType Directory -Force -Path $pongBuild | Out-Null
javac -Xlint:all -Werror -d $pongBuild *.java
java -cp $pongBuild Main
java -cp $pongBuild PongLogicTest
```

## Testes automatizados

Sem JUnit, frameworks ou necessidade de habilitar `-ea`:

```powershell
javac PongLogicTest.java
java PongLogicTest
```

Em uma pasta sem `.class`, o javac encontra automaticamente os fontes das classes de logica utilizadas pelo teste. Nenhum JFrame ou JPanel e instanciado pela suite. Tambem e possivel executar com `java -Djava.awt.headless=true PongLogicTest`.

Resultado esperado: `[OK] Todos os 133 testes passaram.` Uma falha imprime `[FALHA] descricao` e encerra com codigo 1; sucesso encerra com codigo 0. O total inclui 100 verificacoes das velocidades de saque aleatorio; as demais verificam comportamentos de jogo.

Cobertura: teto/chao com correcao de posicao, duas raquetes, ausencia de rebote repetido, saida completa e parcial, pontuacao unica, centralizacao, zeragem do placar, tolerancia da IA, velocidades dos niveis, limites, parada, comandos opostos, vitoria dos dois lados, congelamento apos vitoria, reinicios consecutivos e encapsulamento de Rectangle.

## Controles

| Tecla | Acao |
| --- | --- |
| W ou seta para cima | Subir a raquete esquerda enquanto pressionada |
| S ou seta para baixo | Descer a raquete esquerda enquanto pressionada |
| ESPACO | Iniciar ou reiniciar a partida, zerando o placar |
| D | Alternar Facil/Dificil na tela inicial ou de vitoria |

Soltar a tecla interrompe o respectivo comando. Cima e baixo juntos param a raquete. As teclas equivalentes sao independentes: soltar W nao cancela uma seta ainda pressionada. Perder o foco da janela limpa os comandos. A repeticao automatica de ESPACO nao reinicia varias vezes sem liberacao.

## Regras e dificuldades

- Jogador humano a esquerda contra computador a direita. Nao ha multiplayer local.
- Vence quem chegar primeiro a 9 pontos. Somente a bola completamente fora de uma lateral pontua para o adversario.
- Apos cada ponto, a bola retorna ao centro; o placar permanece. O saque usa velocidade horizontal de modulo 4 pixels por atualizacao e vertical aleatoria entre -4 e +4.
- O jogador anda 6 pixels por atualizacao. Raquetes medem 20 x 100 pixels e a bola tem diametro 20.
- A IA compara os centros verticais, sem aprendizado de maquina, previsao de trajetoria ou teletransporte.
- Facil: 3 pixels por atualizacao, tolerancia de 25 pixels. Dificil: 6 pixels, tolerancia de 8 pixels. Diferencas menores que a tolerancia fazem a IA parar.
- Na vitoria, a logica para e uma mensagem persistente identifica o vencedor, sem caixas de dialogo repetidas. O mesmo timer continua solicitando desenhos.
- ESPACO restaura placar, entidades, velocidades, comandos e estado JOGANDO, mantendo a dificuldade selecionada e o unico timer existente.

## Classes e orientacao a objetos

| Classe | Responsabilidade e escolha de modelagem |
| --- | --- |
| Main | Apenas main; agenda a construcao da janela na thread de eventos Swing |
| JanelaJogo | Herda de JFrame porque e uma janela especializada; adiciona o campo, aplica pack e trata perda de foco |
| CampoJogo | Herda de JPanel e implementa ActionListener; cria e mantem bola, duas raquetes e placar por composicao; associa teclado, timer e renderizacao |
| Partida | Coordena regras, colisoes, pontuacao e estado; recebe as mesmas entidades criadas pelo campo, permitindo testes isolados sem Swing |
| Bola | Encapsula posicao, diametro, velocidades e saque; nao herda de Raquete |
| Raquete | Encapsula dimensoes, limites, movimento e velocidade; duas instancias independentes reutilizam a mesma classe |
| Placar | Encapsula pontos; apenas seus metodos incrementam ou zeram contadores |
| ComputadorIA | Encapsula nivel e decisao vertical; comanda a raquete sem alterar seus campos diretamente |
| PongLogicTest | Executa verificacoes manuais sobre a mesma logica usada pelo jogo |

Todos os campos de estado das entidades sao privados. Os retangulos retornados sao copias. Consultas e operacoes com intencao explicita substituem acesso direto aos campos. O metodo de correcao da bola tem visibilidade de pacote para uso da fisica e montagem dos cenarios, sem reflexao nos testes de logica.

O timer Swing e criado uma unica vez, com intervalo de 16 ms (62,5 atualizacoes nominais por segundo, aproximadamente 60 FPS). Seus eventos e as teclas executam na mesma thread. O desenho apenas consulta o estado; nao move entidades nem marca pontos. `Partida.atualizar` aplica comandos, move entidades, corrige colisoes e verifica pontuacao/vitoria. O painel inicia/paralisa o timer ao entrar/sair da hierarquia Swing, conservando a instancia.

## Registro das fases e validacao

| Fase do roteiro | Resultado e verificacao |
| --- | --- |
| 01 - Janela e campo | Janela fixa, area util 800 x 600 e encerramento do processo verificados por integracao Swing |
| 02 - Raquetes | Duas instancias, encapsulamento, desenho e limites implementados |
| 03 - Teclado | Pressionar/soltar W, perda de foco e comandos opostos verificados; associacoes de setas e S compartilham a regra |
| 04 - Bola e ciclo | Bola centralizada; identidade, intervalo e unico listener do timer conferidos apos tres reinicios |
| 05 - Colisoes | Testes dos quatro contatos, correcao de sobreposicao e afastamento |
| 06 - Placar e saque | Saida completa, pontuacao unica dos dois lados, centralizacao e velocidades de saque testadas |
| 07 - Computador | Tolerancia e velocidades dos dois niveis testadas; selecao inicial e bloqueio durante jogo verificados na interface |
| 08 - Vitoria e reinicio | Dois vencedores, congelamento e restauracao testados; tela de vitoria renderizada e inspecionada visualmente |

Em 14/11/2026: compilacao com `-Xlint:all -Werror` sem avisos e 133 verificacoes aprovadas em modo headless. A verificacao adicional de interface abriu uma janela real na thread de eventos, acionou as acoes associadas ao teclado e confirmou que o evento de fechamento encerra o processo com codigo 0. Isso nao substitui uma sessao de jogo manual com teclado fisico.

### Desenvolvimento orientado a testes e bonus

Os testes foram escritos antes das classes de producao. A primeira compilacao falhou por ausencia de Bola, Raquete, Placar, ComputadorIA e Partida; a saida original esta preservada em `TDD-vermelho.log`. Depois foram implementadas as regras e corrigidos os avisos ate a fase verde. Trata-se de um ciclo inicial vermelho/verde; nao existe historico de commits demonstrando ciclos pequenos por funcionalidade.

A suite executavel atende a parte tecnica de testes. O ponto bonus depende tambem da avaliacao dos nove itens, publicacao no GitHub e divulgacao em redes sociais. Repositorio do projeto: [pong-fatec-osasco](https://github.com/BrunoIchikawa/pong-fatec-osasco). A divulgacao em redes sociais permanece pendente. Antes da entrega, preencher integrantes e acrescentar o link real da divulgacao. Nao ha garantia antecipada de nota.

## Limitacoes conhecidas

- Colisao retangular para a bola circular, conforme a simplificacao do roteiro.
- Velocidades constantes, sem aumento apos rebotes. A bola aceita modulo horizontal de no maximo 10 pixels por atualizacao; a configuracao normal usa 4, inferior a largura da raquete, evitando atravessamento completo em um passo.
- O modo dificil e mais rapido que o modulo horizontal inicial da bola, mas inferior ao teto de seguranca de 10; a IA nao preve rebotes e pode errar.
- A velocidade efetiva depende do agendamento do Swing e da carga da maquina. Nao ha compensacao por tempo decorrido, som ou pausa.
- O painel nao oferece serializacao de partidas. A supressao localizada do aviso serial evita prometer persistencia para entidades de jogo.
- A entrega requer JDK e ambiente grafico. Os testes de logica podem rodar sem desktop.

Os arquivos fornecidos originalmente e a pasta Exemplos foram preservados localmente e ficam fora da publicacao no GitHub. `fontes-sha256.json` registra os hashes SHA256 dos documentos de referencia locais e do registro inicial de teste para rastreabilidade. O estado anterior a revisao dos comentarios permanece no acervo local `.historico/antes-comentarios`, com manifesto SHA256. A revisao altera apenas os comentarios nos fontes Java; as instrucoes executaveis permanecem iguais.
