/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.crm.customer;

import com.google.gson.Gson;
import ejb.sessionbeans.interfaces.CustomerFacadeLocal;
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
import org.persistence.Customer;
import util.ConvertToJsonObject;
import util.JsonReturnDropDown;
import util.JsonReturnMsg;
import util.JsonReturnTable;

/**
 *
 * @author Ken
 */
@WebServlet(name = "CustomerServlet", urlPatterns = {"/CustomerServlet"})
public class CustomerServlet extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @EJB
    CustomerFacadeLocal customerFacade;
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
            }
            if (action.equals("createCustomer")) {
                createCustomer(request, response, out);
            }
            if (action.equals("convertCustomer")) {
                convertCustomer(request, response, out);
            }
            if (action.equals("updateCustomer")) {
                updateCustomer(request, response, out);
            }
            if (action.equals("updateCustSE")) {
                updateCustSE(request, response, out);
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
            String inquirer_id = (request.getParameter("inquirer_id") != null) ? request.getParameter("inquirer_id") : "";
            String company_name = (request.getParameter("company_name") != null) ? request.getParameter("company_name") : "";


            ArrayList<Customer> customerList = new ArrayList(customerFacade.findFilteredCustomers(page, rows, sort, order, inquirer_id, company_name));

            for (Customer c : customerList) {
                c = (Customer) ConvertToJsonObject.convert(c);

            }

            ArrayList<Customer> jsonList = new ArrayList<Customer>();
            // int totalRecord = employeeFacade.countNotConvertedSalesLead();


            System.out.println(customerList.size());
            int totalReord = customerFacade.countFilteredCustomers(page, rows, sort, order, inquirer_id, company_name);
            String json = gson.toJson(new JsonReturnTable(customerList.size() + "", customerList));
            out.println(json);
        } else if (content.equals("tableTRSF")) {
            String emp_id = (request.getParameter("emp_id") != null) ? request.getParameter("emp_id") : "";
           
            ArrayList<Customer> customerList = new ArrayList<Customer>();
            int totalReord = customerList.size();
            if (!emp_id.equals("")) {
                customerList = new ArrayList<Customer>(customerFacade.findFilteredCustomers(emp_id));

                for (Customer c : customerList) {
                    c = (Customer) ConvertToJsonObject.convert(c);

                }

                System.out.println(customerList.size());
                totalReord = customerFacade.count();
            }
            String json = gson.toJson(new JsonReturnTable(totalReord + "", customerList));
            out.println(json);
        } else if (content.equals("details")) {

            long cId = Long.parseLong(request.getParameter("inquirer_id"));
            Customer c = customerFacade.find(cId);
            c = (Customer) ConvertToJsonObject.convert(c);
            String json = gson.toJson(c);
            out.println(json);
        } else if (content.equals("dialog")) {
            //paging
            int page = (request.getParameter("page") != null) ? Integer.parseInt(request.getParameter("page")) : 1;
            int rows = (request.getParameter("rows") != null) ? Integer.parseInt(request.getParameter("rows")) : 10;
            String sort = (request.getParameter("sort") != null) ? request.getParameter("sort") : "inquirer_id";
            String order = (request.getParameter("order") != null) ? request.getParameter("order") : "asc";

            //filter
            String inquirer_id = (request.getParameter("inquirer_id") != null) ? request.getParameter("inquirer_id") : "";
            String company_name = (request.getParameter("company_name") != null) ? request.getParameter("company_name") : "";

            ArrayList<Customer> customerList = new ArrayList(customerFacade.findFilteredCustomers(page, rows, sort, order, inquirer_id, company_name));


            int totalReord = customerFacade.countFilteredCustomers(page, rows, sort, order, inquirer_id, company_name);

            for (Customer c : customerList) {
//                c.getAccount().setCustomer(null);
                c = (Customer) ConvertToJsonObject.convert(c);
            }
            String json = gson.toJson(new JsonReturnTable(totalReord + "", customerList));
            out.println(json);
        } else if (content.equals("dropdown")) {

            //String selected= request.getParameter("selected");
            String json = JsonReturnDropDown.populate(Customer.CustomerType.values());
            out.println(json);
        }
    }

    private void updateCustSE(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        String[] chk_status = request.getParameterValues("selected_inquirer_id");
        String emp2_id = request.getParameter("emp2_id");

//System.out.println(emp2_id+"!!!!!!");
        if ((emp2_id == null) || (emp2_id.equals(""))) {
            String content = "Please select the targeted sales exacutive.";
            String json = gson.toJson(new JsonReturnMsg("Re-Assign Customer", content, "error"));
            out.println(json);
        } else if (chk_status != null) {
            if (chk_status.length != 0) {

                customerFacade.updateCustSE(chk_status, emp2_id);
                String content = "Re-Assign of Customer(s) Successful.";
                String json = gson.toJson(new JsonReturnMsg("Re-Assign Customer", content, "info"));
                out.println(json);
            } else {
                String content = "Please select at least one Customer.";
                String json = gson.toJson(new JsonReturnMsg("Re-Assign Customer", content, "error"));
                out.println(json);
            }
        } else {
            String content = "Please select at least one Customer.";
            String json = gson.toJson(new JsonReturnMsg("Re-Assign Customer", content, "error"));
            out.println(json);

        }


    }

    private void createCustomer(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String json;
        String content;

        String inquirer_id = request.getParameter("inquirer_id");
        String emp_id = request.getParameter("emp_id");
        String company_name = request.getParameter("company_name");
        String contact_person = request.getParameter("contact_person");
        String contact_no = request.getParameter("contact_no");
        String email = request.getParameter("email");
        String remarks = request.getParameter("remarks");
        String company_add = request.getParameter("company_add");
        String fax_no = request.getParameter("fax_no");
        String cust_type = request.getParameter("cust_type");
        String country = request.getParameter("country");
        String city = request.getParameter("city");

        if (inquirer_id.equals("") || emp_id.equals("")) {
            content = "Sales Lead and Sales Executive ID must be specified.";
            json = gson.toJson(new JsonReturnMsg("Create Customer", content, "error"));
        } else {
            customerFacade.createCustomer(inquirer_id, emp_id, company_name, contact_person, contact_no, email, remarks, company_add, fax_no, cust_type, country, city);
            content = "Create Customer Successful.";
            json = gson.toJson(new JsonReturnMsg("Create Customer", content, "info"));
        }
        out.println(json);
    }

    private void convertCustomer(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String content = "";
        String json = "";

        String inquirer_id = request.getParameter("inquirer_id");
        String emp_id = request.getParameter("emp_id");
        String emp_name = request.getParameter("emp_name");
        String company_name = request.getParameter("company_name");
        String contact_person = request.getParameter("contact_person");
        String contact_no = request.getParameter("contact_no");
        String email = request.getParameter("email");
        String remarks = request.getParameter("remarks");
        String company_add = request.getParameter("company_add");
        String fax_no = request.getParameter("fax_no");
        String cust_type = request.getParameter("cust_type");
        String country = request.getParameter("country");
        String city = request.getParameter("city");

        if (emp_id.equals("")) {
            content = "Sales Executive ID must be specified.";
            json = gson.toJson(new JsonReturnMsg("Create Customer", content, "error"));
        } else {
            boolean convertSuccess = customerFacade.convertCustomer(inquirer_id, emp_id, company_name, contact_person, contact_no, email, remarks, company_add, fax_no, cust_type, country, city);
            if (convertSuccess) {
                content = "Create Customer Successful.";
                json = gson.toJson(new JsonReturnMsg("Information", content, "info"));
            } else {
                content = "Sales lead already converted.";
                json = gson.toJson(new JsonReturnMsg("Warning", content, "warning"));
            }
        }
        out.println(json);
    }

    private void updateCustomer(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {

        String inquirer_id = request.getParameter("inquirer_id");
        String company_name = request.getParameter("company_name");
        String contact_person = request.getParameter("contact_person");
        String contact_no = request.getParameter("contact_no");
        String email = request.getParameter("email");
        String remarks = request.getParameter("remarks");
        String company_add = request.getParameter("company_add");
        String fax_no = request.getParameter("fax_no");
        String cust_type = request.getParameter("cust_type");
        String country = request.getParameter("country");
        String city = request.getParameter("city");

        customerFacade.updateCustomer(inquirer_id, company_name, contact_person, contact_no, email, remarks, company_add, fax_no, cust_type, country, city);

        String content = "Update Customer Successful.";
        String json = gson.toJson(new JsonReturnMsg("Information", content, "info"));
        out.println(json);


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
