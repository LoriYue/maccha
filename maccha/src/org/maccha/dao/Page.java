package org.maccha.dao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.maccha.base.util.ExistMap;

public class Page
  implements Serializable
{
  private PageExtParameter[] pageExtParameterArray = null;
  public static final int DEFAULT_ROW_COUNT = 15;
  private static final int PAGE_POSITION_OFFSET = 7;
  private int rowCount;
  private int totalRowCount;
  private int index;
  private String url;

  public Page()
  {
    this.index = 1;
    this.totalRowCount = 0;
    this.rowCount = 15;
  }

  public int getStartRow()
  {
    int intStart = this.rowCount * (this.index - 1);
    if (intStart < 0)
      intStart = 0;
    return intStart;
  }

  public int getRowCount() {
    return this.rowCount;
  }

  public void setRowCount(int rowCount) {
    this.rowCount = rowCount;
  }

  public int getTotalRowCount() {
    return this.totalRowCount;
  }

  public void setTotalRowCount(int totalRowCount) {
    this.totalRowCount = totalRowCount;
  }

  public int getPageCount()
  {
    if (this.rowCount == 0) {
      return 0;
    }
    if (this.totalRowCount == 0) {
      return 0;
    }
    int i = this.totalRowCount % this.rowCount != 0 ? this.totalRowCount / this.rowCount + 1 : this.totalRowCount / this.rowCount;

    return i != 0 ? i : 1;
  }

  public int getIndex()
  {
    return this.index;
  }

  public void setIndex(int _index) {
    this.index = _index;
  }

  public int gotoPage(int _index)
  {
    if (_index > getPageCount()) {
      _index = getPageCount();
    }
    if (_index <= 0) {
      _index = 1;
    }

    return _index;
  }

  public String getUrl()
  {
    return this.url;
  }

  public void setUrl(String url)
  {
    this.url = url;
  }

  public boolean isInitUrl() {
    return this.url != null;
  }

  public String getPageUrl()
  {
    return getUrl() + "?page.rowCount=" + getRowCount() + "&" + getPageExtParameterUrl();
  }

  public String getPageUrl(int index)
  {
    return getPageUrl() + "&page.index=" + gotoPage(index);
  }

  public String getCurrentPageUrl() {
    return getPageUrl() + "&page.index=" + getIndex();
  }

  public String getPagePositionUrl(int position)
  {
    int num = getIndexByPagePosition(position);

    if (num == -1) {
      return "#";
    }
    return getPageUrl() + "&page.index=" + gotoPage(num);
  }

  public String getPagePositionTitle(int position)
  {
    int num = getIndexByPagePosition(position);

    if (num == -1) {
      return "";
    }
    if (num == getIndex()) {
      return "<b>" + num + "</b>";
    }
    return num + "";
  }

  private int getIndexByPagePosition(int position) {
    int n = this.index - 7;

    if (n < 0)
      n = 0;
    n += position;
    if (n > getPageCount()) {
      return -1;
    }
    return n;
  }

  public String getFirstPageUrl()
  {
    return getPageUrl(1);
  }

  public String getLastPageUrl()
  {
    return getPageUrl(getPageCount());
  }

  public String nextPageUrl()
  {
    return getPageUrl(this.index + 1);
  }

  public String prevPageUrl()
  {
    return getPageUrl(this.index - 1);
  }

  public boolean hasNextPage()
  {
    return this.index + 1 <= getPageCount();
  }

  public boolean hasPrevPage()
  {
    return this.index - 1 > 0;
  }

  public PageExtParameter[] getPageExtParameters() {
    return this.pageExtParameterArray;
  }

  public PageExtParameter getPageExtParameter(String name)
  {
    int i = 0;
    for (; (this.pageExtParameterArray != null) && (i < this.pageExtParameterArray.length); i++) {
      if (this.pageExtParameterArray[i].getParameterName().equalsIgnoreCase(name))
      {
        return this.pageExtParameterArray[i];
      }
    }
    return null;
  }

  public String getPageExtParameterUrl() {
    String urlParameter = "";

    PageExtParameter[] _pageExtParameterArray = getPageExtParameters();

    ExistMap existMap = new ExistMap();

    int i = 0;
    for (; (_pageExtParameterArray != null) && (i < _pageExtParameterArray.length); i++)
    {
      if (existMap.exist(_pageExtParameterArray[i].getParameterName())) {
        continue;
      }
      if (i == 0) {
        urlParameter = urlParameter + _pageExtParameterArray[i].getPageExtParameterUrl();
      }

      if (i > 0) {
        urlParameter = urlParameter + "&" + _pageExtParameterArray[i].getPageExtParameterUrl();
      }
    }
    return urlParameter;
  }

  public void setPageExtParameters(PageExtParameter[] pageExtParameterArray) {
    this.pageExtParameterArray = pageExtParameterArray;
  }

  public Map getPageExtQueryParameterFilterMap()
  {
    PageExtParameter[] pageExtParameterArray = getPageExtParameters();
    Map groupFilterMap = new HashMap();
    if ((pageExtParameterArray == null) || (pageExtParameterArray.length <= 0))
      return null;
    for (int i = 0; i < pageExtParameterArray.length; i++)
    {
      if (!(pageExtParameterArray[i] instanceof PageExtQueryParameter))
        continue;
      PageExtQueryParameter pageExtQueryParameter = (PageExtQueryParameter)pageExtParameterArray[i];

      String groupName = pageExtQueryParameter.getGroupName();

      String propertyName = pageExtQueryParameter.getPropertyName();

      String opt = pageExtQueryParameter.getOpt();

      Object value = pageExtQueryParameter.getPropertyValue();

      String innerLink = pageExtQueryParameter.getInnerLink();

      if (value == null) {
        continue;
      }
      Filter filter = Filter.getFilter(propertyName, opt, value);

      Filter _groupFilter = (Filter)groupFilterMap.get(groupName);

      if (_groupFilter == null)
      {
        if ("or".equals(innerLink))
          _groupFilter = Filter.getOrGroupFilter();
        else {
          _groupFilter = Filter.getAndGroupFilter();
        }
        groupFilterMap.put(groupName, _groupFilter);
      }
      _groupFilter.add(filter);
    }
    return groupFilterMap;
  }
}
