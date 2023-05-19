package com.jirepo.core.mybatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumSet;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.jirepo.core.types.CodeEnumType;

/**
 * MyBatis에서 String EnumType을 처리하기 위한 핸들러 클래스이다.
 */
public class MyBatisEnumStringTypeHandler<E extends Enum<E> & CodeEnumType<String>> implements TypeHandler<E> {

  /** Enum Constant 목록 */
  private EnumSet<E> enumSet;

  public MyBatisEnumStringTypeHandler(Class<E> type) {
    this.enumSet = EnumSet.allOf(type);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {

    ps.setString(i, parameter.getCode());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public E getResult(ResultSet rs, String columnName) throws SQLException {

    String code = rs.getString(columnName);
    return getEnum(code);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public E getResult(ResultSet rs, int columnIndex) throws SQLException {

    String code = rs.getString(columnIndex);
    return getEnum(code);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public E getResult(CallableStatement cs, int columnIndex) throws SQLException {

    String code = cs.getString(columnIndex);
    return getEnum(code);
  }

  private E getEnum(String code) {
    return this.enumSet.stream().filter(constant -> constant.getCode().equals(code)).findFirst().orElseGet(() -> null);
  }
}