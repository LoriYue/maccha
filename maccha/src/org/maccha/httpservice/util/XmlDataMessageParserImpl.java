package org.maccha.httpservice.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import org.maccha.httpservice.DataMessage;
import org.maccha.httpservice.IDataMessageParser;
import org.maccha.httpservice.exception.WebServiceException;

public class XmlDataMessageParserImpl
  implements IDataMessageParser
{
  public String parseResponse(DataMessage response)
    throws WebServiceException
  {
    return response.toXml();
  }

  public DataMessage parseRequest(InputStream inputStream)
    throws WebServiceException
  {
    return Xml2DataMessageUtilty.getDataMessage(inputStream);
  }
}
