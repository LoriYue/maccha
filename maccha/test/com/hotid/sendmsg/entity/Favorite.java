package com.hotid.sendmsg.entity;

import java.util.*;



  
public class Favorite  implements java.io.Serializable,Comparable {

   



        private String id;
        private String title;
        private String chanId;
        private String createUserId;
        private String createTime;
        private String lastMsgTime;

    public Favorite() {
    }

    public Favorite(String id) {
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

     
      
    private String _getTitle(String title) {
          return this.title;
  }
  public String getTitle() {
     return _getTitle(this.title);
  }
  public void setTitle(String title) {
     this.title = title;
  }

     
      
    private String _getChanId(String chanId) {
          return this.chanId;
  }
  public String getChanId() {
     return _getChanId(this.chanId);
  }
  public void setChanId(String chanId) {
     this.chanId = chanId;
  }

     
      
    private String _getCreateUserId(String createUserId) {
          return this.createUserId;
  }
  public String getCreateUserId() {
     return _getCreateUserId(this.createUserId);
  }
  public void setCreateUserId(String createUserId) {
     this.createUserId = createUserId;
  }

     
      
    private String _getCreateTime(String createTime) {
          return this.createTime;
  }
  public String getCreateTime() {
     return _getCreateTime(this.createTime);
  }
  public void setCreateTime(String createTime) {
     this.createTime = createTime;
  }

     
      
    private String _getLastMsgTime(String lastMsgTime) {
          return this.lastMsgTime;
  }
  public String getLastMsgTime() {
     return _getLastMsgTime(this.lastMsgTime);
  }
  public void setLastMsgTime(String lastMsgTime) {
     this.lastMsgTime = lastMsgTime;
  }


            
    public String toString(){
            if(this.getId()==null)
        return "";
      return this.getId()+"";
    }
  

    
    public int compareTo(Object obj){
        Favorite other =(Favorite) obj;
				return this.getId().compareTo(other.getId());
    }
}