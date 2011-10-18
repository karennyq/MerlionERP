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
import org.persistence.DailyOverview;
import org.persistence.MonthlyOverview;
import org.persistence.PlannedDemand;
import org.persistence.Product;
import org.persistence.PublicHoliday;
import org.persistence.WeeklyOverview;
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
            if (action.equals("loadMonthlyTable")) {
                loadMonthlyTable(request, response, out);
                
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
                
            } else if (action.equals("updateMPP")) {
                updateMPP(request, response, out);
                
            } else if (action.equals("loadWeeklyTable")) {
                loadWeeklyTable(request, response, out);
                
            } else if (action.equals("loadDailyTable")) {
                loadDailyTable(request, response, out);
                
            } else if (action.equals("getDODetails")) {
                getDODetails(request, response, out);
                
            } else if (action.equals("updateDPP")) {
                updateDPP(request, response, out);
                
            } else if (action.equals("checkMonthAndYear")) {
                checkMonthAndYear(request, response, out);
                
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
    
    private void loadMonthlyTable(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
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
    
    private void loadWeeklyTable(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String mo_id = request.getParameter("mo_id");
        ArrayList<WeeklyOverview> woList = woFacade.findWeekInMonth(Long.parseLong(mo_id));

        for (WeeklyOverview wo : woList) {
            wo.setMonthlyOverview(null);
            wo.setPlannedDemand(null);
            wo.setDailyOverviews(null);
        }
        
        int totalRecord = woList.size();
        
        String json = gson.toJson(new JsonReturnTable(totalRecord + "", woList));
        out.println(json);
    }
    
    private void loadDailyTable(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String wo_id = request.getParameter("wo_id");
        WeeklyOverview wo = woFacade.find(Long.parseLong(wo_id));
        ArrayList <DailyOverview> doList = new ArrayList <DailyOverview> ();

        for (DailyOverview d : wo.getDailyOverviews()) {
            d.setDailyActivity(null);
            d.setPlannedDemand(null);
            d.setPublicHoliday(null);
            d.setWeeklyOverview(null);
            doList.add(d);
        }
        
        int totalRecord = doList.size();
        
        String json = gson.toJson(new JsonReturnTable(totalRecord + "", doList));
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
        String listName = request.getParameter("listName");
        
        HttpSession session = request.getSession();
        session.setAttribute(listName, new ArrayList());
        
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
        
        Double usedCapacity = Double.parseDouble(quantity) / pFacade.find(Long.parseLong(product_id)).getProduction_capacity();
        
        for (PlannedDemand pd: pdList) {
            usedCapacity = usedCapacity + pd.getHours_needed();
        }
        
        Double utilization = usedCapacity * 100 / Integer.parseInt(totalCapacity);
        
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
                    pd.setHours_needed(Double.parseDouble(new java.text.DecimalFormat("0.00").format(pd.getBoxes_to_produce() / pd.getProduct().getProduction_capacity())));
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
                pd.setBoxes_to_produce(Double.parseDouble(quantity));
                pd.setHours_needed(Double.parseDouble(new java.text.DecimalFormat("0.00").format(hoursNeeded)));

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
        mo.setOt_capacity(Integer.parseInt(workingDays) * 4.0 * plFacade.count());
        mo.setOt_utilization(0.00);
        mo.setPlannedDemand(new ArrayList());
        mo.setWeeklyOverviews(new ArrayList());
        
        mo = moFacade.createMO(mo, pdList);
        mo = woFacade.createWO(mo);
        doFacade.createDO(mo);
        
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
            pd.setPlanned_dmd_id(null);
            pdList.add(pd);
        }
        
        mo.setMonth(new java.text.DateFormatSymbols().getMonths()[Integer.parseInt(mo.getMonth())-1]);
        
        HttpSession session = request.getSession();
        session.setAttribute(listName, pdList);
        
        mo.setPlannedDemand(null);
        mo.setWeeklyOverviews(null);
        
        String json = gson.toJson(mo);
        out.println(json);
    }
    
    private void getDODetails(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String do_id = request.getParameter("do_id");
        String listName = request.getParameter("listName");

        DailyOverview d = doFacade.find(Long.parseLong(do_id));
        
        ArrayList<PlannedDemand> pdList = new ArrayList<PlannedDemand>();
        for (PlannedDemand pd: d.getPlannedDemand()) {
            pd.setPlanned_dmd_id(null);
            pdList.add(pd);
        }
        
        HttpSession session = request.getSession();
        session.setAttribute(listName, pdList);
        
        d.setPlannedDemand(null);
        d.setDailyActivity(null);
        d.getWeeklyOverview().setDailyOverviews(null);
        d.getWeeklyOverview().setPlannedDemand(null);
        d.getWeeklyOverview().getMonthlyOverview().setWeeklyOverviews(null);
        d.getWeeklyOverview().getMonthlyOverview().setPlannedDemand(null);
        
        String json = gson.toJson(d);
        out.println(json);
    }
    
    private void updateMPP(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String content = "";
        String json = "";
        
        String mo_id = request.getParameter("mo_id");
        String listName = request.getParameter("listName");
        
        HttpSession session = request.getSession();
        ArrayList<PlannedDemand> pdList = (ArrayList<PlannedDemand>) session.getAttribute(listName);
        
        ArrayList<PlannedDemand> addPDList = new ArrayList<PlannedDemand>();
        ArrayList<PlannedDemand> subPDList = new ArrayList<PlannedDemand>();
        ArrayList<PlannedDemand> newPDList = new ArrayList<PlannedDemand>();
        ArrayList<PlannedDemand> delPDList = new ArrayList<PlannedDemand>();
        
        MonthlyOverview m = moFacade.find(Long.parseLong(mo_id));
        
        for (PlannedDemand originalPD : m.getPlannedDemand()) {
            boolean exist = false;
            for (PlannedDemand updatedPD : pdList) {
                if (originalPD.getProduct().getProduct_id() == updatedPD.getProduct().getProduct_id()) {
                    exist = true;
                    if (originalPD.getBoxes_to_produce() < updatedPD.getBoxes_to_produce()) {
                        updatedPD.setBoxes_to_produce(updatedPD.getBoxes_to_produce() - originalPD.getBoxes_to_produce());
                        addPDList.add(updatedPD);
                    } else if (originalPD.getBoxes_to_produce() > updatedPD.getBoxes_to_produce()) {
                        updatedPD.setBoxes_to_produce(originalPD.getBoxes_to_produce() - updatedPD.getBoxes_to_produce());
                        subPDList.add(updatedPD);
                    }
                }
            }
            
            if (exist == false) {
                delPDList.add(originalPD);
            }
        }
        
        for (PlannedDemand updatedPD : pdList) {
            boolean exist = false;
            for (PlannedDemand originalPD : m.getPlannedDemand()) {
                if (originalPD.getProduct().getProduct_id() == updatedPD.getProduct().getProduct_id()) {
                    exist = true;
                }
            }
            
            if (exist == false) {
                newPDList.add(updatedPD);
            }
        }
        
        moFacade.updateMO(m, addPDList, subPDList, newPDList, delPDList);
        for (WeeklyOverview w : m.getWeeklyOverviews()) {
            woFacade.updateWOByMonth(w, addPDList, subPDList, newPDList, delPDList);
            for (DailyOverview d : w.getDailyOverviews()) {
                doFacade.updateDOByMonth(d, addPDList, subPDList, newPDList, delPDList);
            }
        }
        
        content = "Update Monthly Production Plan " + mo_id + " Successfully.";
        json = gson.toJson(new JsonReturnMsg("Update Monthly Production Plan", content, "info"));
        out.println(json);
    }
    
    private void updateDPP(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String content = "";
        String json = "";
        
        String do_id = request.getParameter("do_id");
        String listName = request.getParameter("listName");
        
        HttpSession session = request.getSession();
        ArrayList<PlannedDemand> pdList = (ArrayList<PlannedDemand>) session.getAttribute(listName);
        
        ArrayList<PlannedDemand> addPDList = new ArrayList<PlannedDemand>();
        ArrayList<PlannedDemand> subPDList = new ArrayList<PlannedDemand>();
        ArrayList<PlannedDemand> newPDList = new ArrayList<PlannedDemand>();
        ArrayList<PlannedDemand> delPDList = new ArrayList<PlannedDemand>();
        
        DailyOverview d = doFacade.find(Long.parseLong(do_id));
        
        for (PlannedDemand originalPD : d.getPlannedDemand()) {
            boolean exist = false;
            for (PlannedDemand updatedPD : pdList) {
                if (originalPD.getProduct().getProduct_id() == updatedPD.getProduct().getProduct_id()) {
                    exist = true;
                    if (originalPD.getBoxes_to_produce() < updatedPD.getBoxes_to_produce()) {
                        updatedPD.setBoxes_to_produce(updatedPD.getBoxes_to_produce() - originalPD.getBoxes_to_produce());
                        addPDList.add(updatedPD);
                    } else if (originalPD.getBoxes_to_produce() > updatedPD.getBoxes_to_produce()) {
                        updatedPD.setBoxes_to_produce(originalPD.getBoxes_to_produce() - updatedPD.getBoxes_to_produce());
                        subPDList.add(updatedPD);
                    }
                }
            }
            
            if (exist == false) {
                delPDList.add(originalPD);
            }
        }
        
        for (PlannedDemand updatedPD : pdList) {
            boolean exist = false;
            for (PlannedDemand originalPD : d.getPlannedDemand()) {
                if (originalPD.getProduct().getProduct_id() == updatedPD.getProduct().getProduct_id()) {
                    exist = true;
                }
            }
            
            if (exist == false) {
                newPDList.add(updatedPD);
            }
        }
        
        doFacade.updateDOByDay(d, addPDList, subPDList, newPDList, delPDList);
        woFacade.updateWOByDay(d.getWeeklyOverview(), addPDList, subPDList, newPDList, delPDList);
        moFacade.updateMO(d.getWeeklyOverview().getMonthlyOverview(), addPDList, subPDList, newPDList, delPDList);
        
        content = "Update Daily Production Plan " + do_id + " Successfully.";
        json = gson.toJson(new JsonReturnMsg("Update Daily Production Plan", content, "info"));
        out.println(json);
    }
    
    private void checkMonthAndYear(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String mo_id = request.getParameter("mo_id");
        MonthlyOverview m = moFacade.find(Long.parseLong(mo_id));
        
        response.setContentType("application/xml");
        response.setCharacterEncoding("UTF-8");
        
        Calendar now = Calendar.getInstance();
        
        if (m.getYear() > now.get(Calendar.YEAR)) {
            response.getWriter().write("<result>pass</result>");
            
        } else if (m.getYear() < now.get(Calendar.YEAR)) {
            response.getWriter().write("<result>fail</result>");
            
        } else {
            if ((Integer.parseInt(m.getMonth()) - 1) <= now.get(Calendar.MONTH)) {
                response.getWriter().write("<result>fail</result>");
                
            } else {
                response.getWriter().write("<result>pass</result>");
            }
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
