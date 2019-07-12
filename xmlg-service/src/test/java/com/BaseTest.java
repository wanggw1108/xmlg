package com;

import app.MyApplication;
import com.temporary.center.ls_common.MD5Utils;
import com.temporary.center.ls_service.dao.UserDao;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
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
    UserDao dao;
    @Test
    public void dbTest(){

        Map<String,Object> update = new HashedMap();
        update.put("id",1);
        update.put("password", MD5Utils.getMD5("123QWE123"));
        update.put("updateTime",new Date());
        dao.updatePassword(update);

    }

}
