package com.temporary.center.ls_service.domain;

import net.sourceforge.pinyin4j.PinyinHelper;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Comparator;
@Table(name="city")
public class City implements Serializable ,Comparator<City> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String city;

    private Integer cid;

    private Integer pid;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

	@Override
	public int compare(City o1, City o2) {
		  
        char c1 = o1.getCity().charAt(0);  
        char c2 = o2.getCity().charAt(0);  
        return concatPinyinStringArray(  
                PinyinHelper.toHanyuPinyinStringArray(c1)).compareTo(  
                concatPinyinStringArray(PinyinHelper  
                        .toHanyuPinyinStringArray(c2)));  
    
	}
	private String concatPinyinStringArray(String[] pinyinArray) {  
        StringBuffer pinyinSbf = new StringBuffer();  
        if ((pinyinArray != null) && (pinyinArray.length > 0)) {  
            for (int i = 0; i < pinyinArray.length; i++) {  
                pinyinSbf.append(pinyinArray[i]);  
            }  
        }  
        return pinyinSbf.toString();  
    }
}