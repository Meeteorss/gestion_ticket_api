package school.spring.school.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;

@Entity
//@AllArgsConstructor
//@NoArgsConstructor
//@Data
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Column(nullable = false, unique = true)
	private String username;
	@Column(nullable = false, unique = true)
	private String email;
	@Column(nullable = false)
	private String password;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	public enum Role {
		CLIENT, DEV, ADMIN
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public User(String username, String email, String password, Role role) {

		this.username = username;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	public User(int id, String username, String email, String password, Role role) {

		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.role = role;
	}
	public User() {
		
	}

}
