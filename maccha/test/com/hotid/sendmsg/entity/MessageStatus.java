package com.hotid.sendmsg.entity;

public class MessageStatus implements java.io.Serializable, Comparable<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String inmeId;
	private String inmeCreateTime;
	private String inmeUpdateTime;
	private String fromUserId;
	
	public String getInmeCreateTime() {
		return inmeCreateTime;
	}

	public void setInmeCreateTime(String inmeCreateTime) {
		this.inmeCreateTime = inmeCreateTime;
	}

	public String getInmeUpdateTime() {
		return inmeUpdateTime;
	}

	public void setInmeUpdateTime(String inmeUpdateTime) {
		this.inmeUpdateTime = inmeUpdateTime;
	}

	public String getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}

	private String toUserId;
	private String pushTime;
	private String receiveTime;
	private String readTime;
	private String status;

	public MessageStatus() {
	}

	public MessageStatus(String id) {
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

	private String _getInmeId(String inmeId) {
		return this.inmeId;
	}

	public String getInmeId() {
		return _getInmeId(this.inmeId);
	}

	public void setInmeId(String inmeId) {
		this.inmeId = inmeId;
	}

	private String getToUserId() {
		return this.toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	private String _getPushTime(String pushTime) {
		return this.pushTime;
	}

	public String getPushTime() {
		return _getPushTime(this.pushTime);
	}

	public void setPushTime(String pushTime) {
		this.pushTime = pushTime;
	}

	private String _getReadTime(String readTime) {
		return this.readTime;
	}

	public String getReadTime() {
		return _getReadTime(this.readTime);
	}

	public void setReadTime(String readTime) {
		this.readTime = readTime;
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
		MessageStatus other = (MessageStatus) obj;
		return this.getId().compareTo(other.getId());
	}

	public String getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}

}