package com.heroku.java.model;

public class AnnouncementModel {

	private accountModel account;
    private String announcementID;
    private String datePosted;
    private String title;
    private String contentImage;
    private String contentDescription;
    
    // constructors
    public AnnouncementModel() {
	}
    public AnnouncementModel(String datePosted, String title, String contentImage, String contentDescription) {
		super();
		this.datePosted = datePosted;
		this.title = title;
		this.contentImage = contentImage;
		this.contentDescription = contentDescription;
	}
	public AnnouncementModel(accountModel account, String announcementID, String datePosted, String title,
			String contentImage, String contentDescription) {
		super();
		this.account = account;
		this.announcementID = announcementID;
		this.datePosted = datePosted;
		this.title = title;
		this.contentImage = contentImage;
		this.contentDescription = contentDescription;
	}
    
	// setters and getters
    public accountModel getAccount() {
		return account;
	}
	public String getAnnouncementID() {
		return announcementID;
	}
	public void setAccount(accountModel account) {
		this.account = account;
	}
	public void setAnnouncementID(String announcementID) {
		this.announcementID = announcementID;
	}
	public String getDatePosted() {
		return datePosted;
	}
	public void setDatePosted(String datePosted) {
		this.datePosted = datePosted;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContentImage() {
		return contentImage;
	}
	public void setContentImage(String contentImage) {
		this.contentImage = contentImage;
	}
	public String getContentDescription() {
		return contentDescription;
	}
	public void setContentDescription(String contentDescription) {
		this.contentDescription = contentDescription;
	}
	
	@Override
	public String toString() {
		return "AnnouncementModel [account=" + account + ", announcementID=" + announcementID + ", datePosted="
				+ datePosted + ", title=" + title + ", contentImage=" + contentImage + ", contentDescription="
				+ contentDescription + "]";
	}
}
