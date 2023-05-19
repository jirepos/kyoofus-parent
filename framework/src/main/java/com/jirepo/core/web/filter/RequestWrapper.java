package com.jirepo.core.web.filter;

import java.lang.reflect.Array;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import lombok.extern.slf4j.Slf4j;

/**
 * ServletRequest를 대체하기 위한 래퍼 클래스이다. 
 */
@Slf4j
public class RequestWrapper extends HttpServletRequestWrapper {
    
    HttpServletRequest request;
    private HashMap<String,Object>params;

    public RequestWrapper (HttpServletRequest request) {
        super(request);
        // body가 json으로 전달되는 것은 request.getParameter()로 꺼낼 수 없음 
        // 파라미터를 조작하려면 request 참조를 여기서 설정 
        // super의 request는 private이어여 접근이 불가능함 
        this.request = request; 
        // ServeltRequest는 setParameter()를 지원하지 않아
        // map을 사용하여 ServletReqeust를 대체해야 함
        this.params = new HashMap<>(request.getParameterMap()); // map에 파라미터를 담음
        this.resetParameters();
    }
    
    private void resetParameters(){ 
        String q = this.request.getParameter("q"); // q 파라미터를 디코딩하여 파라미터를 만들기 위해 꺼낸다. 
        if(q == null) return; 
        String[] urlAndParams = RequestWrapperUtils.splitUrlAndParams(q);
        String decodedParams = urlAndParams[1];
        
        // System.out.println(decodedParams);
        String[] params = decodedParams.split("&");
        for(String param : params){
            String[] keyValue = param.split("=");
            // System.out.println("params=" + keyValue[0]);
            if(this.params.containsKey(keyValue[0])){
                Object existValue = this.params.get(keyValue[0]);
                if(existValue instanceof String[]) { //배열이면
                    String[] values = (String[])existValue;
                    int i=0;
                    String[] newValues = (String[])Array.newInstance(String.class, values.length+1);  // 배열 크기 하나 늘린다. 
                    for(String exval : values) {
                        newValues[i++] = exval;
                    }
                    newValues[i] = keyValue[1];
                    this.params.put(keyValue[0], newValues); // 배열 추가
                } else { // 배열이 아니면 
                    String[] newValues = new String[2];
                    newValues[0] = (String)existValue;
                    newValues[1] = keyValue[1];
                    this.params.put(keyValue[0], newValues); // 배열 추가
                }
            }else {
                this.params.put(keyValue[0], keyValue[1]);
            }
        }
    }//:

    /**
     * getParameter() 래핑 메서드이다. 
     */
    @Override
    public String getParameter(String name) {
        // super에서 꺼내서 가공하던가 
        // 아니면 this.request.getParameter()로 꺼내서 가공 한다. 
        //return super.getParameter(name);
        return (this.params != null) ? (String)this.params.get(name): null;
    }
    
    /**
     * 파라미터를 추가한다. 
     * @param name 파라미터 명
     * @param value 값 
     */
    public void setParameter(String name, String value) {
        String[] oneParam = {value};
        setParameter(name, oneParam);
    }
    /**
     * 파라미터를 추가한다.
     * @param name 파라미터 명
     * @param value 값 
     */
    public void setParameter(String name, String[] value) {
        params.put(name, value);
    }

    /**
     * getParameterValues() 래핑 메서드이다. 
     */
    @Override
    public String[] getParameterValues(String name) {
        return (String[])this.params.get(name);
    }

}///~
