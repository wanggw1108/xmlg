package com.temporary.center.ls_service.controller;

import com.github.pagehelper.PageHelper;
import com.temporary.center.ls_common.DateUtil;
import com.temporary.center.ls_common.RedisBean;
import com.temporary.center.ls_common.RedisKey;
import com.temporary.center.ls_service.common.Json;
import com.temporary.center.ls_service.common.PageData;
import com.temporary.center.ls_service.common.StatusCode;
import com.temporary.center.ls_service.dao.JoinMapper;
import com.temporary.center.ls_service.dao.OrderMapper;
import com.temporary.center.ls_service.dao.RecruitmentInfoMapper;
import com.temporary.center.ls_service.domain.Join;
import com.temporary.center.ls_service.domain.Order;
import com.temporary.center.ls_service.domain.RecruitmentInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.util.StringUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wangguowei
 * @description
 * @create 2019-07-30-10:18
 */
@Controller
@RequestMapping(value = "/order")
public class OrderController {

    private Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    RedisBean redisService;

    @Autowired
    JoinMapper joinService;
    @Autowired
    RecruitmentInfoMapper recruitmentService;
    @Autowired
    OrderMapper orderService;
    @RequestMapping(value="/create", method = RequestMethod.GET)
    @ResponseBody
    public Json createOrder(String token,Integer recruitId,Integer employeeId){

        Json json = new Json();
        if(!redisService.exists(RedisKey.USER_TOKEN+token)){
            json.setSattusCode(StatusCode.TOKEN_ERROR);
            return json;
        }
        int user_id = Integer.valueOf(redisService.hget(RedisKey.USER_TOKEN+token,"user_id"));
        try{
            Join join = new Join();
            join.setRecruitmentInfoId(recruitId);
            join.setUserId(employeeId);
            join = joinService.selectOne(join);
            if(null ==join){
                json.setSattusCode(StatusCode.DATA_ERROR);
                json.setMsg("雇员未报名该职位!");
                return json;
            }

            RecruitmentInfo recruit = recruitmentService.selectByPrimaryKey(Long.valueOf(recruitId));
            if(recruit==null){
                json.setSattusCode(StatusCode.DATA_ERROR);
                json.setMsg("没有该职位!");
                return json;
            }
            Order order = new Order();
            String startDate = join.getStartDate();
            String endDate = join.getEndDate();
            int day = DateUtil.longOfTwoDate(DateUtil.str2date(startDate),DateUtil.str2date(endDate));
            order.setConfirmStartDate(startDate);
            order.setConfirmEndDate(endDate);
            order.setStartDate(recruit.getWorkingStartTime());
            order.setEndDate(recruit.getWorkingEndTime());
            order.setEmployeeId(employeeId);
            order.setOrderState(1);//待付款状态
            order.setRecruitId(recruitId);
            order.setUserId(user_id);
            order.setRecruitTitle(recruit.getTitle());
            order.setAmount(recruit.getBasePay()*day);
            order.setTotalDay(day);
            order.setBasePay(recruit.getBasePay()+recruit.getBasePayUnit());
            order.setCreateTime(new Date());
            String workingTime = recruit.getWorkingTime();
            if(!StringUtil.isEmpty(workingTime) && workingTime.contains("-")){
                String[] ss = workingTime.split(",");
                StringBuilder starts = new StringBuilder();
                StringBuilder ends = new StringBuilder();
                int hours  = 0;
                for(String s:ss){
                    String[] time = s.split("-");
                    String start = time[0];
                    String end = time[1];
                    int startHour = Integer.valueOf(start.split(":")[0]);
                    int endHour = Integer.valueOf(end.split(":")[0]);
                    hours += endHour - startHour;
                    starts.append(start).append(",");
                    ends.append(end).append(",");
                }
                String starttime = starts.toString();
                String endtime = ends.toString();
                if(ss!=null && ss.length>0){
                    starttime = starttime.substring(0,starttime.length()-1);
                    endtime = endtime.substring(0,starttime.length()-1);
                }
                order.setStartTime(starttime);
                order.setEndTime(endtime);
                order.setTotalTime(hours);
                order.setEmployeeName(join.getUserName());
                StringBuilder orderId = new StringBuilder();
                orderId.append(DateUtil.getyyyyMMddHHmmss())
                        .append((100000000+recruitId)+"")
                        .append((100000000+employeeId)+"");
                order.setOrderNo(orderId.toString());
                orderService.insert(order);
                json.setData(order);
                json.setSuc();
                return json;
            }





        }catch (Exception e){
            json.setSattusCode(StatusCode.Create_Order_Error);
            return json;
        }



        return null;

    }
    @RequestMapping(value="/list.do", method = RequestMethod.GET)
    @ResponseBody
    public Json list(String token,Integer status,Integer crr,Integer pageSize){

        Json json = new Json();
        if(!redisService.exists(RedisKey.USER_TOKEN+token)){
            json.setSattusCode(StatusCode.TOKEN_ERROR);
            return json;
        }
        try {
            int user_id = Integer.valueOf(redisService.hget(RedisKey.USER_TOKEN+token,"user_id"));
            PageHelper.startPage(crr,pageSize);
            Order order = new Order();
            order.setOrderState(status);
            order.setUserId(user_id);
            int cnt = orderService.selectCount(order);
            List<Order> list = new ArrayList<>();
            if(cnt>0){
                list =   orderService.select(order);
            }
            PageData pageData = new PageData(list,cnt,crr,pageSize);
            json.setData(pageData);
            json.setSuc();
            return json;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            json.setSattusCode(StatusCode.FAIL);
            return json;
        }
    }

    @RequestMapping(value="/payCallBack", method = RequestMethod.GET)
    @ResponseBody
    public void payCallBack(String token,String orderId){






    }


}
