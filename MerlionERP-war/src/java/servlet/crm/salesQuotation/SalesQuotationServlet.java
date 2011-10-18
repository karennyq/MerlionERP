/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.crm.salesQuotation;

import com.google.gson.Gson;
import ejb.sessionbeans.interfaces.ProductFacadeLocal;
import ejb.sessionbeans.interfaces.SalesInquiryFacadeLocal;
import ejb.sessionbeans.interfaces.SalesLeadFacadeLocal;
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
import org.persistence.BulkDiscount;
import org.persistence.Customer;
import org.persistence.LineItem;
import org.persistence.Product;
import org.persistence.SalesInquiry;
import org.persistence.SalesLead;
import org.persistence.SalesQuotation;
import util.ConvertToJsonObject;
import util.GVDATE;
import util.JsonReturnDropDown;
import util.JsonReturnMsg;
import util.JsonReturnTable;

/**
 *
 * @author karennyq
 */
@WebServlet(name = "salesQuotationServlet", urlPatterns = {"/SalesQuotationServlet"})
public class SalesQuotationServlet extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @EJB
    ProductFacadeLocal productFacade;
    
    @EJB
    SalesQuotationFacadeLocal salesQuotationFacade;
    
    @EJB
    SalesLeadFacadeLocal salesLeadFacade;
    
    @EJB
    SalesInquiryFacadeLocal siFacade;
    
    Gson gson = new Gson();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");
        try {
            if (action.equals("loadPage")) {
                loadPage(request, response, out);

            } else if (action.equals("createQuotation")) {
                createQuotation(request, response, out);

            } else if (action.equals("addProduct")) {
                addProduct(request, response, out);

            } else if (action.equals("removeProductItem")) {
                removeProductItem(request, response, out);

            } else if (action.equals("updateProductItem")) {
                updateProductItem(request, response, out);

            } else if (action.equals("updateDiscount")) {
                updateDiscount(request, response, out);

            } else if (action.equals("getSalesQuotationDetails")) {
                getSalesQuotationDetails(request, response, out);

            } else if (action.equals("updateQuotation")) {
                updateQuotation(request, response, out);

            } else if (action.equals("createQuotationFromInquiry")) {
                createQuotationFromInquiry(request, response, out);
                
            } else if (action.equals("getProductList")) {
                getProductList(request, response, out);
                
            } else if (action.equals("loadTable")) {
                loadTable(request, response, out);
                
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

        if (content.equals("priorityDropdown")) {
            String json = JsonReturnDropDown.populate(SalesQuotation.Priority.values());
            out.println(json);

        } else if (content.equals("inquirySourceDropdown")) {
            String json = JsonReturnDropDown.populate(SalesQuotation.InquirySource.values());
            out.println(json);

        } else if (content.equals("inquiryStatusDropdown")) {
            String json = JsonReturnDropDown.populate(SalesQuotation.InquiryStatus.values());
            out.println(json);

        }
        else if (content.equals("tableALL")) {
            //paging
            int page = (request.getParameter("page") != null) ? Integer.parseInt(request.getParameter("page")) : 1;
            int rows = (request.getParameter("rows") != null) ? Integer.parseInt(request.getParameter("rows")) : 10;
            String sort = (request.getParameter("sort") != null) ? request.getParameter("sort") : "pre_sale_doc_id";
            //inquirer

            if (sort.equals("inquirer")) {
                sort = "inquirer.inquirer_id";
            }

            if (sort.equals("company_name")) {
                sort = "inquirer.company_name";
            }

            String order = (request.getParameter("order") != null) ? request.getParameter("order") : "asc";

            SimpleDateFormat sdf = new SimpleDateFormat(("dd/MM/yyyy"));

            //filter
            String quotation_id = (request.getParameter("quotation_id") != null) ? request.getParameter("quotation_id") : "";
            String inquirer_id = (request.getParameter("inquirer_id") != null) ? request.getParameter("inquirer_id") : "";
            String company_name = (request.getParameter("company_name") != null) ? request.getParameter("company_name") : "";

            String exp_date_1 = "01/01/1970";
            if (request.getParameter("exp_date_1") != null) {
                if (!request.getParameter("exp_date_1").equals("")) {
                    exp_date_1 = request.getParameter("exp_date_1");
                }
            }

            String exp_date_2 = "01/01/9999";
            if (request.getParameter("exp_date_2") != null) {
                if (!request.getParameter("exp_date_2").equals("")) {
                    exp_date_2 = request.getParameter("exp_date_2");
                }
            }

            System.out.println(exp_date_1);
            System.out.println(exp_date_2);

            Date expDate1 = sdf.parse(exp_date_1);
            System.out.println(expDate1.toString());
            Date expDate2 = sdf.parse(exp_date_2);
            expDate2 = GVDATE.addDay(expDate2, 1);

            System.out.println(expDate2.toString());

            ArrayList<SalesQuotation> salesQuotationList = new ArrayList<SalesQuotation>(salesQuotationFacade.findFilteredSalesQuotationALL(page, rows, sort, order, quotation_id, inquirer_id, company_name, expDate1, expDate2));
            int totalReord = salesQuotationFacade.countFilteredSalesQuotationALL(page, rows, sort, order, quotation_id, inquirer_id, company_name, expDate1, expDate2);

            for (SalesQuotation si : salesQuotationList) {
                si = (SalesQuotation) ConvertToJsonObject.convert(si);
                
                if (si.getInquirer() instanceof Customer) {
                    Customer c = (Customer) si.getInquirer();
                    c.setEmployee(null);
                }
                si.setDiscounted_total();
            }

            String json = gson.toJson(new JsonReturnTable(totalReord + "", salesQuotationList));
            System.out.println(json);
            out.println(json);

        }
    }
    
    private void loadTable(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        //paging
        int page = (request.getParameter("page") != null) ? Integer.parseInt(request.getParameter("page")) : 1;
        int rows = (request.getParameter("rows") != null) ? Integer.parseInt(request.getParameter("rows")) : 10;
        String sort = (request.getParameter("sort") != null) ? request.getParameter("sort") : "pre_sale_doc_id";

        //inquirer
        if (sort.equals("inquirer")) {
            sort = "inquirer.inquirer_id";
        }

        if (sort.equals("company_name")) {
            sort = "inquirer.company_name";
        }

        String order = (request.getParameter("order") != null) ? request.getParameter("order") : "asc";

        SimpleDateFormat sdf = new SimpleDateFormat(("dd/MM/yyyy"));

        //filter
        String quotation_id = (request.getParameter("quotation_id") != null) ? request.getParameter("quotation_id") : "";
        String inquirer_id = (request.getParameter("inquirer_id") != null) ? request.getParameter("inquirer_id") : "";
        String company_name = (request.getParameter("company_name") != null) ? request.getParameter("company_name") : "";

        String exp_date_1 = "01/01/1970";
        if (request.getParameter("exp_date_1") != null) {
            if (!request.getParameter("exp_date_1").equals("")) {
                exp_date_1 = request.getParameter("exp_date_1");
            }
        }

        String exp_date_2 = "01/01/9999";
        if (request.getParameter("exp_date_2") != null) {
            if (!request.getParameter("exp_date_2").equals("")) {
                exp_date_2 = request.getParameter("exp_date_2");
            }
        }

        Date expDate1 = sdf.parse(exp_date_1);
        Date expDate2 = sdf.parse(exp_date_2);
        expDate2 = GVDATE.addDay(expDate2, 1);

        ArrayList<SalesQuotation> salesQuotationList = new ArrayList<SalesQuotation>(salesQuotationFacade.findFilteredSalesQuotation(page, rows, sort, order, quotation_id, inquirer_id, company_name, expDate1, expDate2));
        int totalReord = salesQuotationFacade.countFilteredSalesQuotation(page, rows, sort, order, quotation_id, inquirer_id, company_name, expDate1, expDate2);

        for (SalesQuotation sq : salesQuotationList) {
            sq = (SalesQuotation) ConvertToJsonObject.convert(sq);
            if (sq.getInquirer() instanceof Customer) {
                ((Customer)sq.getInquirer()).setEmployee(null);
            }
            sq.setDiscounted_total();
        }

        String json = gson.toJson(new JsonReturnTable(totalReord + "", salesQuotationList));
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
            li.setBase_price(p.getPrice_per_box());
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

    private void createQuotation(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        HttpSession session = request.getSession();

        String inquirer_id = request.getParameter("client_id_hidden");
        String inquiry_source = request.getParameter("quotation_source");
        String priority = request.getParameter("priority");
        String remarks = request.getParameter("remarks");
        String listName = request.getParameter("listName");
        String discountName = request.getParameter("discountName");

        double add_disc = Double.parseDouble(session.getAttribute(discountName).toString());
        ArrayList<LineItem> lineItemList = (ArrayList<LineItem>) session.getAttribute(listName);

        String json;
        if ((inquirer_id.equals("") || inquirer_id == null) && (lineItemList.isEmpty() || lineItemList == null)) {
            json = gson.toJson(new JsonReturnMsg("Error", "Please select a client and add a product!", "error"));
            out.println(json);

        } else if (inquirer_id.equals("") || inquirer_id == null) {
            json = gson.toJson(new JsonReturnMsg("Error", "Please select a client!", "error"));
            out.println(json);

        } else if (lineItemList.isEmpty() || lineItemList == null) {
            json = gson.toJson(new JsonReturnMsg("Error", "Please add a product!", "error"));
            out.println(json);

        } else {
            long id = Long.parseLong(inquirer_id.trim());
            SalesLead inquirer = salesLeadFacade.find(id);

            if (inquirer == null) {
                json = gson.toJson(new JsonReturnMsg("Error", "Client does not exist.", "error"));
                out.println(json);
            } else {
                salesQuotationFacade.create(lineItemList, inquirer, inquiry_source, priority, remarks, add_disc);
                json = gson.toJson(new JsonReturnMsg("Info", "Sales Quotation Created Successfully", "info"));
                session.setAttribute(listName, new ArrayList());
                session.setAttribute(discountName, 0.00);
                out.println(json);
            }
        }
    }

    private void createQuotationFromInquiry(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String json;

        String pre_sale_doc_id = request.getParameter("pre_sale_doc_id");
        String inquirer_id = request.getParameter("client_id_hidden");
        String inquiry_source = request.getParameter("quotation_source");
        String priority = request.getParameter("priority");
        String remarks = request.getParameter("remarks");
        String listName = request.getParameter("listName");
        String discountName = request.getParameter("discountName");

        HttpSession session = request.getSession();
        double add_disc = Double.parseDouble(session.getAttribute(discountName).toString());
        ArrayList<LineItem> lineItemList = (ArrayList<LineItem>) session.getAttribute(listName);

        if (lineItemList.isEmpty() || lineItemList == null) {
            json = gson.toJson(new JsonReturnMsg("Error", "Please add a product!", "error"));
            out.println(json);

        } else {
            SalesLead inquirer = salesLeadFacade.find(Long.parseLong(inquirer_id));
            SalesInquiry si = siFacade.find(Long.parseLong(pre_sale_doc_id));
            SalesQuotation sq = new SalesQuotation();
            sq.setDiscount(add_disc);
            sq.setRemarks(remarks);
            sq.setPriority(SalesQuotation.Priority.valueOf(priority));
            sq.setInquiry_source(SalesQuotation.InquirySource.valueOf(inquiry_source));
            sq.setInquirer(inquirer);

            salesQuotationFacade.createQuotationFromInquiry(lineItemList, sq);
            json = gson.toJson(new JsonReturnMsg("Info", "Sales Quotation Created Successfully", "info"));
            session.setAttribute(listName, new ArrayList());
            session.setAttribute(discountName, 0.00);
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

    private void getSalesQuotationDetails(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String json = "";

        String pre_sale_doc_id = request.getParameter("pre_sale_doc_id");
        String listName = request.getParameter("listName");
        String discountName = request.getParameter("discountName");

        HttpSession session = request.getSession();
        SalesQuotation sq = salesQuotationFacade.find(Long.parseLong(pre_sale_doc_id));
        ArrayList<LineItem> a = new ArrayList<LineItem>();
        for (LineItem li : sq.getLineItems()) {
            a.add(li);
        }
        session.setAttribute(listName, a);
        session.setAttribute(discountName, sq.getDiscount());

        if (sq.getInquirer() instanceof Customer) {
            Customer c = (Customer) sq.getInquirer();
            c.setAccount(null);
            c.setPurchaseOrders(null);
            c.setEmployee(null);
            c.setPurchaseOrders(null);
            c.setPreSaleDocuments(null);
            c.setOperatingRegions(null);
            sq.setInquirer((SalesLead) c);
        }
        
        sq.getInquirer().setPreSaleDocuments(null);
        sq.setPurchaseOrders(null);
        sq.setLineItems(null);

        json = gson.toJson(sq);
        out.println(json);
    }

    private void updateQuotation(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String json;

        String pre_sale_doc_id = request.getParameter("pre_sale_doc_id");
        String quotation_source = request.getParameter("quotation_source");
        String priority = request.getParameter("priority");
        String remarks = request.getParameter("remarks");
        String listName = request.getParameter("listName");
        String discountName = request.getParameter("discountName");

        HttpSession session = request.getSession();
        ArrayList<LineItem> lineItemList = (ArrayList<LineItem>) session.getAttribute(listName);
        Double add_disc = (Double) session.getAttribute(discountName);

        if (lineItemList.isEmpty() || lineItemList == null) {
            json = gson.toJson(new JsonReturnMsg("Error", "Please add a product!", "error"));
            out.println(json);

        } else {
            SalesQuotation sq = salesQuotationFacade.find(Long.parseLong(pre_sale_doc_id));
            sq.setInquiry_source(SalesQuotation.InquirySource.valueOf(quotation_source));
            sq.setPriority(SalesQuotation.Priority.valueOf(priority));
            sq.setRemarks(remarks);
            sq.setDiscount(add_disc);
            salesQuotationFacade.updateSalesQuotation(sq, lineItemList);
            json = gson.toJson(new JsonReturnMsg("Info", "Sales Quotation Updated Successfully", "info"));
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
