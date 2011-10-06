/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.production;

import com.google.gson.Gson;
import ejb.sessionbeans.interfaces.ProductionLineFacadeLocal;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.persistence.ProductionLine;
import util.JsonReturnMsg;
import util.JsonReturnTable;

/**
 *
 * @author Randy
 */
@WebServlet(name = "ProductionLineServlet", urlPatterns = {"/ProductionLineServlet"})
public class ProductionLineServlet extends HttpServlet {

    @EJB
    ProductionLineFacadeLocal plFacade;
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
            if (action.equals("loadTable")) {
                loadTable(request, response, out);
                
            } else if (action.equals("createPL")) {
                createPL(request, response, out);
                
            } else if (action.equals("getDetails")) {
                getDetails(request, response, out);
                
            } else if (action.equals("updatePL")) {
                updatePL(request, response, out);
                
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
    
    private void loadTable(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        ArrayList<ProductionLine> plList = new ArrayList<ProductionLine>(plFacade.findAll());
        int totalRecord = plFacade.count();

        String json = gson.toJson(new JsonReturnTable(totalRecord + "", plList));
        out.println(json);
    }
    
    private void getDetails(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String production_line_id = request.getParameter("production_line_id");
        
        ProductionLine pl = plFacade.find(Long.parseLong(production_line_id));
        
        String json = gson.toJson(pl);
        out.println(json);
    }
    
    private void createPL(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        
        String content = "";
        String json = "";
        
        String production_line_name = request.getParameter("production_line_name");

        if (plFacade.checkPLNameExist(production_line_name)) {
            content = "Production Line Name is in use. Please rename.";
            json = gson.toJson(new JsonReturnMsg("Create Production Line", content, "error"));
            
        } else {
            ProductionLine pl = new ProductionLine();
            pl.setProduction_line_name(production_line_name);

            plFacade.create(pl);
            content = "Created Production Line " + production_line_name + " Successfully.";
            json = gson.toJson(new JsonReturnMsg("Create Production Line", content, "info"));
        }
        
        out.println(json);
    }
    
    private void updatePL(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        
        String content = "";
        String json = "";
        
        String production_line_id = request.getParameter("production_line_id");
        String production_line_name = request.getParameter("production_line_name");    
        
        if (plFacade.checkPLNameExist(production_line_name)) {
            content = "Production Line Name is in use. Please rename.";
            json = gson.toJson(new JsonReturnMsg("Update Production Line", content, "error"));
            
        } else {
            ProductionLine pl = plFacade.find(Long.parseLong(production_line_id));
            pl.setProduction_line_name(production_line_name);

            plFacade.edit(pl);
            content = "Updated Production Line ID " + production_line_id + " Successfully.";
            json = gson.toJson(new JsonReturnMsg("Update Production Line", content, "info"));
        }
        
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
