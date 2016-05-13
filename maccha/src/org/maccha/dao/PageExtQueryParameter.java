package org.maccha.dao;

import org.maccha.dao.impl.ISqlOperator;

public class PageExtQueryParameter extends PageExtParameter {
	public final static String LINK_AND = "and";
	public final static String LINK_OR = "or";
	public final static String OPT_Like = ISqlOperator.Like;
	public final static String OPT_In = ISqlOperator.In;
	public final static String OPT_NotIn = ISqlOperator.NotIn;
	public final static String OPT_Eq = ISqlOperator.Eq;
	public final static String OPT_GreaterThan = ISqlOperator.GreaterThan;
	public final static String OPT_LessThan = ISqlOperator.LessThan;
	public final static String OPT_GreaterThanEq = ISqlOperator.GreaterThanEq;
	public final static String OPT_LessThanEq = ISqlOperator.LessThanEq;
	public final static String OPT_Between = ISqlOperator.Between;
	public final static String SYSTEM_DEFAULT_GROUPNAME = "SYSTEMDEFAULTGROUPNAME0000000001";
	private String propertyName;
	private String groupName;
	private String opt;
	private String innerLink;
	public PageExtQueryParameter() {
		if (this.groupName == null)
			this.groupName = SYSTEM_DEFAULT_GROUPNAME;
		if (this.innerLink == null)
			this.innerLink = LINK_AND;
		if (this.opt == null)
			this.opt = OPT_Eq;
	}
	/**
	 * 
	 * @param parameterName
	 * @param parameterValue
	 * @param groupName
	 * @param propertyName
	 * @param opt
	 * @param innerLink
	 * @param propertyValue
	 */
	public PageExtQueryParameter(String parameterName, String parameterValue, String propertyName, String opt, Object propertyValue, String groupName, String innerLink) {
		this.parameterName = parameterName;
		this.parameterValue = parameterValue;
		this.groupName = groupName;
		this.propertyName = propertyName;
		this.propertyValue = propertyValue;
		this.opt = opt;
		this.innerLink = innerLink;
		if (this.groupName == null)
			this.groupName = SYSTEM_DEFAULT_GROUPNAME;
		if (this.innerLink == null)
			this.innerLink = LINK_AND;
		if (this.opt == null)
			this.opt = OPT_Eq;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getInnerLink() {
		return innerLink;
	}

	public void setInnerLink(String innerLink) {
		this.innerLink = innerLink;
	}

	public String getOpt() {
		return opt;
	}

	public void setOpt(String opt) {
		this.opt = opt;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
}
