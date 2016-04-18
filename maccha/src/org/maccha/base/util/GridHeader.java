package org.maccha.base.util;

public class GridHeader
{
  private String property;
  private String title;
  private int colspan;
  private String parentTitle;
  private Boolean rowSpanable = Boolean.valueOf(false);

  private Boolean columnTotal = Boolean.valueOf(false);
  private String rowTotalType;
  private String rowTotalColumn;

  public String getProperty()
  {
    return this.property;
  }

  public void setProperty(String property) {
    this.property = property;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getColspan() {
    return this.colspan;
  }

  public void setColspan(int colspan) {
    this.colspan = colspan;
  }

  public String getParentTitle() {
    return this.parentTitle;
  }

  public void setParentTitle(String parentTitle) {
    this.parentTitle = parentTitle;
  }

  public Boolean getRowSpanable() {
    return this.rowSpanable;
  }

  public void setRowSpanable(Boolean rowSpanable) {
    this.rowSpanable = rowSpanable;
  }

  public Boolean getColumnTotal() {
    return this.columnTotal;
  }

  public void setColumnTotal(Boolean columnTotal) {
    this.columnTotal = columnTotal;
  }

  public String getRowTotalType() {
    return this.rowTotalType;
  }

  public void setRowTotalType(String rowTotalType) {
    this.rowTotalType = rowTotalType;
  }

  public String getRowTotalColumn() {
    return this.rowTotalColumn;
  }

  public void setRowTotalColumn(String rowTotalColumn) {
    this.rowTotalColumn = rowTotalColumn;
  }

  public String toString()
  {
    return "{property:" + this.property + ",title:" + this.title + ",colspan:" + this.colspan + ",parentTitle:" + this.parentTitle + ",rowSpanable:" + this.rowSpanable + ",rowTotalType:" + this.rowTotalType + ",rowTotalColumn:" + this.rowTotalColumn + ",columnTotal:" + this.columnTotal;
  }
}
