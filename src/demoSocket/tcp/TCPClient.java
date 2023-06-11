package demoSocket.tcp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class TCPClient {
	public static void main(String[] args) throws Exception {
		Socket socket = new Socket("127.0.0.1", 9000);
		
		OutputStream os = socket.getOutputStream();
		DataOutputStream writer = new DataOutputStream(os);
		
		InputStreamReader is = new InputStreamReader(socket.getInputStream());
		BufferedReader reader = new BufferedReader(is);
		
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		
		String texto = inFromUser.readLine(); // Blocking
		
		writer.writeBytes(texto + "\n"); // o que acontece se não enviar o '\n'?
		
		String response = reader.readLine(); // Blocking
		System.out.println("Do Servidor: " + response);
		
		socket.close(); // é uma boa prática fechar o canal
		
	}
}
