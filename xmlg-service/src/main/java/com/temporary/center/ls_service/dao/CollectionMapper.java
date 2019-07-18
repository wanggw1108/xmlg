package com.temporary.center.ls_service.dao;

import com.temporary.center.ls_service.domain.MyCollection;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
@Repository
public interface CollectionMapper extends Mapper<MyCollection> {
}