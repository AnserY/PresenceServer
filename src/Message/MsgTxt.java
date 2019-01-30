package Message;

import java.io.Serializable;
import java.util.ArrayList;



public class MsgTxt extends Message implements Serializable{
    
   public ArrayList<DataAgent> connectedList ;
   
 
  public MsgTxt(ArrayList<DataAgent> connectedList){
      
     this.connectedList=connectedList;
      
  }
    
  @Override
    public String toString(){
        return this.connectedList.toString();
    }
}