/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.frm.account;

import com.google.gson.Gson;
import ejb.sessionbeans.interfaces.AccountFacadeLocal;
import ejb.sessionbeans.interfaces.PurchaseOrderFacadeLocal;
import ejb.sessionbeans.interfaces.SalesLeadFacadeLocal;
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
import org.persistence.Account;
import org.persistence.Customer;
import org.persistence.PurchaseOrder;
import org.persistence.SalesLead;
import org.persistence.SalesOrder;
import util.ConvertToJsonObject;
import util.JsonReturnDropDown;
import util.JsonReturnMsg;
import util.JsonReturnTable;

/**
 *
 * @author alyssia
 */
@WebServlet(name = "AccountServlet", urlPatterns = {"/AccountServlet"})
public class AccountServlet extends HttpServlet {

    @EJB
    AccountFacadeLocal accountFacade;
    @EJB
    TransactionFacadeLocal transactionFacade;
    @EJB
    SalesLeadFacadeLocal salesLeadFacade;
    @EJB
    PurchaseOrderFacadeLocal purchaseOrderFacade;
    @EJB
    SalesOrderFacadeLocal salesOrderFacade;
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

        //response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");

        try {
            if (action.equals("loadPage")) {
                loadPage(request, response, out);
            } else if (action.equals("updateAccount")) {
                updateAccount(request, response, out);
            }
        } catch (Exception ex) {
            String content = ex.getMessage();
            String json = gson.toJson(new JsonReturnMsg("Account Management", content, "error"));
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
            String custId = (request.getParameter("inquirer_id") != null) ? request.getParameter("inquirer_id") : "";
            String accountStatus = (request.getParameter("accountStatus") != null) ? request.getParameter("accountStatus") : "";

            //-1 for dropdown
            try {
                if (accountStatus.equals("0")) {
                    accountStatus = "";
                } else {
                    accountStatus = (Integer.parseInt(accountStatus.trim()) - 1) + "";
                }
            } catch (Exception e) {
                accountStatus = "";
            }

            ArrayList<Account> acctList = new ArrayList<Account>(accountFacade.findFilteredAccount(page, rows, sort, order, custId, accountStatus));

            for (Account a : acctList) {
                //a.getCustomer().setAccount(null);
                a = (Account) ConvertToJsonObject.convert(a);
            }

            ArrayList<Account> jsonList = new ArrayList<Account>();
            // int totalRecord = employeeFacade.countNotConvertedSalesLead();

            for (Account a : acctList) {
                Account ae = new Account();

                Customer c = new Customer();

                c.setInquirer_id(a.getCustomer().getInquirer_id());
                ae.setCustomer(c);

                ae.setAccount_id(a.getAccount_id());
                ae.setCredit_amt(a.getCredit_amt());
                ae.setMax_credit_limit(a.getMax_credit_limit());
                ae.setDeposit_amt(a.getDeposit_amt());

                ae.setRefundable_amt(a.getRefundable_amt());

                jsonList.add(ae);
            }

            int totalRecord = accountFacade.countFilteredAccount(page, rows, sort, order, custId, accountStatus);
            String json = gson.toJson(new JsonReturnTable(totalRecord + "", jsonList));
            System.out.println(json);
            out.println(json);

        } else if (content.equals("accountDetails")) {
            long accountId = Long.parseLong(request.getParameter("account_id"));
            Account a = accountFacade.find(accountId);

            //a.getCustomer().setAccount(null);
            a = (Account) ConvertToJsonObject.convert(a);
            a.getCustomer().setEmployee(null);

            /*Account ae = new Account();
            ae.setAccount_id(a.getAccount_id());
            ae.setCredit_amt(a.getCredit_amt());
            ae.setDeposit_amt(a.getDeposit_amt());
            ae.setMax_credit_limit(a.getMax_credit_limit());
            ae.setRefundable_amt(a.getRefundable_amt());*/

            String json = gson.toJson(a);
            System.out.println(json);
            out.println(json);
        } else if (content.equals("viewAll")) {
            //paging
            int page = (request.getParameter("page") != null) ? Integer.parseInt(request.getParameter("page")) : 1;
            int rows = (request.getParameter("rows") != null) ? Integer.parseInt(request.getParameter("rows")) : 10;
            String sort = (request.getParameter("sort") != null) ? request.getParameter("sort") : "inquirer_id";
            String order = (request.getParameter("order") != null) ? request.getParameter("order") : "asc";

            ArrayList<Account> acctList = new ArrayList<Account>(accountFacade.viewAll(page, rows, sort, order));

            for (Account a : acctList) {
                //a.getCustomer().setAccount(null);
                a = (Account) ConvertToJsonObject.convert(a);
            }

            int totalRecord = accountFacade.countViewAll(page, rows, sort, order);
            String json = gson.toJson(new JsonReturnTable(totalRecord + "", acctList));
            System.out.println(json);
            out.println(json);
        } else if (content.equals("custDetails")) {
            long inquirerId = Long.parseLong(request.getParameter("inquirer_id"));
            SalesLead sl = salesLeadFacade.find(inquirerId);
            sl = (SalesLead) ConvertToJsonObject.convert(sl);

            String json = gson.toJson(sl);
            System.out.println(json);
            out.println(json);
        } else if (content.equals("acctStatusDropdown")) {
            String json = JsonReturnDropDown.populate(Account.AccountStatus.values());
            out.println(json);
        } else if (content.equals("getSalesOrderId")) {
            String inquirerId = request.getParameter("inquirer_id");
            ArrayList<SalesOrder> soList = new ArrayList<SalesOrder>(purchaseOrderFacade.findAllSoId(inquirerId));
            ArrayList<SalesOrder> jList = new ArrayList<SalesOrder>();

            for (SalesOrder so : soList) {

                SalesOrder js = new SalesOrder();

                js.setSo_id(so.getSo_id());
                js.setDeposit_requested(so.getDeposit_requested());

                jList.add(js);
            }

            String json = gson.toJson(new JsonReturnTable(soList.size() + "", jList));
            System.out.println(json);
            out.println(json);
        }
    }

    private void updateAccount(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        boolean result = true;
        String json = "";

        String action = request.getParameter("confirm");
        String soID = request.getParameter("so_id");
        String account_id = "";
        String amount = "";
        String content = "";

        if (action.equals("increaseCredit")) {
            account_id = request.getParameter("account_id1");
            amount = request.getParameter("increase_credit_limit");
            if (Double.parseDouble(amount) < 0) {
                content = "Not allowed.";
                result = false;
            } else {
                content = accountFacade.increaseCredit(account_id, amount);
            }
        } else if (action.equals("decreaseCredit")) {
            account_id = request.getParameter("account_id2");
            amount = request.getParameter("decrease_credit_limit");
            if (Double.parseDouble(amount) <= 0) {
                content = "Not allowed.";
                result = false;
            } else {
                content = accountFacade.decreaseCredit(account_id, amount);
            }
        } else if (action.equals("addDeposit")) {

            account_id = request.getParameter("account_id3");
            amount = request.getParameter("add_deposit");
            if (Double.parseDouble(amount) <= 0) {
                content = "Not allowed.";
                result = false;
            } else {
                // check whether deposit amount = requested deposit amount
                SalesOrder so1 = salesOrderFacade.find(Long.parseLong(soID));
                double req_deposit = so1.getDeposit_requested();
                if (req_deposit != Double.parseDouble(amount)) {
                    content = "Deposit amount is not the same as requested deposit amount.";
                    result = false;

                } else {
                    content = accountFacade.addDeposit(account_id, amount, soID);
                }
            }
        } else if (action.equals("transferDeposit")) {

            account_id = request.getParameter("account_id5");
            amount = request.getParameter("transfer_deposit");
            if (Double.parseDouble(amount) <= 0) {
                content = "No negative amount allowed.";
                result = false;
            } else {
                Account a = accountFacade.find(Long.parseLong(account_id));
                if (a.getDeposit_amt() < Double.parseDouble(amount)) {
                    content = "Insufficient amount in deposit account for transfer.";
                    result = false;
                } else {
                    a.setDeposit_amt(a.getDeposit_amt() - Double.parseDouble(amount));
                    a.setRefundable_amt(a.getRefundable_amt() + Double.parseDouble(amount));
                    accountFacade.edit(a);
                    content = "$" + amount + " transfered from Deposit to Refund account.";
                }
            }
        } else if (action.equals("deductRefund")) {
            account_id = request.getParameter("account_id4");
            amount = request.getParameter("deduct_refund");
            if (Double.parseDouble(amount) <= 0) {
                content = "Not allowed.";
                result = false;
            } else {
                content = accountFacade.deductRefund(account_id, amount);
            }
        }

        if (result == false) {
            json = gson.toJson(new JsonReturnMsg("Update Account", content, "error"));
        } else {
            json = gson.toJson(new JsonReturnMsg("Update Account", content, "info"));
        }

        System.out.println(json);
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
