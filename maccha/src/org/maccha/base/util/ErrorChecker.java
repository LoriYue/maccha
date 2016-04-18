package org.maccha.base.util;

import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

class ErrorChecker extends DefaultHandler
{
  public void error(SAXParseException e)
  {
  }

  public void warning(SAXParseException e)
  {
  }

  public void fatalError(SAXParseException e)
  {
  }
}
