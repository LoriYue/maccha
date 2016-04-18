package org.maccha.dao.dialect;

import org.hibernate.Hibernate;
import org.hibernate.dialect.OracleDialect;
import org.hibernate.type.StringType;

public class OracleDialectExt extends OracleDialect {
  public OracleDialectExt() {
    registerHibernateType(1, Hibernate.STRING.getName());
  }
}