/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author Ken
 */
public class JsonReturnMsg {
  private String type = "";
  private String title = "";
  private String content = "";

  public JsonReturnMsg(String title,String value,String type) {
    this.type=type;
    this.title=title;
    this.content=value;    
  }
}
