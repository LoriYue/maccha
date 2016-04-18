package org.maccha.dao.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;
import org.maccha.base.util.SecurityUtils;

public class Des3String
  implements UserType
{
  public Object assemble(Serializable cached, Object owner)
    throws HibernateException
  {
    return null;
  }

  public Object deepCopy(Object value) throws HibernateException {
    if (value == null) {
      return null;
    }
    return new String((String)value);
  }

  public Serializable disassemble(Object value) throws HibernateException
  {
    return null;
  }
  public boolean equals(Object x, Object y) throws HibernateException {
    return (x == y) || ((x != null) && (y != null) && (x.equals(y)));
  }

  public int hashCode(Object x) throws HibernateException {
    return x.hashCode();
  }
  public boolean isMutable() {
    return false;
  }

  public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException
  {
    String strValue = rs.getString(names[0]);

    if ((strValue == null) || (strValue.trim().length() == 0)) return strValue;
    String strDeCryptValue = SecurityUtils.decrypt("!qaz@wsx", strValue);

    if (strDeCryptValue == null) {
      return strValue;
    }
    return strDeCryptValue;
  }

  public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
    if (value == null) return;

    st.setString(index, SecurityUtils.encrypt("!qaz@wsx", value.toString()));
  }
  public Object replace(Object original, Object target, Object owner) throws HibernateException {
    return null;
  }
  public Class returnedClass() {
    return String.class;
  }
  public int[] sqlTypes() {
    return new int[] { 12 };
  }
}
