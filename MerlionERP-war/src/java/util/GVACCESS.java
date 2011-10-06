/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Ken
 */
public class GVACCESS {

    private ArrayList<GVAccessRight> rights = new ArrayList<GVAccessRight>() {

        {
            //ADMIN
            add(new GVAccessRight("0", "System Adminstration", "", "", false));
            add(new GVAccessRight("00", "Employee Management", GVSYSTEMADMIN.EMP_MGMT, "0", false));
            add(new GVAccessRight("01", "Department Management", GVSYSTEMADMIN.DEPT_MGMT, "0", false));
            add(new GVAccessRight("02", "Role Management", GVSYSTEMADMIN.ROLE_MGMT, "0", false));
            //add(new GVAccessRight("03", "Temp", "", "0", false));


            //CRM
            add(new GVAccessRight("1", "Customer Relationship Management", "", "", false));

            add(new GVAccessRight("10", "Customer Relationship Management", "", "1", false));
            add(new GVAccessRight("100", "Sales Leads Management", GVCRM.SALES_LEAD_MGMT, "10", false));
            add(new GVAccessRight("101", "Customers Management", GVCRM.CUSTOMER_MGMT, "10", false));
            add(new GVAccessRight("102", "Sole Distribution Management", GVCRM.SOLE_DISTRIBUTION_MGMT, "10", false));
            add(new GVAccessRight("103", "Customers Transfer Management", GVCRM.CUSTOMER_TRSF_MGMT, "10", false));


            add(new GVAccessRight("11", "Sales Order Processing", "", "1", false));
            add(new GVAccessRight("110", "Sales Inquiry Management", GVCRM.SALES_INQUIRY_MGMT, "11", false));
            add(new GVAccessRight("111", "Sales Quotation Management", GVCRM.SALES_QUOTATION_MGMT, "11", false));
            add(new GVAccessRight("112", "Purchase Order Management", GVCRM.PURCHASE_ORDER_MGMT, "11", false));
            add(new GVAccessRight("113", "Sales Order Management", GVCRM.SALES_ORDER_MGMT, "11", false));
            add(new GVAccessRight("114", "Delivery Order Management", GVCRM.DELIVERY_ORDER_MGMT, "11", false));


            //FRM
            add(new GVAccessRight("2", "Finance Resource Management", "", "", false));
            add(new GVAccessRight("20", "Account Management", GVFRM.ACCT_MGMT, "2", false));

            //production
            add(new GVAccessRight("3", "Production Management", "", "", false));
            add(new GVAccessRight("30", "Public Holiday Management", GVPRODUCTION.PH_MGMT, "3", false));
            add(new GVAccessRight("31", "Sales Forecast Management", GVPRODUCTION.SF_MGMT, "3", false));
            add(new GVAccessRight("32", "Production Plan Management", GVPRODUCTION.PP_MGMT, "3", false));
            add(new GVAccessRight("33", "Production Line Management", GVPRODUCTION.PL_MGMT, "3", false));

        }
    };
    private HashMap<String, String> accessMap;

    public HashMap<String, String> getAccessMap() {
        this.accessMap = new HashMap<String, String>();
        GVACCESS ac = new GVACCESS();

        for (GVAccessRight a : ac.getRights()) {
            this.accessMap.put(a.getId(), a.getParentId());
        }


        return this.accessMap;

    }

    public ArrayList<GVAccessRight> getRights() {
        return rights;
    }
//    public class GVAccessRight {
//
//        private String id;
//        private String name;
//        private String url;
//        private String _parentId;
//
//        public boolean isChecked() {
//            return checked;
//        }
//
//        public void setChecked(boolean checked) {
//            this.checked = checked;
//        }
//        private boolean checked; 
//
//        public String getParentId() {
//            return _parentId;
//        }
//
//        public void setParentId(String _parentId) {
//            this._parentId = _parentId;
//        }
//
//        public GVAccessRight(String id, String name, String url,String parentID,boolean checked) {
//            this.id = id;
//            this.name = name;
//            this.url = url;
//            this._parentId=parentID;
//            this.checked=checked;
//        }
//
//        public String getId() {
//            return id;
//        }
//
//        public void setId(String id) {
//            this.id = id;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public String getUrl() {
//            return url;
//        }
//
//        public void setUrl(String url) {
//            this.url = url;
//        }
//    }
}
