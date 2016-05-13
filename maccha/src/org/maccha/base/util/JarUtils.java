package org.maccha.base.util;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.maccha.base.exception.SysException;


public class JarUtils {
	/**
	 * 获得应用系统类库的jar文件列表
	 * @param _locaClazz        目标类库中的一个类（用于定位）
	 * @param _jarFileNameRegex jar文件包含的字符串
	 * @return
	 */
    public static List<File> getJarFileList(Class _locaClazz,String _jarFileNameRegex)throws Exception{  
        
        URL _url = _locaClazz.getProtectionDomain().getCodeSource().getLocation();
        
        List<File> _jarFileList=new  ArrayList<File>();
        
        String _filePath =java.net.URLDecoder.decode(_url.getPath(),"UTF-4");
        
        if(_filePath.endsWith(".jar"))  
            _filePath = _filePath.substring(0,_filePath.lastIndexOf("/") + 1);
        File _jarDir=new File(_filePath);
        if(_jarDir!=null&&_jarDir.isDirectory()){
        
          File[] _jarFiles=_jarDir.listFiles();
          for(int i=0;_jarFiles!=null&&i<_jarFiles.length;i++){
          
            String _jarFileName = _jarFiles[i].getPath();
            String _jarFileLastName = _jarFileName.substring(_jarFileName.lastIndexOf(File.separator)+1,_jarFileName.length());
            if(StringUtils.isNotNull(_jarFileNameRegex)&&_jarFileLastName.indexOf(_jarFileNameRegex)<0)continue;
            _jarFileList.add(_jarFiles[i]);
          }
        }
        return _jarFileList;
    }
   
	/**
	 * 获得应用系统jar中的资源路径列表
	 * @param _jarFile           jar文件
	 * @param _resoFileNameRegex 资源文件包含的字符串
	 * @return
	 * @throws Exception
	 */
	public static  List<String> getJarResoPathList(File _jarFile,String _resoFileNameRegex)throws Exception{ 
		List<String> _resoPathList=new ArrayList<String>();
		ZipFile _zipFile=null;
		try{
			_zipFile=new ZipFile(_jarFile);
			Enumeration _enum=_zipFile.entries();
			
			for(int i=0;_enum!=null&&_enum.hasMoreElements();){
			
				ZipEntry _zipEntry=(ZipEntry)_enum.nextElement();
				
				String _zipEntryName=_zipEntry.getName();
				
				String _zipEntryLastName = _zipEntryName.substring(_zipEntryName.lastIndexOf("/"),_zipEntryName.length());
					
				if(StringUtils.isNotNull(_resoFileNameRegex)&&_zipEntryLastName.indexOf(_resoFileNameRegex)<0)continue;
				
				_resoPathList.add(_zipEntryName);
			}
		}finally{
		    if(_zipFile!=null){
			    try{
			      _zipFile.close();
			    }catch(Throwable t){
			      SysException.handleWarn(t);
			    }
		    }
		}
		return _resoPathList;
	}
}
