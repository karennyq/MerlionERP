/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author Ken
 */
public class GVCOMMON {
     //Json msg box
    //for look and feel http://www.jeasyui.com/demo/index.php
    
    /*
     * if user confirm is needed
     function confirm1(){
			$.messager.confirm('My Title', 'Are you confirm this?', function(r){
				if (r){
					alert('confirmed:'+r);
					location.href = 'http://www.google.com';
				}
			});
		}
		function prompt1(){
			$.messager.prompt('My Title', 'Please type something', function(r){
				if (r){
					alert('you type:'+r);
				}
			});
		}

     */
    public static final String JSON_MSGTYPE_INFO = "info";
    public static final String JSON_MSGTYPE_ERROR = "error";
    public static final String JSON_MSGTYPE_QUESTION = "question";
    public static final String JSON_MSGTYPE_WARNING = "warning";
    
    
    
}
