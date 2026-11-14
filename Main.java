import javax.swing.SwingUtilities;

/*
 * COMECO DA APRESENTACAO
 * O programa comeca pelo main. Ele pede ao Swing para abrir a JanelaJogo.
 * Swing e a parte do Java que usamos para montar a janela e receber as teclas.
 * invokeLater coloca essa abertura na fila de tarefas da tela.
 * As regras do jogo ficam em outras classes, por isso o main e pequeno.
 */
public class Main {
    public static void main(String[] args) {
        // A janela nasce na mesma thread que recebe o teclado e atualiza o jogo.
        SwingUtilities.invokeLater(() -> new JanelaJogo());
    }
}
