package com.hotid.sendmsg.entity;

public class OfflineMessage implements java.io.Serializable, Comparable<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String userIds;
	private String inmeContent;

	public OfflineMessage() {
	}

	public OfflineMessage(String id) {
		this.id = id;
	}

	public int compareTo(Object obj) {
		OfflineMessage other = (OfflineMessage) obj;
		return this.getId().compareTo(other.getId());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public String getInmeContent() {
		return inmeContent;
	}

	public void setInmeContent(String inmeContent) {
		this.inmeContent = inmeContent;
	}
}