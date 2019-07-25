package com.temporary.center.ls_service.dao;

import com.temporary.center.ls_service.domain.CurriculumVitae;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface CurriculumVitaeMapper extends Mapper<CurriculumVitae> {

    public CurriculumVitae selectByCreateBy(@Param("create_by") int create_by);
    public void updateById(CurriculumVitae vitea);
    public Map<String,Object> searchByParams(@Param("cityName") String cityName,@Param("areaName") String areaName,@Param("ageMin") Integer ageMin,@Param("ageMax")Integer ageMix,@Param("sex") String sex,@Param("expectPosition") String expectPosition,@Param("employeeReputation")String employeeReputation);

}