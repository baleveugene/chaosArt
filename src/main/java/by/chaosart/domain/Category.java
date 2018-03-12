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
import javax.persistence.UniqueConstraint; 
 
@Entity 
@Table(name = "CATEGORY", uniqueConstraints = { 
        @UniqueConstraint(columnNames = "ID"), 
        @UniqueConstraint(columnNames = "CATEGORY_NAME") }) 
public class Category implements Serializable {

	public Category() {        
    }
	
	@Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "ID", unique = true, nullable = false) 
    private String id;
	
	@Column(name = "CATEGORY_NAME", unique = true, nullable = false, length = 64) 
	private String name; 
	
	@OneToMany (mappedBy="category", fetch=FetchType.LAZY, orphanRemoval=true)
    private List<Art> arts;
	
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
	public List<Art> getArts() {
		return this.arts;
	}
}
