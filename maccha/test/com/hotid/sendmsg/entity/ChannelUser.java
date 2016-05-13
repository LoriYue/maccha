package com.hotid.sendmsg.entity;

public class ChannelUser implements java.io.Serializable, Comparable<Object> {

	private static final long serialVersionUID = 1L;
	private String id;
	private String chanId;
	private String userId;
	private String joinTime;
	private String exitTime;
	private String status;
	private String isAdmin;

	public ChannelUser() {
	}

	public ChannelUser(String id) {
		this.id = id;
	}

	public int compareTo(Object obj) {
		ChannelUser other = (ChannelUser) obj;
		return this.getId().compareTo(other.getId());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getChanId() {
		return chanId;
	}

	public void setChanId(String chanId) {
		this.chanId = chanId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(String joinTime) {
		this.joinTime = joinTime;
	}

	public String getExitTime() {
		return exitTime;
	}

	public void setExitTime(String exitTime) {
		this.exitTime = exitTime;
	}

	public String getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return userId+" "+chanId;
	}
}