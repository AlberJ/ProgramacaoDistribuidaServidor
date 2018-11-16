import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RecebeMensagem extends Thread {
	private Socket socket;

	public RecebeMensagem(Socket s) {
		this.socket = s;
	}

	public void run() {
		try {
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			String msg = "";
			String linha;
			String comandos[] = null;
			String nome = "";

			do {
				if (!nome.equals("")) {
					linha = in.readUTF();
					msg = linha;
					System.out.println(msg);
					comandos = linha.split(":");
					for (String c : comandos) {
						c.trim();
					}

					if (!msg.equals("")) {
						switch (comandos[0]) {
						case "bye":
							msg = comandos[0];
							System.out.println("pegou comando bye!");
							break;
						case "-all":
							msg = comandos[0];
							System.out.println("pegou comando -all!");
							break;
						case "-user":
							msg = comandos[0];
							System.out.println("pegou comando -user!");
							break;
						case "list":
							msg = comandos[0];
							System.out.println("pegou comando list!");
							break;
						case "rename": // ISSO AKI FICA NO LADO DO SERVIDOR E NO
										// CLIENTE
							msg = comandos[0];
							System.out.println("pegou comando rename!");
							break;

						default:
							System.out.println("Comando inválido!");
						}

						// SERVIDOR ECOA MENSAGEM
						out.writeUTF("eco " + msg);
					}
				} else {
					nome = in.readUTF();
					out.writeUTF("Bem vindo " + nome);
				}

			} while (!nome.equals(""));

		} catch (IOException ex) {
			Logger.getLogger(RecebeMensagem.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
