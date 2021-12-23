package br.com.rafael.catalogo.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import br.com.rafael.catalogo.entities.User;

public class UserDTO implements Serializable {

	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	@NotEmpty(message = "Campo Nome Obrigatório")
	private String firstName;

	private String lastName;
	
	@Email(message = "Informe um email válido")
	private String email;
	
	Set<RoleDTO> roles = new HashSet<>();

	
	
	public UserDTO() {
		// TODO Auto-generated constructor stub
	}


	public UserDTO(Long id, String firstName, String lastName, String email) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	
	}
	
	public UserDTO(User entity) {
		this.id = entity.getId();
		this.firstName = entity.getFirstName();
		this.lastName = entity.getLastName();
		this.email = entity.getEmail();
		entity.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
		
	}


	public Set<RoleDTO> getRoles() {
		return roles;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}
	
	

}
