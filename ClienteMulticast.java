import java.net.*;

// Classe que recebe mensagens enviadas para o grupo multicast
public class ClienteMulticast {
    public static void main(String[] args) throws Exception {
        // Define o mesmo endereço IP multicast do servidor
        InetAddress grupo = InetAddress.getByName("239.1.1.1");

        // Define a porta de comunicação (deve ser a mesma do servidor)
        int porta = 5000;

        // Cria um buffer de bytes para armazenar as mensagens recebidas
        byte[] buffer = new byte[1024];

        // Cria um socket multicast (diferente do DatagramSocket usado pelo servidor)
        MulticastSocket socket = new MulticastSocket(porta);

        // Solicita ao sistema operacional para ingressar no grupo multicast
        // O kernel passa a replicar pacotes enviados a 239.1.1.1:5000 para este socket
        socket.joinGroup(grupo);

        // Mensagem informando que o cliente está aguardando mensagens do grupo
        System.out.println("[Cliente] Aguardando mensagens...");

        // Cria um pacote vazio para armazenar os dados recebidos
        DatagramPacket pacote = new DatagramPacket(buffer, buffer.length);

        // Bloqueia a execução até que um pacote seja recebido
        socket.receive(pacote);/

        // Converte o conteúdo do pacote de bytes para String legível
        String msg = new String(pacote.getData(), 0, pacote.getLength());

        // Exibe a mensagem recebida e o conteúdo transmitido pelo servidor
        System.out.println("[Cliente] Recebido: " + msg);

        // Sai do grupo multicast (boa prática)
        socket.leaveGroup(grupo);

        // Fecha o socket e libera os recursos
        socket.close();
    }
}
