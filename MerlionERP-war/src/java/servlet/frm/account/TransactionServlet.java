/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.frm.account;

import com.google.gson.Gson;
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
import org.persistence.Transaction;
import util.JsonReturnDropDown;
import util.JsonReturnMsg;
import util.JsonReturnTable;

/**
 *
 * @author alyssia
 */
@WebServlet(name = "TransactionServlet", urlPatterns = {"/TransactionServlet"})
public class TransactionServlet extends HttpServlet {
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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");
       
        try {
            if (action.equals("loadPage")) {
              loadPage(request, response, out);
            }
        } 
        catch(Exception ex){
            String content = ex.getMessage();
            String json = gson.toJson(new JsonReturnMsg("Transaction Management", content, "error"));
            out.println(json);
            System.out.print(ex);
        }
        finally {            
            out.close();
        }
    }
    
    private void loadPage(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String content = request.getParameter("content");
        if (content.equals("table")) {
           
            //paging
            int page= (request.getParameter("page")!=null)?Integer.parseInt(request.getParameter("page")):1;
            int rows= (request.getParameter("rows")!=null)?Integer.parseInt(request.getParameter("rows")):10;
            String sort=(request.getParameter("sort")!=null)?request.getParameter("sort"):"trans_id";
            String order=(request.getParameter("order")!=null)?request.getParameter("order"):"asc";
            
            //filter
            String acctID= (request.getParameter("account_id")!=null)?request.getParameter("account_id"):"";
            String transID= (request.getParameter("trans_id")!=null)?request.getParameter("trans_id"):"";
            String trxType= (request.getParameter("trans_type")!=null)?request.getParameter("trans_type"):"";
            String trxNature= (request.getParameter("transaction_nature")!=null)?request.getParameter("transaction_nature"):"";
            
            ArrayList<Transaction> trxList = new ArrayList<Transaction>(transactionFacade.findFilteredTransaction(page,rows,sort,order,acctID,trxType,trxNature));
                    
            int totalRecord = transactionFacade.countFilteredTransaction(page,rows,sort,order,acctID,trxType,trxNature);
            String json = gson.toJson(new JsonReturnTable(totalRecord + "", trxList));
            System.out.println(json);
            out.println(json);
       }else if(content.equals("trxTypeDropdown")) {
            String json = JsonReturnDropDown.populate(Transaction.TransType.values());
            out.println(json);
        }else if(content.equals("trxNatureDropdown")) {
            String json = JsonReturnDropDown.populate(Transaction.TransactionNature.values());
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
