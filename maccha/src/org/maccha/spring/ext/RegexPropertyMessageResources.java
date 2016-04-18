package org.maccha.spring.ext;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
/**
 * 国际化资源加载类
 * 覆盖了父类的setBasenames方法，提供支持通配符配置
 *
 * @see org.springframework.context.support.ResourceBundleMessageSource
 */
public class RegexPropertyMessageResources extends ResourceBundleMessageSource {
	private PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();

	public void setBasenames(String[] baseNames) {
		try {
        	List<String> baseNameList = new ArrayList<String>();
            for (String baseName : baseNames) {
                // 根据通配符获取资源对象集合
                Resource[] resources = patternResolver.getResources(baseName);
                List<String> _arrayBaseNames = new ArrayList<String>();
                // 申明文件名集合
                for (Resource resource : resources) {
                    String fileURL = resource.getURL().getPath();
                    int intFirst = fileURL.indexOf("com");
                    if(intFirst == -1)intFirst = fileURL.indexOf("org");
                    int intLast = fileURL.indexOf("_zh_CN");
                    if (intFirst !=-1 && intLast != -1) _arrayBaseNames.add(fileURL.substring(intFirst, intLast).replaceAll("/", "."));
                }
                for (int i = 0; i < _arrayBaseNames.size(); i++) baseNameList.add(_arrayBaseNames.get(i));
            }
            super.setBasenames(baseNameList.toArray(new String[baseNameList.size()]));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	}
}
