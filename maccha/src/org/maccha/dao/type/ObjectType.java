package org.maccha.dao.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.hibernate.dialect.Dialect;
import org.hibernate.type.DiscriminatorType;
import org.hibernate.type.ImmutableType;

public class ObjectType extends ImmutableType
  implements DiscriminatorType
{
  public Object get(ResultSet rs, String name)
    throws SQLException
  {
    return rs.getString(name);
  }

  public Class getReturnedClass() {
    return String.class;
  }

  public void set(PreparedStatement st, Object value, int index) throws SQLException {
    st.setObject(index, value);
  }

  public int sqlType() {
    return 12;
  }
  public String getName() {
    return "object";
  }
  public String objectToSQLString(Object value, Dialect dialect) throws Exception {
    return '\'' + value.toString() + '\'';
  }

  public Object stringToObject(String xml) throws Exception {
    return xml;
  }

  public String toString(Object value) {
    if (value == null) return null;
    return value.toString();
  }

  public Object fromStringValue(String xml) {
    return xml;
  }
}
