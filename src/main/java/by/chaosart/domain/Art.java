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
@Table(name = "ART", uniqueConstraints = { 
        @UniqueConstraint(columnNames = "ID"), 
        @UniqueConstraint(columnNames = "ART_NAME") }) 
public class Art implements Serializable {

	public Art() {        
    }
	
	@Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "ID", unique = true, nullable = false) 
    private String id;
	
	@Column(name = "ART_NAME", unique = true, nullable = false, length = 64) 
	private String name; 
	
	@Column(name = "IMAGE", unique = false, nullable = false, length = 64) 
	private String image; 
	
	@ManyToOne
    @JoinColumn (name="ARTIST_ID") 
	private Artist artist;
	
	@ManyToOne
    @JoinColumn (name="CATEGORY_ID") 
	private Category category;
	
	@OneToMany (mappedBy="artComment", fetch=FetchType.LAZY, orphanRemoval=true)
    private List<Comment> comments;
	
	@Column(name = "ORIGINAL_URL", unique = false, nullable = false, length = 64) 
	private String originalUrl; 
	
	public Artist getArtist() {
        return this.artist;
        }
	public void setArtist(Artist artist) {
        this.artist = artist;
        }

	public Category getCategory() {
        return this.category;
        }
	public void setCategory(Category category) {
        this.category = category;
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

	public String getOriginalUrl() {
		return originalUrl;
	}

	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	public List<Comment> getComments() {
		return this.comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
}
