package com.temporary.center.ls_common;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {
	
	/**
	 * Timestamp 类型转字符串yyyy-MM-dd
	 * @param tt Timestamp
	 * @return yyyy-MM-dd
	 */
	public static String timestamp2Str(Timestamp tt){
		DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");	
		return format2.format(tt);
	}
	
	/**
	 * Date类型的转字符串类型
	 * @param d  
	 * @return  yyyy-MM-dd
	 */
	public static String Date2String(Date d){
		DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");	
		if(d==null)return "";
		return format2.format(d);
	}
	
	public static String getYYMMDD(){
		Calendar   cal=Calendar.getInstance();
		//int year=cal.get(Calendar.YEAR);
		//String 
		DateFormat format2 = new SimpleDateFormat("yyMMdd");	
		return format2.format(cal.getTime());
	}
	
	public static Date parase(String dateStr,String formatStr) throws ParseException {
		DateFormat df = new SimpleDateFormat(formatStr);
		Date dt1 = df.parse(dateStr);
		return dt1;
	}
	
	public static int compare_date(String DATE1, String DATE2) throws ParseException{
		 DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 Date dt1 = df.parse(DATE1);
         Date dt2 = df.parse(DATE2);
         if (dt1.getTime() > dt2.getTime()) {
        	 //System.out.println("大");
        	 return 1;
         }else if(dt1.getTime() < dt2.getTime()){
        	 //System.out.println("小");
        	 return -1;
         }else{
        	 //System.out.println("相等");
        	 return 0;
         } 
	}
	
	/**
	 * yyyy/MM/dd
	 * @return yyyy/MM/dd
	 */
	public static String getYYMMDD2(){
		Calendar   cal=Calendar.getInstance();
		DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");	
		return format2.format(cal.getTime());
	}
	
	public static Timestamp Calendar2Timestamp(Calendar cal){
		return new Timestamp(cal.getTimeInMillis());
	}
	
	
	public static String getYYYYMMDD(){
		Calendar   cal=Calendar.getInstance();
		DateFormat format2 = new SimpleDateFormat("yyyyMMdd");	
		return format2.format(cal.getTime());
	}
	
	
	/**
	 * Date类型的转字符串类型
	 * @param d
	 * @return Date2DTstring yyyy-MM-dd HH:mm:ss
	 */
	public static String Date2DTstring(Date d){
		//DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(d==null)return "";
			return format2.format(d);
	}
	
	/**
	 * 获得当前的日期字符串
	 * @return yyyyMMddHHmmss 20140717142649 
	 */
	public static String getyyyyMMddHHmmss(){
		Date d=getNowTimestamp();
		DateFormat format2 = new SimpleDateFormat("yyyyMMddHHmmss");
		if(d==null)return "";
		return format2.format(d);
	}
	
	/**
	 * 获得当前的时间  （Timestamp形式的）
	 * @return
	 */
	public static Timestamp getNowTimestamp(){
		Calendar  c=Calendar.getInstance();
		c.setTime(new Date());
		Timestamp   t=new Timestamp(c.getTimeInMillis());
		return t;
	}
	
	/**
	 * yyyy-MM-dd HH:mm:ss 格式的字符串 格式化成 Timestamp
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Timestamp str2Timestamp(String str) throws ParseException{
		 DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 Date d=format2.parse(str);
		 Timestamp t=new Timestamp(d.getTime());
		 return t;
	}
	
	/**
	 * yyyy-MM-dd HH:mm:ss 格式的字符串格式化成 Date
	 * @param str
	 * @return 
	 * @throws ParseException
	 */
	public static Date str2date(String str) throws ParseException{
		 DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		 Date d=format2.parse(str);
		 return d;
	}
	
	
	/**
	 *获得当前的时间（Date形式）
	 * @return
	 */
	public static Date  getNowDate(){
		Calendar  c=Calendar.getInstance();
		c.setTime(new Date());
		return c.getTime();
	}
	public static void main(String[] args) {
		try {
			int b=compare_date("2016-07-12 10:06:58", "2016-07-12 10:06:58");
			System.out.println(b);
		} catch (ParseException e) {
			e.printStackTrace();
		}
			
		/*
		 System.out.println(getYYMMDD2());
		 String date="2016-03-09T00:00:00+08:00";
		 DateFormat format2 = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss");
		 try {
			format2.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Date d=new Date(date);
		 //tdate3();
		 String d=format2.format(date);
		 System.out.println(d);
		  String tzId = TimeZone.getDefault().getID();
		  TimeZone tz = TimeZone.getTimeZone(tzId);
		  Date currentTime = new Date();
		   String time = "2015-04-09T15:09:46";
		   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss");
		   formatter.setTimeZone(tz);
		   String s= formatter.parse(time);
		  
		 SimpleDateFormat result_form = new SimpleDateFormat("yyyy MM dd HH mm ss");
		String ss= result_form.parse(s);
	*/}
	public static void tdate3(){
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date ftime = null;
		try {
			//ftime = sdf.parse("Thu, 14 Jun 2012 07:17:21 GMT");
			ftime = sdf.parse("2016-03-09T00:00:00+08:00");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//sdf2.setTimeZone(TimeZone.getDefault());
		System.out.println(sdf.format(ftime));
		System.out.println(sdf2.format(ftime));
	}
	
	/**
	 * @param interval 间隔 比如10*60*1000=十分钟
	 * @return
	 */
	public static String before(long interval){
		Date now = new Date();
		Date now_10 = new Date(now.getTime() - interval); //10分钟前的时间
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//可以方便地修改日期格式
		String nowTime_10 = dateFormat.format(now_10);
		return nowTime_10;
	}
	
	public static String beforeDate(long curreLong,long interval) {
		Date now_10 = new Date(curreLong - interval); //10分钟前的时间
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//可以方便地修改日期格式
		String nowTime_10 = dateFormat.format(now_10);
		return nowTime_10;
	}
	/**
	 * 获得日期加设定时间
	 * @param s 日期 2015-05-05
	 * @param suffix 后缀 00:00:00 或者 23:59:59
	 * @return 2015-05-05 00:00:00
	 */
	public static Timestamp getDateAddSuffix(Timestamp s,String suffix)throws SQLException{
		Timestamp s2=null;
			if(s!=null){
				String s_str= DateUtil.timestamp2Str(s);
				try {
					 s2= DateUtil.str2Timestamp(s_str+" "+suffix);
				} catch (ParseException e) {
					new SQLException("日期格式不正确："+e.getMessage());
				} 
			}
		return s2; 
	}
	
    public  static int getAgeByBirth(Date birthday) {
        int age = 0;
        try {
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());// 当前时间

            Calendar birth = Calendar.getInstance();
            birth.setTime(birthday);

            if (birth.after(now)) {//如果传入的时间，在当前时间的后面，返回0岁
                age = 0;
            } else {
                age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
                if (now.get(Calendar.DAY_OF_YEAR) > birth.get(Calendar.DAY_OF_YEAR)) {
                    age += 1;
                }
            }
            return age;
        } catch (Exception e) {//兼容性更强,异常后返回数据
           return 0;
        }
   }
	
	
}
