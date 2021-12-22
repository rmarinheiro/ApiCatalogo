package br.com.rafael.catalogo.dto;

public class UserInsertDTO extends UserDTO {

	private static final long serialVersionUID = 1L;
	
	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public UserInsertDTO() {
		// TODO Auto-generated constructor stub
	}

}
