package by.chaosart.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column; 
import javax.persistence.Entity; 
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue; 
import javax.persistence.GenerationType; 
import javax.persistence.Id; 
import javax.persistence.OneToMany;
import javax.persistence.Table; 
 
@Entity 
@org.hibernate.annotations.OptimisticLocking 
@Table(name = "ROLE") 
public class Role implements Serializable {

	public Role() {        
    }
	
	@Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "ID", unique = true, nullable = false) 
    private String id;
	
	@Column(name = "ROLE_NAME", unique = false, nullable = false, length = 64) 
	private String name; 
	
	@OneToMany (mappedBy="role", fetch=FetchType.LAZY, orphanRemoval=true)
    private List<User> users;

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
	
	public List<User> getUsers() {
		return this.users;
	}
}
