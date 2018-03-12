package by.chaosart.domain;

import java.io.Serializable;
import java.util.List;

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
@org.hibernate.annotations.OptimisticLocking 
@Table(name = "ARTIST", uniqueConstraints = { 
        @UniqueConstraint(columnNames = "ID"), 
        @UniqueConstraint(columnNames = "ARTIST_NAME") }) 
public class Artist implements Serializable {

	public Artist() {        
    }
	
	@Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "ID", unique = true, nullable = false) 
    private String id;
	
	@Column(name = "ARTIST_NAME", unique = true, nullable = false, length = 64) 
	private String name; 
	
	@OneToMany (mappedBy="artist", fetch=FetchType.LAZY, orphanRemoval=true)
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
