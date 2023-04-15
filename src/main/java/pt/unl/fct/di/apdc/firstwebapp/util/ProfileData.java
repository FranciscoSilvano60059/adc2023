package pt.unl.fct.di.apdc.firstwebapp.util;


public class ProfileData {

	public String username;
	public String email;
	public String name;
	public String telefone;
	public String telemovel;
	public String visibilidade;
	public String morada;
	public String codigoPostal;
	public String nif;
	public String ocupacao;
	
	
	
	public ProfileData(String name,
			String email,
			String nif, String ocupacao, String codigoPostal, 
			String morada, String telefone,
			String telemovel, String visibilidade) {
		this.name = name;
		this.email = email;
		this.nif=nif;
		this.ocupacao=ocupacao;
		this.codigoPostal=codigoPostal;
		this.morada=morada;
		this.telefone=telefone;
		this.telemovel=telemovel;
		this.visibilidade=visibilidade;

		
		
	}
}
