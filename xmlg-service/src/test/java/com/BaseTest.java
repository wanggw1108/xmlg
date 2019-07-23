package com;

import app.MyApplication;
import com.github.pagehelper.PageHelper;
import com.temporary.center.ls_common.MD5Utils;
import com.temporary.center.ls_service.dao.*;
import com.temporary.center.ls_service.domain.*;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangguowei
 * @description
 * @create 2019-07-12-10:50
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseTest {

    @Autowired
    CarouselPictureMapper dao;
    @Autowired
    TypeWorkMapper typeWorkMapper;
    @Autowired
    CurriculumVitaeMapper curriculumVitaeMapper;
    @Autowired
    ProjectExperienceMapper projectExperienceMapper;

    @Autowired
    private RecruitmentInfoMapper recruitmentInfoMapper;
    @Autowired
    private UserDao userService;

    @Test
    public void dbTest(){

//        CarouselPicture user = new CarouselPicture();
//        user.setSort(5);
//        dao.insert(user);
//        System.out.println(user.getId());
//        PageHelper.startPage(4,1);//分页
//        Example example = new Example(CarouselPicture.class); //定义对象CarouselPicture
//        String by="sort";
//        example.setOrderByClause(by);
//        Example.Criteria criteria = example.createCriteria();
//        criteria.andEqualTo("createBy",1);
//        List<CarouselPicture> pictureList = dao.selectByExample(example);
//
//        System.out.println(pictureList.size());
        Map<String,Object> queryUser = new HashedMap();
        queryUser.put("phone","17318035749");
        List<User> userList = userService.queryUserByParams(queryUser);
        System.out.println(userList);

    }

}
