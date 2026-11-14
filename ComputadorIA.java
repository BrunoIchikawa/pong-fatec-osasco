/*
 * O computador compara o meio da bola com o meio da raquete para escolher a direcao.
 */
public class ComputadorIA {
    private boolean dificil;

    public void atualizar(Bola bola, Raquete raquete) {
        // Primeiro aplico o limite de velocidade do nivel escolhido.
        // dificil ? 6 : 3 significa: usar 6 no dificil e 3 no facil.
        raquete.definirVelocidade(dificil ? 6 : 3);
        // Comparo os centros: diferenca negativa pede subida, positiva pede descida.
        double diferenca = bola.getY() + bola.getDiametro() / 2.0
                - (raquete.getY() + raquete.getAltura() / 2.0);
        // Dentro da tolerancia, a raquete ja esta alinhada e permanece parada.
        // Math.abs mede a distancia sem considerar se a bola esta acima ou abaixo.
        if (Math.abs(diferenca) < getTolerancia()) { raquete.parar(); }
        else if (diferenca < 0) { raquete.moverParaCima(); }
        else { raquete.moverParaBaixo(); }
    }

    public void alternar() { dificil = !dificil; }
    // Facil aceita distancia menor que 25 pixels; dificil, menor que 8.
    public int getTolerancia() { return dificil ? 8 : 25; }
    public String getNivel() { return dificil ? "Dificil" : "Facil"; }
}
