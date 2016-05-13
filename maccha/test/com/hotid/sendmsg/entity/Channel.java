package com.hotid.sendmsg.entity;

public class Channel implements java.io.Serializable, Comparable<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String createtime;
	private String createUserId;
	private String type;
	private String status;
	private String name;
	private String title;
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	private String desc;
	private String closetime;

	public Channel() {
	}

	public Channel(String id) {
		this.id = id;
	}

	public int compareTo(Object obj) {
		Channel other = (Channel) obj;
		return this.getId().compareTo(other.getId());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getClosetime() {
		return closetime;
	}

	public void setClosetime(String closetime) {
		this.closetime = closetime;
	}
}