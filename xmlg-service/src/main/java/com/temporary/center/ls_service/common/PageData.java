package com.temporary.center.ls_service.common;

public class PageData {

	private  Object list;
	
	private  Object count;

	private int currentPage;
	private int totalPage;
	private int size;
	public PageData(){

	}
	public PageData(Object list,Object count,int currentPage,int size,int totalPage){
		this.list = list;
		this.count = count;
		this.currentPage = currentPage;
		this.totalPage = totalPage;
		this.size = size;
	}

	public Object getList() {
		return list;
	}

	public void setList(Object list) {
		this.list = list;
	}

	public Object getCount() {
		return count;
	}

	public void setCount(Object count) {
		this.count = count;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
