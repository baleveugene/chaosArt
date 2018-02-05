package by.java.dokwork.domain;

public class Comment {

	private String id;
	private Integer userId;
	private String artId;
	private String text;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getArtId() {
		return artId;
	}
	public void setArtId(String artId) {
		this.artId = artId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
