package by.chaosart.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column; 
import javax.persistence.Entity; 
import javax.persistence.GeneratedValue; 
import javax.persistence.GenerationType; 
import javax.persistence.Id; 
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
 
@Entity 
@Table(name = "COMMENTS")
public class Comment implements Serializable {

	public Comment() {        
    }
	
	@Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "ID", unique = true, nullable = false) 
    private String id;
	
	@ManyToOne
    @JoinColumn (name="USER_ID") 
	private User userComment;
	
	@ManyToOne
    @JoinColumn (name="ART_ID") 
	private Art artComment;
	
	@Column(name = "COMMENT_TEXT", unique = false, nullable = false, length = 128) 
	private String text; 
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public User getUser() {
		return this.userComment;
	}
	public void setUser(User user) {
		this.userComment = user;
	}
	public Art getArt() {
		return artComment;
	}
	public void setArt(Art art) {
		this.artComment = art;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
