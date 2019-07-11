package com.temporary.center.ls_service.service;

import com.temporary.center.ls_service.domain.Picture;

import java.util.List;

public interface PictureService {
	
	/**
	 * ��ȡ��ҳ10����ǩ
	 * @param pictureTypeTenTab
	 * @return
	 */
	List<Picture> getTenTab(String pictureTypeTenTab);

}
