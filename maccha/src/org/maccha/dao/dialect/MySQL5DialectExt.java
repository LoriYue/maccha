package org.maccha.dao.dialect;

import org.hibernate.Hibernate;
import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.StringType;

public class MySQL5DialectExt extends MySQL5Dialect {
  public MySQL5DialectExt() {
    registerColumnType(12, 65535, "varchar($l)");
    registerColumnType(12, 65535, "tinytext");
    registerColumnType(-1, "longtext");
    registerColumnType(-1, 16777215, "mediumtext");
    registerColumnType(-1, 65535, "text");
    registerHibernateType(3, Hibernate.BIG_DECIMAL.getName());
    registerHibernateType(-1, Hibernate.STRING.getName());
    registerHibernateType(1, Hibernate.STRING.getName());
  }
}
