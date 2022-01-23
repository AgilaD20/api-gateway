package com.flightapp.user.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role {
	
	@Id
	@Column(name="roleid")
	private Integer roleId;
	
	@Column(name="rolename")
	private String roleName;
	
	@ManyToMany(mappedBy="roles")
	private List<Userentity> users;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="roles_authorities",
	joinColumns = @JoinColumn(name="role_id",referencedColumnName="roleid"),
	inverseJoinColumns=@JoinColumn(name="privilege_id",referencedColumnName="id"))
	private List<Privilege> privileges;
	
	
	
}