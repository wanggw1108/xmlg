package com.temporary.center.ls_service.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class Dictionary {

    public  static Map<Integer,String> jobPosition = new HashMap<Integer,String>();//岗位类型
    public  static Map<Integer,String> settlementCycle = new HashMap<Integer,String>();//结算周期
    public  static Map<Integer,String> wagesUnit = new HashMap<Integer,String>();//工资单位
    public  static Map<Integer,String> workType = new HashMap<Integer,String>();//工作种类
    public  static Map<Integer,String> SEX = new HashMap<Integer,String>();//性别
    public  static Map<Integer,String> welfare = new HashMap<Integer,String>();//福利

    /**
     * 工作工种
     * @return
     */
    static{
        jobPosition.put(5,"其他");
        jobPosition.put(6,"校园活动");
        jobPosition.put(7,"临时工");
        jobPosition.put(8,"服务员");
        jobPosition.put(9,"实习");
        jobPosition.put(10,"调研");
        jobPosition.put(11,"送餐员");
        jobPosition.put(12,"导购");
        jobPosition.put(13,"网络");
        jobPosition.put(14,"普工");
        jobPosition.put(15,"设计");
        jobPosition.put(16,"文员助理");
        jobPosition.put(17,"派单");
        jobPosition.put(18,"销售");
        jobPosition.put(19,"安保");
        jobPosition.put(20,"礼仪");
        jobPosition.put(21,"促销");
        jobPosition.put(22,"翻译");
        jobPosition.put(23,"客服");
        jobPosition.put(24,"演出");
        jobPosition.put(25,"家教");
        jobPosition.put(26,"模特");
        jobPosition.put(27,"快递");
        jobPosition.put(28,"家政");
    }

    /**
     * 结算方式
     * @return
     */
    static{
        settlementCycle.put(100,"完工结");
        settlementCycle.put(101,"日结");
        settlementCycle.put(102,"周结");
        settlementCycle.put(103,"月结");
    }

    /**
     * 工资单位
     * @return
     */
    static{
        wagesUnit.put(200,"元/小时");
        wagesUnit.put(201,"元/天");
        wagesUnit.put(202,"元/月");
        wagesUnit.put(203,"元/次");
        wagesUnit.put(204,"元/单");
    }

    
    
    
    /**
     * 转换成每小时的薪资
     * @param unit 200/201/202/203/204 
     * @param number 100元
     * @return
     */
    public static Float switchUnit(String unit,Float number) {
    	switch (unit) {
		case "元/小时":
			//元/小时
			return number;
		case "元/天":
			//元/天
			return divide(number.toString(),"24");
		case "元/月":
			//元/月  按30天计算
			return divide(number.toString(),"720");
		case "元/次":
			//元/次  一次按8小时
			return divide(number.toString(),"8");
		case "元/单":
			//元/单  一单按8小时
			return divide(number.toString(),"8");
		default:
			break;
		}
    	return 0f;
    }
    
    /**
     * bi1string 除以 bi2string2  保留2位小数点
     * @param bi1string
     * @param bi2string2
     * @return
     */
    private static Float divide(String bi1string, String bi2string2) {
    	BigDecimal bi1 = new BigDecimal(bi1string); 
		BigDecimal bi2 = new BigDecimal(bi2string2);
		BigDecimal divide = bi1.divide(bi2, 2, RoundingMode.HALF_UP);
		return divide.floatValue();
	}

	/**
     * 工作类型
     * @return
     */
    static{
        workType.put(300,"周末");
        workType.put(301,"校园");
        workType.put(302,"直聘");
        workType.put(303,"临工");
    };

    /**
     * 性别
     * @return
     */
    static{
        SEX.put(500,"不限");
        SEX.put(501,"男");
        SEX.put(502,"女");
    }

    static{
        welfare.put(600,"保险");
        welfare.put(601,"包吃");
        welfare.put(602,"包住");
        welfare.put(603,"交通补贴");
        welfare.put(604,"提成");
        welfare.put(605,"免费培训");
    }
	
}