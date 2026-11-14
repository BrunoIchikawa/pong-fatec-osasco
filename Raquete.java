import java.awt.Graphics2D;
import java.awt.Rectangle;

/*
 * SEXTO ARQUIVO: AS RAQUETES
 * A mesma classe cria duas raquetes independentes: uma humana e uma do computador.
 * x fica fixo. y muda conforme a direcao escolhida e a velocidade da raquete.
 * Os comandos escolhem a direcao. mover aplica essa escolha uma vez por atualizacao.
 * Math.min pega o menor valor e Math.max pega o maior. Juntos, eles mantem y
 * entre zero e a altura do campo menos a altura da raquete.
 * Assim a raquete inteira permanece dentro da tela.
 */
public class Raquete {
    private final int x;
    private int y;
    private final int largura;
    private final int altura;
    private int velocidade;
    private int velocidadeAtual;
    private final int alturaDoJogo;

    public Raquete(int x, int y, int largura, int altura, int velocidade, int alturaDoJogo) {
        if (largura <= 0 || altura <= 0 || altura > alturaDoJogo || velocidade <= 0) {
            throw new IllegalArgumentException("Dimensao ou velocidade invalida");
        }
        this.x = x;
        this.largura = largura;
        this.altura = altura;
        this.velocidade = velocidade;
        this.alturaDoJogo = alturaDoJogo;
        // A posicao inicial ja entra no intervalo que comporta a raquete inteira.
        this.y = Math.max(0, Math.min(y, alturaDoJogo - altura));
    }

    // O comando escolhe a direcao; o timer aplica o deslocamento ao chamar mover.
    public void moverParaCima() { velocidadeAtual = -velocidade; }
    public void moverParaBaixo() { velocidadeAtual = velocidade; }
    public void parar() { velocidadeAtual = 0; }
    // O topo para em zero. O limite inferior desconta a altura da propria raquete.
    public void mover() { y = Math.max(0, Math.min(y + velocidadeAtual, alturaDoJogo - altura)); }
    public void reiniciar() { y = (alturaDoJogo - altura) / 2; parar(); }
    void definirVelocidade(int valor) {
        // A nova velocidade entra sem carregar a direcao da decisao anterior.
        if (valor <= 0) { throw new IllegalArgumentException("Velocidade deve ser positiva"); }
        velocidade = valor;
        parar();
    }
    public void desenhar(Graphics2D g) { g.fillRect(x, y, largura, altura); }
    public Rectangle getRectangle() { return new Rectangle(x, y, largura, altura); }
    public int getY() { return y; }
    public int getAltura() { return altura; }
}
