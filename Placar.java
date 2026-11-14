import java.awt.Font;
import java.awt.Graphics2D;

/*
 * OITAVO ARQUIVO: OS PONTOS
 * Os dois contadores sao privados. Quem marca um ponto chama o metodo correto.
 * Quem desenha ou verifica a vitoria consulta os valores pelos metodos get.
 * Isso e encapsulamento: a classe controla como seus dados sao alterados.
 * reiniciar zera os dois lados. desenhar mostra os numeros no alto do campo.
 */
public class Placar {
    private int pontosJogador;
    private int pontosComputador;

    // A contagem muda por estas operacoes. O restante do jogo apenas consulta os valores.
    public void marcarPontoJogador() { pontosJogador++; }
    public void marcarPontoComputador() { pontosComputador++; }
    public void reiniciar() { pontosJogador = 0; pontosComputador = 0; }
    public int getPontosJogador() { return pontosJogador; }
    public int getPontosComputador() { return pontosComputador; }

    public void desenhar(Graphics2D g, int larguraCampo) {
        // Mantenho cada numero do seu lado, com a largura do texto considerada no alinhamento.
        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 48));
        String esquerda = Integer.toString(pontosJogador);
        String direita = Integer.toString(pontosComputador);
        g.drawString(esquerda, larguraCampo / 2 - 70 - g.getFontMetrics().stringWidth(esquerda), 80);
        g.drawString(direita, larguraCampo / 2 + 70, 80);
    }
}
