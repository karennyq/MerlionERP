/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;

/**
 *
 * @author Ken
 */
public class JsonReturnTable {

    private String total;
    private ArrayList rows;
    private ArrayList footer;

    public JsonReturnTable(String total, ArrayList rows) {
        this.total = total;
        this.rows = rows;
    }

    public JsonReturnTable(String total, ArrayList rows, ArrayList footer) {
        this.total = total;
        this.rows = rows;
        this.footer= footer;
    }

    
}
