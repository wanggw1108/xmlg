package com.temporary.center.ls_service.dao;

import com.temporary.center.ls_service.domain.Order;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.BaseMapper;

/**
 * @author wangguowei
 * @description
 * @create 2019-07-30-14:02
 */
@Component
public interface OrderMapper extends BaseMapper<Order> {
}
