import javax.swing.SwingUtilities;

/*
 * O main apenas abre a janela. As regras ficam na Partida.
 */
public class Main {
    public static void main(String[] args) {
        // A janela nasce na mesma thread que recebe o teclado e atualiza o jogo.
        // invokeLater coloca a abertura na fila de tarefas do Swing, que cuida da tela.
        SwingUtilities.invokeLater(() -> new JanelaJogo());
    }
}
