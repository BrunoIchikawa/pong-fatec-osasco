import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

/*
 * TERCEIRO ARQUIVO: O CAMPO E OS COMANDOS
 * JPanel e uma area de desenho do Java. Nosso campo herda essa base.
 * Aqui criamos uma bola, duas raquetes, um placar e a regra da partida.
 * Ter esses objetos dentro do campo e o exemplo de composicao do projeto.
 * ActionListener permite receber os avisos do timer pelo metodo actionPerformed.
 * O timer chama esse metodo a cada 16 milissegundos, aproximadamente 60 vezes
 * por segundo. Ele atualiza o jogo e pede para desenhar a tela de novo.
 * As teclas pressionadas ficam em um conjunto, sem repetir a mesma tecla.
 * A tela inicial espera ESPACO. D escolhe a dificuldade antes do inicio.
 * O aviso serial e ignorado porque este painel nao salva partidas em arquivo.
 */
@SuppressWarnings("serial")
public final class CampoJogo extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    private final Bola bola = new Bola(390, 290, 20, 4, 2);
    private final Raquete jogador = new Raquete(30, 250, 20, 100, 6, Partida.ALTURA);
    private final Raquete computador = new Raquete(750, 250, 20, 100, 3, Partida.ALTURA);
    private final Placar placar = new Placar();
    // A regra recebe os mesmos objetos que desenho. Existe uma unica versao da partida.
    private final Partida partida = new Partida(bola, jogador, computador, placar);
    // Este timer acompanha o painel inteiro, inclusive depois de cada reinicio.
    private final Timer timer = new Timer(16, this);
    private final Set<String> pressionadas = new HashSet<>();
    private boolean aguardandoInicio = true;

    public CampoJogo() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(new Color(16, 22, 33));
        // Cada tecla tem entrada e saida. O movimento depende do estado entre esses eventos.
        for (String tecla : new String[] {"W", "S", "UP", "DOWN", "SPACE", "D"}) {
            associar(tecla, true);
            associar(tecla, false);
        }
        partida.reiniciar();
    }

    @Override
    public void addNotify() {
        // O campo entrou na janela. O ciclo pode comecar com a interface pronta.
        super.addNotify();
        timer.start();
    }

    @Override
    public void removeNotify() {
        // Fora da janela, o painel deixa de receber atualizacoes do timer.
        timer.stop();
        super.removeNotify();
    }

    private void associar(String tecla, boolean pressionar) {
        // InputMap liga a tecla a um nome. ActionMap liga esse nome ao que deve acontecer.
        // Registrar pressionar e soltar permite andar continuamente e parar na hora certa.
        String nome = (pressionar ? "pressed " : "released ") + tecla;
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(nome), nome);
        getActionMap().put(nome, new AbstractAction() {
            private static final long serialVersionUID = 1L;
            @Override
            public void actionPerformed(ActionEvent evento) {
                // A liberacao encerra apenas esta tecla; as outras continuam registradas.
                if (!pressionar) { pressionadas.remove(tecla); return; }
                // Uma tecla mantida pressionada ja esta no conjunto. A repeticao termina aqui.
                if (!pressionadas.add(tecla)) { return; }
                if (tecla.equals("SPACE")) {
                    // Limpo os movimentos, mas mantenho ESPACO registrado ate a liberacao.
                    limparComandos();
                    pressionadas.add("SPACE");
                    partida.reiniciar();
                    aguardandoInicio = false;
                } else if (tecla.equals("D")) {
                    partida.alternarDificuldade();
                }
                repaint();
            }
        });
    }

    public void limparComandos() {
        // Sem foco ou ao reiniciar, a proxima atualizacao parte de comandos vazios.
        pressionadas.clear();
        jogador.parar();
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        // Na abertura, o timer desenha a tela enquanto o jogador escolhe o nivel.
        if (!aguardandoInicio) {
            partida.atualizar(pressionadas.contains("W") || pressionadas.contains("UP"),
                    pressionadas.contains("S") || pressionadas.contains("DOWN"));
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        // Este metodo apenas desenha. Mover a bola aqui faria o jogo depender do desenho.
        // super limpa o desenho anterior; Graphics2D oferece cores, linhas e texto.
        super.paintComponent(graphics);
        // Trabalho em uma copia para manter as configuracoes de desenho dentro deste metodo.
        Graphics2D g = (Graphics2D) graphics.create();
        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(new Color(63, 77, 95));
            g.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                    10, new float[] {10, 12}, 0));
            g.drawLine(400, 100, 400, 550);
            g.setColor(new Color(95, 210, 235));
            jogador.desenhar(g);
            g.setColor(new Color(245, 183, 104));
            computador.desenhar(g);
            g.setColor(new Color(235, 241, 248));
            bola.desenhar(g);
            placar.desenhar(g, Partida.LARGURA);
            g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
            centralizar(g, "JOGADOR                         COMPUTADOR", 25);
            centralizar(g, "IA: " + partida.getNivel() + " | Primeiro a 9 pontos", 565);
            centralizar(g, "W/S ou setas: mover | ESPACO: nova partida | D: nivel no inicio/vitoria", 589);
            // A mensagem vem do estado atual. Desenhar de novo conserva a mesma vitoria.
            if (aguardandoInicio || partida.getEstado() == Partida.Estado.VITORIA) {
                g.setColor(new Color(16, 22, 33, 240));
                g.fillRect(80, 215, 640, 170);
                g.setColor(Color.WHITE);
                g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));
                centralizar(g, aguardandoInicio ? "PONG" : partida.getVencedor() + " venceu", 270);
                g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 17));
                centralizar(g, "D: escolher nivel | IA " + partida.getNivel(), 315);
                centralizar(g, "ESPACO: " + (aguardandoInicio ? "iniciar" : "jogar novamente"), 350);
            }
        } finally {
            g.dispose();
        }
    }

    private void centralizar(Graphics2D g, String texto, int y) {
        // Desconto a largura do texto da largura do campo e divido a sobra entre os lados.
        g.drawString(texto, (Partida.LARGURA - g.getFontMetrics().stringWidth(texto)) / 2, y);
    }
}
