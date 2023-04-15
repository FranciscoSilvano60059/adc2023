package pt.unl.fct.di.apdc.firstwebapp.util;

import com.google.cloud.datastore.Value;

public class RegisterData {

	public String username;
	public String password;
	public String password2;
	public String email;
	public String name;
	public String telefoneFixo;
	public String telefoneMovel;
	public String visibilidade;
	public String morada;
	public String codigoPostal;
	public String nif;
	public String ocupacao;
	
	
	
	public RegisterData() {
	}

	public RegisterData(String username, String password, String password2, 
			String email, String name, 
			String nif, String codigoPostal, String morada, 
			String visibilidade, String telefoneFixo, 
			String telefoneMovel, String ocupacao) {
		this.name = name;
		this.username = username;
		this.password = password;
		this.password2 = password2;
		this.email = email;
		this.telefoneFixo=telefoneFixo;
		this.telefoneMovel=telefoneMovel;
		this.visibilidade=visibilidade;
		this.morada=morada;
		this.codigoPostal=codigoPostal;
		this.nif=nif;
		this.ocupacao=ocupacao;
	}
	public boolean validEmail() {
        String EMAIL = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        boolean result=email.matches(EMAIL);
        return result;
                
    }
	public boolean validRegistration() {
		if (password.equals(password2)  && password.length() > 8) {
			return true;
		} else {
			return false;
		}
	}

}
