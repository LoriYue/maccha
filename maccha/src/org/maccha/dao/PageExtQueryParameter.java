package org.maccha.dao;

public class PageExtQueryParameter extends PageExtParameter
{
  public static final String LINK_AND = "and";
  public static final String LINK_OR = "or";
  public static final String OPT_Like = "LIKE";
  public static final String OPT_In = "IN";
  public static final String OPT_NotIn = "NOT IN";
  public static final String OPT_Eq = "=";
  public static final String OPT_GreaterThan = ">";
  public static final String OPT_LessThan = "<";
  public static final String OPT_GreaterThanEq = ">=";
  public static final String OPT_LessThanEq = "<=";
  public static final String OPT_Between = "between {a} and {b}";
  public static final String SYSTEM_DEFAULT_GROUPNAME = "SYSTEMDEFAULTGROUPNAME0000000001";
  private String propertyName;
  private String groupName;
  private String opt;
  private String innerLink;

  public PageExtQueryParameter()
  {
    if (this.groupName == null) {
      this.groupName = "SYSTEMDEFAULTGROUPNAME0000000001";
    }
    if (this.innerLink == null) {
      this.innerLink = "and";
    }
    if (this.opt == null)
      this.opt = "=";
  }

  public PageExtQueryParameter(String parameterName, String parameterValue, String propertyName, String opt, Object propertyValue, String groupName, String innerLink)
  {
    this.parameterName = parameterName;
    this.parameterValue = parameterValue;

    this.groupName = groupName;
    this.propertyName = propertyName;
    this.propertyValue = propertyValue;
    this.opt = opt;

    this.innerLink = innerLink;

    if (this.groupName == null) {
      this.groupName = "SYSTEMDEFAULTGROUPNAME0000000001";
    }
    if (this.innerLink == null) {
      this.innerLink = "and";
    }
    if (this.opt == null)
      this.opt = "=";
  }

  public String getGroupName() {
    return this.groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public String getInnerLink() {
    return this.innerLink;
  }

  public void setInnerLink(String innerLink) {
    this.innerLink = innerLink;
  }

  public String getOpt() {
    return this.opt;
  }

  public void setOpt(String opt) {
    this.opt = opt;
  }

  public String getPropertyName() {
    return this.propertyName;
  }

  public void setPropertyName(String propertyName) {
    this.propertyName = propertyName;
  }
}
