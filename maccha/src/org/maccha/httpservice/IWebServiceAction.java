package org.maccha.httpservice;

public abstract interface IWebServiceAction {
  public static final String WEBCONTEXT_PATH = "WEBCONTEXT_PATH";
  public static final String WEBCONTEXT_REQUEST_HOST_IP = "WEBCONTEXT_REQUEST_HOST_IP";
  public static final String WEBCONTEXT_REQUEST_HOST_NAME = "WEBCONTEXT_REQUEST_HOST_NAME";
  public static final String WEBCONTEXT_REQUEST_SESSION_ID = "WEBCONTEXT_REQUEST_SESSION_ID";

  public abstract void execute(DataMessage requestDataMessage, DataMessage responseDataMessage);
}
