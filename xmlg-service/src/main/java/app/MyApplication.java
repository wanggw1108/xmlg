package app;

import com.github.pagehelper.PageHelper;
import com.temporary.center.ls_common.SpringContextUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;

import java.util.Properties;

/**
 * @author wangguowei
 * @description
 * @create 2019-07-09-12:22
 */
@ComponentScan("com.temporary")
@MapperScan("com.temporary.center.ls_service.dao")
@SpringBootApplication(exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class })
public class MyApplication {

   public static void main(String[] args){
       ConfigurableApplicationContext context = SpringApplication.run(MyApplication.class, args);
       SpringContextUtils.setContext(context);
   }
    @Bean(name = "pageHelper")
    @Primary
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties props = new Properties();
        props.setProperty("reasonable", "true");
        props.setProperty("supportMethodsArguments", "true");
        props.setProperty("returnPageInfo", "check");
        props.setProperty("params", "count=countSql");
        pageHelper.setProperties(props);
        return pageHelper;
    }

}
