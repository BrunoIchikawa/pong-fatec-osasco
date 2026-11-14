import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/*
 * Esta classe monta a janela e coloca o CampoJogo dentro dela.
 * extends JFrame aproveita a base de janela que o Java ja oferece.
 */
public final class JanelaJogo extends JFrame {
    // Identificador usado pelo recurso Java de salvar objetos; nao salva a partida aqui.
    private static final long serialVersionUID = 1L;

    public JanelaJogo() {
        super("Pong | FATEC Osasco | Tecnicas de Programacao");
        CampoJogo campo = new CampoJogo();
        // Fechar a janela tambem encerra o programa.
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        add(campo);
        // O tamanho vem do campo. As bordas da janela ficam fora dos 800 por 600.
        pack();
        setLocationRelativeTo(null);
        // Ao sair da janela, encerro os comandos que poderiam ficar pressionados.
        addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowLostFocus(WindowEvent evento) { campo.limparComandos(); }
        });
        setVisible(true);
    }
}
