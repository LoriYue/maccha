package org.maccha.dao.dialect;

import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.dialect.SQLServerDialect;

public class SQLServerDialectExt extends SQLServerDialect {
	public SQLServerDialectExt(){
		super();
		registerHibernateType(Types.CHAR, Hibernate.STRING.getName());
	}
}