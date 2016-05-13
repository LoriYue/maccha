package org.maccha.base.util;

/**
 * Grid Header对象
 * 
 * @author fxy
 * 
 */
public class GridHeader {

	/**
	 * 列名
	 */
	private String property;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 列合并
	 */
	private int colspan;
	/**
	 * 父标题
	 */
	private String parentTitle;
	/**
	 * 是否行合并
	 */
	private Boolean rowSpanable = false;
	/**
	 * 列合计类型
	 */
	private Boolean columnTotal = false;
	/**
	 * 行合计类型
	 */
	private String rowTotalType;
	/**
	 * 行合计列
	 */
	private String rowTotalColumn;

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getColspan() {
		return colspan;
	}

	public void setColspan(int colspan) {
		this.colspan = colspan;
	}

	public String getParentTitle() {
		return parentTitle;
	}

	public void setParentTitle(String parentTitle) {
		this.parentTitle = parentTitle;
	}

	public Boolean getRowSpanable() {
		return rowSpanable;
	}

	public void setRowSpanable(Boolean rowSpanable) {
		this.rowSpanable = rowSpanable;
	}

	public Boolean getColumnTotal() {
		return columnTotal;
	}

	public void setColumnTotal(Boolean columnTotal) {
		this.columnTotal = columnTotal;
	}

	public String getRowTotalType() {
		return rowTotalType;
	}

	public void setRowTotalType(String rowTotalType) {
		this.rowTotalType = rowTotalType;
	}

	public String getRowTotalColumn() {
		return rowTotalColumn;
	}

	public void setRowTotalColumn(String rowTotalColumn) {
		this.rowTotalColumn = rowTotalColumn;
	}

	@Override
	public String toString() {
		return "{property:" + property + ",title:" + title + ",colspan:"
				+ colspan + ",parentTitle:" + parentTitle + ",rowSpanable:"
				+ rowSpanable + ",rowTotalType:" + rowTotalType
				+ ",rowTotalColumn:" + rowTotalColumn + ",columnTotal:"
				+ columnTotal;
	}

}
