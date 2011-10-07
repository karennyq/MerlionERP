/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.production;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ejb.sessionbeans.interfaces.DailyOverviewFacadeLocal;
import ejb.sessionbeans.interfaces.MonthlyOverviewFacadeLocal;
import ejb.sessionbeans.interfaces.ProductFacadeLocal;
import ejb.sessionbeans.interfaces.ProductionLineFacadeLocal;
import ejb.sessionbeans.interfaces.PublicHolidayFacadeLocal;
import ejb.sessionbeans.interfaces.WeeklyOverviewFacadeLocal;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.persistence.MonthlyOverview;
import org.persistence.PlannedDemand;
import org.persistence.Product;
import org.persistence.PublicHoliday;
import util.JsonReturnMsg;
import util.JsonReturnTable;

/**
 *
 * @author Randy
 */
@WebServlet(name = "ProductionPlanServlet", urlPatterns = {"/ProductionPlanServlet"})
public class ProductionPlanServlet extends HttpServlet {

    @EJB
    MonthlyOverviewFacadeLocal moFacade;
    
    @EJB
    PublicHolidayFacadeLocal phFacade;
    
    @EJB
    ProductionLineFacadeLocal plFacade;
    
    @EJB
    ProductFacadeLocal pFacade;
    
    @EJB
    WeeklyOverviewFacadeLocal woFacade;
    
    @EJB
    DailyOverviewFacadeLocal doFacade;
    
    Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
    
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
                
            } else if (action.equals("checkPPDone")) {
                checkPPDone(request, response, out);
                
            } else if (action.equals("calculatePPDetails")) {
                calculatePPDetails(request, response, out);
                
            } else if (action.equals("getPlannedDmdList")) {
                getPlannedDmdList(request, response, out);
                
            } else if (action.equals("addPlannedDmd")) {
                addPlannedDmd(request, response, out);
                
            } else if (action.equals("deletePlannedDmd")) {
                deletePlannedDmd(request, response, out);
                
            } else if (action.equals("createPP")) {
                createPP(request, response, out);
                
            } else if (action.equals("getMODetails")) {
                getMODetails(request, response, out);
                
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
        ArrayList<MonthlyOverview> moList = new ArrayList<MonthlyOverview>(moFacade.findAll());
        int totalRecord = moFacade.count();

        for (int i=0; i<moList.size(); i++) {
            MonthlyOverview mo = moList.get(i);
            mo.setMonth(new java.text.DateFormatSymbols().getMonths()[Integer.parseInt(mo.getMonth())-1]);
            mo.setPlannedDemand(null);
            mo.setWeeklyOverviews(null);
        }
        
        String json = gson.toJson(new JsonReturnTable(totalRecord + "", moList));
        out.println(json);
    }
    
    private void checkPPDone(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        
        boolean result = moFacade.checkPPDone(month, Integer.parseInt(year));
        
        response.setContentType("application/xml");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("<result>" + result + "</result>");
    }
    
    private void calculatePPDetails(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(year), Integer.parseInt(month)-1, 1);
        
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        Date min = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(1 + "/" + month + "/" + year);
        Date max = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(maxDay + "/" + month + "/" + year);
        
        ArrayList<PublicHoliday> phList = new ArrayList<PublicHoliday>(phFacade.getPHInPeriod(min, max));
        
        for (PublicHoliday ph: phList) {
            Calendar date = Calendar.getInstance();
            date.setTime(ph.getPh_date());
            if (date.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                continue;
            } else {
                maxDay = maxDay - 1;
            }
        }
        
        do {
            int day = cal.get(Calendar.DAY_OF_WEEK);
            if (day == Calendar.SATURDAY || day == Calendar.SUNDAY) {
                maxDay = maxDay - 1;
            }
            cal.add(Calendar.DAY_OF_YEAR, 1);
        } while (cal.get(Calendar.MONTH) == Integer.parseInt(month)-1);
        
        response.setContentType("application/xml");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("<details>");
        response.getWriter().write("<working_days>" + maxDay + "</working_days>");
        response.getWriter().write("<capacity>" + (plFacade.count() * maxDay * 8) + "</capacity>");
        response.getWriter().write("<utilization>0</utilization>");
        response.getWriter().write("</details>");
    }
    
    private void getPlannedDmdList(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String listName = request.getParameter("listName");
        HttpSession session = request.getSession();
        ArrayList<PlannedDemand> pdList = (ArrayList<PlannedDemand>) session.getAttribute(listName);
        for (PlannedDemand pd: pdList) {
            pd.setPlannedDemandAmendment(null);
            pd.getProduct().setBulkDiscounts(null);
            pd.getProduct().setDeliveryOrderDetails(null);
            pd.getProduct().setIngredients(null);
            pd.getProduct().setPdtBatches(null);
            pd.getProduct().setSalesForecasts(null);
        }
        
        String json = gson.toJson(new JsonReturnTable(pdList.size() + "", pdList));
        out.println(json);
    }
    
    private void addPlannedDmd(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String product_id = request.getParameter("product_id");
        String quantity = request.getParameter("quantity");
        String totalCapacity = request.getParameter("capacity");
        String listName = request.getParameter("listName");
        
        HttpSession session = request.getSession();
        ArrayList<PlannedDemand> pdList = (ArrayList<PlannedDemand>) session.getAttribute(listName);
        
        double usedCapacity = Double.parseDouble(quantity) / pFacade.find(Long.parseLong(product_id)).getProduction_capacity();
        
        for (PlannedDemand pd: pdList) {
            usedCapacity = usedCapacity + pd.getHours_needed();
        }
        
        double utilization = usedCapacity * 100 / Integer.parseInt(totalCapacity);
        
        if (utilization > 90) {
            response.setContentType("application/xml");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("<reply>");
            response.getWriter().write("<message>false</message>");
            response.getWriter().write("</reply>");
            
        } else {
            boolean exist = false;
            for (PlannedDemand pd: pdList) {
                if (pd.getProduct().getProduct_id() == Long.parseLong(product_id)) {
                    pd.setBoxes_to_produce(pd.getBoxes_to_produce() + Integer.parseInt(quantity));
                    pd.setHours_needed((double)pd.getBoxes_to_produce() / pd.getProduct().getProduction_capacity());
                    exist = true;
                    break;
                }
            }

            if (exist == false) {
                Product p1 = pFacade.find(Long.parseLong(product_id));
                p1.setBulkDiscounts(null);
                p1.setSalesForecasts(null);
                p1.setPdtBatches(null);
                p1.setIngredients(null);

                Double hoursNeeded = Double.parseDouble(quantity) / p1.getProduction_capacity();
                PlannedDemand pd = new PlannedDemand();
                pd.setProduct(p1);
                pd.setBoxes_to_produce(Integer.parseInt(quantity));
                pd.setHours_needed(hoursNeeded);

                pdList.add(pd);
            }
            
            session.setAttribute(listName, pdList);
            response.setContentType("application/xml");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("<reply>");
            response.getWriter().write("<message>true</message>");
            response.getWriter().write("<utilization>" + new java.text.DecimalFormat("0.00").format(utilization) + "</utilization>");
            response.getWriter().write("</reply>");
        }
    }
    
    private void deletePlannedDmd(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String position = request.getParameter("position");
        String totalCapacity = request.getParameter("capacity");
        String listName = request.getParameter("listName");
        
        HttpSession session = request.getSession();
        ArrayList<PlannedDemand> pdList = (ArrayList<PlannedDemand>) session.getAttribute(listName);
        pdList.remove(Integer.parseInt(position));
        
        session.setAttribute(listName, pdList);
        
        double usedCapacity = 0;
        
        for (PlannedDemand pd: pdList) {
            usedCapacity = usedCapacity + pd.getHours_needed();
        }
        
        double utilization = usedCapacity * 100 / Integer.parseInt(totalCapacity);
        
        response.setContentType("application/xml");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("<utilization>" + new java.text.DecimalFormat("0.00").format(utilization) + "</utilization>");
    }
    
    private void createPP(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        
        String content = "";
        String json = "";
        
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        String workingDays = request.getParameter("working_days");
        String capacity = request.getParameter("capacity");
        String utilization = request.getParameter("utilization");
        String listName = request.getParameter("listName");
        
        HttpSession session = request.getSession();
        ArrayList<PlannedDemand> pdList = (ArrayList<PlannedDemand>) session.getAttribute(listName);
        
        MonthlyOverview mo = new MonthlyOverview();
        mo.setYear(Integer.parseInt(year));
        mo.setMonth(month);
        mo.setWorking_days(Integer.parseInt(workingDays));
        mo.setCapacity(Double.parseDouble(capacity));
        mo.setUtilization(Double.parseDouble(utilization));
        mo.setOt_capacity(0.00);
        mo.setOt_utilization(0.00);
        mo.setPlannedDemand(new ArrayList());
        mo.setWeeklyOverviews(new ArrayList());
        
        mo = moFacade.createMO(mo, pdList);
        mo = woFacade.createWO(mo, pdList);
        doFacade.createDO(mo, pdList);
        
        content = "Created Planned Demand on " + new java.text.DateFormatSymbols().getMonths()[Integer.parseInt(month)-1] + ", " + year + " Successfully.";
        
        session.setAttribute(listName, new ArrayList());
        json = gson.toJson(new JsonReturnMsg("Create Production Plan", content, "info"));
        out.println(json);
    }
    
    private void getMODetails(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String mo_id = request.getParameter("mo_id");
        String listName = request.getParameter("listName");

        MonthlyOverview mo = moFacade.find(Long.parseLong(mo_id));
        
        ArrayList<PlannedDemand> pdList = new ArrayList<PlannedDemand>();
        for (PlannedDemand pd: mo.getPlannedDemand()) {
            pdList.add(pd);
        }
        
        HttpSession session = request.getSession();
        session.setAttribute(listName, pdList);
        
        mo.setPlannedDemand(null);
        mo.setWeeklyOverviews(null);
        
        String json = gson.toJson(mo);
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
