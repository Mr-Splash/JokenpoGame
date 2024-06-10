package server;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Menu extends Thread {
    private Socket jogador;
    private Scanner input;
    private PrintStream output;
    

    public Menu(Socket jogador) {
        this.jogador = jogador;
        try {
            input = new Scanner(jogador.getInputStream());
            output = new PrintStream(jogador.getOutputStream());
        } catch (Exception e) {
            System.out.println("Erro ao inicializar o jogador: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            output.println("Escolha o modo de jogo: [1] PvP ou [2] PvE ou [3] Sair");
            String modoDeJogo = input.nextLine().trim();

            while (!modoDeJogo.equals("1") && !modoDeJogo.equals("2") && !modoDeJogo.equals("3")) {
                output.println("Modo de jogo invalido. Escolha o modo de jogo: [1] PvP ou [2] PvE ou [3] Sair");
                modoDeJogo = input.nextLine().trim();
            }

            if (modoDeJogo.equalsIgnoreCase("1")) {
                System.out.println("Jogador conectado para o modo PvP");
                matchPvPPlayer();
            } else if (modoDeJogo.equalsIgnoreCase("2")) {
                System.out.println("Jogador escolheu o modo PvE");
                new Thread(new JokenpoGame2(jogador)).start();
                
            }else if(modoDeJogo.equalsIgnoreCase("3")){
                jogador.close();
            }

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage()); // Erro quando um jogador desconecta
        }
    }

    
    private synchronized void matchPvPPlayer() throws Exception {
        Socket jogador2 = JokenpoGame1.waitingPlayer;
        if (jogador2 == null) {
            JokenpoGame1.waitingPlayer = jogador;
            output.println("Aguardando outro jogador para o modo PvP...");
        } else {
            JokenpoGame1.waitingPlayer = null;
            new Thread(new JokenpoGame1(jogador2, jogador)).start();
        }
    }
}
