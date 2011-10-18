/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.scm.rawMaterial;

import com.google.gson.Gson;
import ejb.sessionbeans.interfaces.RawMaterialDetailFacadeLocal;
import ejb.sessionbeans.interfaces.RawMaterialFacadeLocal;
import ejb.sessionbeans.interfaces.SupplierFacadeLocal;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.persistence.RawMaterial;
import org.persistence.RawMaterialDetail;
import org.persistence.Supplier;
import util.ConvertToJsonObject;
import util.JsonReturnMsg;
import util.JsonReturnTable;

/**
 *
 * @author alyssia
 */
@WebServlet(name = "SupplierServlet", urlPatterns = {"/SupplierServlet"})
public class SupplierServlet extends HttpServlet {
    @EJB
    SupplierFacadeLocal supplierFacade;
    @EJB
    RawMaterialFacadeLocal rmFacade;
    @EJB
    RawMaterialDetailFacadeLocal rmDFacade;
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
            }else if(action.equals("addRMDetails")){
                addRMDetails(request, response, out);
            }else if (action.equals("updateRawMaterialItem")) {
                updateRawMaterialItem(request, response, out);
            }else if (action.equals("removeRMItem")) {
                removeRMItem(request, response, out);
            }else if (action.equals("createSupplier")) {
                createSupplier(request, response, out);
            }else if (action.equals("getSupplierDetails")) {
                getSupplierDetails(request, response, out);
            }else if (action.equals("updateSupplier")) {
                updateSupplier(request, response, out);
            }else if (action.equals("retrieveSuppliers")) {
                retrieveSuppliers(request, response, out);
            }else if (action.equals("deleteSupplier")) {
                deleteSupplier(request, response, out);
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
       
        if (content.equals("table")) {
            System.out.println("table");
            //paging
            int page = (request.getParameter("page") != null) ? Integer.parseInt(request.getParameter("page")) : 1;
            int rows = (request.getParameter("rows") != null) ? Integer.parseInt(request.getParameter("rows")) : 10;
            String sort = (request.getParameter("sort") != null) ? request.getParameter("sort") : "supplier_id";
            String order = (request.getParameter("order") != null) ? request.getParameter("order") : "asc";

            //filter
            String supplier_id = (request.getParameter("supplier_id") != null) ? request.getParameter("supplier_id") : "";
            String supplier_name = (request.getParameter("supplier_name") != null) ? request.getParameter("supplier_name") : "";
           
            ArrayList<Supplier> suppList = new ArrayList<Supplier>(supplierFacade.findFilteredSupplier(page, rows, sort, order, supplier_id, supplier_name));
            int totalRecord = supplierFacade.countFilteredSupplier(page, rows, sort, order, supplier_id, supplier_name);
               
            ArrayList<Supplier> jsonList = new ArrayList<Supplier>();
                
            for (Supplier s1 : suppList) {
          
                Supplier s = new Supplier();
                s.setSupplier_id(s1.getSupplier_id());
                s.setSupplier_name(s1.getSupplier_name());
                s.setSupplier_address(s1.getSupplier_address());
                jsonList.add(s);
            }

            String json = gson.toJson(new JsonReturnTable(totalRecord + "", jsonList));
            out.println(json);

        }else if (content.equals("rmTable")) {
            ArrayList<RawMaterialDetail> rmItemList = new ArrayList<RawMaterialDetail>();
            String listName = request.getParameter("listName");
            if (request.getParameter("reset") != null) {
                String reset = request.getParameter("reset");
                if (reset.equals("true")) {
                    session.setAttribute(listName, new ArrayList());
                }
            }

            rmItemList = (ArrayList<RawMaterialDetail>) session.getAttribute(listName);
            int totalRecord = (rmItemList != null) ? rmItemList.size() : 0;
            ArrayList<RawMaterialDetail> jsonList = new ArrayList<RawMaterialDetail>();
                
            for (RawMaterialDetail rmD1 : rmItemList) {
          
                RawMaterialDetail rm = new RawMaterialDetail();
            
                RawMaterial r = new RawMaterial();
                r.setRaw_material_id(rmD1.getRawMaterial().getRaw_material_id());
                r.setMat_name(rmD1.getRawMaterial().getMat_name());
                rm.setRawMaterial(r);
                rm.setLead_time(rmD1.getLead_time());
                rm.setLot_size(rmD1.getLot_size());
                rm.setShelf_life(rmD1.getShelf_life());
                rm.setStorage_unit(rmD1.getStorage_unit());
               
                jsonList.add(rm);
            }
            String json = gson.toJson(new JsonReturnTable(totalRecord + "", jsonList));
            out.println(json);
        }
    }
    
    private void addRMDetails(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String json;
        HttpSession session = request.getSession();
        ArrayList<RawMaterialDetail> rmDetailList = new ArrayList<RawMaterialDetail>();
        String rmId = request.getParameter("rmId");
        String leadTime = request.getParameter("leadTime");
        String shelfLife = request.getParameter("shelfLife");
        String lotSize = request.getParameter("lotSize");
        String storageUnit = request.getParameter("storageUnit");
        String listName = request.getParameter("listName");
       
        System.out.println("leadtime: " +leadTime.isEmpty());
        System.out.println("leadtime2: " +leadTime);
        if (leadTime.isEmpty() || shelfLife.isEmpty() || lotSize.isEmpty() || storageUnit.isEmpty()) {
            json = gson.toJson(new JsonReturnMsg("Error", "Please enter all raw material details!", "error"));
            out.println(json);

        } /*else if (leadTime.isEmpty()) {
            json = gson.toJson(new JsonReturnMsg("Error", "Please enter lead time!", "error"));
            out.println(json);

        } else if (shelfLife.isEmpty()) {
            json = gson.toJson(new JsonReturnMsg("Error", "Please enter the shelf life!", "error"));
            out.println(json);

        } */else {
            rmDetailList = (ArrayList) session.getAttribute(listName);
            RawMaterialDetail rmD = new RawMaterialDetail();
            int index = -1;
            int prevLeadTime = 0;
            int prevShelfLife =0;
            int prevLotSize = 0;
            int prevStorageUnit=0;
            for (RawMaterialDetail rmD1 : rmDetailList) {
                if (rmD1.getRawMaterial().getRaw_material_id().equals(Long.parseLong(rmId))){
                    rmD = rmD1;
                    index = rmDetailList.indexOf(rmD1);
                    prevLeadTime = rmD1.getLead_time();
                    prevShelfLife=rmD1.getShelf_life();
                    prevLotSize=rmD1.getLot_size();
                    prevStorageUnit=rmD1.getStorage_unit();
                }
            }
            
            RawMaterial rm = rmFacade.find(Long.parseLong(rmId));
            rmD.setRawMaterial(rm);
            rmD.setLead_time(Integer.parseInt(leadTime)+prevLeadTime);
            rmD.setLot_size(Integer.parseInt(lotSize)+prevLotSize);
            rmD.setShelf_life(Integer.parseInt(shelfLife)+prevShelfLife);
            rmD.setStorage_unit(Integer.parseInt(storageUnit)+prevStorageUnit);
            
            if (index == -1) {
                rmDetailList.add(rmD);
            } else {
                rmDetailList.set(index, rmD);
            }
            session.setAttribute(listName, rmDetailList);
            json = gson.toJson(new JsonReturnMsg("Create Raw Material Details", "Raw Material Detail Added Successfully!", "info"));
            out.println(json);
        }
     }
    
       private void updateRawMaterialItem(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        int listIndex = Integer.parseInt(request.getParameter("listIndex"));
        int update_leadTime = Integer.parseInt(request.getParameter("update_leadTime"));
        int update_lotSize = Integer.parseInt(request.getParameter("update_lotSize"));
        int update_shelfLife = Integer.parseInt(request.getParameter("update_shelfLife"));
        int update_storageUnit = Integer.parseInt(request.getParameter("update_storageUnit"));
        String listName = request.getParameter("listName");

        HttpSession session = request.getSession();
        ArrayList<RawMaterialDetail> rmItemList = new ArrayList<RawMaterialDetail>();
        rmItemList = (ArrayList) session.getAttribute(listName);

        RawMaterialDetail rmD= rmItemList.get(listIndex);
        rmD.setLead_time(update_leadTime);
        rmD.setLot_size(update_lotSize);
        rmD.setShelf_life(update_shelfLife);
        rmD.setStorage_unit(update_storageUnit);

        session.setAttribute(listName, rmItemList);
        String json = gson.toJson(new JsonReturnMsg("Info", "Raw Material Details Updated.", "info"));
        out.println(json);
    }
       
    private void removeRMItem(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        int listIndex = Integer.parseInt(request.getParameter("listIndex"));
        String listName = request.getParameter("listName");

        HttpSession session = request.getSession();
        ArrayList<RawMaterialDetail> rmItemList = new ArrayList<RawMaterialDetail>();

        rmItemList = (ArrayList) session.getAttribute(listName);
        RawMaterialDetail rmD = rmItemList.get(listIndex);
        rmItemList.remove(rmD);
        session.setAttribute(listName, rmItemList);
        String json = gson.toJson(new JsonReturnMsg("Info", "Raw Material Details Deleted!", "info"));
        out.println(json);
    }


   private void createSupplier(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String json;

        String supplier_name = request.getParameter("supplier_name");
        String supplier_address = request.getParameter("supplier_address");
        String raw_material_id = request.getParameter("raw_material_id_hidden");
        String listName = request.getParameter("listName");

        HttpSession session = request.getSession();
        ArrayList<RawMaterialDetail> rmItemList = (ArrayList<RawMaterialDetail>) session.getAttribute(listName);
       if ((supplier_name.isEmpty() || supplier_address.isEmpty()) && (rmItemList.isEmpty() || rmItemList == null)) {
            json = gson.toJson(new JsonReturnMsg("Error", "Please enter supplier and raw material details!", "error"));
            out.println(json);

        } else if (supplier_name.isEmpty() || supplier_address.isEmpty()) {
            json = gson.toJson(new JsonReturnMsg("Error", "Please enter all supplier details!", "error"));
            out.println(json);

        } else if (rmItemList.isEmpty() || rmItemList == null) {
            json = gson.toJson(new JsonReturnMsg("Error", "Please enter at least a raw material!", "error"));
            out.println(json);

        } else {
           RawMaterial rm = rmFacade.find(Long.parseLong(raw_material_id));
           
            if (rm == null) {
                json = gson.toJson(new JsonReturnMsg("Error", "Raw Material does not exist.", "error"));
                out.println(json);

            } else {
                supplierFacade.create(rmItemList, raw_material_id,supplier_name, supplier_address);
                json = gson.toJson(new JsonReturnMsg("Info", "Supplier Created Successfully", "info"));
                session.setAttribute(listName, new ArrayList());
                out.println(json);
            }
        }
    }

    private void getSupplierDetails(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String json = "";

        String supplier_id = request.getParameter("supplier_id");
        String listName = request.getParameter("listName");
       
        HttpSession session = request.getSession();
        Supplier s = supplierFacade.find(Long.parseLong(supplier_id));
        ArrayList<RawMaterialDetail> rmItemList = new ArrayList<RawMaterialDetail>();
        for (RawMaterialDetail rmD : s.getRawMaterialDetail()) {
            rmItemList.add(rmD);
        }
        session.setAttribute(listName, rmItemList);
        
        //json
        s = (Supplier) ConvertToJsonObject.convert(s);
       
        json = gson.toJson(s);
        out.println(json);
    }

   private void updateSupplier(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String json;

        String supplier_id = request.getParameter("supplier_id");
        String supplier_name = request.getParameter("supplier_name");
        String supplier_address = request.getParameter("supplier_address");
        String listName = request.getParameter("listName");

        HttpSession session = request.getSession();
        ArrayList<RawMaterialDetail> rmItemList = (ArrayList<RawMaterialDetail>) session.getAttribute(listName);
        if (rmItemList.isEmpty() || rmItemList == null) {
            json = gson.toJson(new JsonReturnMsg("Error", "Please add a raw material!", "error"));
            out.println(json);

        } else {
            Supplier s = supplierFacade.find(Long.parseLong(supplier_id));
            s.setSupplier_name(supplier_name);
            s.setSupplier_address(supplier_address);
            supplierFacade.updateSupplier(s, rmItemList);
            json = gson.toJson(new JsonReturnMsg("Info", "Supplier Updated Successfully", "info"));
            out.println(json);
        }
    }
   
   private void retrieveSuppliers(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {

        String raw_material_id = request.getParameter("raw_material_id");

        ArrayList<Supplier> suppList = new ArrayList<Supplier>(supplierFacade.retrieveSuppliers(raw_material_id));
        //json
        ArrayList<Supplier> jsonList = new ArrayList<Supplier>();
                
            for (Supplier s1 : suppList) {
                Supplier s = new Supplier();
                s.setSupplier_id(s1.getSupplier_id());
                s.setSupplier_name(s1.getSupplier_name());
                s.setSupplier_address(s1.getSupplier_address());
                jsonList.add(s);
            }
        String json = gson.toJson(new JsonReturnTable(jsonList.size() + "", jsonList));
        //System.out.println(json);
        out.println(json);
    }
   
   private void deleteSupplier(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String supplier_id = request.getParameter("supplier_id");
        String raw_material_id = request.getParameter("raw_material_id");
        String content = supplierFacade.deleteSupplier(supplier_id,raw_material_id);

        String json = gson.toJson(new JsonReturnMsg("Delete Supplier", content, "info"));
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

   