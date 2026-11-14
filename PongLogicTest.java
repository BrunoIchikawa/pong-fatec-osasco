/*
 * Cada cenario prepara uma situacao, executa uma acao e confere o resultado.
 * Estes testes usam a regra do jogo sem abrir uma janela.
 */
public class PongLogicTest {
    private static int total;

    public static void main(String[] args) {
        try {
            executar();
            System.out.println("[OK] Todos os " + total + " testes passaram.");
        } catch (AssertionError erro) {
            System.err.println("[FALHA] " + erro.getMessage());
            System.exit(1);
        }
    }

    // Uma condicao falsa provoca a falha com a descricao do que deveria acontecer.
    private static void conferir(boolean condicao, String descricao) {
        // A primeira divergencia encerra o teste com a descricao do comportamento esperado.
        if (!condicao) {
            throw new AssertionError(descricao);
        }
        total++;
    }

    private static Partida criar(Bola bola) {
        return new Partida(bola, new Raquete(30, 250, 20, 100, 6, 600),
                new Raquete(750, 250, 20, 100, 3, 600), new Placar());
    }

    private static void executar() {
        // Comeco pelas bordas verticais e confirmo que o afastamento nao provoca outro rebote.
        Bola bola = new Bola(390, 1, 20, 4, -4);
        Partida partida = criar(bola);
        partida.atualizar(false, false);
        conferir(bola.getDy() > 0 && bola.getY() == 0, "Teto corrige posicao e inverte dy");
        partida.atualizar(false, false);
        conferir(bola.getDy() > 0, "Teto nao repete o rebote no afastamento");

        bola = new Bola(390, 579, 20, 4, 4);
        partida = criar(bola);
        partida.atualizar(false, false);
        conferir(bola.getDy() < 0 && bola.getY() == 580, "Chao corrige posicao e inverte dy");

        // Agora aproximo a bola de cada raquete e confiro o lado de saida.
        bola = new Bola(51, 290, 20, -4, 1);
        partida = criar(bola);
        partida.atualizar(false, false);
        conferir(bola.getDx() > 0 && bola.getX() == 50, "Raquete esquerda rebate e separa");
        partida.atualizar(false, false);
        conferir(bola.getDx() > 0, "Raquete esquerda nao rebate no afastamento");
        bola = new Bola(729, 290, 20, 4, 1);
        partida = criar(bola);
        partida.atualizar(false, false);
        conferir(bola.getDx() < 0 && bola.getX() == 730, "Raquete direita rebate e separa");

        // Uma saida completa vale um ponto. A atualizacao seguinte mantem essa contagem.
        bola = new Bola(-16, 100, 20, -4, 1);
        partida = criar(bola);
        partida.atualizar(false, false);
        conferir(partida.getPlacar().getPontosComputador() == 1, "Saida esquerda marca exatamente um ponto");
        conferir(bola.getX() == 390 && bola.getY() == 290, "Saque centralizado apos ponto");
        partida.atualizar(false, false);
        conferir(partida.getPlacar().getPontosComputador() == 1, "Ponto esquerdo nao se repete");
        bola = new Bola(796, 100, 20, 4, 1);
        partida = criar(bola);
        partida.atualizar(false, false);
        conferir(partida.getPlacar().getPontosJogador() == 1, "Saida direita marca exatamente um ponto");
        partida.atualizar(false, false);
        conferir(partida.getPlacar().getPontosJogador() == 1, "Ponto direito nao se repete");
        bola = new Bola(-15, 100, 20, -4, 1);
        partida = criar(bola);
        partida.atualizar(false, false);
        conferir(partida.getPlacar().getPontosComputador() == 0, "Bola parcialmente no campo nao pontua");

        Placar placar = new Placar();
        placar.marcarPontoJogador();
        placar.marcarPontoComputador();
        placar.reiniciar();
        conferir(placar.getPontosJogador() == 0 && placar.getPontosComputador() == 0,
                "Reinicio do placar zera os dois contadores");
        // Com a mesma raquete, separo tolerancia de alinhamento e velocidade de cada nivel.
        Raquete raquete = new Raquete(750, 250, 20, 100, 3, 600);
        ComputadorIA ia = new ComputadorIA();
        ia.atualizar(new Bola(390, 300, 20, 4, 1), raquete);
        raquete.mover();
        conferir(raquete.getY() == 250, "IA para dentro da tolerancia");
        ia.atualizar(new Bola(390, 500, 20, 4, 1), raquete);
        raquete.mover();
        conferir(raquete.getY() == 253, "IA facil desloca tres pixels");
        ia.alternar();
        ia.atualizar(new Bola(390, 500, 20, 4, 1), raquete);
        raquete.mover();
        conferir(raquete.getY() == 259, "IA dificil desloca seis pixels");
        ia.atualizar(new Bola(390, 309, 20, 4, 1), raquete);
        raquete.mover();
        conferir(raquete.getY() == 265, "IA dificil reage a diferenca de dez pixels");
        raquete.moverParaCima();
        for (int i = 0; i < 200; i++) { raquete.mover(); }
        conferir(raquete.getY() == 0, "Raquete respeita limite superior");
        raquete.moverParaBaixo();
        for (int i = 0; i < 200; i++) { raquete.mover(); }
        conferir(raquete.getY() == 500, "Raquete respeita limite inferior");
        raquete.parar();
        raquete.mover();
        conferir(raquete.getY() == 500, "Soltar comando interrompe movimento");

        // Repito o encerramento para os dois lados. O vencedor muda, mas o bloqueio e o mesmo.
        for (boolean jogadorVence : new boolean[] {true, false}) {
            bola = new Bola(jogadorVence ? 796 : -16, 100, 20, jogadorVence ? 4 : -4, 1);
            partida = criar(bola);
            for (int i = 0; i < 8; i++) {
                if (jogadorVence) { partida.getPlacar().marcarPontoJogador(); }
                else { partida.getPlacar().marcarPontoComputador(); }
            }
            partida.atualizar(false, false);
            conferir(partida.getEstado() == Partida.Estado.VITORIA, "Nono ponto encerra a partida");
            conferir(partida.getVencedor().equals(jogadorVence ? "Jogador" : "Computador"), "Vencedor correto");
            double x = bola.getX();
            for (int i = 0; i < 100; i++) { partida.atualizar(true, false); }
            conferir(bola.getX() == x && partida.getJogador().getY() == 250,
                    "Vitoria congela bola e raquetes");
            conferir(partida.getPlacar().getPontosJogador() + partida.getPlacar().getPontosComputador() == 9,
                    "Vitoria mantem placar estavel");
            partida.reiniciar();
            partida.reiniciar();
            conferir(partida.getEstado() == Partida.Estado.JOGANDO
                    && partida.getPlacar().getPontosJogador() == 0
                    && partida.getPlacar().getPontosComputador() == 0
                    && bola.getX() == 390 && bola.getY() == 290
                    && partida.getComputador().getY() == 250, "Reinicios restauram o estado inicial");
            partida.atualizar(true, true);
            conferir(partida.getJogador().getY() == 250, "Comandos opostos simultaneos param jogador");
        }
        bola = new Bola(390, 290, 20, 4, 1);
        // O sorteio pode variar. Os limites de velocidade continuam iguais em todos os saques.
        // Cem das 133 verificacoes repetem o saque para conferir as velocidades sorteadas.
        for (int i = 0; i < 100; i++) {
            bola.reiniciar(400, 300);
            conferir(Math.abs(bola.getDx()) == 4 && Math.abs(bola.getDy()) <= 4,
                    "Saque respeita velocidade inicial");
        }
        java.awt.Rectangle copia = bola.getRectangle();
        copia.x = 0;
        conferir(bola.getX() == 390, "Rectangle retornado nao permite alterar entidade");
    }
}
