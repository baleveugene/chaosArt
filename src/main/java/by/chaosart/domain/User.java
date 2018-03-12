package by.chaosart.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column; 
import javax.persistence.Entity; 
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue; 
import javax.persistence.GenerationType; 
import javax.persistence.Id; 
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table; 
import javax.persistence.UniqueConstraint; 
 
@Entity 
@org.hibernate.annotations.OptimisticLocking 
@Table(name = "USERS", uniqueConstraints = { 
        @UniqueConstraint(columnNames = "ID"), 
        @UniqueConstraint(columnNames = "LOGIN") }) 
public class User implements Serializable {

	public User() {        
    }
	
	@Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "ID", unique = true, nullable = false) 
    private String id;
	
	@Column(name = "FIRST_NAME", unique = false, nullable = false, length = 64) 
	private String name; 
	
	@Column(name = "SECOND_NAME", unique = false, nullable = true, length = 64) 
	private String surname; 
	
	@Column(name = "LOGIN", unique = true, nullable = false, length = 64) 
	private String login; 
	
	@Column(name = "USER_PASSWORD", unique = false, nullable = false, length = 64) 
	private String password; 
	
	@OneToMany (mappedBy="userComment", fetch=FetchType.LAZY, orphanRemoval=true)
    private List<Comment> comments;
	
	@ManyToOne (optional=false)
    @JoinColumn (name="ROLE_ID") 
	private Role role;
	
	public Role getRole() {
        return this.role;
        }
	public void setRole(Role role) {
		this.role = role;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public List<Comment> getComments() {
		return this.comments;
	}
}
