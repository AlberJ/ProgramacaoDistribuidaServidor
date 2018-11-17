import java.io.DataOutputStream;

public class Cliente {
	private String nome;
	DataOutputStream out;
	
	public Cliente(DataOutputStream out){
		this.out = out;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
