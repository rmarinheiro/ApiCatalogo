package br.com.rafael.catalogo.dto;

import java.io.Serializable;

import br.com.rafael.catalogo.entities.Role;

public class RoleDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String authority;
	
	public RoleDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public RoleDTO(Role role) {
		 this.id = role.getId();
		 this.authority = role.getAuthority();
	}

	public RoleDTO(Long id, String authority) {
		super();
		this.id = id;
		this.authority = authority;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public RoleDTO(Long id) {
		super();
		this.id = id;
	}
	
	

}
