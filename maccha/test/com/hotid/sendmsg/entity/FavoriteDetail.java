package com.hotid.sendmsg.entity;

import java.util.*;



  
public class FavoriteDetail  implements java.io.Serializable,Comparable {

   



        private String id;
        private String favoId;
        private Integer index;
        private String inmeId;
    /**
     *消息类型 0 文字 1 图片 2 语音 3 普通文件 4指令
     */
         private String type;
        private String inmeCreateTime;
    /**
     *消息主体
     */
         private String content;
    /**
     *用户ID
     */
         private String userId;

    public FavoriteDetail() {
    }

    public FavoriteDetail(String id) {
        this.id = id;
    }



     
      
    private String _getId(String id) {
          return this.id;
  }
  public String getId() {
     return _getId(this.id);
  }
  public void setId(String id) {
     this.id = id;
  }

     
      
    private String _getFavoId(String favoId) {
          return this.favoId;
  }
  public String getFavoId() {
     return _getFavoId(this.favoId);
  }
  public void setFavoId(String favoId) {
     this.favoId = favoId;
  }

     
      
    private Integer _getIndex(Integer index) {
          return this.index;
  }
  public Integer getIndex() {
     return _getIndex(this.index);
  }
  public void setIndex(Integer index) {
     this.index = index;
  }

     
      
    private String _getInmeId(String inmeId) {
          return this.inmeId;
  }
  public String getInmeId() {
     return _getInmeId(this.inmeId);
  }
  public void setInmeId(String inmeId) {
     this.inmeId = inmeId;
  }

     
    
    private String _getType(String type) {
          return this.type;
  }
  public String getType() {
     return _getType(this.type);
  }
  public void setType(String type) {
     this.type = type;
  }

     
      
    private String _getInmeCreateTime(String inmeCreateTime) {
          return this.inmeCreateTime;
  }
  public String getInmeCreateTime() {
     return _getInmeCreateTime(this.inmeCreateTime);
  }
  public void setInmeCreateTime(String inmeCreateTime) {
     this.inmeCreateTime = inmeCreateTime;
  }

     
    
    private String _getContent(String content) {
          return this.content;
  }
  public String getContent() {
     return _getContent(this.content);
  }
  public void setContent(String content) {
     this.content = content;
  }

     
    
    private String _getUserId(String userId) {
          return this.userId;
  }
  public String getUserId() {
     return _getUserId(this.userId);
  }
  public void setUserId(String userId) {
     this.userId = userId;
  }


            
    public String toString(){
            if(this.getId()==null)
        return "";
      return this.getId()+"";
    }
  

    
    public int compareTo(Object obj){
        FavoriteDetail other =(FavoriteDetail) obj;
				return this.getId().compareTo(other.getId());
    }
}