package pt.unl.fct.di.apdc.firstwebapp.util;

import com.google.cloud.datastore.Value;

public class ChangeInfoData {
	public String usernameResponsible;
	public String username;
	public String email;
	public String name;
	public String telefoneFixo;
	public String telefoneMovel;
	public String visibilidade;
	public String morada;
	public String codigoPostal;
	public String nif;
	public String role;
	public String state;
	public String ocupacao;
	
	
	
	public ChangeInfoData() {
	}

	public ChangeInfoData(String usernameResponsible, String username,
			String email, String name, 
			String nif, String codigoPostal, String morada, 
			String visibilidade, String telefoneFixo, 
			String telefoneMovel,String ocupacao, String role, String state) {
		this.usernameResponsible=usernameResponsible;
		this.username = username;
		this.email = email;
		this.name = name;
		this.nif=nif;
		this.codigoPostal=codigoPostal;
		this.morada=morada;
		this.visibilidade=visibilidade;
		this.telefoneFixo=telefoneFixo;
		this.telefoneMovel=telefoneMovel;
		this.ocupacao=ocupacao;
		this.role=role;
		this.state=state;
	}
}
