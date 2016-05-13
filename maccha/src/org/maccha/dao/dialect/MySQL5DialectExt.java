package org.maccha.dao.dialect;

import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.dialect.MySQL5Dialect;

public class MySQL5DialectExt extends MySQL5Dialect {
	public MySQL5DialectExt() {
        super();
        registerColumnType(Types.VARCHAR, 65535, "varchar($l)");
        registerColumnType(Types.VARCHAR, 65535, "tinytext");
        registerColumnType(Types.LONGVARCHAR, "longtext");
        registerColumnType(Types.LONGVARCHAR, 16777215, "mediumtext");
        registerColumnType(Types.LONGVARCHAR, 65535, "text");   
        registerHibernateType(Types.DECIMAL, Hibernate.BIG_DECIMAL.getName());   
        registerHibernateType(-1, Hibernate.STRING.getName());
        registerHibernateType(Types.CHAR, Hibernate.STRING.getName());
    }
}
