import java.awt.Rectangle;

/*
 * Aqui ficam os movimentos, os contatos, os pontos e a vitoria.
 * Esta classe usa os objetos do campo e pode funcionar sem abrir uma janela.
 */
public class Partida {
    public static final int LARGURA = 800;
    public static final int ALTURA = 600;
    public static final int META = 9;
    // enum lista os dois estados possiveis da partida.
    public enum Estado { JOGANDO, VITORIA }

    private final Bola bola;
    private final Raquete jogador;
    private final Raquete computador;
    private final Placar placar;
    private final ComputadorIA ia = new ComputadorIA();
    private Estado estado = Estado.JOGANDO;
    private boolean iniciada;
    private String vencedor = "";

    // Os parametros trazem os objetos do campo. this indica o dado deste objeto.
    public Partida(Bola bola, Raquete jogador, Raquete computador, Placar placar) {
        this.bola = bola;
        this.jogador = jogador;
        this.computador = computador;
        this.placar = placar;
    }

    public void atualizar(boolean cima, boolean baixo) {
        // A partida terminou. Nenhuma entidade avanca depois deste ponto.
        if (estado == Estado.VITORIA) { return; }
        iniciada = true;
        // Dois comandos iguais significam repouso: ambos soltos ou ambos pressionados.
        if (cima == baixo) { jogador.parar(); }
        else if (cima) { jogador.moverParaCima(); }
        else { jogador.moverParaBaixo(); }
        ia.atualizar(bola, computador);
        jogador.mover();
        computador.mover();
        bola.mover();
        // Primeiro retiro a bola do limite; o sinal da velocidade confirma a aproximacao.
        // y zero e o teto; dy negativo significa que a bola esta subindo.
        if (bola.getY() <= 0 && bola.getDy() < 0) {
            bola.posicionar(bola.getX(), 0);
            bola.inverterY();
        // No chao, considero a parte de baixo da bola, e nao apenas seu canto.
        } else if (bola.getY() + bola.getDiametro() >= ALTURA && bola.getDy() > 0) {
            bola.posicionar(bola.getX(), ALTURA - bola.getDiametro());
            bola.inverterY();
        }
        rebater(jogador, true);
        rebater(computador, false);
        // O ponto entra apenas quando o diametro inteiro deixou o campo.
        if (bola.getX() + bola.getDiametro() <= 0) {
            placar.marcarPontoComputador();
            concluirPonto();
        } else if (bola.getX() >= LARGURA) {
            placar.marcarPontoJogador();
            concluirPonto();
        }
    }

    private void rebater(Raquete raquete, boolean esquerda) {
        // getRectangle cria a area retangular de cada objeto.
        // intersects verifica se essas areas se sobrepoem, ou seja, se houve contato.
        // dx negativo significa esquerda; positivo significa direita.
        // O contato so rebate se a bola estiver chegando nesta raquete.
        if ((esquerda ? bola.getDx() < 0 : bola.getDx() > 0)
                && bola.getRectangle().intersects(raquete.getRectangle())) {
            Rectangle area = raquete.getRectangle();
            // Deixo a bola na face de saida antes da inversao. O proximo passo ja afasta os objetos.
            bola.posicionar(esquerda ? area.x + area.width : area.x - bola.getDiametro(), bola.getY());
            bola.inverterX();
        }
    }

    private void concluirPonto() {
        // Sair pela esquerda da ponto ao computador; pela direita, ao jogador.
        // Este metodo recebe o placar ja atualizado e prepara o proximo saque.
        // O saque central encerra esta saida lateral e impede contar o mesmo ponto outra vez.
        bola.reiniciar(LARGURA / 2, ALTURA / 2);
        // META vale 9. Basta um dos lados atingir esse valor para terminar.
        if (placar.getPontosJogador() >= META || placar.getPontosComputador() >= META) {
            // Ao atingir nove pontos, guardo o vencedor e interrompo a logica pelo estado.
            // A proxima chamada de atualizar retorna logo no inicio, mantendo tudo parado.
            estado = Estado.VITORIA;
            vencedor = placar.getPontosJogador() >= META ? "Jogador" : "Computador";
            jogador.parar();
            computador.parar();
        }
    }

    public void reiniciar() {
        // ESPACO chega ate aqui pelo CampoJogo. Nenhum timer novo e criado neste caminho.
        // Reutilizo as entidades e restauro o inicio. A dificuldade escolhida permanece.
        placar.reiniciar();
        bola.reiniciar(LARGURA / 2, ALTURA / 2);
        jogador.reiniciar();
        computador.reiniciar();
        // Aplico a velocidade do nivel e deixo a raquete parada para o primeiro ciclo.
        ia.atualizar(bola, computador);
        computador.parar();
        vencedor = "";
        estado = Estado.JOGANDO;
        iniciada = false;
    }

    public void alternarDificuldade() {
        // O nivel muda antes do primeiro movimento ou depois que a partida terminou.
        if (!iniciada || estado == Estado.VITORIA) { ia.alternar(); }
    }
    public Estado getEstado() { return estado; }
    public String getVencedor() { return vencedor; }
    public String getNivel() { return ia.getNivel(); }
    public Raquete getJogador() { return jogador; }
    public Raquete getComputador() { return computador; }
    public Placar getPlacar() { return placar; }
}
