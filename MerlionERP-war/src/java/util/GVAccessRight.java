/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author Ken
 */
public class GVAccessRight {
    private String id;
        private String name;
        private String url;
         private boolean checked; 
        private String _parentId;

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }
       

        public String getParentId() {
            return _parentId;
        }

        public void setParentId(String _parentId) {
            this._parentId = _parentId;
        }

        public GVAccessRight(String id, String name, String url,String parentID,boolean checked) {
            this.id = id;
            this.name = name;
            this.url = url;
            this._parentId=parentID;
            this.checked=checked;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

