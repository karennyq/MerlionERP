/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.scm.rawMaterial;

import com.google.gson.Gson;
import ejb.sessionbeans.interfaces.RawMaterialDetailFacadeLocal;
import ejb.sessionbeans.interfaces.RawMaterialFacadeLocal;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.persistence.RawMaterial;
import util.ConvertToJsonObject;
import util.JsonReturnMsg;
import util.JsonReturnTable;

/**
 *
 * @author alyssia
 */
@WebServlet(name = "RawMaterialServlet", urlPatterns = {"/RawMaterialServlet"})
public class RawMaterialServlet extends HttpServlet {

    @EJB
    RawMaterialFacadeLocal rmFacade;
    @EJB
    RawMaterialDetailFacadeLocal rmDetailFacade;
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
            } else if (action.equals("createRM")) {
                createRM(request, response, out);
            } else if (action.equals("updateRM")) {
                updateRM(request, response, out);
            } else if (action.equals("getRMDetails")) {
                getRMDetails(request, response, out);
            }
        } catch (Exception ex) {
            String content = ex.getMessage();
            String json = gson.toJson(new JsonReturnMsg("Raw Material Management", content, "error"));
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
            String sort = (request.getParameter("sort") != null) ? request.getParameter("sort") : "raw_material_id";
            String order = (request.getParameter("order") != null) ? request.getParameter("order") : "asc";

            //filter
            String rmID = (request.getParameter("raw_material_id") != null) ? request.getParameter("raw_material_id") : "";
            String rmName = (request.getParameter("mat_name") != null) ? request.getParameter("mat_name") : "";

            ArrayList<RawMaterial> rmList = new ArrayList<RawMaterial>(rmFacade.findFilteredRawMaterial(page, rows, sort, order, rmID, rmName));

            ArrayList<RawMaterial> jsonList = new ArrayList<RawMaterial>();
            
            for (RawMaterial r : rmList) {
                RawMaterial rm = new RawMaterial();
                rm.setRaw_material_id(r.getRaw_material_id());
                rm.setMat_name(r.getMat_name());

                jsonList.add(rm);
            }

            int totalRecord = rmFacade.countFilteredRawMaterial(page, rows, sort, order, rmID, rmName);
            String json = gson.toJson(new JsonReturnTable(totalRecord + "", jsonList));
            System.out.println(json);
            out.println(json);
        }else if (content.equals("rmDetails")) {
            ArrayList<RawMaterial> rmList = new ArrayList<RawMaterial>(rmFacade.findAll());
            
            ArrayList<RawMaterial> jsonList = new ArrayList<RawMaterial>();
            
            for (RawMaterial r : rmList) {
                RawMaterial rm = new RawMaterial();
                rm.setRaw_material_id(r.getRaw_material_id());
                rm.setMat_name(r.getMat_name());

                jsonList.add(rm);
            }

            String json = gson.toJson(jsonList);
            out.println(json);
        }else if (content.equals("dialog")) {
           
            String matName= request.getParameter("raw_mat_name_search");
            ArrayList <RawMaterial> rmList = new ArrayList <RawMaterial>(rmFacade.findFilteredRawMaterials(matName));
            ArrayList<RawMaterial> jsonList = new ArrayList<RawMaterial>();
            
            for (RawMaterial r : rmList) {
                RawMaterial rm = new RawMaterial();
                rm.setRaw_material_id(r.getRaw_material_id());
                rm.setMat_name(r.getMat_name());

                jsonList.add(rm);
            }
            int totalRecord = rmFacade.count();
            String json = gson.toJson(new JsonReturnTable(totalRecord + "", jsonList));
            out.println(json);
        }
    }
    
     private void createRM(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        
        String content = "";
        String json = "";
        
        String mat_name = request.getParameter("mat_name");

        if (rmFacade.checkRMNameExist(mat_name)) {
            content = "Raw Material Name is in use. Please rename.";
            json = gson.toJson(new JsonReturnMsg("Create Raw Material", content, "error"));
            
        } else {
            RawMaterial rm = new RawMaterial();
            rm.setMat_name(mat_name);

            rmFacade.create(rm);
            content = "Created Raw Material " + mat_name + " Successfully.";
            json = gson.toJson(new JsonReturnMsg("Create Raw Material", content, "info")); 
        }
        
        out.println(json);
    }
    
    private void updateRM(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        
        String content = "";
        String json = "";
        
        String raw_material_id = request.getParameter("raw_material_id");
        String mat_name = request.getParameter("mat_name");    
        
        if (rmFacade.checkRMNameExist(mat_name)) {
            content = "Raw Material Name is in use. Please rename.";
            json = gson.toJson(new JsonReturnMsg("Update Raw Material", content, "error"));
            
        } else {
            RawMaterial rm = rmFacade.find(Long.parseLong(raw_material_id));
            rm.setMat_name(mat_name);

            rmFacade.edit(rm);
            content = "Updated Raw Material Name " + mat_name + " Successfully.";
            json = gson.toJson(new JsonReturnMsg("Update Raw Material", content, "info"));
        }
        
        out.println(json);
    }
    
     private void getRMDetails(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String raw_material_id = request.getParameter("raw_material_id");
        
        RawMaterial rm = rmFacade.find(Long.parseLong(raw_material_id));
        rm = (RawMaterial) ConvertToJsonObject.convert(rm);
        String json = gson.toJson(rm);
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
