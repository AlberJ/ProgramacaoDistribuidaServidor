import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorTCP extends Thread {
	private static ArrayList<Cliente> clientes = new ArrayList<Cliente>();
	private Socket socket;
	private static ServerSocket servidor;

	public ServidorTCP(Socket socket) {
		this.socket = socket;
	}

	public boolean addCliente(Cliente cli) {
		for (Cliente c : ServidorTCP.clientes) {
			if (c.getNome().equals(cli.getNome())) {
				return false;
			} 
		}
		clientes.add(cli);
		return true;
	}

	public void removeCliente(Cliente cli) {
		for (Cliente c : ServidorTCP.clientes) {
			if (c.getNome().equals(cli.getNome())) {
				clientes.remove(c);
			}
		}
	}
	
	public String listarCliente() {
		String lista = "Lista de Usuarios: \n";
		for (Cliente c : ServidorTCP.clientes) {
			lista += c.getNome() + "\n";
		}
		return lista;
	}
	public static void main(String[] args) {

		try {
			servidor = new ServerSocket(6500);
			System.out.println("Servidor rodando...");

			while (true) {
				Socket socket = servidor.accept();

				Thread thread = new ServidorTCP(socket);
				thread.start();
			}

		} catch (IOException e) {
			Logger.getLogger(ServidorTCP.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	public void run() {
		try {
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			Cliente cli = new Cliente();
			String msg = "";
			String linha;
			String comandos[] = null;

			cli.setNome(in.readUTF());
			
			if (addCliente(cli)) {
				out.writeUTF("Bem vindo " + cli.getNome());

				do {
					linha = in.readUTF();
					msg = linha;
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
							msg = listarCliente();
							break;
						case "rename":
							msg = comandos[0];
							System.out.println("pegou comando rename!");
							break;

						default:
							System.out.println("Comando inválido!");
						}

						// SERVIDOR ECOA MENSAGEM
						out.writeUTF(msg);
					}

				} while (!comandos[0].equals("bye"));
				
				removeCliente(cli);
				cli = null;
				this.socket.close();
				
			}else{
				msg= "User name ja em uso!";
				out.writeUTF(msg);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Falha na Conexao... .. ."+" IOException: " + e);
		}

	}
}
