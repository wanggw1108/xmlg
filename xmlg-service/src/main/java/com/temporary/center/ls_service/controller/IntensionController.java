package com.temporary.center.ls_service.controller;

import com.temporary.center.ls_common.Constant;
import com.temporary.center.ls_common.RedisBean;
import com.temporary.center.ls_common.RedisKey;
import com.temporary.center.ls_service.common.Json;
import com.temporary.center.ls_service.common.StatusCode;
import com.temporary.center.ls_service.dao.IntensionMapper;
import com.temporary.center.ls_service.domain.Intension;
import com.temporary.center.ls_service.domain.Join;
import com.temporary.center.ls_service.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.UUID;

/**
 * @author wangguowei
 * @description
 * @create 2019-07-19-14:28
 */
@Controller
@RequestMapping(value = "/intension")
public class IntensionController {

    private Logger logger = LoggerFactory.getLogger(IntensionController.class);
    @Autowired
    IntensionMapper intensionService;
    @Autowired
    RedisBean redisBean;
    /**
     * 更新求职意向
     * @param token
     * @return
     */
    @RequestMapping(value = "/update.do", method = RequestMethod.POST)
    @ResponseBody
    @Description("更新求职意向")
    public Json update(String token, Intension intension) {
        logger.info("更新求职意向"+ Constant.METHOD_BEGIN);

        Json json=new Json ();
        try {
            if(!redisBean.exists(RedisKey.USER_TOKEN+token)){
                json.setSattusCode(StatusCode.TOKEN_ERROR);
                return json;
            }
            String userId = redisBean.hget(RedisKey.USER_TOKEN+token,"user_id");
            intension.setUser_id(Integer.valueOf(userId));
            if(!userId.equals(intension.getUser_id()+"")){
                json.setSattusCode(StatusCode.DATA_ERROR);
                return json;
            }
            intension.setUpdate_time(new Date());
            intensionService.updateByPrimaryKey(intension);
            json.setSuc("求职意向更新成功");
            json.setData(intension);
            return json;
        }catch(Exception e) {
            e.printStackTrace();
            json.setSattusCode(StatusCode.PROGRAM_EXCEPTION);
            return json;
        }
    }
    @RequestMapping(value = "/query.do", method = RequestMethod.GET)
    @ResponseBody
    @Description("添加求职意向")
    public Json query(String token){
        logger.info("查询求职意向:"+token);
        Json json=new Json ();
        if(!redisBean.exists(RedisKey.USER_TOKEN+token)){
            json.setSattusCode(StatusCode.TOKEN_ERROR);
            return json;
        }

        String user_id = redisBean.hget(RedisKey.USER_TOKEN+token,"user_id");
        Intension intension = new Intension();
        intension.setUser_id(Integer.valueOf(user_id));
        intension = intensionService.selectOne(intension);
        if(intension==null){
            Intension add = new Intension();
            add.setUser_id(Integer.valueOf(user_id));
            add.setCreate_time(new Date());
            intensionService.insert(add);
            json.setData(add);
        }else {
            json.setData(intension);
        }

        json.setSuc();
        return json;
    }

}
