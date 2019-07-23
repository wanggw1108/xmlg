package com.temporary.center.ls_service.dao;

import com.temporary.center.ls_service.domain.RecruitmentInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
@Component
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

	List<String> getEmployeeByUserId(@Param("user_id") Integer user_id);
    List<String> getServiceArea(@Param("user_id") Integer user_id);
}