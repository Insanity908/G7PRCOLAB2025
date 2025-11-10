// Importa as classes necessárias para comunicação de rede (InetAddress, DatagramPacket, DatagramSocket)
import java.net.*;

// Classe principal que enviará mensagens para o grupo multicast
public class ServidorMulticast {
    public static void main(String[] args) throws Exception {
        // Define o endereço IP do grupo multicast (faixa 224.0.0.0 a 239.255.255.255)
        InetAddress grupo = InetAddress.getByName("239.1.1.1");

        // Define a porta que será usada para envio e recepção dos pacotes multicast
        int porta = 5000;

        // Cria um socket UDP simples (DatagramSocket) para enviar pacotes
        DatagramSocket socket = new DatagramSocket();

        // Mensagem a ser enviada; inclui o timestamp para mostrar quando foi criada
        String msg = "Olá grupo multicast! " + System.currentTimeMillis();

        // Cria um pacote UDP com a mensagem, o endereço do grupo multicast e a porta
        DatagramPacket pacote = new DatagramPacket(msg.getBytes(), msg.length(), grupo, porta);

        // Envia o pacote UDP para o grupo multicast
        socket.send(pacote);

        // Fecha o socket após o envio (boa prática para liberar recursos)
        socket.close();

        // Mostra no terminal do servidor que a mensagem foi enviada
        System.out.println("[Servidor] Mensagem enviada: " + msg);
    }
}
