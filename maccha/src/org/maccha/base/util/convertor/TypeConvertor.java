package org.maccha.base.util.convertor;

public abstract interface TypeConvertor {
  public abstract Object convert(Object paramObject, String paramString)
    throws ClassCastException;
}