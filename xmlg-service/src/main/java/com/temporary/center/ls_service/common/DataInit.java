package com.temporary.center.ls_service.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 初始化一部分数据  用户
 * Created by Administrator on 2018/7/4.
 */
@Component
public class DataInit implements ApplicationRunner {
    @Value("${fileBasePath}")
    String fileBasePath;
    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        File f = new File(fileBasePath);
        if(!f.exists()){
            f.mkdirs();
        }
    }
}
