package com.temporary.center.ls_service.dao;

import com.temporary.center.ls_service.domain.RecruitmentInfo;

import java.util.List;

public interface RecruitmentInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RecruitmentInfo record);

    int insertSelective(RecruitmentInfo record);

    RecruitmentInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RecruitmentInfo record);

    int updateByPrimaryKey(RecruitmentInfo record);

	List<RecruitmentInfo> list(RecruitmentInfo recruitmentInfo);

	Long countByParams(RecruitmentInfo recruitmentInfo);

	List<RecruitmentInfo> listIndex(RecruitmentInfo recruitmentInfo);
}