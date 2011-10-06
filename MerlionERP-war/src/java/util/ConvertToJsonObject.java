/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;
import org.persistence.Account;
import org.persistence.BackOrder;
import org.persistence.Customer;
import org.persistence.DeliveryOrder;
import org.persistence.Department;
import org.persistence.Employee;
import org.persistence.LineItem;
import org.persistence.PurchaseOrder;
import org.persistence.Role;
import org.persistence.SalesInquiry;
import org.persistence.SalesLead;
import org.persistence.SalesOrder;
import org.persistence.SalesQuotation;
import org.persistence.SoleDistribution;

/**
 *
 * @author Ken
 */
public class ConvertToJsonObject {

    public static Object convert(Object o) {
        Object obj = o;

        if (obj instanceof SalesLead) {

            SalesLead sl = (SalesLead) obj;
            for (SalesInquiry si : sl.getPreSaleDocuments()) {
                si.setInquirer(null);
                si.setLineItems(null);
            }
            if (sl instanceof Customer) {
                Customer c = (Customer) sl;
                c.getAccount().setCustomer(null);
                c.getEmployee().setRoles(null);
                c.getEmployee().setAuditTrials(null);
                
                for (PurchaseOrder po : c.getPurchaseOrders()) {
                    po.setCustomer(null);
                    po.setLineItems(null);
                    po.setSalesOrder(null);
                    po.setSalesQuotation(null);
                }

                for (SoleDistribution sd : c.getSoleDistribution()) {
                    sd.setCustomer(null);
                }

                return c;
            }

            return sl;

        } else if (obj instanceof Account) {

            Account ac = (Account) obj;
            ac.getCustomer().setAccount(null);
            ac.getCustomer().setPreSaleDocuments(null);
            ac.getCustomer().setPurchaseOrders(null);
            ac.getCustomer().setSoleDistribution(null);

            return ac;
        } else if (obj instanceof LineItem) {

            LineItem li = (LineItem) obj;
            li.getProduct().setPdtBatches(null);
            li.getProduct().setSalesForecasts(null);
            li.getProduct().setDeliveryOrderDetails(null);
            li.getProduct().setIngredients(null);
            li.getProduct().setSafety_stock_boxes(null);

            return li;
        } else if (obj instanceof SalesInquiry) {

            SalesInquiry si = (SalesInquiry) obj;

 


            for (LineItem li : si.getLineItems()) {
                li.setProduct(null);
            }

            si.getInquirer().setPreSaleDocuments(null);

            if (si.getInquirer() instanceof Customer) {
                Customer c = (Customer) si.getInquirer();
                c.setAccount(null);
                c.setPurchaseOrders(null);
                c.setSoleDistribution(null);
            }

            if (si instanceof SalesQuotation) {
                SalesQuotation sq = (SalesQuotation) si;
//                sq.getSalesInquiry().setInquirer(null);
//                sq.getSalesInquiry().setLineItems(null);
//                sq.getSalesInquiry().setSalesQuotations(null);
//                
//                
//                
//                
                for (PurchaseOrder po : sq.getPurchaseOrders()) {
                    po.setLineItems(null);
                    po.setCustomer(null);
                    po.setCustomer(null);
                    po.setSalesOrder(null);
                    po.setSalesOrder(null);
                    po.setSalesQuotation(null);
                }

                return sq;
            }

            return si;

        } else if (obj instanceof SalesOrder) {
            SalesOrder so = (SalesOrder) obj;

            ArrayList boList = new ArrayList();
            for (BackOrder bo : so.getBackOrders()) {
                bo.setSalesOrder(null);
                boList.add(bo);
            }
            so.setBackOrders(boList);

            ArrayList dloList = new ArrayList();
            for (DeliveryOrder dlo : so.getDeliveryOrders()) {
                dlo.setSalesOrder(null);
                dlo.setDeliveryOrderDetails(new ArrayList());
                dloList.add(dlo);
            }
            so.setDeliveryOrders(dloList);

            ArrayList liList = new ArrayList();
            for (LineItem li : so.getLineItems()) {
                li.setProduct(null);
                liList.add(li);

            }
            so.setLineItems(liList);

            return so;

        } else if (obj instanceof PurchaseOrder) {
            PurchaseOrder po = (PurchaseOrder) obj;
            po.getCustomer().setPurchaseOrders(null);
            po.getCustomer().setAccount(null);
            po.getCustomer().setPreSaleDocuments(null);
            po.getCustomer().setSoleDistribution(null);
            po.getCustomer().setEmployee(null);

            po.setSalesOrder(null);
            po.getSalesQuotation().setPurchaseOrders(null);
            po.getSalesQuotation().setInquirer(null);
            po.getSalesQuotation().setLineItems(null);

            po.setSalesOrder(null);//1layer

            for (LineItem li : po.getLineItems()) {
                li.setProduct(null);
            }

        } else if (obj instanceof Department) {
            Department d = (Department) obj;
            for (Role r : d.getRoles()) {
                r.setDepartment(null);
                r.setEmployees(null);
            }
            return d;
        } else if (obj instanceof Role) {

            Role r = (Role) obj;
            r.getDepartment().setRoles(null);

            for (Employee e : r.getEmployees()) {
                e.setRoles(null);
                e.setAuditTrials(null);
                e.setMessages(null);
            }

            return r;
        } else if (obj instanceof LineItem) {

            LineItem li = (LineItem) obj;
            li.getProduct().setDeliveryOrderDetails(null);
            li.getProduct().setIngredients(null);
            li.getProduct().setPdtBatches(null);
            li.getProduct().setSafety_stock_boxes(null);
            li.getProduct().setSalesForecasts(null);
            return li;
        }else if (obj instanceof SoleDistribution) {
            
            SoleDistribution sd = (SoleDistribution) obj;
            sd.getCustomer().setSoleDistribution(null);
            sd.getCustomer().setAccount(null);
            sd.getCustomer().setPreSaleDocuments(null);
            sd.getCustomer().setPurchaseOrders(null);
            sd.getCustomer().setEmployee(null);
        }






        return obj;
    }
}
