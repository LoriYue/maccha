package org.maccha.dao.type;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.transaction.TransactionManager;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.orm.hibernate3.support.AbstractLobType;

public class ZipClobStringType extends AbstractLobType {
	/**
	 * Constructor used by Hibernate: fetches config-time LobHandler and
	 * config-time JTA TransactionManager from LocalSessionFactoryBean.
	 * 
	 * @see org.springframework.orm.hibernate.LocalSessionFactoryBean#getConfigTimeLobHandler
	 * @see org.springframework.orm.hibernate.LocalSessionFactoryBean#getConfigTimeTransactionManager
	 */
	public ZipClobStringType() {
		super();
	}
	protected ZipClobStringType(LobHandler lobHandler,
			TransactionManager jtaTransactionManager) {
		super(lobHandler, jtaTransactionManager);
	}
	public int[] sqlTypes() {
		return new int[] { Types.CLOB };
	}
	public Class returnedClass() {
		return String.class;
	}
	protected Object nullSafeGetInternal(ResultSet rs, String[] names,
			Object owner, LobHandler lobHandler) throws SQLException {
		String _clobStr = lobHandler.getClobAsString(rs, names[0]);
		try {
			if (org.apache.commons.codec.binary.Base64.isArrayByteBase64(_clobStr.getBytes())) {
				return this.deCompress(_clobStr);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return _clobStr;
	}
	protected void nullSafeSetInternal(PreparedStatement ps, int index,
			Object value, LobCreator lobCreator) throws SQLException {
		try {
			System.out.println("::::::::::::::::压缩前大小:"+((String) value).length()/1024);
			String strCompress = this.compress((String) value);
			System.out.println("::::::::::::::::压缩后大小:"+strCompress.length()/1024);
			lobCreator.setClobAsString(ps, index, strCompress);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	private String compress(String s) {
		Object obj = null;
		String s1 = "";
		try {
			ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
			DataOutputStream dataoutputstream = new DataOutputStream(new GZIPOutputStream(bytearrayoutputstream));
			dataoutputstream.write(s.getBytes());
			dataoutputstream.close();
			bytearrayoutputstream.close();
			s1 = new String(org.apache.commons.codec.binary.Base64
					.encodeBase64(bytearrayoutputstream.toByteArray()));
		} catch (IOException ioexception) {
			ioexception.printStackTrace();
		}
		return s1;
	}
	private String deCompress(String s) {
		String s1 = null;
		try {
			byte abyte0[] = org.apache.commons.codec.binary.Base64.decodeBase64(s.getBytes());
			ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(abyte0);
			GZIPInputStream gzipinputstream = new GZIPInputStream(new ByteArrayInputStream(abyte0));
			ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
			byte abyte1[] = new byte[1024];
			int i;
			while ((i = gzipinputstream.read(abyte1)) >= 0)
				bytearrayoutputstream.write(abyte1, 0, i);
			s1 = new String(bytearrayoutputstream.toByteArray());
			bytearrayoutputstream.close();
			gzipinputstream.close();
		} catch (IOException ioexception) {
			ioexception.printStackTrace();
		}
		return s1;
	}
}
