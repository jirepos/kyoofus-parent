package com.jirepo.core.util;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


/**
 * 음력 계산을 위한 빈이다. 
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LunarDateBean {

    /** 음력과 대응되는 양력날짜 */
    private LocalDate solarDate;
    /** 양력과 대응되는 음력 날짜 */
    private LocalDate lunarDate; 
    
    /**
     * LunarDateBean 인스턴스를 생성한다.
     * @param solarDate 양력 날짜
     * @param lunarDate 음력 날짜
     * @return   기준일
     */
    public static LunarDateBean of(LocalDate solarDate, LocalDate lunarDate) {
        LunarDateBean bean = new LunarDateBean();
        bean.setSolarDate(solarDate);
        bean.setLunarDate(lunarDate);
        return bean;
    }//
}///~
