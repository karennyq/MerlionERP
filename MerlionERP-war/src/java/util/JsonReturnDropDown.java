/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.google.gson.Gson;
import java.util.ArrayList;

/**
 *
 * 
 * usage:
 * 
 * String inquiry_priority = (request.getParameter("inquiry_priority") != null) ? request.getParameter("inquiry_priority") : "0";
        
            inquiry_priority=(Integer.parseInt(inquiry_priority)-1)+"";
            if(inquiry_priority.equals("-1"))  //need to -1!!!!
               inquiry_priority=""; 
 * @author Ken
 */
public class JsonReturnDropDown {

    //    "id":1,  
    // "text":"text1"  
    private int id;
    private String text;
    private boolean selected;

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public static String populate(Object[] text) {

        ArrayList list = new ArrayList();

        JsonReturnDropDown jsDd = new JsonReturnDropDown();
        jsDd.setId(0);
        jsDd.setText("");
        jsDd.setSelected(false);
        list.add(jsDd);

        for (int i = 0; i < text.length; i++) {
            jsDd = new JsonReturnDropDown();
            jsDd.setId(i+1);
            jsDd.setText(text[i].toString().replace('_',' '));
//            if(text[i].toString().equals(selected))
//                jsDd.setSelected(true);
//            else{
//                jsDd.setSelected(false);
//            }
            list.add(jsDd);
        }

        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
