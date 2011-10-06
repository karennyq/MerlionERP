/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.crm.salesOrder;

import com.google.gson.Gson;
import ejb.sessionbeans.interfaces.AccountFacadeLocal;
import ejb.sessionbeans.interfaces.DeliveryOrderFacadeLocal;
import ejb.sessionbeans.interfaces.ProductFacadeLocal;
import ejb.sessionbeans.interfaces.PurchaseOrderFacadeLocal;
import ejb.sessionbeans.interfaces.SalesOrderFacadeLocal;
import ejb.sessionbeans.interfaces.TransactionFacadeLocal;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.persistence.BulkDiscount;
import org.persistence.Customer;
import org.persistence.DeliveryOrder;
import org.persistence.LineItem;
import org.persistence.Product;
import org.persistence.PurchaseOrder;
import org.persistence.SalesOrder;
import util.ConvertToJsonObject;
import util.JsonReturnDropDown;
import util.JsonReturnMsg;
import util.JsonReturnTable;

/**
 *
 * @author karennyq
 */
@WebServlet(name = "SalesOrderServlet", urlPatterns = {"/SalesOrderServlet"})
public class SalesOrderServlet extends HttpServlet {

    @EJB
    SalesOrderFacadeLocal salesOrderFacade;
    @EJB
    DeliveryOrderFacadeLocal deliveryOrderFacade;
    @EJB
    ProductFacadeLocal productFacade;
    @EJB
    AccountFacadeLocal accountFacade;
    @EJB
    PurchaseOrderFacadeLocal purchaseOrderFacade;
    @EJB
    TransactionFacadeLocal transactionFacade;
    Gson gson = new Gson();

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");
        try {
            if (action.equals("loadPage")) {
                loadPage(request, response, out);

            } else if (action.equals("getATPCheckEnum")) {
                getATPCheckEnum(request, response, out);

            } else if (action.equals("getCreditCheckEnum")) {
                getCreditCheckEnum(request, response, out);

            } else if (action.equals("getStatusEnum")) {
                getStatusEnum(request, response, out);

            } else if (action.equals("cancelSO")) {
                cancelSo(request, response, out);
                
            } else if (action.equals("addProduct")) {
                addProduct(request, response, out);

            } else if (action.equals("removeProductItem")) {
                removeProductItem(request, response, out);

            } else if (action.equals("updateProductItem")) {
                updateProductItem(request, response, out);

            } else if (action.equals("updateDiscount")) {
                updateDiscount(request, response, out);

            } else if (action.equals("getSODetails")) {
                getSODetails(request, response, out);

            } else if (action.equals("getProductList")) {
                getProductList(request, response, out);
                
            } else if (action.equals("updateSO")) {
                updateSO(request, response, out);

            }

        } catch (Exception ex) {
            String content = ex.getMessage();
            String json = gson.toJson(new JsonReturnMsg("Error", content, "error"));
            out.println(json);
            System.out.print(ex);
        } finally {
            out.close();
        }
    }

    private void loadPage(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {

        String content = request.getParameter("content");

        if (content.equals("table")) {
            //paging
            int page = (request.getParameter("page") != null) ? Integer.parseInt(request.getParameter("page")) : 1;
            int rows = (request.getParameter("rows") != null) ? Integer.parseInt(request.getParameter("rows")) : 10;
            String sort = (request.getParameter("sort") != null) ? request.getParameter("sort") : "so_id";

            //inquirer

            if (sort.equals("purchaseOrder")) {
                sort = "purchaseOrder.po_reference_id";
            }

            String order = (request.getParameter("order") != null) ? request.getParameter("order") : "asc";

            //filter
            String po_id = (request.getParameter("po_id") != null) ? request.getParameter("po_id") : "";
            String so_id = (request.getParameter("so_id") != null) ? request.getParameter("so_id") : "";
            String atpcheck = ((request.getParameter("atpCheck") != null)) ? request.getParameter("atpCheck") : "0";
            System.out.println("=====" + atpcheck);
            try {
                if (atpcheck.equals("0")) {
                    atpcheck = "";
                } else {
                    atpcheck = (Integer.parseInt(atpcheck.trim()) - 1) + "";
                }
            } catch (Exception e) {
                atpcheck = "";
            }
            System.out.println("=====" + atpcheck);


            String creditcheck = ((request.getParameter("creditCheck") != null)) ? request.getParameter("creditCheck") : "0";
            System.out.println("=====" + creditcheck);

            try {
                if (creditcheck.equals("0")) {
                    creditcheck = "";
                } else {
                    creditcheck = (Integer.parseInt(creditcheck.trim()) - 1) + "";
                }
            } catch (Exception e) {
                creditcheck = "";
            }
            System.out.println("=====" + creditcheck);

            String status = ((request.getParameter("status") != null)) ? request.getParameter("status") : "0";

            try {
                if (status.equals("0")) {
                    status = "";
                } else {
                    status = (Integer.parseInt(status.trim()) - 1) + "";
                }
            } catch (Exception e) {
                status = "";
            }

            ArrayList<SalesOrder> soList = new ArrayList<SalesOrder>(salesOrderFacade.findFilteredSalesOrder(page, rows, sort, order, po_id, so_id, atpcheck, creditcheck, status));
            int totalReord = salesOrderFacade.countFilteredSalesOrder(page, rows, sort, order, po_id, so_id, atpcheck, creditcheck, status);

            ArrayList newSoList = new ArrayList();
            for (SalesOrder so : soList) {
                Double totalAmt = so.getActual_total();
                SalesOrder newSo = new SalesOrder();
                newSo.setAtpCheck(so.getAtpCheck());
                newSo.setCompleted_date(so.getCompleted_date());
                newSo.setCreditCheck(so.getCreditCheck());
                newSo.setDate_confirmed(so.getDate_confirmed());
                newSo.setDate_creation(so.getDate_creation());
                newSo.setDeposit_requested(so.getDeposit_requested());
                newSo.setDiscount(so.getDiscount());
                newSo.setLead_time(so.getLead_time());
                PurchaseOrder newPo = new PurchaseOrder();
                newPo.setPo_reference_no(so.getPurchaseOrder().getPo_reference_no());
                Customer c = so.getPurchaseOrder().getCustomer();
                c = (Customer) ConvertToJsonObject.convert(c);
                newPo.setCustomer(c);

                newSo.setPurchaseOrder(newPo);

                newSo.setSo_id(so.getSo_id());
                newSo.setStatus(so.getStatus());
                //newSo.setActual_total();
                
                newSo.setDiscounted_total(totalAmt);
                newSoList.add(newSo);
            }

            String json = gson.toJson(new JsonReturnTable(totalReord + "", newSoList));
            System.out.println(json);
            out.println(json);

        }

    }

    private void getATPCheckEnum(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String json = JsonReturnDropDown.populate(SalesOrder.ATPCheck.values());
        out.println(json);
    }

    private void getCreditCheckEnum(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String json = JsonReturnDropDown.populate(SalesOrder.CreditCheck.values());
        out.println(json);
    }

    private void getStatusEnum(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String json = JsonReturnDropDown.populate(SalesOrder.Status.values());
        out.println(json);
    }

    private void cancelSo(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String so_id = (request.getParameter("so_id") != null) ? request.getParameter("so_id") : "";
        String content = "";
        SalesOrder so = salesOrderFacade.find(Long.parseLong(so_id));

        if (so.getStatus().equals(SalesOrder.Status.Cancelled)) {
            String json = gson.toJson(new JsonReturnMsg("Cancel Sales Order", "Sales Order already cancelled", "info"));
            out.println(json);
        } else {


            content = "Cancel Sales Order Successfully.";

            for (DeliveryOrder dlo : so.getDeliveryOrders()) {
                dlo.setStatus(DeliveryOrder.Status.Inactive);
                deliveryOrderFacade.edit(dlo);
            }
            salesOrderFacade.refresh();

            Double soAmt = so.getDiscounted_total();
            Double currentCredit = so.getPurchaseOrder().getCustomer().getAccount().getCredit_amt();
            System.out.println("CC===="+currentCredit);
            Double currentDeposit = so.getPurchaseOrder().getCustomer().getAccount().getDeposit_amt();
            System.out.println("CD===="+currentDeposit);
            Double depositAmt = so.getDeposit_requested();
                        System.out.println("Deposit===="+depositAmt);

            Double refundableAmt = so.getPurchaseOrder().getCustomer().getAccount().getRefundable_amt();
                                    System.out.println("refundableAmt===="+refundableAmt);

            if (so.getCreditCheck().equals(SalesOrder.CreditCheck.Approved)) {
                if (depositAmt == 0.00) {
                    so.getPurchaseOrder().getCustomer().getAccount().setCredit_amt(currentCredit - soAmt);
                    accountFacade.edit(so.getPurchaseOrder().getCustomer().getAccount());
                    transactionFacade.createTransaction(so.getPurchaseOrder().getCustomer().getAccount().getAccount_id() + "", soAmt + "", "Credit", "Debit");
                    content = "Cancel Sales Order Successfully.";
                    salesOrderFacade.refresh();
                } else {
                    if (so.getStatus().equals(SalesOrder.Status.Confirmed)) {
                        if (so.getDeposit_requested() != 0.00) {
                            Double newDeposit = currentDeposit - depositAmt;
                            so.getPurchaseOrder().getCustomer().getAccount().setDeposit_amt(newDeposit);
                            transactionFacade.createTransaction(so.getPurchaseOrder().getCustomer().getAccount().getAccount_id() + "", depositAmt + "", "Deposit", "Debit");
                        }
                        
                                                    Double newCredit = currentCredit - soAmt + depositAmt;

                        so.getPurchaseOrder().getCustomer().getAccount().setCredit_amt(newCredit);
                        accountFacade.edit(so.getPurchaseOrder().getCustomer().getAccount());
                        transactionFacade.createTransaction(so.getPurchaseOrder().getCustomer().getAccount().getAccount_id() + "", soAmt - depositAmt + "", "Credit", "Debit");
                        content = "Cancel Sales Order Successfully with deposit amount of " + depositAmt + " will be forfeited.";
                        salesOrderFacade.refresh();
                    } else if (so.getStatus().equals(SalesOrder.Status.Pending)) {
                        if (so.getDeposit_requested() != 0.00) {
                            so.getPurchaseOrder().getCustomer().getAccount().setDeposit_amt(currentDeposit - depositAmt);
                            so.getPurchaseOrder().getCustomer().getAccount().setRefundable_amt(refundableAmt + depositAmt);
                            accountFacade.edit(so.getPurchaseOrder().getCustomer().getAccount());
                            transactionFacade.createTransaction(so.getPurchaseOrder().getCustomer().getAccount().getAccount_id() + "", depositAmt + "", "Deposit", "Debit");
                            transactionFacade.createTransaction(so.getPurchaseOrder().getCustomer().getAccount().getAccount_id() + "", depositAmt + "", "Refund", "Credit");
                        }
                        so.getPurchaseOrder().getCustomer().getAccount().setCredit_amt(currentCredit - (soAmt - depositAmt));
                        transactionFacade.createTransaction(so.getPurchaseOrder().getCustomer().getAccount().getAccount_id() + "", depositAmt + "", "Credit", "Debit");
                        salesOrderFacade.refresh();
                        content = "Cancel Sales Order Successfully.";


                    }
                }
            }

            so.setDeposit_requested(0.00);
            salesOrderFacade.edit(so);
            salesOrderFacade.refresh();


            if (so.getAtpCheck().equals(SalesOrder.ATPCheck.Sufficient)) { // ----- need to change... now only check against the current inventory  
                for (LineItem li : so.getLineItems()) {
                    Product p = li.getProduct();
                    Integer currentAvailable = p.getAvail_boxes();
                    Integer currentReserved = p.getReserved_boxes();
                    Integer qty = li.getQuantity();
                    p.setAvail_boxes(currentAvailable + qty);
                    p.setReserved_boxes(currentReserved - qty);
                    productFacade.edit(p);
                    salesOrderFacade.refresh();
                }
            }


            //salesOrderFacade.edit(so);

            so.setStatus(SalesOrder.Status.Cancelled);
            salesOrderFacade.edit(so);
            salesOrderFacade.refresh();
            so.getPurchaseOrder().setStatus(PurchaseOrder.Status.Inactive);
            purchaseOrderFacade.edit(so.getPurchaseOrder());
            salesOrderFacade.edit(so);
            salesOrderFacade.refresh();

            String json = gson.toJson(new JsonReturnMsg("Cancel Sales Order", content, "info"));
            out.println(json);
        }
    }
    
    private void addProduct(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String listName = request.getParameter("listName");
        HttpSession session = request.getSession();
        ArrayList<LineItem> lineItemList = new ArrayList<LineItem>();
        String pdtId = request.getParameter("product_id");
        Integer qty;
        String json;
        if (request.getParameter("pdt_qty").isEmpty()) {
            qty = null;
        } else {
            qty = Integer.parseInt(request.getParameter("pdt_qty"));
        }

        if (pdtId.isEmpty() && qty == null) {
            json = gson.toJson(new JsonReturnMsg("Error", "Please select a product and enter the quantity!", "error"));
            out.println(json);

        } else if (pdtId.isEmpty()) {
            json = gson.toJson(new JsonReturnMsg("Error", "Please select a product!", "error"));
            out.println(json);

        } else if (qty == null) {
            json = gson.toJson(new JsonReturnMsg("Error", "Please enter the quantity!", "error"));
            out.println(json);

        } else {
            lineItemList = (ArrayList) session.getAttribute(listName);
            LineItem li = new LineItem();
            int index = -1;
            int previousQty = 0;
            for (LineItem li1 : lineItemList) {
                if (li1.getProduct().getProduct_id().equals(Long.parseLong(pdtId))) {
                    li = li1;
                    index = lineItemList.indexOf(li1);
                    previousQty = li.getQuantity();
                }
            }
            li.setQuantity(previousQty + qty);
            li.setBulk_discount(0.0);
            int pdtQty = li.getQuantity();
            int boxReq = 0;
            Product p = productFacade.find(Long.parseLong(pdtId));

            if (p.getAvail_boxes() >= pdtQty) {
                li.setIndicative_lead_time(5); //this is hardcoded ====================================
            } else {
                // see current production plan
                li.setIndicative_lead_time(10); //fake one....
            }

            for (BulkDiscount bd : p.getBulkDiscounts()) {
                if (pdtQty >= bd.getBoxes_required()) {
                    if (boxReq < bd.getBoxes_required()) {
                        boxReq = bd.getBoxes_required();
                        li.setBulk_discount(bd.getDiscount_given());
                    }
                }
            }

            li.setProduct(p);
            li.setBase_price(p.getPrice_per_unit());
            li.setActual_price();

            if (index == -1) {
                lineItemList.add(li);
            } else {
                lineItemList.set(index, li);
            }

            session.setAttribute(listName, lineItemList);
            json = gson.toJson(new JsonReturnMsg("Info", "Product Add Successfully!", "info"));
            out.println(json);
        }
    }
    
    private void removeProductItem(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String listName = request.getParameter("listName");
        HttpSession session = request.getSession();
        ArrayList<LineItem> liList = new ArrayList<LineItem>();

        int listIndex = Integer.parseInt(request.getParameter("listIndex"));
        liList = (ArrayList) session.getAttribute(listName);
        LineItem li = liList.get(listIndex);
        liList.remove(li);
        session.setAttribute(listName, liList);
        String json = gson.toJson(new JsonReturnMsg("Info", "Product Deleted!", "info"));
        out.println(json);
    }
    
    private void updateProductItem(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String listName = request.getParameter("listName");
        HttpSession session = request.getSession();

        int listIndex = Integer.parseInt(request.getParameter("listIndex"));
        int update_qty = Integer.parseInt(request.getParameter("update_qty"));

        ArrayList<LineItem> liList = (ArrayList) session.getAttribute(listName);
        LineItem li = liList.get(listIndex);
        li.setQuantity(update_qty);
        li.setBulk_discount(0.0);

        int pdtQty = li.getQuantity();
        int boxReq = 0;
        long pdtId = li.getProduct().getProduct_id();
        Product p = productFacade.find(pdtId);

        if (p.getAvail_boxes() >= pdtQty) {
            li.setIndicative_lead_time(5); //this is hardcoded ====================================
        } else {
            // see current production plan
            li.setIndicative_lead_time(10); //fake one....
        }

        for (BulkDiscount bd: p.getBulkDiscounts()) {
            if (pdtQty >= bd.getBoxes_required()) {
                if (boxReq < bd.getBoxes_required()) {
                    boxReq = bd.getBoxes_required();
                    li.setBulk_discount(bd.getDiscount_given());
                }
            }
        }

        li.setActual_price();

        session.setAttribute(listName, liList);
        String json = gson.toJson(new JsonReturnMsg("Info", "Product Updated.", "info"));
        out.println(json);
    }
    
    private void updateDiscount(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String discountName = request.getParameter("discountName");
        HttpSession session = request.getSession();
        double add_disc = 0.0;
        try {
            add_disc = Double.parseDouble(request.getParameter("add_disc"));
        } catch (Exception e) {
            String json = gson.toJson(new JsonReturnMsg("Create Sales Quotation", "Please enter a correct discount value.", "error"));
            out.println(json);
        }

        if (add_disc < 0 || add_disc > 100.00) {
            String json = gson.toJson(new JsonReturnMsg("Create Sales Quotation", "Please enter a correct discount value.", "error"));
            out.println(json);

        } else {
            session.setAttribute(discountName, add_disc);
            String json = gson.toJson(new JsonReturnMsg("Info", "Additional Discount Updated.", "info"));
            out.println(json);
        }
    }
    
    private void getSODetails(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String json = "";

        String so_id = request.getParameter("so_id");
        String listName = request.getParameter("listName");
        String discountName = request.getParameter("discountName");

        HttpSession session = request.getSession();
        SalesOrder so = salesOrderFacade.find(Long.parseLong(so_id));
        ArrayList<LineItem> a = new ArrayList<LineItem>();
        for (LineItem li : so.getLineItems()) {
            a.add(li);
        }
        session.setAttribute(listName, a);
        session.setAttribute(discountName, so.getDiscount());

        Customer c = so.getPurchaseOrder().getCustomer();
        c.setAccount(null);
        c.setPurchaseOrders(null);
        c.setEmployee(null);
        c.setPurchaseOrders(null);
        c.setPreSaleDocuments(null);
        c.setSoleDistribution(null);
        so.setLineItems(null);
        so.setBackOrders(null);
        for(DeliveryOrder o: so.getDeliveryOrders()){
                o.setSalesOrder(null);
                o.setDeliveryOrderDetails(null);
        }
        so.setPurchaseOrder(new PurchaseOrder());
        so.getPurchaseOrder().setCustomer(c);

        json = gson.toJson(so);
        System.out.println(json);
        out.println(json);
    }
    
    private void getProductList(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        HttpSession session = request.getSession();

        String listName = request.getParameter("listName");
        String discountName = request.getParameter("discountName");
        String reset = request.getParameter("reset");

        if (reset != null && reset.equals("true")) {
            session.setAttribute(listName, new ArrayList());
            session.setAttribute(discountName, "0.00");

        } else {
            ArrayList<LineItem> liList = (ArrayList<LineItem>) session.getAttribute(listName);
            int totalRecord = liList.size();
            Double totalPrice = 0.0;
            Integer totalLeadTime = 0;

            for (LineItem li : liList) {
                totalPrice = totalPrice + li.getActual_price();

                li = (LineItem) ConvertToJsonObject.convert(li);

                if (li.getIndicative_lead_time() == null) {
                    //ATP check to be done
                    int pdtQty = li.getQuantity();
                    int boxReq = 0;
                    Product p = productFacade.find(li.getProduct().getProduct_id());

                    if (p.getAvail_boxes() >= pdtQty) {
                        li.setIndicative_lead_time(5); //this is hardcoded ====================================
                    } else {
                        // see current production plan
                        li.setIndicative_lead_time(10); //fake one....
                    }

                    for (BulkDiscount bd : p.getBulkDiscounts()) {
                        if (pdtQty >= bd.getBoxes_required()) {
                            if (boxReq < bd.getBoxes_required()) {
                                boxReq = bd.getBoxes_required();
                                li.setBulk_discount(bd.getDiscount_given());
                            }
                        }
                    }
                }
                
                if (li.getBulk_discount() == null) {
                    li.setBulk_discount(0.0);
                }

                if (li.getIndicative_lead_time() >= totalLeadTime) {
                    totalLeadTime = li.getIndicative_lead_time();
                }
            }

            FooterItem fi = new FooterItem("Sub Total:", totalPrice);
            ArrayList li = new ArrayList();
            
            double disc = Double.parseDouble(session.getAttribute(discountName).toString());
            FooterItem fi2 = new FooterItem("Add. Discount:", disc);
            ArrayList li2 = new ArrayList();
            
            double netTotal = totalPrice * (1 - disc / 100);
            FooterItem fi3 = new FooterItem("Net Total:", netTotal);
            ArrayList li3 = new ArrayList();

            FooterItem fi4 = new FooterItem("Lead Time:", totalLeadTime);
            ArrayList li4 = new ArrayList();
            
            li.add(fi);
            li.add(fi2);
            li.add(fi3);
            li.add(fi4);

            String json = gson.toJson(new JsonReturnTable(totalRecord + "", liList, li));
            out.println(json);
        }
    }
    
    private class FooterItem {

        private String bulk_discount;
        private double actual_price;
        //private Integer indicative_lead_time;

        public FooterItem(String bulk_discount, double actual_price) {
            this.bulk_discount = bulk_discount;
            this.actual_price = actual_price;
            //this.indicative_lead_time = indicative_lead_time;
        }
    }
    
    private void updateSO(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String json;

        String so_id = request.getParameter("so_id");
        String destination = request.getParameter("destination");
        String listName = request.getParameter("listName");
        String discountName = request.getParameter("discountName");

        HttpSession session = request.getSession();
        ArrayList<LineItem> lineItemList = (ArrayList<LineItem>) session.getAttribute(listName);
        Double add_disc = (Double) session.getAttribute(discountName);

        if (lineItemList.isEmpty() || lineItemList == null) {
            json = gson.toJson(new JsonReturnMsg("Error", "Please add a product!", "error"));
            out.println(json);

        } else {
            SalesOrder so = salesOrderFacade.find(Long.parseLong(so_id));
            so.setDiscount(add_disc);
            salesOrderFacade.updateSalesOrder(so, lineItemList, destination);
            json = gson.toJson(new JsonReturnMsg("Info", "Sales Order Updated Successfully", "info"));
            out.println(json);
        }
    }


    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
