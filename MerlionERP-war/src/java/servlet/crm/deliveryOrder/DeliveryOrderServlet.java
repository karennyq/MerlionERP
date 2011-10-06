/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.crm.deliveryOrder;

import com.google.gson.Gson;
import ejb.sessionbeans.interfaces.DeliveryOrderFacadeLocal;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.persistence.DeliveryOrder;
import org.persistence.SalesOrder;
import util.JsonReturnDropDown;
import util.JsonReturnMsg;
import util.JsonReturnTable;

/**
 *
 * @author karennyq
 */
@WebServlet(name = "DeliveryOrderServlet", urlPatterns = {"/DeliveryOrderServlet"})
public class DeliveryOrderServlet extends HttpServlet {

    @EJB
    DeliveryOrderFacadeLocal deliveryOrderFacade;
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
        //response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");

        try {
            if (action.equals("loadPage")) {
                loadPage(request, response, out);
            }
            /* TODO output your page here
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet DeliveryOrderServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DeliveryOrderServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
             */
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
        if (content.equals("statusDropdown")) {
            String json = JsonReturnDropDown.populate(DeliveryOrder.DeliveryOrderStatus.values());
            out.println(json);
        } else if (content.equals("table")) {
            //paging
            int page = (request.getParameter("page") != null) ? Integer.parseInt(request.getParameter("page")) : 1;
            int rows = (request.getParameter("rows") != null) ? Integer.parseInt(request.getParameter("rows")) : 10;
            String sort = (request.getParameter("sort") != null) ? request.getParameter("sort") : "delivery_order_id";

            if (sort.equals("salesOrder")) {
                sort = "salesOrder.so_id";
            }

            String order = (request.getParameter("order") != null) ? request.getParameter("order") : "asc";

            //filter
            String do_id = (request.getParameter("do_id") != null) ? request.getParameter("do_id") : "";
            String so_id = (request.getParameter("so_id") != null) ? request.getParameter("so_id") : "";
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

            ArrayList<DeliveryOrder> doList = new ArrayList<DeliveryOrder>(deliveryOrderFacade.findFilteredDeliveryOrder(page, rows, sort, order, do_id, so_id, status));
            int totalRecord = deliveryOrderFacade.countFilteredDeliveryOrder(page, rows, sort, order, do_id, so_id, status);
            
            ArrayList newDoList = new ArrayList();
            for(DeliveryOrder dlo : doList){
                DeliveryOrder newDo = new DeliveryOrder();
                newDo.setDelivery_order_id(dlo.getDelivery_order_id());
                newDo.setDeliveryStatus(dlo.getDeliveryStatus());
                newDo.setStatus(dlo.getStatus());
                SalesOrder so = new SalesOrder();
                so.setSo_id(((SalesOrder)dlo.getSalesOrder()).getSo_id());
                newDo.setSalesOrder(so);
                newDoList.add(newDo);
            }
            
            String json = gson.toJson(new JsonReturnTable(totalRecord + "", newDoList));
            System.out.println(json);
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
