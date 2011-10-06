/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.crm.purchaseOrder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ejb.sessionbeans.interfaces.AccountFacadeLocal;
import ejb.sessionbeans.interfaces.ProductFacadeLocal;
import ejb.sessionbeans.interfaces.PurchaseOrderFacadeLocal;
import ejb.sessionbeans.interfaces.SalesInquiryFacadeLocal;
import ejb.sessionbeans.interfaces.SalesOrderFacadeLocal;
import ejb.sessionbeans.interfaces.SalesQuotationFacadeLocal;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.persistence.Account;
import org.persistence.BulkDiscount;
import org.persistence.Customer;
import org.persistence.LineItem;
import org.persistence.Product;
import org.persistence.PurchaseOrder;
import org.persistence.SalesInquiry;
import org.persistence.SalesLead;
import org.persistence.SalesOrder;
import org.persistence.SalesQuotation;
import util.ConvertToJsonObject;
import util.JsonReturnDropDown;
import util.JsonReturnMsg;
import util.JsonReturnTable;

/**
 *
 * @author karennyq
 */
@WebServlet(name = "PurchaseOrderServlet", urlPatterns = {"/PurchaseOrderServlet"})
public class PurchaseOrderServlet extends HttpServlet {

    @EJB
    ProductFacadeLocal productFacade;
    @EJB
    SalesInquiryFacadeLocal salesInquiryFacade;
    @EJB
    SalesQuotationFacadeLocal salesQuotationFacade;
    @EJB
    PurchaseOrderFacadeLocal purchaseOrderFacade;
    @EJB
    SalesOrderFacadeLocal salesOrderFacade;
    @EJB
    AccountFacadeLocal accountFacade;
    Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");
        try {
            /* TODO output your page here
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet PurchaseOrderServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PurchaseOrderServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
             */
            if (action.equals("loadPage")) {
                loadPage(request, response, out);
            } else if (action.equals("addProduct")) {
                addProduct(request, response, out);
            } else if (action.equals("removeProductItem")) {
                removeProductItem(request, response, out);
            } else if (action.equals("updateProductItem")) {
                updateProductItem(request, response, out);
            } else if (action.equals("updateDiscount")) {
                updateDiscount(request, response, out);
            } else if (action.contains("createPurchaseOrder")) {
                createPurchaseOrder(request, response, out);
            } else if (action.contains("updatePurchaseOrder")) {
                updatePurchaseOrder(request, response, out);
            } else if (action.contains("convertToSO")) {
                convertToSalesOrder(request, response, out);
            } else if (action.contains("checkConvertToSO")) {
                checkConvertToSalesOrder(request, response, out);
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
        HttpSession session = request.getSession();
        ArrayList<LineItem> lineItemList = new ArrayList<LineItem>();
        String lineItemListSession = (request.getParameter("lineItemListSession") != null) ? request.getParameter("lineItemListSession") : "";
        String discountSession = (request.getParameter("discountSession") != null) ? request.getParameter("discountSession") : "";

        if (content.equals("statusDropdown")) {
            String json = JsonReturnDropDown.populate(PurchaseOrder.ApprovedStatus.values());
            out.println(json);
        } else if (content.equals("pdtTable")) {
            System.out.println(lineItemListSession);
            lineItemList = (ArrayList<LineItem>) session.getAttribute(lineItemListSession);
            int totalRecord = (lineItemList != null) ? lineItemList.size() : 0;

            Double totalPrice = 0.0;
            Integer totalLeadTime = 0;
            ArrayList newLiList = new ArrayList();

            for (LineItem li1 : lineItemList) {
                li1.setActual_price();
                totalPrice = totalPrice + li1.getActual_price();

                LineItem newLi = new LineItem();
                newLi.setBase_price(li1.getBase_price());
                newLi.setBulk_discount(li1.getBulk_discount());
                newLi.setIndicative_lead_time(li1.getIndicative_lead_time());
                Product p = li1.getProduct();
                p.setDeliveryOrderDetails(null);
                p.setPdtBatches(null);
                p.setSalesForecasts(null);
                p.setIngredients(null);

                newLi.setProduct(p);
                newLi.setQuantity(li1.getQuantity());
                newLi.setActual_price();

                if (li1.getIndicative_lead_time() >= totalLeadTime) {
                    totalLeadTime = li1.getIndicative_lead_time();
                }

                newLiList.add(newLi);
            }

            FooterItem fi = new FooterItem("Sub Total:", totalPrice);
            ArrayList li = new ArrayList();

            double disc = Double.parseDouble(session.getAttribute(discountSession).toString());
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

            String json = gson.toJson(new JsonReturnTable(totalRecord + "", newLiList, li));
            System.out.println(json);
            out.println(json);

        } else if (content.equals("quotationDetailsForPO")) {
            long qId = Long.parseLong(request.getParameter("quotation_id"));
            SalesInquiry sq = salesInquiryFacade.find(qId);
            //SalesQuotation sq = (SalesQuotation)si;

            String json = "";
            //lineItemList = lineItemFacade.findLineItems(qId);

            if (sq.getInquirer().getConvert_status() == null) {

                ArrayList a = new ArrayList();
                for (LineItem li : sq.getLineItems()) {
                    LineItem newli = new LineItem();
                    newli.setBase_price(li.getBase_price());
                    newli.setBulk_discount(li.getBulk_discount());
                    newli.setIndicative_lead_time(li.getIndicative_lead_time());
                    newli.setProduct(li.getProduct());
                    newli.setQuantity(li.getQuantity());
                    newli.setActual_price();
                    a.add(newli);
                }
                session.setAttribute(lineItemListSession, a);

                SalesQuotation sq1 = (SalesQuotation) sq;

                SalesQuotation newSq = new SalesQuotation();

                newSq.setExpiry_date(sq1.getExpiry_date());
                newSq.setIndicative_lead_time(sq1.getIndicative_lead_time());

                Customer c = (Customer) sq1.getInquirer();
                c.setAccount(null);
                c.setPreSaleDocuments(new ArrayList());
                c.setPurchaseOrders(new ArrayList());
                c.setSoleDistribution(new ArrayList());
                c.setEmployee(null);
                sq1.setInquirer(c);

                newSq.setInquirer(c);

                newSq.setInquiryStatus(sq1.getInquiryStatus());
                newSq.setPre_sale_doc_id(sq1.getPre_sale_doc_id());
                newSq.setRemarks(sq1.getRemarks());
                newSq.setRequest_date(sq1.getRequest_date());
                newSq.setStatus(sq1.getStatus());

                json = gson.toJson(newSq);
                // session.setAttribute("createPurchaseOrderLineItemList",(ArrayList)sq.getLineItems());
                session.setAttribute(discountSession, sq1.getDiscount());
            } else if (sq.getInquirer().getConvert_status().equals(SalesLead.ConvertStatus.Not_Converted)) {
                json = gson.toJson(new JsonReturnMsg("Quotation ID: " + qId, "Client ID: " + sq.getInquirer().getInquirer_id() + " is not yet a customer. &emsp;&emsp;Do you want to convert now?", "error"));
            }
            System.out.println(json);
            out.println(json);
        } else if (content.equals("quotationDialog")) {
            ArrayList<SalesQuotation> saleQuotationList = new ArrayList<SalesQuotation>(salesQuotationFacade.findAllQuotation());
            int totalReord = salesQuotationFacade.countAllQuotation();

            for (SalesQuotation sl : saleQuotationList) {
                sl = (SalesQuotation) ConvertToJsonObject.convert(sl);
                if (sl.getInquirer() instanceof Customer) {
                    ((Customer) sl.getInquirer()).setEmployee(null);
                }
            }

            String json = gson.toJson(new JsonReturnTable(totalReord + "", saleQuotationList));
            System.out.println(json);
            out.println(json);
        } else if (content.equals("table")) {
            //paging
            int page = (request.getParameter("page") != null) ? Integer.parseInt(request.getParameter("page")) : 1;
            int rows = (request.getParameter("rows") != null) ? Integer.parseInt(request.getParameter("rows")) : 10;
            String sort = (request.getParameter("sort") != null) ? request.getParameter("sort") : "po_id";

            //inquirer

            if (sort.equals("customer")) {
                sort = "customer.inquirer_id";
            }

            if (sort.equals("salesQuotation")) {
                sort = "salesQuotation.pre_sale_doc_id";
            }

            String order = (request.getParameter("order") != null) ? request.getParameter("order") : "asc";

            SimpleDateFormat sdf = new SimpleDateFormat(("dd/MM/yyyy"));

            //filter
            String po_ref_no = (request.getParameter("po_ref_no") != null) ? request.getParameter("po_ref_no") : "";
            String inquirer_id = (request.getParameter("inquirer_id") != null) ? request.getParameter("inquirer_id") : "";
            String approved_status = ((request.getParameter("approved_status") != null)) ? request.getParameter("approved_status") : "0";

            try {
                if (approved_status.equals("0")) {
                    approved_status = "";
                } else {
                    approved_status = (Integer.parseInt(approved_status.trim()) - 1) + "";
                }
            } catch (Exception e) {
                approved_status = "";
            }

            ArrayList<PurchaseOrder> poList = new ArrayList<PurchaseOrder>(purchaseOrderFacade.findFilteredPurchaseOrder(page, rows, sort, order, po_ref_no, inquirer_id, approved_status));
            int totalReord = purchaseOrderFacade.countFilteredPurchaseOrder(page, rows, sort, order, po_ref_no, inquirer_id, approved_status);
            for (PurchaseOrder po : poList) {
                po.setDiscounted_total();
                po = (PurchaseOrder) ConvertToJsonObject.convert(po);
            }
            
        

            String json = gson.toJson(new JsonReturnTable(totalReord + "", poList));
            System.out.println(json);
            out.println(json);

        } else if (content.equals("approvedStatusDropdown")) {
            //String selected= request.getParameter("selected");
            String json = JsonReturnDropDown.populate(PurchaseOrder.ApprovedStatus.values());
            out.println(json);
        } else if (content.equals("purchaseOrderDetails")) {
            String po_id = (request.getParameter("po_id") != null) ? request.getParameter("po_id") : "";
            System.out.println("===============================" + lineItemListSession);
            session.setAttribute(lineItemListSession, new ArrayList());
            session.setAttribute(discountSession, 0.00);

            PurchaseOrder po = purchaseOrderFacade.find(Long.parseLong(po_id));

            PurchaseOrder newPO = new PurchaseOrder();
            newPO.setPo_id(po.getPo_id());
            newPO.setPo_reference_no(po.getPo_reference_no());
            newPO.setSent_date(po.getSent_date());
            newPO.setShipping_Address(po.getShipping_Address());
            newPO.setRemarks(po.getRemarks());
            newPO.setReceived_date(po.getReceived_date());
            newPO.setApproved_status(po.getApproved_status());

            Customer c = new Customer();
            c.setInquirer_id(po.getCustomer().getInquirer_id());
            c.setCompany_name(po.getCustomer().getCompany_name());
            c.setCompany_add(po.getCustomer().getCompany_add());
            c.setContact_person(po.getCustomer().getContact_person());
            c.setContact_no(po.getCustomer().getContact_no());
            c.setEmail(po.getCustomer().getEmail());
            c.setFax_no(po.getCustomer().getFax_no());
            c.setCust_type(po.getCustomer().getCust_type());
            newPO.setCustomer(c);

            ArrayList<LineItem> a = new ArrayList<LineItem>();
            for (LineItem li : po.getLineItems()) {
                LineItem newli = new LineItem();
                newli.setBase_price(li.getBase_price());
                newli.setBulk_discount(li.getBulk_discount());
                newli.setIndicative_lead_time(li.getIndicative_lead_time());
                newli.setProduct(li.getProduct());
                newli.setQuantity(li.getQuantity());
                newli.setActual_price();
                a.add(newli);
            }

            System.out.println("========================" + po.getLineItems().size());

            session.setAttribute(lineItemListSession, a);

            String json = gson.toJson(newPO);
            // session.setAttribute("createPurchaseOrderLineItemList",(ArrayList)sq.getLineItems());
            session.setAttribute(discountSession, po.getDiscount());
            System.out.println(json);
            out.println(json);


        }


    }

    private void updateProductItem(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        HttpSession session = request.getSession();
        ArrayList<LineItem> lineItemList = new ArrayList<LineItem>();
        String lineItemListSession = (request.getParameter("lineItemListSession") != null) ? request.getParameter("lineItemListSession") : "";
        String discountSession = (request.getParameter("discountSession") != null) ? request.getParameter("discountSession") : "";

        int listIndex = Integer.parseInt(request.getParameter("listIndex"));

        int update_qty = Integer.parseInt(request.getParameter("update_qty"));

        lineItemList = (ArrayList<LineItem>) session.getAttribute(lineItemListSession);
        LineItem li = lineItemList.get(listIndex);
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

        for (BulkDiscount bd : p.getBulkDiscounts()) {
            if (pdtQty >= bd.getBoxes_required()) {
                if (boxReq < bd.getBoxes_required()) {
                    boxReq = bd.getBoxes_required();
                    li.setBulk_discount(bd.getDiscount_given());
                }
            }
        }

        li.setActual_price();

        session.setAttribute(lineItemListSession, lineItemList);
        String json = gson.toJson(new JsonReturnMsg("Info", "Product Updated.", "info"));
        out.println(json);

    }

    private void addProduct(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {

        HttpSession session = request.getSession();
        String lineItemListSession = (request.getParameter("lineItemListSession") != null) ? request.getParameter("lineItemListSession") : "";
        String discountSession = (request.getParameter("discountSession") != null) ? request.getParameter("discountSession") : "";

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
            lineItemList = (ArrayList<LineItem>) session.getAttribute(lineItemListSession);
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
            li.setBase_price(p.getPrice_per_box());
            li.setActual_price();

            if (index == -1) {
                lineItemList.add(li);
            } else {
                lineItemList.set(index, li);
            }

            session.setAttribute(lineItemListSession, lineItemList);
            json = gson.toJson(new JsonReturnMsg("Info", "Product Add Successfully!", "info"));
            out.println(json);

        }

    }

    private void updateDiscount(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        HttpSession session = request.getSession();
        String lineItemListSession = (request.getParameter("lineItemListSession") != null) ? request.getParameter("lineItemListSession") : "";
        String discountSession = (request.getParameter("discountSession") != null) ? request.getParameter("discountSession") : "";

        double add_disc = Double.parseDouble(request.getParameter("add_disc"));
        session.setAttribute(discountSession, add_disc);
        String json = gson.toJson(new JsonReturnMsg("Info", "Additional Discount Updated.", "info"));
        System.out.println(json);
        out.println(json);

    }

    private void removeProductItem(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {

        HttpSession session = request.getSession();
        ArrayList<LineItem> lineItemList = new ArrayList<LineItem>();

        String lineItemListSession = (request.getParameter("lineItemListSession") != null) ? request.getParameter("lineItemListSession") : "";
        String discountSession = (request.getParameter("discountSession") != null) ? request.getParameter("discountSession") : "";

        int listIndex = Integer.parseInt(request.getParameter("listIndex"));
        lineItemList = (ArrayList<LineItem>) session.getAttribute(lineItemListSession);
        LineItem li = lineItemList.get(listIndex);
        lineItemList.remove(li);
        session.setAttribute(lineItemListSession, lineItemList);
        String json = gson.toJson(new JsonReturnMsg("Info", "Product Deleted!", "info"));
        out.println(json);
    }

    private void checkConvertToSalesOrder(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        HttpSession session = request.getSession();
        String po_id = request.getParameter("po_id");
        PurchaseOrder po = purchaseOrderFacade.find(Long.parseLong(po_id));
        if (po.getSalesOrder() != null) {
            String json = gson.toJson(new JsonReturnMsg("Convert to SO", "SO already converted.", "error"));
            out.println(json);
        } else {
            String json = gson.toJson(new JsonReturnMsg("Convert to SO", "SO not yet converted.", "info"));
            out.println(json);
        }
    }

    private void convertToSalesOrder(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        HttpSession session = request.getSession();
        String po_id = request.getParameter("po_id");

        PurchaseOrder po = purchaseOrderFacade.find(Long.parseLong(po_id));

        if (po.getSalesOrder() == null) {

            SalesOrder so = salesOrderFacade.create(po);
            po.setSalesOrder(so);
            purchaseOrderFacade.edit(po);

            //Credit Check
            if (po.getCustomer().getAccount().getAccountStatus().equals(Account.AccountStatus.New)) {
                po.getSalesOrder().setCreditCheck(SalesOrder.CreditCheck.Pending);
                salesOrderFacade.edit(po.getSalesOrder());
                purchaseOrderFacade.edit(po);
            } else if (po.getCustomer().getAccount().getAccountStatus().equals(Account.AccountStatus.Blacklist)) {
                po.getSalesOrder().setCreditCheck(SalesOrder.CreditCheck.Not_Approved);
                salesOrderFacade.edit(po.getSalesOrder());
            } else {
                Double amtToDeposit = accountFacade.amtToDeposit(po.getCustomer().getInquirer_id(), po.getDiscounted_total());
                if (accountFacade.requestForDeposit(po.getCustomer().getInquirer_id(), po.getDiscounted_total())) {
                    po.getSalesOrder().setDeposit_requested(amtToDeposit);
                    po.getSalesOrder().setCreditCheck(SalesOrder.CreditCheck.Pending);
                    po.getCustomer().getAccount().setCredit_amt(po.getCustomer().getAccount().getMax_credit_limit());
                    salesOrderFacade.edit(po.getSalesOrder());
                } else {
                    po.getSalesOrder().setDeposit_requested(amtToDeposit);
                    po.getSalesOrder().setCreditCheck(SalesOrder.CreditCheck.Approved);
                    salesOrderFacade.edit(po.getSalesOrder());

                    Double creditAmt = po.getCustomer().getAccount().getCredit_amt();
                    po.getCustomer().getAccount().setCredit_amt(creditAmt + po.getDiscounted_total() - po.getSalesOrder().getDeposit_requested());
                    accountFacade.edit(po.getCustomer().getAccount());
                }
                purchaseOrderFacade.edit(po);
            }

            //ATP Check  ----- need to change... now only check against the current inventory  
            for (LineItem li : po.getLineItems()) {
                Product p = li.getProduct();
                if (p.getAvail_boxes() >= li.getQuantity()) {
                    po.getSalesOrder().setAtpCheck(SalesOrder.ATPCheck.Sufficient);

                    Integer total = li.getProduct().getAvail_boxes();
                    Integer qty = li.getQuantity();
                    Integer reserved = li.getProduct().getReserved_boxes();
                    li.getProduct().setAvail_boxes(total - qty);
                    li.getProduct().setReserved_boxes(reserved + qty);



                } else {
                    po.getSalesOrder().setAtpCheck(SalesOrder.ATPCheck.Not_Sufficient);
                    po.getSalesOrder().setStatus(SalesOrder.Status.Pending);
                    break;
                }
            }
            salesOrderFacade.edit(po.getSalesOrder());
            purchaseOrderFacade.edit(po);

            po = purchaseOrderFacade.find(Long.parseLong(po_id));

            System.out.println(po.getSalesOrder().getAtpCheck());

            if (po.getSalesOrder().getAtpCheck().equals(SalesOrder.ATPCheck.Sufficient)
                    && po.getSalesOrder().getCreditCheck().equals(SalesOrder.CreditCheck.Approved)) {
                po.getSalesOrder().setStatus(SalesOrder.Status.Confirmed);
                po.getSalesOrder().setDate_confirmed(new Date());
            }

            purchaseOrderFacade.edit(po);


            String json = gson.toJson(new JsonReturnMsg("Convert to Sales Order", "Converted to Sales Order Successfully.", "info"));
            System.out.println(json);
            out.println(json);
        } else {
            String json = gson.toJson(new JsonReturnMsg("Convert to Sales Order", "You have converted this Purchase Order before.", "error"));
            System.out.println(json);
            out.println(json);

        }





    }

    private void createPurchaseOrder(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        System.out.println("======================== CREATE PO ================");
        HttpSession session = request.getSession();
        String lineItemListSession = (request.getParameter("lineItemListSession") != null) ? request.getParameter("lineItemListSession") : "";
        String discountSession = (request.getParameter("discountSession") != null) ? request.getParameter("discountSession") : "";

        String quotation_id = request.getParameter("quotation_id_hidden");
        System.out.println("======================== " + quotation_id + " ================");

        String po_ref_no = request.getParameter("po_ref_no");
        String sent_date = request.getParameter("sent_date");
        String shipping_address = request.getParameter("shipping_address");
        String remarks = request.getParameter("remarks");
        String status = request.getParameter("status");

        String json = "";
        if (sent_date.equals("") || sent_date == null) {
            json = gson.toJson(new JsonReturnMsg("Create Purchase Order", "Sent Date is empty.", "error"));
            System.out.println(json);
            out.println(json);
        } else {

            SimpleDateFormat sdf = new SimpleDateFormat(("dd/MM/yyyy"));
            Date sentDate = sdf.parse(sent_date);
            if (sentDate.getTime() > new Date().getTime()) {
                json = gson.toJson(new JsonReturnMsg("Create Purchase Order", "Sent date is incorrect. It should not be later than received date.", "error"));
                System.out.println(json);
                out.println(json);
            } else {
                double add_disc = Double.parseDouble(session.getAttribute(discountSession).toString());

                ArrayList<LineItem> lineItemList = (ArrayList<LineItem>) session.getAttribute(lineItemListSession);

                if (lineItemList.isEmpty() || lineItemList == null) {
                    json = gson.toJson(new JsonReturnMsg("Create Purchase Order", "Please add a product!", "error"));
                    out.println(json);

                } else {

                    long id = Long.parseLong(quotation_id.trim());
                    SalesQuotation sq = salesQuotationFacade.find(id);

                    if (sq.getInquirer() == null) {
                        json = gson.toJson(new JsonReturnMsg("Create Purchase Order", "Customer does not exist.", "error"));
                        out.println(json);
                    } else {
                        int counter = 0;
                        for (PurchaseOrder po1 : purchaseOrderFacade.findAll()) {
                            if (po1.getCustomer().getInquirer_id().equals(sq.getInquirer().getInquirer_id()) && po1.getPo_reference_no().equals(po_ref_no)) {
                                json = gson.toJson(new JsonReturnMsg("Create Purchase Order", "Purchase Order already exist.", "error"));
                                session.setAttribute(lineItemListSession, new ArrayList());
                                session.setAttribute(discountSession, 0.00);
                                System.out.println(json);
                                out.println(json);
                                counter = 1;
                                break;
                            }
                        }

                        if (counter == 0) {
                            PurchaseOrder po = purchaseOrderFacade.create(sq, lineItemList, add_disc, po_ref_no, sentDate, shipping_address, remarks, status);

                            json = gson.toJson(new JsonReturnMsg("Create Purchase Order", "Purchase Order Created Successfully.", "info"));
                            session.setAttribute(lineItemListSession, new ArrayList());
                            session.setAttribute(discountSession, 0.00);
                            System.out.println(json);
                            out.println(json);
                        }

                    }

                }

            }

        }
    }

    private void updatePurchaseOrder(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        HttpSession session = request.getSession();
        String lineItemListSession = (request.getParameter("lineItemListSession") != null) ? request.getParameter("lineItemListSession") : "";
        String discountSession = (request.getParameter("discountSession") != null) ? request.getParameter("discountSession") : "";

        String po_id = request.getParameter("po_id_hidden");
        System.out.println("======================== " + po_id + " ================");

        String po_ref_no = request.getParameter("po_ref_no");
        String sent_date = request.getParameter("sent_date");
        String shipping_address = request.getParameter("shipping_address");
        String remarks = request.getParameter("remarks");
        String status = request.getParameter("status");


        PurchaseOrder po = purchaseOrderFacade.find(Long.parseLong(po_id));

        double add_disc = Double.parseDouble(session.getAttribute(discountSession).toString());

        ArrayList<LineItem> lineItemList = (ArrayList<LineItem>) session.getAttribute(lineItemListSession);


        String json = "";

        SimpleDateFormat sdf = new SimpleDateFormat(("dd/MM/yyyy"));
        Date sentDate = sdf.parse(sent_date);

        if (sentDate.getTime() > po.getReceived_date().getTime()) {
            json = gson.toJson(new JsonReturnMsg("Update Purchase Order", "Sent date is incorrect. It should not be later than received date.", "error"));
            out.println(json);
        } else {
            if (lineItemList.isEmpty() || lineItemList == null) {
                json = gson.toJson(new JsonReturnMsg("Update Purchase Order", "Please add a product!", "error"));
                out.println(json);

            } else {
                po.setSent_date(sentDate);
                po.setPo_reference_no(po_ref_no);
                po.setShipping_Address(shipping_address);
                po.setRemarks(remarks);
                po.setDiscount(add_disc);
                po.setApproved_status(PurchaseOrder.ApprovedStatus.valueOf(status));
                purchaseOrderFacade.updatePurchaseOrder(po, lineItemList);

                json = gson.toJson(new JsonReturnMsg("Update Purchase Order", "Purchase Order Updated Successfully.", "info"));
                session.setAttribute(lineItemListSession, new ArrayList());
                session.setAttribute(discountSession, 0.00);
                System.out.println(json);
                out.println(json);
            }
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
