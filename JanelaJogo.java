import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/*
 * SEGUNDO ARQUIVO: A JANELA
 * JFrame e uma janela pronta do Java. extends significa que JanelaJogo herda
 * essa base e acrescenta as escolhas do nosso jogo: titulo, campo e fechamento.
 * O campo fica dentro da janela. pack ajusta a janela ao tamanho desse campo.
 * EXIT_ON_CLOSE encerra o programa ao fechar. setResizable(false) fixa o tamanho.
 * Ao trocar de janela, limpamos as teclas para a raquete nao continuar andando.
 * serialVersionUID e um identificador da classe para o recurso do Java de salvar
 * objetos. Nosso jogo nao usa esse recurso para guardar partidas.
 */
public final class JanelaJogo extends JFrame {
    private static final long serialVersionUID = 1L;

    public JanelaJogo() {
        super("Pong | FATEC Osasco | Tecnicas de Programacao");
        CampoJogo campo = new CampoJogo();
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
