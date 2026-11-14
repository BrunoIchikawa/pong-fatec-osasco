/*
 * SETIMO ARQUIVO: O COMPUTADOR
 * A inteligencia artificial aqui e uma regra simples, sem aprendizado.
 * Comparo o meio da bola com o meio da raquete e escolho subir, descer ou parar.
 * A tolerancia e uma pequena distancia em que a raquete ja pode ficar parada.
 * Isso evita que ela fique tremendo para tentar alinhar o centro exatamente.
 * No facil, anda 3 pixels por atualizacao e aceita distancia menor que 25.
 * No dificil, anda 6 pixels e aceita distancia menor que 8.
 * O sinal de interrogacao escolhe entre dois valores: dificil ? 6 : 3 significa
 * usar 6 quando dificil for verdadeiro e usar 3 no outro caso.
 */
public class ComputadorIA {
    private boolean dificil;

    public void atualizar(Bola bola, Raquete raquete) {
        // Primeiro aplico o limite de velocidade do nivel escolhido.
        raquete.definirVelocidade(dificil ? 6 : 3);
        // Comparo os centros: diferenca negativa pede subida, positiva pede descida.
        double diferenca = bola.getY() + bola.getDiametro() / 2.0
                - (raquete.getY() + raquete.getAltura() / 2.0);
        // Dentro da tolerancia, a raquete ja esta alinhada e permanece parada.
        if (Math.abs(diferenca) < getTolerancia()) { raquete.parar(); }
        else if (diferenca < 0) { raquete.moverParaCima(); }
        else { raquete.moverParaBaixo(); }
    }

    public void alternar() { dificil = !dificil; }
    public int getTolerancia() { return dificil ? 8 : 25; }
    public String getNivel() { return dificil ? "Dificil" : "Facil"; }
}
