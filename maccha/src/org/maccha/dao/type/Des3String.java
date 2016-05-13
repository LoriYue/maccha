package org.maccha.dao.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.hibernate.HibernateException;
import org.maccha.base.util.SecurityUtils;

public class Des3String implements org.hibernate.usertype.UserType{
	
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
       return null;
    }
    public Object deepCopy(Object value) throws HibernateException {
       if (value == null) {
           return null;
       } else {
           return new String((String) value);
       }
    }
    public Serializable disassemble(Object value) throws HibernateException {
       return null;
    }
    public boolean equals(Object x, Object y) throws HibernateException {
       return (x == y) || (x != null && y != null && (x.equals(y)));
    }
    public int hashCode(Object x) throws HibernateException {
       return x.hashCode();
    }
    public boolean isMutable() {
       return false;
    }
    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
    	//Get bin data from database then decrypt to String
    	String strValue = rs.getString(names[0]);
    	if(strValue == null || strValue.trim().length() == 0)return strValue ;
    	String strDeCryptValue = SecurityUtils.decrypt(SecurityUtils.ENCRYPT_KEY,strValue);
    	if(strDeCryptValue == null) 
    		return strValue ;
    	else 
    		return strDeCryptValue ;
    }

    public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
       if (value == null) return;
       //Encrypt String to bin data
       st.setString(index, SecurityUtils.encrypt(SecurityUtils.ENCRYPT_KEY,value.toString()));
    }
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
       return null;
    }
    public Class returnedClass() {
       return java.lang.String.class;
    }
    public int[] sqlTypes() {
       return new int[] { Types.VARCHAR };
    }
}
