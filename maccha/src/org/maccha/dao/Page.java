package org.maccha.dao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.maccha.base.util.ExistMap;

public class Page implements Serializable {
	private PageExtParameter[] pageExtParameterArray = null;
	public static final int DEFAULT_ROW_COUNT = 15;
	// 分页的位置序号同实际数据页序号的偏移位置
	private static final int PAGE_POSITION_OFFSET = 7;
	// 每页行数
	private int rowCount;
	// 总行数
	private int totalRowCount;
	// 页序号
	private int index;
	// 分页跳转url
	private String url;
	public Page() {
		//log.info("初始化分页实例，默认第一页开始，每页15行记录。。。。。。。。。。。。。。。。。");
		index = 1;
		totalRowCount = 0;
		rowCount = DEFAULT_ROW_COUNT;
	}
	/**
	 * 获得开始行序号
	 * 
	 * @return
	 */
	public int getStartRow() {
		int intStart = rowCount * (index - 1);
		if (intStart < 0)
			intStart = 0;
		return intStart;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	public int getTotalRowCount() {
		return totalRowCount;
	}
	public void setTotalRowCount(int totalRowCount) {
		this.totalRowCount = totalRowCount;
	}
	/**
	 * 获取分页数
	 * 
	 * @return
	 */
	public int getPageCount() {
		if (rowCount == 0)
			return 0;
		if (totalRowCount== 0) {
			return 0;
		} else {
			int i = totalRowCount % rowCount != 0 ? totalRowCount / rowCount + 1
					: totalRowCount / rowCount;
			return i != 0 ? i : 1;
		}
	}
	public int getIndex() {
		return this.index;
	}
	public void setIndex(int _index) {
		this.index = _index;
	}
	/**
	 * 获取指定页码的起始位置
	 * 
	 * @return
	 */
	public int gotoPage(int _index) {
		if (_index > this.getPageCount())
			_index = this.getPageCount();
		if (_index <= 0)
			_index = 1;
		return _index;
	}
	/**
	 * 获得分页跳转连接url
	 * 
	 * @return
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * 设置分页跳转连接url
	 * 
	 * @param pageUrl
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isInitUrl() {
		return url != null;
	}
	public String getPageUrl() {

		return getUrl() + "?page.rowCount=" + this.getRowCount() + "&"
				+ this.getPageExtParameterUrl();
	}
	public String getPageUrl(int index) {
		return getPageUrl() + "&page.index=" + this.gotoPage(index);
	}
	public String getCurrentPageUrl() {
		return getPageUrl() + "&page.index=" + this.getIndex();
	}
	public String getPagePositionUrl(int position) {
		int num = getIndexByPagePosition(position);
		if (num == -1)
			return "#";
		return getPageUrl() + "&page.index=" + this.gotoPage(num);
	}
	public String getPagePositionTitle(int position) {
		int num = getIndexByPagePosition(position);
		if (num == -1)
			return "";
		if (num == this.getIndex())
			return "<b>" + num + "</b>";
		else
			return num + "";
	}
	private int getIndexByPagePosition(int position) {
		int n = this.index - PAGE_POSITION_OFFSET;
		if (n < 0)
			n = 0;
		n = n + position;
		if (n > this.getPageCount())
			return -1;
		else
			return n;
	}
	/**
	 * 首页url
	 * 
	 * @return
	 */
	public String getFirstPageUrl() {
		return getPageUrl(1);
	}

	/**
	 * 最后一页url
	 * 
	 * @return
	 */
	public String getLastPageUrl() {
		return getPageUrl(this.getPageCount());

	}
	/**
	 * 获取下一页url
	 * 
	 * @return
	 */
	public String nextPageUrl() {
		return this.getPageUrl(this.index + 1);
	}
	/**
	 * 获取上一页url
	 * 
	 * @return
	 */
	public String prevPageUrl() {
		return this.getPageUrl(this.index - 1);

	}
	/**
	 * 是否存在下一页
	 * 
	 * @return
	 */
	public boolean hasNextPage() {
		return this.index + 1 <= this.getPageCount();
	}
	/**
	 * 是否存在上一页
	 * 
	 * @return
	 */
	public boolean hasPrevPage() {
		return this.index - 1 > 0;
	}
	public PageExtParameter[] getPageExtParameters() {
		return this.pageExtParameterArray;
	}
	/**
	 * 获得分页扩展参数
	 * @param name
	 * @return
	 */
	public PageExtParameter getPageExtParameter(String name) {
		for (int i = 0; this.pageExtParameterArray != null && i < this.pageExtParameterArray.length; i++) {
			if (this.pageExtParameterArray[i].getParameterName().equalsIgnoreCase(name))
				return this.pageExtParameterArray[i];
		}
		return null;
	}
	public String getPageExtParameterUrl() {
		String urlParameter = "";
		PageExtParameter[] _pageExtParameterArray = this.getPageExtParameters();
		ExistMap existMap = new ExistMap();
		for (int i = 0; _pageExtParameterArray != null && i < _pageExtParameterArray.length; i++) {
			if (existMap.exist(_pageExtParameterArray[i].getParameterName()))
				continue;
			if (i == 0)
				urlParameter += _pageExtParameterArray[i].getPageExtParameterUrl();
			if (i > 0)
				urlParameter += "&" + _pageExtParameterArray[i].getPageExtParameterUrl();
		}
		return urlParameter;
	}
	public void setPageExtParameters(PageExtParameter[] pageExtParameterArray) {
		this.pageExtParameterArray = pageExtParameterArray;
	}
	public Map getPageExtQueryParameterFilterMap() {
		PageExtParameter[] pageExtParameterArray = this.getPageExtParameters();
		Map groupFilterMap = new HashMap();
		if (pageExtParameterArray == null || pageExtParameterArray.length <= 0)
			return null;
		for (int i = 0; i < pageExtParameterArray.length; i++) {
			if (!(pageExtParameterArray[i] instanceof PageExtQueryParameter))
				continue;
			PageExtQueryParameter pageExtQueryParameter = (PageExtQueryParameter) pageExtParameterArray[i];
			String groupName = pageExtQueryParameter.getGroupName();
			String propertyName = pageExtQueryParameter.getPropertyName();
			String opt = pageExtQueryParameter.getOpt();
			Object value = pageExtQueryParameter.getPropertyValue();
			String innerLink = pageExtQueryParameter.getInnerLink();
			if (value == null)
				continue;
			Filter filter = Filter.getFilter(propertyName, opt, value);
			Filter _groupFilter = (Filter) groupFilterMap.get(groupName);
			if (_groupFilter == null) {
				if (PageExtQueryParameter.LINK_OR.equals(innerLink))
					_groupFilter = Filter.getOrGroupFilter();
				else
					_groupFilter = Filter.getAndGroupFilter();
				groupFilterMap.put(groupName, _groupFilter);
			}
			_groupFilter.add(filter);
		}
		return groupFilterMap;
	}
}
