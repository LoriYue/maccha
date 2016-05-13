package org.maccha.base.util.convertor;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.type.ClobType;
import org.maccha.base.util.DateUtils;
import org.maccha.base.util.StringUtils;

public final class ConvertorTypesUtility {
	/**
	 * Date format used by the date convertor.
	 */
	private static DateFormat _dateFormat = new SimpleDateFormat();
	/**
	 * Date format used by the date convertor when nonempty parameter
	 * is specified.
	 */
	private static SimpleDateFormat _paramDateFormat = new SimpleDateFormat();
	/**
	 * Date format used by the double->date convertor.
	 */
	private static DecimalFormat _decimalFormat = new DecimalFormat("#################0");
    public static TypeConvertor getConvertor(Class fromType, Class toType) throws Exception {
		// System.out.println("From : " + fromType + ": To :" + toType) ;
		// first seek for exact match
		// TODO: the closest possible match
		for (int i = 0; i < _typeConvertors.length; ++i) {
			// System.out.println("From : " + _typeConvertors[ i ].fromType + ":
			// To :" + _typeConvertors[ i ].toType) ;
			if (_typeConvertors[i].fromType.equals(fromType)
					&& toType.equals(_typeConvertors[i].toType))
				return _typeConvertors[i].convertor;
		}
		// else seek for any match
		for (int i = 0; i < _typeConvertors.length; ++i) {
			if (_typeConvertors[i].fromType.isAssignableFrom(fromType)
					&& toType.isAssignableFrom(_typeConvertors[i].toType))
				return _typeConvertors[i].convertor;
		}
		throw new Exception("From :" + fromType.getName() + " To :"
				+ toType.getName() + " : mapping.noConvertor");
	}
	/**
	 * Information used to locate a type convertor.
	 */
	static class TypeConvertorInfo {
		/**
		 *  The type being converted to.
		 */
		final Class toType;
		/**
		 * The type being converted from.
		 */
		final Class fromType;
		/**
		 * The convertor.
		 */
		final SimpleTypeConvertor convertor;
		TypeConvertorInfo(SimpleTypeConvertor convertor) {
			this.convertor = convertor;
			this.fromType = convertor.fromType;
			this.toType = convertor.toType;
		}
	}

	private abstract static class SimpleTypeConvertor implements TypeConvertor {
		Class fromType;
		Class toType;
		SimpleTypeConvertor(Class fromType, Class toType) {
			this.fromType = fromType;
			this.toType = toType;
		}
		public abstract Object convert(Object obj, String param);
		public String toString() {
			return fromType.getName() + "-->" + toType.getName();
		}
	}

	/**
	 * List of all the default convertors between Java types.
	 */
	static TypeConvertorInfo[] _typeConvertors = new TypeConvertorInfo[] {
		// Convertors to boolean
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.Short.class, java.lang.Boolean.class) {
			public Object convert(Object obj, String param) {
				return ((Short) obj).shortValue() == 0 ? java.lang.Boolean.FALSE
						: java.lang.Boolean.TRUE;
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.String.class, java.math.BigDecimal.class) {
			public Object convert(Object obj, String param) {
				return new BigDecimal((String) obj);
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.Boolean.class, java.lang.Short.class) {
			public Object convert(Object obj, String param) {
				return new Short(((Boolean) obj).booleanValue() ? (byte) 1
						: (byte) 0);
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.Integer.class, java.lang.Boolean.class) {
			public Object convert(Object obj, String param) {
				return ((Integer) obj).intValue() == 0 ? java.lang.Boolean.FALSE
						: java.lang.Boolean.TRUE;
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.Boolean.class, java.lang.Integer.class) {
			public Object convert(Object obj, String param) {
				return new Integer(((Boolean) obj).booleanValue() ? 1 : 0);
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(java.lang.String.class, boolean.class) {
			public Object convert(Object obj, String param) {					 
				switch (((String) obj).length()) {
				case 0:
					return Boolean.FALSE;
				case 1:
					char ch = ((String) obj).charAt(0);
					if (param == null || param.length() != 2)
						return (ch == 'T' || ch == 't' || ch == '1') ? Boolean.TRUE : Boolean.FALSE;
					else
						return (ch == param.charAt(1)) ? Boolean.TRUE : Boolean.FALSE;
				case 4:
					return ((String) obj).equalsIgnoreCase("true") ? Boolean.TRUE : Boolean.FALSE;
				case 5:
					return ((String) obj).equalsIgnoreCase("false") ? Boolean.FALSE: Boolean.TRUE;
				}
				return Boolean.FALSE;
			}
		}),			
		new TypeConvertorInfo(new SimpleTypeConvertor(java.lang.String.class, java.lang.Boolean.class) {
			public Object convert(Object obj, String param) {					 
				switch (((String) obj).length()) {
				case 0:
					return Boolean.FALSE;
				case 1:
					char ch = ((String) obj).charAt(0);
					if (param == null || param.length() != 2)
						return (ch == 'T' || ch == 't' || ch == '1') ? Boolean.TRUE : Boolean.FALSE;
					else
						return (ch == param.charAt(1)) ? Boolean.TRUE : Boolean.FALSE;
				case 4:
					return ((String) obj).equalsIgnoreCase("true") ? Boolean.TRUE : Boolean.FALSE;
				case 5:
					return ((String) obj).equalsIgnoreCase("false") ? Boolean.FALSE : Boolean.TRUE;
				}
				return Boolean.FALSE;
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.math.BigDecimal.class, java.lang.Boolean.class) {
			public Object convert(Object obj, String param) {
				return new Boolean(
						((java.math.BigDecimal) obj).intValue() != 0);
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.math.BigDecimal.class, java.lang.String.class) {
			public Object convert(Object obj, String param) {
				return ((java.math.BigDecimal) obj).toString();
			}
		}),
		// Convertors to integer
		new TypeConvertorInfo(new SimpleTypeConvertor(java.lang.Byte.class,
				java.lang.Integer.class) {
			public Object convert(Object obj, String param) {
				return new Integer(((Byte) obj).intValue());
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.Short.class, java.lang.Integer.class) {
			public Object convert(Object obj, String param) {
				return new Integer(((Short) obj).intValue());
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(java.lang.Long.class,
				java.lang.Integer.class) {
			public Object convert(Object obj, String param) {
				return new Integer(((Long) obj).intValue());
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.Float.class, java.lang.Integer.class) {
			public Object convert(Object obj, String param) {
				return new Integer(((Float) obj).intValue());
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.Double.class, java.lang.Integer.class) {
			public Object convert(Object obj, String param) {
				return new Integer(((Double) obj).intValue());
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.math.BigDecimal.class, java.lang.Integer.class) {
			public Object convert(Object obj, String param) {
				return new Integer(((BigDecimal) obj).intValue());
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.String.class, java.lang.Integer.class) {
			public Object convert(Object obj, String param) {
				if(!StringUtils.isNotNull((String) obj))
					return new Integer(0);
				return Integer.valueOf((String) obj);
			}
		}),
		// Convertors to long
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.Integer.class, java.lang.Long.class) {
			public Object convert(Object obj, String param) {					
				return new Long(((Integer) obj).longValue());
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.Short.class, java.lang.Long.class) {
			public Object convert(Object obj, String param) {
				return new Long(((Short) obj).longValue());
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.Float.class, java.lang.Long.class) {
			public Object convert(Object obj, String param) {
				return new Long(((Float) obj).longValue());
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.Double.class, java.lang.Long.class) {
			public Object convert(Object obj, String param) {
				return new Long(((Double) obj).longValue());
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.math.BigDecimal.class, java.lang.Long.class) {
			public Object convert(Object obj, String param) {
				return new Long(((BigDecimal) obj).longValue());
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.String.class, java.lang.Long.class) {
			public Object convert(Object obj, String param) {
				if(!StringUtils.isNotNull((String) obj))
					return new Long(0);
				return NumberUtils.createLong((String) obj);					
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(java.util.Date.class,
				java.lang.Long.class) {
			public Object convert(Object obj, String param) {
				return new Long(((java.util.Date) obj).getTime());
			}
		}),
		// Convertors to short
		new TypeConvertorInfo(new SimpleTypeConvertor(java.lang.Byte.class,
				java.lang.Short.class) {
			public Object convert(Object obj, String param) {
				return new Short(((Byte) obj).shortValue());
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.Integer.class, java.lang.Short.class) {
			public Object convert(Object obj, String param) {
				return new Short(((Integer) obj).shortValue());
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(java.lang.Long.class,
				java.lang.Short.class) {
			public Object convert(Object obj, String param) {
				return new Short(((Long) obj).shortValue());
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.math.BigDecimal.class, java.lang.Short.class) {
			public Object convert(Object obj, String param) {
				return new Short((short) ((BigDecimal) obj).intValue());
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.String.class, java.lang.Short.class) {
			public Object convert(Object obj, String param) {
				if(!StringUtils.isNotNull((String)obj))
					return new Short("0");
				return Short.valueOf((String) obj);
			}
		}),
		// Convertors to byte
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.Short.class, java.lang.Byte.class) {
			public Object convert(Object obj, String param) {
				return new Byte(((Short) obj).byteValue());
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.Integer.class, java.lang.Byte.class) {
			public Object convert(Object obj, String param) {
				return new Byte(((Integer) obj).byteValue());
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.math.BigDecimal.class, java.lang.Byte.class) {
			public Object convert(Object obj, String param) {
				return new Byte((byte) ((BigDecimal) obj).intValue());
			}
		}),
		// Convertors to double
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.Float.class, java.lang.Double.class) {
			public Object convert(Object obj, String param) {
				return new Double(((Float) obj).floatValue());
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.Integer.class, java.lang.Double.class) {
			public Object convert(Object obj, String param) {
				return new Double((double) ((Integer) obj).intValue());
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(java.lang.Long.class,
				java.lang.Double.class) {
			public Object convert(Object obj, String param) {
				return new Double((double) ((Long) obj).longValue());
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.math.BigDecimal.class, java.lang.Double.class) {
			public Object convert(Object obj, String param) {
				return new Double(((BigDecimal) obj).doubleValue());
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.String.class, java.lang.Double.class) {
			public Object convert(Object obj, String param) {
				if(!StringUtils.isNotNull((String)obj))return new Double(0.0);
				return Double.valueOf((String) obj);
			}
		}),
		// Convertors to float
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.Double.class, java.lang.Float.class) {
			public Object convert(Object obj, String param) {
				return new Float(((Double) obj).floatValue());
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.Integer.class, java.lang.Float.class) {
			public Object convert(Object obj, String param) {
				return new Float((float) ((Integer) obj).intValue());
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(java.lang.Long.class,
				java.lang.Float.class) {
			public Object convert(Object obj, String param) {
				return new Float((float) ((Long) obj).longValue());
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.math.BigDecimal.class, java.lang.Float.class) {
			public Object convert(Object obj, String param) {
				return new Float(((BigDecimal) obj).floatValue());
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.String.class, java.lang.Float.class) {
			public Object convert(Object obj, String param) {
				if(!StringUtils.isNotNull((String)obj))return new Float(0.0);
				return Float.valueOf((String) obj);
			}
		}),
		// Convertors to big decimal
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.Double.class, java.math.BigDecimal.class) {
			public Object convert(Object obj, String param) {
				// Don't remove "toString" below! Otherwise the result is incorrect.
				return new BigDecimal(((Double) obj).toString());
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.Float.class, java.math.BigDecimal.class) {
			public Object convert(Object obj, String param) {
				// Don't remove "toString" below! Otherwise the result is incorrect.
				return new BigDecimal(((Float) obj).toString());
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.Integer.class, java.math.BigDecimal.class) {
			public Object convert(Object obj, String param) {
				return BigDecimal.valueOf(((Integer) obj).intValue());
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(java.lang.Byte.class,
				java.math.BigDecimal.class) {
			public Object convert(Object obj, String param) {
				return BigDecimal.valueOf(((Byte) obj).byteValue());
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.Short.class, java.math.BigDecimal.class) {
			public Object convert(Object obj, String param) {
				return BigDecimal.valueOf(((Short) obj).shortValue());
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(java.lang.Long.class,
				java.math.BigDecimal.class) {
			public Object convert(Object obj, String param) {
				return BigDecimal.valueOf(((Long) obj).longValue());
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.Boolean.class, java.math.BigDecimal.class) {
			public Object convert(Object obj, String param) {
				return BigDecimal
						.valueOf(((Boolean) obj).booleanValue() ? 1 : 0);
			}
		}),
		// Convertors to string
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.Short.class, java.lang.String.class) {
			public Object convert(Object obj, String param) {
				return obj.toString();
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.Integer.class, java.lang.String.class) {
			public Object convert(Object obj, String param) {
				return obj.toString();
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(java.lang.Long.class,
				java.lang.String.class) {
			public Object convert(Object obj, String param) {
				return obj.toString();
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.Float.class, java.lang.String.class) {
			public Object convert(Object obj, String param) {
				return obj.toString();
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.Double.class, java.lang.String.class) {
			public Object convert(Object obj, String param) {
				return obj.toString();
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.Object.class, java.lang.String.class) {
			public Object convert(Object obj, String param) {
				return obj.toString();
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(java.util.Date.class,
				java.lang.String.class) {
			public Object convert(Object obj, String param) {
				if (param == null || param.length() == 0)
					return obj.toString();
				else {
					_paramDateFormat.applyPattern(param);
					return _paramDateFormat.format((java.util.Date) obj);
				}
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.Character.class, java.lang.String.class) {
			public Object convert(Object obj, String param) {
				return obj.toString();
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(char[].class,
				java.lang.String.class) {
			public Object convert(Object obj, String param) {
				return new String((char[]) obj);
			}
		}),
		/*        new TypeConvertorInfo( new SimpleTypeConvertor( byte[].class, java.lang.String.class ) {
		 public Object convert( Object obj, String param ) {
		 MimeBase64Encoder encoder;
		 encoder = new MimeBase64Encoder();
		 encoder.translate( (byte[]) obj );
		 return new String( encoder.getCharArray() );
		 }
		 } ),*/
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.Boolean.class, java.lang.String.class) {
			public Object convert(Object obj, String param) {
				if (param == null || param.length() != 2)
					return ((Boolean) obj).booleanValue() ? "T" : "F";
				else
					return ((Boolean) obj).booleanValue() ? param
							.substring(1, 2) : param.substring(0, 1);
			}
		}),
		// Convertors to character/byte array
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.String.class, java.lang.Character.class) {
			public Object convert(Object obj, String param) {
				String str = (String) obj;
				return (new Character(str.length() == 0 ? 0 : str.charAt(0)));
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.String.class, char[].class) {
			public Object convert(Object obj, String param) {
				return ((String) obj).toCharArray();
			}
		}),
		// Convertors to date
		new TypeConvertorInfo(new SimpleTypeConvertor(java.lang.Long.class,
				java.util.Date.class) {
			public Object convert(Object obj, String param) {
				return new java.util.Date(((Long) obj).longValue());
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(java.sql.Date.class,
				java.lang.String.class) {
			public Object convert(Object obj, String param) {
				try {
					_paramDateFormat.applyPattern("yyyy-MM-dd");
					return _paramDateFormat.format((java.util.Date) obj);
				} catch (Exception except) {
					throw new IllegalArgumentException(except.toString());
				}
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.String.class, java.sql.Date.class) {
			public Object convert(Object obj, String param) {
				try {
					if (obj != null && ((String) obj).length() > 10)
						param = "yyyy-MM-dd HH:mm:ss";
					if (obj != null && ((String) obj).trim().length() == 10)
						param = "yyyy-MM-dd";
					java.util.Date dataObj = null;
					if (param == null || param.length() == 0) {
						dataObj = DateUtils.parse((String) obj);
					} else {
						_paramDateFormat.applyPattern(param);
						dataObj = (java.util.Date) _paramDateFormat
								.parse((String) obj);
					}
					return new java.sql.Date(dataObj.getTime());
				} catch (ParseException except) {
					throw new IllegalArgumentException(except.toString());
				}
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.String.class, java.util.Date.class) {
			public Object convert(Object obj, String param) {
				if(!StringUtils.isNotNull((String)obj))return null ;
				try {
					if (obj != null && ((String) obj).length() > 10)
						param = "yyyy-MM-dd HH:mm:ss";
					if (obj != null && ((String) obj).trim().length() == 10)
						param = "yyyy-MM-dd";
					java.util.Date dataObj = null;
					if (param == null || param.length() == 0) {
						dataObj = DateUtils.parse((String) obj);
					} else {
						_paramDateFormat.applyPattern(param);
						dataObj = (java.util.Date) _paramDateFormat.parse((String) obj);
					}
					return new java.util.Date(dataObj.getTime());
				} catch (ParseException except) {
					throw new IllegalArgumentException(except.toString());
				}
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(java.util.Date.class,
				java.sql.Date.class) {
			public Object convert(Object obj, String param) {
				return new java.sql.Date(((java.util.Date) obj).getTime());
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(java.sql.Date.class,
				java.util.Date.class) {
			public Object convert(Object obj, String param) {
				return new java.util.Date(((java.sql.Date) obj).getTime());
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(java.util.Date.class,
				java.sql.Time.class) {
			public Object convert(Object obj, String param) {
				return new java.sql.Time(((java.util.Date) obj).getTime());
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(java.sql.Time.class,
				java.util.Date.class) {
			public Object convert(Object obj, String param) {
				return obj;
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(java.util.Date.class,
				java.sql.Timestamp.class) {
			public Object convert(Object obj, String param) {
				long time = ((java.util.Date) obj).getTime();
				java.sql.Timestamp timestamp = new java.sql.Timestamp(time);
				timestamp.setNanos((int) ((time % 1000) * 1000000));
				//timestamp.setNanos(0);  // this can workaround the bug in SAP DB
				return timestamp;
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.sql.Timestamp.class, java.util.Date.class) {
			public Object convert(Object obj, String param) {
				java.sql.Timestamp timestamp = (java.sql.Timestamp) obj;
				return new java.util.Date(timestamp.getTime()
						+ timestamp.getNanos() / 1000000);
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.String.class, java.sql.Timestamp.class) {
			public Object convert(Object obj, String param) {
				long time;
				if (param == null || param.length() == 0) {
					//param = "yyyy-MM-dd HH:mm:ss.SSS";
					param = "yyyy-MM-dd HH:mm:ss";
				}
				try {
					_paramDateFormat.applyPattern(param);
					time = _paramDateFormat.parse((String) obj).getTime();
				} catch (ParseException except) {
					throw new IllegalArgumentException(except.toString());
				}
				java.sql.Timestamp timestamp = new java.sql.Timestamp(time);
				timestamp.setNanos((int) ((time % 1000) * 1000000));
				//timestamp.setNanos(0);  // this can workaround the bug in SAP DB
				return timestamp;
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.sql.Timestamp.class, java.lang.String.class) {
			public Object convert(Object obj, String param) {
				if (param == null || param.length() == 0) {
					param = "yyyy-MM-dd HH:mm:ss.SSS";
				}
				java.sql.Timestamp timestamp = (java.sql.Timestamp) obj;
				_paramDateFormat.applyPattern(param);
				return _paramDateFormat.format(new java.util.Date(timestamp
						.getTime()
						+ timestamp.getNanos() / 1000000));
			}
		}),
		// InputStream convertors
		new TypeConvertorInfo(new SimpleTypeConvertor(byte[].class,
				java.io.InputStream.class) {
			public Object convert(Object obj, String param) {
				return new java.io.ByteArrayInputStream((byte[]) obj);
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.io.InputStream.class, byte[].class) {
			public Object convert(Object obj, String param) {
				try {
					java.io.InputStream is = (java.io.InputStream) obj;
					java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
					byte[] buffer = new byte[256];
					int len = 0;
					int b;
					while ((len = is.read(buffer)) > 0)
						bos.write(buffer, 0, len);
					return bos.toByteArray();
				} catch (java.io.IOException except) {
					throw new IllegalArgumentException(except.toString());
				}
			}
		}),
		// Reader convertors
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.lang.String.class, java.sql.Clob.class) {
			public Object convert(Object obj, String param) {
				String str = (java.lang.String) obj;
				return new ClobType().fromString(str);
				//return new ClobImpl(new java.io.StringReader(str), str.length());
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(char[].class,
				java.sql.Clob.class) {
			public Object convert(Object obj, String param) {
				char[] chars = (char[]) obj;
				return new ClobType().fromString(new String(chars));
				//return new org.hibernate.lob.ClobImp(new java.io.CharArrayReader(chars),chars.length);
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(java.sql.Clob.class,
				java.lang.String.class) {
			public Object convert(Object obj, String param) {
				try {
					java.io.Reader reader = ((java.sql.Clob) obj)
							.getCharacterStream();
					java.io.CharArrayWriter writer = new java.io.CharArrayWriter();
					char[] buffer = new char[256];
					int len = 0;
					int b;
					while ((len = reader.read(buffer)) > 0)
						writer.write(buffer, 0, len);
					return writer.toString();
				} catch (Exception except) {
					throw new IllegalArgumentException(except.toString());
				}
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(java.sql.Clob.class,
				char[].class) {
			public Object convert(Object obj, String param) {
				try {
					java.io.Reader reader = ((java.sql.Clob) obj)
							.getCharacterStream();
					java.io.CharArrayWriter writer = new java.io.CharArrayWriter();
					char[] buffer = new char[256];
					int len = 0;
					int b;
					while ((len = reader.read(buffer)) > 0)
						writer.write(buffer, 0, len);
					return writer.toCharArray();
				} catch (Exception except) {
					throw new IllegalArgumentException(except.toString());
				}
			}
		}),
		// It's a special case for Serializable
		// Because Serializable need the right ClassLoader when serializing, we convert to byte[] and vice-versa
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.io.Serializable.class, java.io.InputStream.class) {
			public Object convert(Object obj, String param) {
				return new java.io.ByteArrayInputStream((byte[]) obj);
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.io.InputStream.class, java.io.Serializable.class) {
			public Object convert(Object obj, String param) {
				try {
					java.io.InputStream is = (java.io.InputStream) obj;
					java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
					byte[] buffer = new byte[256];
					int len = 0;
					int b;
					while ((len = is.read(buffer)) > 0)
						bos.write(buffer, 0, len);
					return bos.toByteArray();
				} catch (java.io.IOException except) {
					throw new IllegalArgumentException(except.toString());
				}
			}
		}),
		// It's a special case for Serializable
		// Because Serializable need the right ClassLoader when serializing, we convert to byte[] and vice-versa
		new TypeConvertorInfo(new SimpleTypeConvertor(
				java.io.Serializable.class, byte[].class) {
			public Object convert(Object obj, String param) {
				return obj;
			}
		}),
		new TypeConvertorInfo(new SimpleTypeConvertor(byte[].class,
				java.io.Serializable.class) {
			public Object convert(Object obj, String param) {
				return obj;
			}
		}) 
	};
}
