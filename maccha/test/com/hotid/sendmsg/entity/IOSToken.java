package com.hotid.sendmsg.entity;


public class IOSToken implements java.io.Serializable, Comparable<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String token;
	private String userId;
	private String time;
	private String status;

	public IOSToken() {
	}

	public IOSToken(String id) {
		this.id = id;
	}

	private String _getId(String id) {
		return this.id;
	}

	public String getId() {
		return _getId(this.id);
	}

	public void setId(String id) {
		this.id = id;
	}

	private String _getToken(String token) {
		return this.token;
	}

	public String getToken() {
		return _getToken(this.token);
	}

	public void setToken(String token) {
		this.token = token;
	}

	private String _getUserId(String userId) {
		return this.userId;
	}

	public String getUserId() {
		return _getUserId(this.userId);
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}


	private String _getTime(String time) {
		return this.time;
	}

	public String getTime() {
		return _getTime(this.time);
	}

	public void setTime(String time) {
		this.time = time;
	}

	private String _getStatus(String status) {
		return this.status;
	}

	public String getStatus() {
		return _getStatus(this.status);
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String toString() {
		if (this.getId() == null)
			return "";
		return this.getId() + "";
	}

	public int compareTo(Object obj) {
		IOSToken other = (IOSToken) obj;
		return this.getId().compareTo(other.getId());
	}
}