package com.temporary.center.ls_service.common;

public class Page {

	/**
	 * 当前页数
	 */
	private Integer curr;
	
	/**
	 * 一页的大小
	 */
	private Integer pageSize;
	
	/**
	 * 总页数
	 */
	private Integer count;

	/**
	 * 排序字段
	 */
	private String sort;
	
	/**
	 * 升序ASC或者降序DESC
	 */
	private String sortRule;
	
	private Integer start;
	
	private Integer end;
	
	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getEnd() {
		return end;
	}

	public void setEnd(Integer end) {
		this.end = end;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getSortRule() {
		return sortRule;
	}

	public void setSortRule(String sortRule) {
		this.sortRule = sortRule;
	}

	public Integer getCurr() {
		return curr;
	}

	public void setCurr(Integer curr) {
		if(null!=curr && null!=pageSize) {
			this.start=(curr-1)*pageSize;
			this.end=start+pageSize;
		}
		this.curr = curr;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		if(null!=curr && null!=pageSize) {
			this.start=(curr-1)*pageSize;
			this.end=start+pageSize;
		}
		this.pageSize = pageSize;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	
}
