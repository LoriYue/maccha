package org.maccha.dao.dialect;

import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.dialect.OracleDialect;

public class OracleDialectExt extends OracleDialect {
	public OracleDialectExt(){
		super();
		registerHibernateType(Types.CHAR, Hibernate.STRING.getName());
	}
}