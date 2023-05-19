package com.jirepo.core.logging;



import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Stack;
import java.util.stream.Stream;

import com.jirepo.core.config.util.PropertyUtil;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

public class P6spyPrettySqlFormatter implements MessageFormattingStrategy {

    private static final String NEW_LINE = System.lineSeparator();
    /** StackTrace에서 출력할 패키지 이름 */
    private String packageName = null; 
    /** PreparedStatement를 출력할지 여부. 디폴터는 False */
    private Boolean showPreparedSql = Boolean.FALSE; 
  
    public P6spyPrettySqlFormatter() {
      this.packageName = PropertyUtil.getProperty("decorator.package-name", "com.sogood");
      this.showPreparedSql =  Boolean.parseBoolean(PropertyUtil.getProperty("decorator.show-prepared-sql", "false"));
    }
  
    @Override
    /**
     * @param connectionId  커넥션 아이디 
     * @param now 현재시간. 밀리세컨드로 표시 
     * @param elapsed 쿼리 수행후 경과시간 
     * @param category 오퍼레이션의 카테고리
     * @param prepared Prerpared SQL statement
     * @param sql 실행된 SQL 
     * @param url database URL 
     */
    public String formatMessage(final int connectionId, final String now, final long elapsed, final String category, final String prepared, final String sql, final String url) {
  
        StringBuilder callStackBuilder = getStackBuilder();
        StringBuilder sb = new StringBuilder()
                .append("\n\tConnection ID: ").append(connectionId)
                .append("\n\ttime ").append(now).append(", ").append("category ").append(category).append(", ")
                .append("\n\tExecution Time: ").append(elapsed).append(" ms\n")
                .append("\n\tCall Stack (number 1 is entry point): ").append(callStackBuilder)
                .append("\n\t");
        if (showPreparedSql) {
            sb.append(prepared)
                    .append("\n----------------------------------------------------------------------------------------------------")
                    .append("\n\t");
        }
        sb.append("\n").append(sql);
  
        return sb.toString();
    }//:
  
    /**
     * 
     * @return 
     */
    private StringBuilder getStackBuilder() {
      /*
      Stack Trace란? 
      응용 프로그램(Application)이 시작된 시점부터 프로그램 내에서 현재 실행 위치까지의 메서드 호출 목록
      예외가 발생했을 때까지 프로그램의 위치와 진행정도를 나타내기 위해 예외가 발생하면 JVM에 의해 자동으로 생성
      가장 최근의 메서드 호출이 목록에 맨 위에 있음
      
      StackTrace는 java.lang.StackTraceElement 클래스 배열로 캡슐화됨(JDK 1.4 ~)
       - java.lang.StackTraceElement 클래스 배열 : Throwable.getStackTrace()로 반환된 StackTrace 요소 배열
         > 각 요소는 단일 스택 프레임
         > 스택의 상단에 있는 것을 제외한 모든 스택 프레임은 메서드 호출
         > 스택 상단 프레임은 스택 추적이 생성된 실행 지점 - 일반적으로 throwable이 생성된 지점
  
      java.lang.StackTraceElement 클래스 : StackTrace 요소
        - 지정된 실행 지점을 나타내는 스택 추적 요소 생성
  
      */
  
      final Stack<String> callStack = new Stack<>();
      Stream<StackTraceElement> stream = Arrays.stream(new Throwable().getStackTrace());
      stream.map(element -> element.toString())
            .filter(elementStr -> elementStr.startsWith(packageName) && !elementStr.contains("P6spyPrettySqlFormatter") && !elementStr.contains("doFilter"))
            .forEach(callStack::push);
  
      int order = 1;
      final StringBuilder callStackBuilder = new StringBuilder();
      while (!callStack.empty()) {
          callStackBuilder.append(MessageFormat.format("{0}\t\t{1}. {2}", NEW_LINE, order++, callStack.pop()));
      }
      return callStackBuilder;
  
    }//:
  
  }///~