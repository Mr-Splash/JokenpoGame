package server;

import java.io.PrintStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

public class JokenpoGame2 extends Thread {

    private Socket jogador1;
    private Scanner jogador1in;
    private PrintStream jogador1out;
    private int jogador1Vitorias;
    private int servidorVitorias;
    private int draws;
    private Random random = new Random();
    

    public JokenpoGame2(Socket jogador1) {
        this.jogador1 = jogador1;

        try {
            jogador1in = new Scanner(jogador1.getInputStream());
            jogador1out = new PrintStream(jogador1.getOutputStream());
        } catch (Exception e) {
            System.out.println("Erro ao inicializar o jogador: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            
                int contador = 0;
                int rodadas = 2;
                jogador1Vitorias = 0;
                servidorVitorias = 0;
                draws = 0;

                while (contador < rodadas) {
                    contador++;
                    jogador1out.println("Rodada " + contador + ":");
                    String j1escolha = escolhaValida(jogador1out, jogador1in);
                    String servidorEscolha = escolhaServidor();

                    int result = Vencedor(j1escolha, servidorEscolha);

                    if (result == 1) {
                        jogador1out.println("Voce venceu! Servidor escolheu " + servidorEscolha);
                        jogador1Vitorias++;
                    } else if (result == -1) {
                        jogador1out.println("Voce perdeu! Servidor escolheu " + servidorEscolha);
                        servidorVitorias++;
                    } else {
                        jogador1out.println("Empate! Ambos escolheram " + servidorEscolha);
                        draws++;
                    }

                    jogador1out.println("Placar: Vitorias - " + jogador1Vitorias + ", Derrotas - " + servidorVitorias + ", Empates - " + draws);
                }

                    if (jogador1Vitorias > servidorVitorias) {
                        jogador1out.println("Vencedor: Jogador");
                    } else if (servidorVitorias > jogador1Vitorias) {
                        jogador1out.println("Vencedor: Servidor");
                    } else {
                        jogador1out.println("Empatou");
                    }
                        jogador1out.println("Fim da partida");

                        enviarMensagensComIntervalo(jogador1out);

                        
                        new Menu(jogador1).start();
                
            
        } catch (NoSuchElementException e) {
            System.out.println("Um jogador desconectou.");
        
        } 
    }

    private void enviarMensagensComIntervalo(PrintStream jogador1out) {
        try {
            String[] mensagens = {
                "Retornando para o menu...",
            };

            for (String mensagem : mensagens) {
                jogador1out.println(mensagem);
                
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Erro ao enviar mensagens com intervalo: " + e.getMessage());
        }
    }

    private String escolhaServidor() {
        int escolha = random.nextInt(3);
        switch (escolha) {
            case 0:
                return "Pedra";
            case 1:
                return "Papel";
            case 2:
                return "Tesoura";
            default:
                return "Pedra";
        }
    }

    private String escolhaValida(PrintStream out, Scanner in) {
        out.println("Escolha: Pedra(1), Papel(2) ou Tesoura(3)");
        String escolha = in.nextLine();
        while (!escolha.equalsIgnoreCase("1") &&
                !escolha.equalsIgnoreCase("2") &&
                !escolha.equalsIgnoreCase("3")) {
            out.println("Escolha invalida! Escolha: Pedra(1), Papel(2) ou Tesoura(3)");
            escolha = in.nextLine();
        }
        return escolha;
    }


    private int Vencedor(String j1escolha, String j2escolha) {
        if (j1escolha.equals("1") && j2escolha.equals("Pedra")||
            j1escolha.equals("2") && j2escolha.equals("Papel")||
            j1escolha.equals("3") && j2escolha.equals("Tesoura")) {
            return 0;
        } else if ((j1escolha.equals("1") && j2escolha.equals("Tesoura")) ||
                (j1escolha.equals("2") && j2escolha.equals("Pedra")) ||
                (j1escolha.equals("3") && j2escolha.equals("Papel"))) {
            return 1;
        } else {
            return -1;
        }
    }

}


