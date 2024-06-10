package server;

import java.io.PrintStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class JokenpoGame1 extends Thread {

    private Socket jogador1;
    private Socket jogador2;
    private Scanner jogador1in;
    private Scanner jogador2in;
    private PrintStream jogador1out;
    private PrintStream jogador2out;
    private int jogador1Vitorias;
    private int jogador2Vitorias;
    private int empates;
    public static Socket waitingPlayer = null;

    public JokenpoGame1(Socket jogador1, Socket jogador2) {
        this.jogador1 = jogador1;
        this.jogador2 = jogador2;

        try {
            jogador1in = new Scanner(jogador1.getInputStream());
            jogador2in = new Scanner(jogador2.getInputStream());
            jogador1out = new PrintStream(jogador1.getOutputStream());
            jogador2out = new PrintStream(jogador2.getOutputStream());
        } catch (Exception e) {
            System.out.println("Erro ao inicializar o jogo: " + e.getMessage());
        }
    }

    @Override
    public void run() {

        int contador = 0;
        int rodadas = 3;

        try {
            jogador1out.println("Você é o jogador 1");
            jogador2out.println("Você é o jogador 2");
            
            while (true) {
                for (int i = 0; i < rodadas; i++) {
                    contador++;
                    jogador1out.println("Rodada " + contador + ":");
                    jogador2out.println("Aguardando o jogador 1 jogar");

                    String j1escolha = escolhaValida(jogador1out, jogador1in);
                    jogador2out.println("Rodada " + contador + ":");
                    String j2escolha = escolhaValida(jogador2out, jogador2in);

                    int result = Vencedor(j1escolha, j2escolha);

                    if (result == 1) {
                        jogador1out.println("Você venceu! o jogador 2 escolheu " + j2escolha);
                        jogador2out.println("Você perdeu! o jogador 1 escolheu " + j1escolha);
                        jogador1Vitorias++;
                    } else if (result == -1) {
                        jogador2out.println("Você venceu! o jogador 1 escolheu " + j1escolha);
                        jogador1out.println("Você perdeu! o jogador 2 escolheu " + j2escolha);
                        jogador2Vitorias++;
                    } else {
                        jogador1out.println("Empate! Ambos escolheram " + j1escolha);
                        jogador2out.println("Empate! Ambos escolheram " + j2escolha);
                        empates++;
                    }

                    jogador1out.println("Placar: Vitórias - " + jogador1Vitorias + " | Derrotas - " + jogador2Vitorias + " | Empates - " + empates);
                    jogador2out.println("Placar: Vitórias - " + jogador2Vitorias + " | Derrotas - " + jogador1Vitorias + " | Empates - " + empates);

                    if (contador == rodadas) {
                        if (jogador1Vitorias > jogador2Vitorias) {
                            System.out.println("Resultado da partida -> Vencedor: jogador 1" );
                            jogador1out.println("Resultado da partida -> Vencedor: jogador 1");
                            jogador2out.println("Resultado da partida -> Vencedor: jogador 1");
                        } else if (jogador2Vitorias > jogador1Vitorias) {
                            System.out.println("Resultado da partida -> Vencedor: jogador 2");
                            jogador1out.println("Resultado da partida -> Vencedor: jogador 2");
                            jogador2out.println("Resultado da partida -> Vencedor: jogador 2");
                        } else {
                            System.out.println("Resultado da partida -> Empatou");
                            jogador1out.println("Resultado da partida -> Empatou");
                            jogador2out.println("Resultado da partida -> Empatou");
                        }
                        
                        System.out.println("Fim da Partida");
                        jogador1out.println("Fim da Partida");
                        jogador2out.println("Fim da Partida");

                        enviarMensagensComIntervalo(jogador1out, jogador2out);

                        new Menu(jogador1).start();
                        new Menu(jogador2).start();
                        
                        return;
                        
                    }
                }
                break;
            }
            
        } catch (NoSuchElementException e) {
            System.out.println("Um jogador desconectou.");
            if (!jogador1.isClosed()) {
                jogador1out.println("O outro jogador desconectou. Você será redirecionado para o menu.");
                new Thread(new Menu(jogador1)).start();
                
            }
            if (!jogador2.isClosed()) {
                jogador2out.println("O outro jogador desconectou. Você será redirecionado para o menu.");
                new Thread(new Menu(jogador2)).start();
            }
            
        }
        
    }

    private void enviarMensagensComIntervalo(PrintStream jogador1out, PrintStream jogador2out) {
        try {
            String[] mensagens = {
                "Retornando para o menu...",
               
            };

            for (String mensagem : mensagens) {
                jogador1out.println(mensagem);
                jogador2out.println(mensagem);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Erro ao enviar mensagens com intervalo: " + e.getMessage());
        }
    }

    private String escolhaValida(PrintStream out, Scanner in) {
        out.println("Escolha: Pedra(1), Papel(2) ou Tesoura(3)");
        String escolha = in.nextLine();
        while (!escolha.equalsIgnoreCase("1") &&
                !escolha.equalsIgnoreCase("2") &&
                !escolha.equalsIgnoreCase("3")) {
            out.println("Escolha inválida! Escolha: Pedra(1), Papel(2) ou Tesoura(3)");
            escolha = in.nextLine();
        }
        return converterEscolha(escolha);
    }

    private String converterEscolha(String escolha) {
        switch (escolha) {
            case "1":
                return "Pedra";
            case "2":
                return "Papel";
            case "3":
                return "Tesoura";
            default:
                return "Pedra"; // Valor padrão em caso de erro
        }
    }

    private int Vencedor(String j1escolha, String j2escolha) {
        if (j1escolha.equals(j2escolha)) {
            return 0;
        } else if ((j1escolha.equals("Pedra") && j2escolha.equals("Tesoura")) ||
                (j1escolha.equals("Papel") && j2escolha.equals("Pedra")) ||
                (j1escolha.equals("Tesoura") && j2escolha.equals("Papel"))) {
            return 1;
        } else {
            return -1;
        }
    }
}
