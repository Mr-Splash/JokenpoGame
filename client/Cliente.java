package client;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;



public class Cliente {
    
    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        String ipServidor = "127.0.0.1";
        int PORT=1222;
        Socket socket;
        PrintStream out;
        


        try {
          
           System.out.println("Informe o IP do Servidor (IP padrao: " + ipServidor + ")");
           String ipServer = entrada.nextLine();

           if(ipServer.length() > 0) {
            ipServidor = ipServer;
        }
           System.out.println("Informe a PORTA do Servidor (PORTA : "+ PORT + ")");
             String inputClient = entrada.nextLine();

             if(inputClient.length() > 0) {
                PORT  = Integer.parseInt(inputClient);
            }

            socket = new Socket(ipServidor, PORT);
            System.out.println("Conectando com o servidor...");
            
            Scanner in = new Scanner(socket.getInputStream());

            out = new PrintStream(socket.getOutputStream());

            System.out.println("---------------------------------");
            System.out.println("             JOKENPO             ");
            System.out.println("---------------------------------");

            while(true){

                String mensagem = in.nextLine();
                System.out.println(mensagem);
                if(mensagem.contains("Escolha o modo de jogo: [1] PvP ou [2] PvE ou [3] Sair")){
                    String escolherModo = entrada.nextLine();
                    out.println(escolherModo);
                    if(escolherModo == "3"){
                        break;
                    }
                } else if(mensagem.contains("Escolha: Pedra(1), Papel(2) ou Tesoura(3)")){
                    String escolha = entrada.nextLine();
                    out.println(escolha);
                }else if(mensagem.contains("Jogar Novamente? (Sim/Nao)")){
                    String novo = entrada.nextLine();
                    out.println(novo);
                }

                
            }

           socket.close();
           entrada.close();
           in.close();
        } catch (Exception e) {
            System.out.println("Saindo do jogo...");
        }
    }
}

 
