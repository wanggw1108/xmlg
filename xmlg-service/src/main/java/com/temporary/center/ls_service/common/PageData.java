package com.temporary.center.ls_service.common;

public class PageData {

	private  Object list;
	
	private  long count;

	private int currentPage;
	private int totalPage;
	private int size;
	public PageData(){

	}
	public PageData(Object list,long count,int currentPage,int size,int totalPage){
		this.list = list;
		this.count = count;
		this.currentPage = currentPage;
		this.totalPage = totalPage;
		this.size = size;
	}
	public PageData(Object list,long count,int currentPage,int size){
		this.list = list;
		this.count = count;
		this.currentPage = currentPage;
		this.size = size;
		this.totalPage = count%size==0?(int)count/size:((int)(count/size))+1;
	}

	public Object getList() {
		return list;
	}

	public void setList(Object list) {
		this.list = list;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
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
