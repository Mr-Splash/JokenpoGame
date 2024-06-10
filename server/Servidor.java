package server;


import java.net.ServerSocket;
import java.net.Socket;


public class Servidor {

    public static void main(String[] args) {
        final int PORT = 1222;
        ServerSocket serverSocket;

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Aguardando jogadores...");

            while (true) {
                Socket jogador = serverSocket.accept();
                new Thread(new Menu(jogador)).start();
                
            }

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            System.out.println("O jogador desconectou");
            
        }
    }
}
