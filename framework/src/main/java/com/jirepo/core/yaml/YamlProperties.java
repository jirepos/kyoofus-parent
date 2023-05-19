package com.jirepo.core.yaml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.yaml.snakeyaml.Yaml;



public class YamlProperties {


      /**  여기에 프러퍼티 형식의 키를 사용하여 프러퍼티를 저장한다.  */
      private Map<String, Object> finalMap = new HashMap<String, Object>();

      /**
       * 프퍼퍼티 값을 반환한다.  반환값은 Stirng, Boolean, ArrayList, Integer와 같이 
       * 설정한 값에 따라 다르다. 값을 꺼내서 값을 캐스팅하거나 문자열로 변환하여 사용한다. 
       * @param key 프러퍼티 키
       * @return  프러퍼티 값
       */
      public Object getProperty(String key) {
          return finalMap.get(key);
      }
  
      /**
       * 프퍼퍼티 값을 반환한다.  반환값은 Stirng, Boolean, ArrayList, Integer와 같이 
       * 설정한 값에 따라 다르다. 값을 꺼내서 값을 캐스팅하거나 문자열로 변환하여 사용한다. 
       * @param key 프러퍼티 키
       * @param defaultValue  값이 없는 경우의 디폴트 값 
       * @return  프러퍼티 값
       */
      public Object getProperty(String key, Object defaultValue)  {
          return finalMap.getOrDefault(key, defaultValue);
      }
      
      
      
      /**
       * 
       * @param path 클래스 패스에 있는 파일. 경로를 포함한다. 예) aaa/bb/cc.yaml
       * @param profiles 프로파일 목록. 예) dev, prod
       * @return  이 큺래스의 인스턴스를 반환한다.
       */
      public static YamlProperties createInstance(String path, String... profiles) {
          try {
              YamlProperties yamlPropertiesImport = new YamlProperties();
              yamlPropertiesImport.load(path, profiles);
              return yamlPropertiesImport;
          } catch (Exception e) {
              throw new RuntimeException("yaml 파일을 읽는데 실패하였습니다.", e);
          }
      }
  

      /**
       * YMAML 파일을 로드한다. 
       * @param path 클래스 패스에 있는 파일. 경로를 포함한다. 예) aaa/bb/cc.yaml
       * @param profiles 프로파일 목록. 예) dev, prod
       * @return  이 큺래스의 인스턴스를 반환한다.
       */
      private void load( String path, String... profiles) throws Exception {
        load(finalMap, path, profiles);
        loadFileToImport(finalMap); 
      }



      /**
       * YMAML 파일을 로드한다. 
       * @param path 클래스 패스에 있는 파일. 경로를 포함한다. 예) aaa/bb/cc.yaml
       * @param profiles 프로파일 목록. 예) dev, prod
       * @return  이 큺래스의 인스턴스를 반환한다.
       */
      private void load(Map<String, Object> target,  String path, String... profiles) throws Exception {
          Yaml yaml = new Yaml();
          List<Object> propList = new ArrayList<Object>();
          for (Object object : yaml.loadAll(new ClassPathResource(path).getInputStream())) {
              propList.add(object);
          }// for 
          
          @SuppressWarnings("unchecked") 
          Map<String, Object> firstMap = (Map<String, Object>)propList.get(0);   // ROOT 프러퍼티 
          Iterator<String> keys = firstMap.keySet().iterator();
          while(keys.hasNext()){
              String key = keys.next();
              Object value = firstMap.get(key);
              if(value instanceof Map<?, ?>) {
                  putChildren(target, key, value);
              }else { 
                target.put(key, value);
              }
          }
  
          // 프로파일별 속성을 담는다. 
          List<Map<String, Object>> profileMapList = new ArrayList<Map<String,Object>>(); 
  
          for(int i=1; i < propList.size(); i++) {
              Map<String, Object> trgtMap = new HashMap<String, Object>();
              @SuppressWarnings("unchecked") 
              Map<String, Object> srcMap = (Map<String, Object>)propList.get(i);   // profile 프러퍼티 
              Iterator<String> profileKeys = srcMap.keySet().iterator();
              while(profileKeys.hasNext()){
                  String key = profileKeys.next();
                  // System.out.println(key);
                  Object value = srcMap.get(key);
                  // System.out.println(value);
                  if(value instanceof Map<?, ?>) {
                      //System.out.println(value.getClass().toString());
                      putChildren(trgtMap, key, value);
                  }else { 
                      trgtMap.put(key, value);
                  }
              }
              profileMapList.add(trgtMap);
          }

          setProfiles(  target, profileMapList, profiles);
          
      }//:

      private void setProfiles(Map<String, Object> target, List<Map<String, Object>> profileMapList, String... profiles ){ 
        for(String profileName: profiles) {
            Map<String, Object> profileMap = findProfile(profileName, profileMapList);
            // System.out.println(profileMap);
            if(profileMap != null) {
              target.putAll(profileMap);
            }
        }
      }
  

      private void loadFileToImport(Map<String, Object> target) {
        @SuppressWarnings("unchecked")
        List<String> importArr = (List<String>)target.get("spring.config.import"); 
        if(importArr != null) {
          for(String importPath: importArr) {
            System.out.println("importPath: " + importPath);
            String fileName = importPath.substring(importPath.indexOf(":") + 1);
            try {
              load(target, fileName);
            } catch (Exception e) {
              throw new RuntimeException("yaml 파일을 읽는데 실패하였습니다.", e);
            }
          }
        }
      }
  
      
      /**
       * 값이 Map인 경우에 하위 키를 찾아서 최종 맵에 저장한다.
       * @param propMap 프러퍼티를 저장할 맵 
       * @param key 키
       * @param mapTypeValue Map 형태의 값 
       */
      private void putChildren(Map<String, Object> propMap, String key, Object mapTypeValue) {
          @SuppressWarnings("unchecked")
          Map<String, Object> map = (Map<String, Object>)mapTypeValue;
          Iterator<String> keys = map.keySet().iterator();
          while(keys.hasNext()){
              String subKey = keys.next();
              String newKey = key + "." + subKey;
              Object value = map.get(subKey);
              if(value instanceof Map<?, ?>) {
                  putChildren(propMap, newKey , value);
              }else { 
                  propMap.put(newKey, value);
              }
          }// while 
      }//:
  
  
  
      /**
       * 프로파일에 해당하는 맵 반환한다. 
       * @param profileName 프로파일명. 예) local
       * @param profileMapList 프로파일 맵 리스트. 
       * @return  프로파일에 해당하는 맵. 없으면 null 
       */
      private Map<String, Object> findProfile(String profileName, List<Map<String, Object>> profileMapList) {
          for(Map<String, Object> profileMap : profileMapList) {
              // System.out.println(profileMap);
              if(profileMap.containsKey("spring.config.activate.on-profile")) {
                  String profiles = (String)profileMap.get("spring.config.activate.on-profile");
                  if(profiles.equals(profileName)) {
                      return profileMap;
                  }
              }
          }
          return null;
      }
    
}

