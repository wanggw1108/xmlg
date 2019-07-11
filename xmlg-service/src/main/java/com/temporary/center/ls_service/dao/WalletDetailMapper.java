package com.temporary.center.ls_service.dao;

import com.temporary.center.ls_service.domain.WalletDetail;

import java.util.List;

public interface WalletDetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(WalletDetail record);

    int insertSelective(WalletDetail record);

    WalletDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WalletDetail record);

    int updateByPrimaryKey(WalletDetail record);

	List<WalletDetail> list(WalletDetail businessLicense);

	Long count(WalletDetail walletDetail);

	Double currAmount(WalletDetail walletDetail);
}