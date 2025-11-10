import java.net.*;
import java.io.*;
import java.util.Scanner;

// Classe principal do chat multicast
public class ChatMulticast {
    public static void main(String[] args) {
        try {
            // Define a porta e o endereço IP multicast (faixa 224.0.0.0 a 239.255.255.255)
            int porta = 5000;
            InetAddress grupo = InetAddress.getByName("239.1.1.2");

            // Cria o socket multicast
            // O MulticastSocket permite enviar e receber pacotes para um grupo multicast
            MulticastSocket socket = new MulticastSocket(porta);

            // Junta-se ao grupo multicast especificado (gera mensagem IGMP Membership Report)
            socket.joinGroup(grupo);

            System.out.println("=== Chat Multicast ===");
            System.out.println("Digite suas mensagens e pressione Enter para enviar.");
            System.out.println("Saia com /sair\n");

            // Thread para RECEBER mensagens do grupo
            // Essa thread roda em paralelo à principal e escuta continuamente as mensagens multicast
            Thread receptor = new Thread(() -> {
                byte[] buffer = new byte[256]; // Buffer para armazenar dados recebidos
                while (true) {
                    try {
                        // Cria o pacote UDP vazio para receber os dados
                        DatagramPacket pacote = new DatagramPacket(buffer, buffer.length);

                        // Espera até que um pacote chegue (bloqueia até receber algo)
                        socket.receive(pacote);

                        // Converte os bytes recebidos em String legível
                        String msg = new String(pacote.getData(), 0, pacote.getLength());

                        // Exibe a mensagem recebida no console
                        System.out.println("\n[Recebido] " + msg);
                        System.out.print("> "); // mantém o prompt visível para o usuário
                    } catch (IOException e) {
                        // Sai do loop se o socket for fechado ou ocorrer erro
                        break;
                    }
                }
            });
            receptor.start(); // Inicia a thread de recepção

            // Thread principal: ENVIA mensagens digitadas
            Scanner teclado = new Scanner(System.in);
            while (true) {
                System.out.print("> "); // prompt do chat
                String texto = teclado.nextLine();

                // Se o usuário digitar "/sair", encerra o chat
                if (texto.equalsIgnoreCase("/sair")) {
                    break;
                }

                // Cria o pacote UDP e envia para o grupo multicast
                DatagramPacket pacote = new DatagramPacket(
                    texto.getBytes(),  // mensagem convertida em bytes
                    texto.length(),    // tamanho da mensagem
                    grupo,             // endereço multicast
                    porta              // porta de destino
                );

                // Envia o pacote UDP para o grupo multicast
                socket.send(pacote);
            }

            // Sai do grupo multicast (envia IGMP Leave Group)
            socket.leaveGroup(grupo);

            // Fecha o socket e libera recursos
            socket.close();
            System.out.println("Você saiu do chat.");

        } catch (Exception e) {
            // Captura e exibe qualquer exceção (como erros de rede)
            e.printStackTrace();
        }}
}
