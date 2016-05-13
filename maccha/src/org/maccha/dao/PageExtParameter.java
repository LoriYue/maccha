package org.maccha.dao;

public class PageExtParameter {
	protected String parameterName;
	protected String parameterValue;
	protected Object propertyValue;
	public PageExtParameter() {

	}
	public PageExtParameter(String parameterName, String parameterValue) {

		this.parameterName = parameterName;
		this.parameterValue = parameterValue;
	}
	public PageExtParameter(String parameterName, String parameterValue,
			Object propertyValue) {

		this.parameterName = parameterName;
		this.parameterValue = parameterValue;
		this.propertyValue = propertyValue;
	}
	public String getPageExtParameterUrl() {
		return this.parameterName + "=" + this.parameterValue;
	}
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	public String getParameterValue() {
		return parameterValue;
	}
	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}
	public Object getPropertyValue() {
		return propertyValue;
	}
	public void setPropertyValue(Object propertyValue) {
		this.propertyValue = propertyValue;
	}
}
