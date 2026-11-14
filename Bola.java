import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.concurrent.ThreadLocalRandom;

/*
 * QUINTO ARQUIVO: A BOLA
 * x e y guardam a posicao do canto da bola. O diametro define seu tamanho.
 * dx e quanto ela anda para os lados; dy e quanto ela anda para cima ou para baixo.
 * Na tela, y menor fica mais alto. Por isso dy negativo faz a bola subir.
 * mover soma essas velocidades a posicao. inverterX e inverterY trocam o sinal,
 * fazendo a bola voltar na direcao contraria depois de um contato.
 * private protege os dados: outras classes usam os metodos para trabalhar com a bola.
 * Os metodos get permitem consultar esses valores.
 */
public class Bola {
    private double x;
    private double y;
    private final int diametro;
    private double dx;
    private double dy;
    private final double velocidadeInicial;

    public Bola(double x, double y, int diametro, double dx, double dy) {
        // O deslocamento horizontal fica entre um e dez pixels para manter contatos controlaveis.
        if (diametro <= 0 || Math.abs(dx) < 1 || Math.abs(dx) > 10 || Math.abs(dy) > 10) {
            throw new IllegalArgumentException("Dimensao ou velocidade invalida");
        }
        this.x = x;
        this.y = y;
        this.diametro = diametro;
        this.dx = dx;
        this.dy = dy;
        velocidadeInicial = Math.abs(dx);
    }

    public void mover() { x += dx; y += dy; }
    public void inverterX() { dx = -dx; }
    public void inverterY() { dy = -dy; }

    public void reiniciar(int centroX, int centroY) {
        // As coordenadas representam o canto. Desconto meio diametro para centralizar a bola.
        x = centroX - diametro / 2.0;
        y = centroY - diametro / 2.0;
        // Cada saque recupera a velocidade inicial e sorteia o lado e a inclinacao.
        dx = ThreadLocalRandom.current().nextBoolean() ? velocidadeInicial : -velocidadeInicial;
        dy = ThreadLocalRandom.current().nextDouble(-velocidadeInicial, velocidadeInicial);
    }

    public void desenhar(Graphics2D g) {
        g.fillOval((int) Math.round(x), (int) Math.round(y), diametro, diametro);
    }

    public Rectangle getRectangle() {
        // Entrego uma area nova. Quem consulta a colisao nao recebe acesso a posicao da bola.
        return new Rectangle((int) Math.round(x), (int) Math.round(y), diametro, diametro);
    }

    // Aqui corrijo a posicao do contato. A inversao da velocidade fica na regra da partida.
    void posicionar(double novoX, double novoY) { x = novoX; y = novoY; }
    public double getX() { return x; }
    public double getY() { return y; }
    public int getDiametro() { return diametro; }
    public double getDx() { return dx; }
    public double getDy() { return dy; }
}
