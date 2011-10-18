/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.crm.salesLead;

import com.google.gson.Gson;
import ejb.sessionbeans.interfaces.SalesLeadFacadeLocal;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.persistence.SalesLead;
import util.ConvertToJsonObject;
import util.JsonReturnDropDown;
import util.JsonReturnMsg;
import util.JsonReturnTable;

/**
 *
 * @author Ken
 */
@WebServlet(name = "SalesLeadServlet", urlPatterns = {"/SalesLeadServlet"})
public class SalesLeadServlet extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @EJB
    SalesLeadFacadeLocal salesLeadFacade;
    Gson gson = new Gson();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");
        try {

            if (action.equals("loadPage")) {
                loadPage(request, response, out);
            } else if (action.equals("createSalesLead")) {
                createSalesLead(request, response, out);
            } else if (action.equals("updateSalesLead")) {
                updateSalesLead(request, response, out);
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
            String sort = (request.getParameter("sort") != null) ? request.getParameter("sort") : "inquirer_id";
            String order = (request.getParameter("order") != null) ? request.getParameter("order") : "asc";

            //filter
            String salesLeadID = (request.getParameter("inquirer_id") != null) ? request.getParameter("inquirer_id") : "";
            String compName = (request.getParameter("company_name") != null) ? request.getParameter("company_name") : "";

            ArrayList<SalesLead> saleLeadList = new ArrayList<SalesLead>(salesLeadFacade.findFilteredSalesLead(page, rows, sort, order, salesLeadID, compName));

            //gson = new Gson();
            for (SalesLead sl : saleLeadList) {
                sl = (SalesLead) ConvertToJsonObject.convert(sl);
//                for (SalesInquiry si : s.getPreSaleDocuments()) {
//                    si.setInquirer(null);
//                    si.setLineItems(null);
//                }
//
//                if (s instanceof Customer) {
//                    Customer c = (Customer) s;
//                    c.getAccount().setCustomer(null);
//                }
            }

            int totalReord = salesLeadFacade.countFilteredSalesLead(page, rows, sort, order, salesLeadID, compName);
            String json = gson.toJson(new JsonReturnTable(totalReord + "", saleLeadList));
            System.out.println(json);
            out.println(json);
        } else if (content.equals("details")) {
            long slId = Long.parseLong(request.getParameter("inquirer_id"));
            SalesLead sl = salesLeadFacade.find(slId);


            sl = (SalesLead) ConvertToJsonObject.convert(sl);

//            for (SalesInquiry si : sl.getPreSaleDocuments()) {
//                si.setInquirer(null);
//                si.setLineItems(null);
//            }
//
//            if (sl instanceof Customer) {
//                Customer c = (Customer) sl;
//                c.getAccount().setCustomer(null);
//            }

            String json = gson.toJson(sl);
            out.println(json);
        } else if (content.equals("dialog")) {

            //paging
            int page = (request.getParameter("page") != null) ? Integer.parseInt(request.getParameter("page")) : 1;
            int rows = (request.getParameter("rows") != null) ? Integer.parseInt(request.getParameter("rows")) : 10;
            String sort = (request.getParameter("sort") != null) ? request.getParameter("sort") : "inquirer_id";
            String order = (request.getParameter("order") != null) ? request.getParameter("order") : "asc";

            //filter
            String compName = (request.getParameter("company_name") != null) ? request.getParameter("company_name") : "";
            System.out.println("Company Name: " + request.getParameter("company_name"));
            ArrayList<SalesLead> saleLeadList = new ArrayList<SalesLead>(salesLeadFacade.findNotConvertedSalesLead(page, rows, sort, order, compName));
            int totalReord = salesLeadFacade.countNotConvertedSalesLead(page, rows, sort, order, compName);

            for (SalesLead sl : saleLeadList) {
                sl = (SalesLead) ConvertToJsonObject.convert(sl);

            }

            String json = gson.toJson(new JsonReturnTable(totalReord + "", saleLeadList));
            out.println(json);
        } else if (content.equals("dropdown")) {
            //String selected= request.getParameter("selected");
            String json = JsonReturnDropDown.populate(SalesLead.SalesLeadStatus.values());
            out.println(json);
        } else if (content.equals("inquirerDialog")) {
            ArrayList<SalesLead> saleLeadList = new ArrayList<SalesLead>(salesLeadFacade.findAllInquirer());
            int totalReord = salesLeadFacade.countAllInquirer();

            for (SalesLead sl : saleLeadList) {
                sl = (SalesLead) ConvertToJsonObject.convert(sl);
//                for (SalesInquiry si : s.getPreSaleDocuments()) {
//                    si.setInquirer(null);
//                    si.setLineItems(null);
//                }
//
//                if (s instanceof Customer) {
//                    Customer c = (Customer) s;
//                    c.getAccount().setCustomer(null);
//                }

            }

            String json = gson.toJson(new JsonReturnTable(totalReord + "", saleLeadList));
            out.println(json);
        } else if (content.equals("inquirerDetails")) {
            long slId = Long.parseLong(request.getParameter("inquirer_id"));
            SalesLead sl = salesLeadFacade.find(slId);
            sl = (SalesLead) ConvertToJsonObject.convert(sl);
//            for (SalesInquiry si : sl.getPreSaleDocuments()) {
//                si.setInquirer(null);
//                si.setLineItems(null);
//            }
//
//            if (sl instanceof Customer) {
//                Customer c = (Customer) sl;
//                c.getAccount().setCustomer(null);
//            }

            String json = gson.toJson(sl);
            out.println(json);
        }
        /*else if (content.equals("getCustType")) {
        Customer.CustomerType[] custTypeList = Customer.CustomerType.values();
        
        response.setContentType("application/xml");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("<custTypeList>");
        int counter = 0;
        
        for (Customer.CustomerType ctl : custTypeList) {
        System.out.println(ctl.ordinal());
        response.getWriter().write("<custType>");
        response.getWriter().write("<custType_id>" + ctl.ordinal() + "</custType_id>");
        response.getWriter().write("<custType_name>Customer (" + ctl.name().replace('_', ' ') + ")</custType_name>");
        response.getWriter().write("</custType>");
        counter++;
        }
        response.getWriter().write("<custType>");
        response.getWriter().write("<custType_id>" + counter + "</custType_id>");
        response.getWriter().write("<custType_name>Sales Lead</custType_name>");
        response.getWriter().write("</custType>");
        
        response.getWriter().write("</custTypeList>");
        }*/

    }

    private void createSalesLead(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {

        String company_name = request.getParameter("company_name");
        String contact_person = request.getParameter("contact_person");
        String contact_no = request.getParameter("contact_no");
        String email = request.getParameter("email");
        String remarks = request.getParameter("remarks");
        String company_add = request.getParameter("company_add");
        String fax_no = request.getParameter("fax_no");
        String country = request.getParameter("country");
        String city = request.getParameter("city");
        String cust_type = request.getParameter("cust_type");

        cust_type = cust_type.replace(" ", "_");

        //check for company's name existence
        if (salesLeadFacade.salesLeadCompNameExist(company_name)) {
            String content = "Company name already exist.";
            String json = gson.toJson(new JsonReturnMsg("Create Sales Lead", content, "error"));
            out.println(json);
        } else {
            salesLeadFacade.createSalesLead(company_name, contact_person, contact_no, email, remarks, company_add, fax_no, country, city, cust_type);
            // gson = new Gson();
            String content = "Create Sales Lead Successful.";
            String json = gson.toJson(new JsonReturnMsg("Create Sales Lead", content, "info"));
            out.println(json);
        }
    }

    private void updateSalesLead(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {

        String inquirer_id = request.getParameter("inquirer_id");
        String company_name = request.getParameter("company_name");
        String contact_person = request.getParameter("contact_person");
        String contact_no = request.getParameter("contact_no");
        String email = request.getParameter("email");
        String remarks = request.getParameter("remarks");
        String company_add = request.getParameter("company_add");
        String fax_no = request.getParameter("fax_no");
        String country = request.getParameter("country");
        String city = request.getParameter("city");
        String sales_lead_status = request.getParameter("sales_lead_status");
        String cust_type = request.getParameter("cust_type");
        cust_type = cust_type.replace(" ", "_");


        if (salesLeadFacade.verifyCompName(inquirer_id, company_name)) { //inqurier ID matches

            salesLeadFacade.updateSalesLead(inquirer_id, company_name, contact_person, contact_no, email, remarks, company_add, fax_no, country, city, sales_lead_status, cust_type);

            // gson = new Gson();
            String content = "Update Sales Lead Successful.";
            String json = gson.toJson(new JsonReturnMsg("Update Sales Lead", content, "info"));
            out.println(json);
        } else { //inqurier ID dont match

            String content = "Company name already exist.";
            String json = gson.toJson(new JsonReturnMsg("Update Sales Lead", content, "error"));
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
