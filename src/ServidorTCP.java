import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorTCP extends Thread{
	
	private Socket socket;
	
	public ServidorTCP(Socket socket) {
        this.socket = socket;
    }

	public static void main(String[] args) {
		
		try{
//			SERVIDOR DISPONIBILIZA CONEXÃO
			ServerSocket servidor = new ServerSocket(6500);
			System.out.println("Servidor rodando...");
			
			while(true){
				Socket socket = servidor.accept();
				
				RecebeMensagem RecebeMensagem = new RecebeMensagem(socket);
				RecebeMensagem.start();
			}
			
		}catch(IOException e){
			Logger.getLogger(ServidorTCP.class.getName()).log(Level.SEVERE, null, e);
		}

		

	}

}
