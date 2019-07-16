package com.temporary.center.ls_service.service.impl;

import com.temporary.center.ls_service.dao.WalletDetailMapper;
import com.temporary.center.ls_service.domain.WalletDetail;
import com.temporary.center.ls_service.service.WalletDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletDetailServiceImpl implements WalletDetailService {

	@Autowired
	private WalletDetailMapper dao;
	
	@Override
	public List<WalletDetail> list(WalletDetail businessLicense) {
		return null;
	}

	@Override
	public void add(WalletDetail businessLicense) {
		dao.insertSelective(businessLicense);
	}

	@Override
	public Long count(WalletDetail walletDetail) {
		return null;
	}

	@Override
	public Double currAmount(WalletDetail walletDetail) {
		return null;
	}
	
	
	
}
