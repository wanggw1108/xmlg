package com.temporary.center.ls_service.controller;

import com.alibaba.fastjson.JSONObject;
import com.temporary.center.ls_common.RedisBean;
import com.temporary.center.ls_common.RedisKey;
import com.temporary.center.ls_service.common.Json;
import com.temporary.center.ls_service.common.StatusCode;
import com.temporary.center.ls_service.dao.CurriculumVitaeMapper;
import com.temporary.center.ls_service.dao.InvitationMapper;
import com.temporary.center.ls_service.dao.RecruitmentInfoMapper;
import com.temporary.center.ls_service.domain.CurriculumVitae;
import com.temporary.center.ls_service.domain.Invitation;
import com.temporary.center.ls_service.domain.InvitationMessage;
import com.temporary.center.ls_service.domain.RecruitmentInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

/**
 * @author wangguowei
 * @description
 * @create 2019-07-24-14:00
 */
@Controller
@RequestMapping(value = "/invitation")
public class InvitationController {

    private Logger logger = LoggerFactory.getLogger(InvitationController.class);
    @Autowired
    InvitationMapper invitationMapper;
    @Autowired
    RecruitmentInfoMapper recruitmentInfoMapper;
    @Autowired
    CurriculumVitaeMapper curriculumVitaeMapper;
    @Autowired
    RedisBean redisBean;

    @RequestMapping(value = "/add.do", method = RequestMethod.POST)
    @ResponseBody
    public Json add(String token, Integer recruitment_id,Integer invited_curriculum_id, String msg){

        logger.info("添加邀请token={},recruitment_id={}",token,recruitment_id);
        Json json = new Json();
        if(!redisBean.exists(RedisKey.USER_TOKEN+token)){
            json.setSattusCode(StatusCode.TOKEN_ERROR);
            return json;
        }
        try {
            Invitation invitation = new Invitation();
            String user_id = redisBean.hget(RedisKey.USER_TOKEN+token,"user_id");
            invitation.setCreate_by(Integer.valueOf(user_id));
            invitation.setRecruitment_id(recruitment_id);
            invitation.setCreate_time(new Date());
            invitation.setInvited_curriculum_id(invited_curriculum_id);
            CurriculumVitae vitae = curriculumVitaeMapper.selectByPrimaryKey(invited_curriculum_id);
            if(vitae==null){
                logger.info("invited_curriculum_id：{}无简历");
                json.setSattusCode(StatusCode.DATA_ERROR);
                return json;
            }
            invitation.setInvited_user_id(vitae.getCreate_by());
            RecruitmentInfo info = recruitmentInfoMapper.selectByPrimaryKey(Long.valueOf(recruitment_id+""));
            if(info==null){
                logger.info("recruitment_id：{}无职位");
                json.setSattusCode(StatusCode.DATA_ERROR);
                return json;
            }
            invitationMapper.insert(invitation);
            InvitationMessage message = new InvitationMessage();
            message.setBase_pay(info.getBasePay());
            message.setId(invitation.getId());
            message.setBase_pay_unit(info.getBasePayUnit());
            message.setRecruitment_id(recruitment_id);
            message.setTitle(info.getTitle());
            message.setType_work(info.getTypeWork());
            message.setWork_place(info.getWorkPlace());
            message.setMsg(msg);
            redisBean.lpush(RedisKey.USER_MSG+vitae.getCreate_by(), JSONObject.toJSONString(message));
            json.setSuc();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            json.setSattusCode(StatusCode.DATA_ERROR);
        }

        return json;

    }
    @RequestMapping(value = "/getInvitation.do", method = RequestMethod.GET)
    @ResponseBody
    public Json getInvitation(String token){

        logger.info("获取邀请信息token={}",token);
        Json json = new Json();
        if(!redisBean.exists(RedisKey.USER_TOKEN+token)){
            json.setSattusCode(StatusCode.TOKEN_ERROR);
            return json;
        }
        try {
            String user_id = redisBean.hget(RedisKey.USER_TOKEN+token,"user_id");
            String msg = redisBean.lpop(RedisKey.USER_MSG+user_id);
            json.setData(msg==null?null:JSONObject.parseObject(msg,InvitationMessage.class));
            json.setSuc();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            json.setSattusCode(StatusCode.DATA_ERROR);
        }

        return json;

    }
}
