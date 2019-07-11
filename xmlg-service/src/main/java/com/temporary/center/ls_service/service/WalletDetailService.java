package com.temporary.center.ls_service.service;

import com.temporary.center.ls_service.domain.WalletDetail;

import java.util.List;

public interface WalletDetailService {

	List<WalletDetail> list(WalletDetail walletDetail);

	void add(WalletDetail walletDetail);

	Long count(WalletDetail walletDetail);

	Double currAmount(WalletDetail walletDetail);

}
