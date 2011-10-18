/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.crm.salesInquiry;

import com.google.gson.Gson;
import ejb.sessionbeans.interfaces.ProductFacadeLocal;
import ejb.sessionbeans.interfaces.SalesInquiryFacadeLocal;
import ejb.sessionbeans.interfaces.SalesLeadFacadeLocal;
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
import util.ConvertToJsonObject;
import util.JsonReturnDropDown;
import util.JsonReturnMsg;
import util.JsonReturnTable;
import util.GVDATE;

/**
 *
 * @author karennyq
 */
@WebServlet(name = "SalesInquiryServlet", urlPatterns = {"/SalesInquiryServlet"})
public class SalesInquiryServlet extends HttpServlet {

    @EJB
    ProductFacadeLocal productFacade;
    @EJB
    SalesInquiryFacadeLocal salesInquiryFacade;
    @EJB
    SalesLeadFacadeLocal salesLeadFacade;
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

            } else if (action.equals("createInquiry")) {
                createInquiry(request, response, out);

            } else if (action.equals("addProduct")) {
                addProduct(request, response, out);

            } else if (action.equals("removeProductItem")) {
                removeProductItem(request, response, out);

            } else if (action.equals("updateProductItem")) {
                updateProductItem(request, response, out);

            } else if (action.equals("getSalesInquiryDetails")) {
                getSalesInquiryDetails(request, response, out);

            } else if (action.equals("updateInquiry")) {
                updateInquiry(request, response, out);
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

        if (content.equals("priorityDropdown")) {
            String json = JsonReturnDropDown.populate(SalesInquiry.Priority.values());
            out.println(json);

        } else if (content.equals("inquirySourceDropdown")) {
            String json = JsonReturnDropDown.populate(SalesInquiry.InquirySource.values());
            out.println(json);

        } else if (content.equals("inquiryStatusDropdown")) {
            String json = JsonReturnDropDown.populate(SalesInquiry.InquiryStatus.values());
            out.println(json);

        } else if (content.equals("table")) {
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
            String inquiry_id = (request.getParameter("inquiry_id") != null) ? request.getParameter("inquiry_id") : "";
            String inquirer_id = (request.getParameter("inquirer_id") != null) ? request.getParameter("inquirer_id") : "";
            String inquiry_priority = ((request.getParameter("inquiry_priority") != null)) ? request.getParameter("inquiry_priority") : "0";

            try {
                if (inquiry_priority.equals("0")) {
                    inquiry_priority = "";
                } else {
                    inquiry_priority = (Integer.parseInt(inquiry_priority.trim()) - 1) + "";
                }
            } catch (Exception e) {
                inquiry_priority = "";
            }

            String company_name = (request.getParameter("company_name") != null) ? request.getParameter("company_name") : "";

            String req_date_1 = "01/01/1970";
            if (request.getParameter("req_date_1") != null) {
                if (!request.getParameter("req_date_1").equals("")) {
                    req_date_1 = request.getParameter("req_date_1");
                }
            }

            String req_date_2 = "01/01/9999";
            if (request.getParameter("req_date_2") != null) {
                if (!request.getParameter("req_date_2").equals("")) {
                    req_date_2 = request.getParameter("req_date_2");
                }
            }

            Date reqDate1 = sdf.parse(req_date_1);
            Date reqDate2 = sdf.parse(req_date_2);
            reqDate2 = GVDATE.addDay(reqDate2, 1);

            ArrayList<SalesInquiry> salesInquiryList = new ArrayList<SalesInquiry>(salesInquiryFacade.findFilteredSalesInquiry(page, rows, sort, order, inquiry_id, inquirer_id, inquiry_priority, company_name, reqDate1, reqDate2));
            int totalRecord = salesInquiryFacade.countFilteredSalesInquiry(page, rows, sort, order, inquiry_id, inquirer_id, inquiry_priority, company_name, reqDate1, reqDate2);

            for (SalesInquiry si : salesInquiryList) {
                si = (SalesInquiry) ConvertToJsonObject.convert(si);
                if (si.getInquirer() instanceof Customer) {
                    ((Customer) si.getInquirer()).setEmployee(null);
                }
                si.setTotal_price();
            }

            String json = gson.toJson(new JsonReturnTable(totalRecord + "", salesInquiryList));
            out.println(json);

        } else if (content.equals("tableALL")) {

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
            String inquiry_id = (request.getParameter("inquiry_id") != null) ? request.getParameter("inquiry_id") : "";
            String inquirer_id = (request.getParameter("inquirer_id") != null) ? request.getParameter("inquirer_id") : "";
            String inquiry_priority = ((request.getParameter("inquiry_priority") != null)) ? request.getParameter("inquiry_priority") : "0";

            try {
                if (inquiry_priority.equals("0")) {
                    inquiry_priority = "";
                } else {
                    inquiry_priority = (Integer.parseInt(inquiry_priority.trim()) - 1) + "";
                }
            } catch (Exception e) {
                inquiry_priority = "";
            }

            String company_name = (request.getParameter("company_name") != null) ? request.getParameter("company_name") : "";

            String req_date_1 = "01/01/1970";
            if (request.getParameter("req_date_1") != null) {
                if (!request.getParameter("req_date_1").equals("")) {
                    req_date_1 = request.getParameter("req_date_1");
                }
            }

            String req_date_2 = "01/01/9999";
            if (request.getParameter("req_date_2") != null) {
                if (!request.getParameter("req_date_2").equals("")) {
                    req_date_2 = request.getParameter("req_date_2");
                }
            }

            System.out.println(req_date_1);
            System.out.println(req_date_2);

            Date reqDate1 = sdf.parse(req_date_1);
            System.out.println(reqDate1.toString());
            Date reqDate2 = sdf.parse(req_date_2);
            reqDate2 = GVDATE.addDay(reqDate2, 1);

            System.out.println(reqDate2.toString());


            ArrayList<SalesInquiry> salesInquiryList = new ArrayList<SalesInquiry>(salesInquiryFacade.findFilteredSalesInquiryALL(page, rows, sort, order, inquiry_id, inquirer_id, inquiry_priority, company_name, reqDate1, reqDate2));
            int totalReord = salesInquiryFacade.countFilteredSalesInquiryALL(page, rows, sort, order, inquiry_id, inquirer_id, inquiry_priority, company_name, reqDate1, reqDate2);

            for (SalesInquiry si : salesInquiryList) {
                si = (SalesInquiry) ConvertToJsonObject.convert(si);

                if (si.getInquirer() instanceof Customer) {
                    Customer c = (Customer) si.getInquirer();
                    c.setEmployee(null);
                }

                /*
                if(si.getInquirer() instanceof Customer){
                Customer c = (Customer) si.getInquirer();
                c.setAccount(null);
                c.setPurchaseOrders(null);
                c.setEmployee(null);
                c.setPurchaseOrders(null);
                c.setPreSaleDocuments(null);
                c.setSoleDistribution(null);
                si.setInquirer((SalesLead)c);
                }
                
                si.getInquirer().setPreSaleDocuments(null);*/
            }

            String json = gson.toJson(new JsonReturnTable(totalReord + "", salesInquiryList));
            System.out.println(json);
            out.println(json);


        } else if (content.equals("pdtTable")) {
            String listName = request.getParameter("listName");
            if (request.getParameter("reset") != null) {
                String reset = request.getParameter("reset");
                if (reset.equals("true")) {
                    session.setAttribute(listName, new ArrayList());
                }
            }

            lineItemList = (ArrayList<LineItem>) session.getAttribute(listName);
            int totalRecord = (lineItemList != null) ? lineItemList.size() : 0;

            Double totalPrice = 0.0;
            for (LineItem li1 : lineItemList) {
                totalPrice = totalPrice + li1.getActual_price();
                li1 = (LineItem) ConvertToJsonObject.convert(li1);
                Product p = productFacade.find(li1.getProduct().getProduct_id());

                int pdtQty = li1.getQuantity();
                int boxReq = 0;

                for (BulkDiscount bd : p.getBulkDiscounts()) {
                    if (pdtQty >= bd.getBoxes_required()) {
                        if (boxReq < bd.getBoxes_required()) {
                            boxReq = bd.getBoxes_required();
                            li1.setBulk_discount(bd.getDiscount_given());
                        }
                    }
                }

                if (li1.getBulk_discount() == null) {
                    li1.setBulk_discount(0.0);
                }

            }

            FooterItem fi = new FooterItem("Grand Total:", totalPrice);
            ArrayList li = new ArrayList();
            li.add(fi);

            String json = gson.toJson(new JsonReturnTable(totalRecord + "", lineItemList, li));
            out.println(json);
        }
    }

    private void removeProductItem(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        int listIndex = Integer.parseInt(request.getParameter("listIndex"));
        String listName = request.getParameter("listName");

        HttpSession session = request.getSession();
        ArrayList<LineItem> lineItemList = new ArrayList<LineItem>();

        lineItemList = (ArrayList) session.getAttribute(listName);
        LineItem li = lineItemList.get(listIndex);
        lineItemList.remove(li);
        session.setAttribute(listName, lineItemList);
        String json = gson.toJson(new JsonReturnMsg("Info", "Product Deleted!", "info"));
        out.println(json);
    }

    private void updateProductItem(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        int listIndex = Integer.parseInt(request.getParameter("listIndex"));
        int update_qty = Integer.parseInt(request.getParameter("update_qty"));
        String listName = request.getParameter("listName");

        HttpSession session = request.getSession();
        ArrayList<LineItem> lineItemList = new ArrayList<LineItem>();
        lineItemList = (ArrayList) session.getAttribute(listName);

        LineItem li = lineItemList.get(listIndex);

        li.setQuantity(update_qty);
        li.setBulk_discount(0.0);

        int pdtQty = li.getQuantity();
        int boxReq = 0;
        long pdtId = li.getProduct().getProduct_id();
        Product p = productFacade.find(pdtId);
        
        for (BulkDiscount bd : p.getBulkDiscounts()) {
            if (pdtQty >= bd.getBoxes_required()) {
                if (boxReq < bd.getBoxes_required()) {
                    boxReq = bd.getBoxes_required();
                    li.setBulk_discount(bd.getDiscount_given());
                }
            }
        }
        li.setActual_price();

        session.setAttribute(listName, lineItemList);
        String json = gson.toJson(new JsonReturnMsg("Info", "Product Updated.", "info"));
        out.println(json);
    }

    private void addProduct(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String json;

        HttpSession session = request.getSession();
        ArrayList<LineItem> lineItemList = new ArrayList<LineItem>();
        String pdtId = request.getParameter("product_id");
        String listName = request.getParameter("listName");
        Integer qty;

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

            Product p = productFacade.find(Long.parseLong(pdtId));

            li.setQuantity(previousQty + qty);

            li.setBulk_discount(0.0);
            int pdtQty = li.getQuantity();
            int boxReq = 0;

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

    private void createInquiry(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String json;

        String inquirer_id = request.getParameter("inquirer_id_hidden");
        String inquiry_source = request.getParameter("inquiry_source");
        String priority = request.getParameter("priority");
        String remarks = request.getParameter("remarks");
        String listName = request.getParameter("listName");

        HttpSession session = request.getSession();
        ArrayList<LineItem> lineItemList = (ArrayList<LineItem>) session.getAttribute(listName);

        if ((inquirer_id.equals("") || inquirer_id == null) && (lineItemList.isEmpty() || lineItemList == null)) {
            json = gson.toJson(new JsonReturnMsg("Error", "Please select a inquirer and add a product!", "error"));
            out.println(json);

        } else if (inquirer_id.equals("") || inquirer_id == null) {
            json = gson.toJson(new JsonReturnMsg("Error", "Please select a inquirer!", "error"));
            out.println(json);

        } else if (lineItemList.isEmpty() || lineItemList == null) {
            json = gson.toJson(new JsonReturnMsg("Error", "Please add a product!", "error"));
            out.println(json);

        } else {
            long id = Long.parseLong(inquirer_id.trim());
            SalesLead inquirer = salesLeadFacade.find(id);

            if (inquirer instanceof Customer) {
                Customer c = (Customer) inquirer;
                c.getAccount().setCustomer(null);
            }

            if (inquirer == null) {
                json = gson.toJson(new JsonReturnMsg("Error", "Inquirer does not exist.", "error"));
                out.println(json);

            } else {
                salesInquiryFacade.create(lineItemList, inquirer, inquiry_source, priority, remarks);
                json = gson.toJson(new JsonReturnMsg("Info", "Sales Inquiry Created Successfully", "info"));
                session.setAttribute(listName, new ArrayList());
                out.println(json);
            }
        }
    }

    private class FooterItem {

        private String quantity;
        private double actual_price;

        public FooterItem(String quantity, double actual_price) {
            this.quantity = quantity;
            this.actual_price = actual_price;
        }
    }

    private void getSalesInquiryDetails(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String json = "";

        String pre_sale_doc_id = request.getParameter("pre_sale_doc_id");
        String listName = request.getParameter("listName");
        String discountName = request.getParameter("discountName");
        String reset = request.getParameter("reset");

        HttpSession session = request.getSession();
        SalesInquiry si = salesInquiryFacade.find(Long.parseLong(pre_sale_doc_id));
        ArrayList<LineItem> a = new ArrayList<LineItem>();
        for (LineItem li : si.getLineItems()) {
            a.add(li);
        }
        session.setAttribute(listName, a);

        if (si.getInquirer() instanceof Customer) {
            Customer c = (Customer) si.getInquirer();
            c.setAccount(null);
            c.setPurchaseOrders(null);
            c.setEmployee(null);
            c.setPurchaseOrders(null);
            c.setPreSaleDocuments(null);
            c.setOperatingRegions(null);
            si.setInquirer((SalesLead) c);
        }

        si.getInquirer().setPreSaleDocuments(null);
        si.setLineItems(null);

        if (reset != null && reset.equals("true")) {
            session.setAttribute(discountName, 0.0);
        }

        json = gson.toJson(si);
        out.println(json);
    }

    private void updateInquiry(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String json;

        String pre_sale_doc_id = request.getParameter("pre_sale_doc_id");
        String inquiry_source = request.getParameter("inquiry_source");
        String priority = request.getParameter("priority");
        String remarks = request.getParameter("remarks");
        String listName = request.getParameter("listName");

        HttpSession session = request.getSession();
        ArrayList<LineItem> lineItemList = (ArrayList<LineItem>) session.getAttribute(listName);

        if (lineItemList.isEmpty() || lineItemList == null) {
            json = gson.toJson(new JsonReturnMsg("Error", "Please add a product!", "error"));
            out.println(json);

        } else {
            SalesInquiry si = salesInquiryFacade.find(Long.parseLong(pre_sale_doc_id));
            si.setInquiry_source(SalesInquiry.InquirySource.valueOf(inquiry_source));
            si.setPriority(SalesInquiry.Priority.valueOf(priority));
            si.setRemarks(remarks);
            salesInquiryFacade.updateSalesInquiry(si, lineItemList);
            json = gson.toJson(new JsonReturnMsg("Info", "Sales Inquiry Updated Successfully", "info"));
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
