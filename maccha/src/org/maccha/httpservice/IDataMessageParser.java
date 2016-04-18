package org.maccha.httpservice;

import java.io.InputStream;
import org.maccha.httpservice.exception.WebServiceException;

public abstract interface IDataMessageParser
{
  public abstract String parseResponse(DataMessage paramDataMessage)
    throws WebServiceException;

  public abstract DataMessage parseRequest(InputStream paramInputStream)
    throws WebServiceException;
}
