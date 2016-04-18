package org.maccha.httpservice.util;

import java.util.List;
import org.maccha.base.util.ObjectUtils;
import org.maccha.httpservice.Entity;

public class EntityUtils
{
  public static Object entity2PO(Entity entity, Class objCls, List excludeProp)
  {
    return ObjectUtils.map2PO(objCls, entity.getPropertyMap(), null, null, excludeProp);
  }
}
