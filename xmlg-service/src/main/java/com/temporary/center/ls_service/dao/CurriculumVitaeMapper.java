package com.temporary.center.ls_service.dao;

import com.temporary.center.ls_service.domain.CurriculumVitae;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
@Repository
public interface CurriculumVitaeMapper extends Mapper<CurriculumVitae> {

    public CurriculumVitae selectByCreateBy(@Param("create_by") int create_by);
    public void updateById(CurriculumVitae vitea);

}