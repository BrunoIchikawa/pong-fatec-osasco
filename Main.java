import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // A janela nasce na mesma thread que recebe o teclado e atualiza o jogo.
        SwingUtilities.invokeLater(() -> new JanelaJogo());
    }
}
