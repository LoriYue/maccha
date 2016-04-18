package org.maccha.dao.dialect;

import org.hibernate.Hibernate;
import org.hibernate.dialect.SQLServerDialect;
import org.hibernate.type.StringType;

public class SQLServerDialectExt extends SQLServerDialect {
  public SQLServerDialectExt() {
    registerHibernateType(1, Hibernate.STRING.getName());
  }
}