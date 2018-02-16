package by.chaosart.domain;

public class Comment {

	private String id;
	private String userId;
	private String artId;
	private String text;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId2) {
		this.userId = userId2;
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
