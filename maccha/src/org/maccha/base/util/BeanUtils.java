package org.maccha.base.util;

import java.sql.Timestamp;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.converters.SqlTimestampConverter;

public class BeanUtils extends org.apache.commons.beanutils.BeanUtils {
	static ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();
	static {
		convertUtilsBean.register(new SqlTimestampConverter(null),Timestamp.class);
	}
	public static BeanUtilsBean beanUtils = new BeanUtilsBean(convertUtilsBean, new PropertyUtilsBean());
}
