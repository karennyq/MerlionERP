/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.reports;

import com.google.gson.Gson;
import ejb.sessionbeans.interfaces.SalesQuotationFacadeLocal;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.engine.export.JRGraphics2DExporterParameter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import org.persistence.LineItem;
import org.persistence.SalesLead;
import org.persistence.SalesQuotation;
import util.GVREPORTFORMAT;
import util.JsonReturnMsg;

/**
 *
 * @author YvonneOng
 */
@WebServlet(name = "ReportServlet", urlPatterns = {"/ReportServlet"})
public class ReportServlet extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    Gson gson = new Gson();
    @EJB
    SalesQuotationFacadeLocal salesQuotationFacade;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //response.setContentType("text/html;charset=UTF-8");
        //PrintWriter out = response.getWriter();
        ServletOutputStream out = response.getOutputStream();
        String action = request.getParameter("action");
        try {
            if (action.equals("produceQuotationReport")) {
                produceQuotationReport(request, response, out);
            } else if (action.equals("producePOReport")) {
                producePOReport(request, response, out);
            }
        } catch (Exception ex) {
//            String content = ex.getMessage();
//            String json = gson.toJson(new JsonReturnMsg("Error", content, "error"));
//            out.println(json);
            System.out.print(ex);
        } finally {
            out.close();
        }
    }

    private void produceQuotationReport(HttpServletRequest request, HttpServletResponse response, ServletOutputStream out) {
        // LogHandler.getLogger().info("ReportFormatController: produceAuditTrialReport() is called.");
        //String page = "";
        //String message = "";

        //String currentTimestamp = request.getParameter(GVREPORTFORMAT.PARAM_TIME_STAMP);
        //LogHandler.getLogger().debug("currentTimestamp-->" + currentTimestamp);

        //List<AuditTrial> auditTrialList = (ArrayList) request.getSession().getAttribute(GVAUDITTRIAL.PARAM_AUDIT_TRIAL_LIST + currentTimestamp);
        // LogHandler.getLogger().debug("auditTrialList-->" + auditTrialList.size());
        String quotation_id = request.getParameter("pre_sale_doc_id");
        String fileType=request.getParameter("fileType");
        HttpSession session = request.getSession();
        try {

            session.setAttribute("quotation" + quotation_id, null);
            SalesQuotation sq = salesQuotationFacade.find(Long.parseLong(quotation_id));

            SalesLead sl = sq.getInquirer();
            //report data
            ArrayList quotationReportList = new ArrayList();
            for (LineItem li : sq.getLineItems()) {
                QuotationReportHelperClass qReport = new QuotationReportHelperClass();
                qReport.setLine_item_id(li.getLine_item_id());
                qReport.setProduct_name(li.getProduct().getProduct_name());
                qReport.setQuantity(li.getQuantity());
                qReport.setBase_price(li.getBase_price());
                qReport.setBulk_discount(li.getBulk_discount());
                qReport.setActual_price(li.getActual_price());
                qReport.setActual_total(sq.getActual_total());
                qReport.setDiscount(sq.getDiscount());
                qReport.setDiscounted_total(Double.parseDouble(new java.text.DecimalFormat("0.00").format(sq.getDiscounted_total())));
                quotationReportList.add(qReport);
            }

            //report para
            Map parameters = new HashMap();
            parameters.put("pre_sale_doc_id", sq.getPre_sale_doc_id());
            parameters.put("expiry_date", sq.getExpiry_date());
            parameters.put("contact_person", sl.getContact_person());
            parameters.put("company_name", sl.getCompany_name());
            parameters.put("company_add", sl.getCompany_add());
            parameters.put("country", sl.getCountry());
            parameters.put("city", sl.getCity());
            parameters.put("contact_no", sl.getContact_no());

            JasperPrint jasperPrint = reportProcessing("SalesQuotationReport",
                    quotationReportList, parameters);

            //String fileType = request.getParameter(GVREPORTFORMAT.PARAM_FILETYPE_EXPORT);

            formatReportExport(request, response, jasperPrint, "SalesQuotationReport", fileType);
            
           // producePOReport(request, response, out);
            //PrintWriter out = response.getWriter();
            //request.getSession().setAttribute(GVREPORTFORMAT.PARAM_JRPRINT_REPORT + currentTimestamp, jasperPrint);
            //request.setAttribute(GVREPORTFORMAT.PARAM_TIME_STAMP, currentTimestamp);
        } catch (Exception ex) {
            //LogHandler.getLogger().error(ex.getMessage());
            System.out.println(ex.getMessage());
        } finally {
             session.setAttribute("quotation" + quotation_id, "");
        }
        //return GVPAGE.REPORT_FORMAT_PAGE;
    }

    private void producePOReport(HttpServletRequest request, HttpServletResponse response, ServletOutputStream out) {
        // LogHandler.getLogger().info("ReportFormatController: produceAuditTrialReport() is called.");
        //String page = "";
        //String message = "";

        //String currentTimestamp = request.getParameter(GVREPORTFORMAT.PARAM_TIME_STAMP);
        //LogHandler.getLogger().debug("currentTimestamp-->" + currentTimestamp);

        //List<AuditTrial> auditTrialList = (ArrayList) request.getSession().getAttribute(GVAUDITTRIAL.PARAM_AUDIT_TRIAL_LIST + currentTimestamp);
        // LogHandler.getLogger().debug("auditTrialList-->" + auditTrialList.size());

        String quotation_id = request.getParameter("pre_sale_doc_id");
         String fileType=request.getParameter("fileType");
         
         //System.out.println(fileType);
        HttpSession session = request.getSession();
        try {

            session.setAttribute("quotation" + quotation_id, null);
            SalesQuotation sq = salesQuotationFacade.find(Long.parseLong(quotation_id));

            SalesLead sl = sq.getInquirer();
            //report data
            ArrayList poReportList = new ArrayList();
            for (LineItem li : sq.getLineItems()) {
                POReportHelperClass qReport = new POReportHelperClass();
                qReport.setLine_item_id(li.getLine_item_id());
                qReport.setProduct_name(li.getProduct().getProduct_name());
                qReport.setQuantity(li.getQuantity());
                qReport.setBase_price(li.getBase_price());
                qReport.setBulk_discount(li.getBulk_discount());
                qReport.setActual_price(li.getActual_price());
                qReport.setActual_total(sq.getActual_total());
                qReport.setDiscount(sq.getDiscount());
                qReport.setDiscounted_total(Double.parseDouble(new java.text.DecimalFormat("0.00").format(sq.getDiscounted_total())));
                poReportList.add(qReport);
            }

            //report para
            Map parameters = new HashMap();
            parameters.put("pre_sale_doc_id", sq.getPre_sale_doc_id());
            parameters.put("expiry_date", sq.getExpiry_date());
            parameters.put("contact_person", sl.getContact_person());
            parameters.put("company_name", sl.getCompany_name());
            parameters.put("company_add", sl.getCompany_add());
            parameters.put("country", sl.getCountry());
            parameters.put("city", sl.getCity());
            parameters.put("contact_no", sl.getContact_no());

            JasperPrint jasperPrint = reportProcessing("PurchaseOrderTemplate",
                    poReportList, parameters);

            //String fileType = request.getParameter(GVREPORTFORMAT.PARAM_FILETYPE_EXPORT);

            formatReportExport(request, response, jasperPrint, "PurchaseOrderTemplate", fileType);
            //PrintWriter out = response.getWriter();
            //request.getSession().setAttribute(GVREPORTFORMAT.PARAM_JRPRINT_REPORT + currentTimestamp, jasperPrint);
            //request.setAttribute(GVREPORTFORMAT.PARAM_TIME_STAMP, currentTimestamp);
        } catch (Exception ex) {
            //LogHandler.getLogger().error(ex.getMessage());
            System.out.println(ex.getMessage());
        } finally {
             session.setAttribute("quotation" + quotation_id, "");
        }
        //return GVPAGE.REPORT_FORMAT_PAGE;
    }

    //format report
//    private void formatReportView(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        try {
//            //print to view
//            String timeStamp = request.getParameter(GVREPORTFORMAT.PARAM_TIME_STAMP);
//            JasperPrint jasperPrint = (JasperPrint) request.getSession().getAttribute(GVREPORTFORMAT.PARAM_JRPRINT_REPORT + timeStamp);
//            if (jasperPrint != null) {
//
//                response.setContentType("application/octet-stream");
//                ServletOutputStream ouputStream = response.getOutputStream();
//                ObjectOutputStream oos = new ObjectOutputStream(ouputStream);
//                oos.writeObject(jasperPrint);
//                oos.flush();
//                oos.close();
//                ouputStream.flush();
//                ouputStream.close();
//
//            } else {
//                System.out.println("report filled failed.");
//            }
//
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
//        }
//
//    }
    private JasperPrint reportProcessing(String reportName, List dataList, Map parameters) throws Exception {
        ServletContext context = this.getServletConfig().getServletContext();
        String reportFileName = context.getRealPath(GVREPORTFORMAT.PARAM_FOLDER_REPORT_LOCATION + reportName + ".jasper");

        File reportFile = new File(reportFileName);
        if (!reportFile.exists()) {
            //Compile report
            JasperCompileManager.compileReportToFile(context.getRealPath(GVREPORTFORMAT.PARAM_FOLDER_REPORT_LOCATION + reportName + ".jrxml"), reportFileName);
            //LogHandler.getLogger().debug("report path-->" + GVREPORTFORMAT.PARAM_FOLDER_REPORT_LOCATION + reportName + " compiled.");
            //LogHandler.getLogger().debug("report-->" + "File WebappReport.jasper not found. The report design must be compiled first.");
            //throw new JRRuntimeException("File WebappReport.jasper not found. The report design must be compiled first.");
        }


        JasperPrint jasperPrint =
                JasperFillManager.fillReport(
                reportFileName,
                parameters,
                new JRBeanCollectionDataSource(dataList));
        //LogHandler.getLogger().debug("report path-->" + GVREPORTFORMAT.PARAM_FOLDER_REPORT_LOCATION + reportName + " filled.");
        System.out.println("report path-->" + GVREPORTFORMAT.PARAM_FOLDER_REPORT_LOCATION + reportName + " filled.");
        return jasperPrint;
    }

    //export report
    private void formatReportExport(HttpServletRequest request, HttpServletResponse response, JasperPrint jasperPrintIn, String fileName, String fileType) {
        //LogHandler.getLogger().info("ReportFormatController: formatReportExport() is called.");

//        String timeStamp = request.getParameter(GVREPORTFORMAT.PARAM_TIME_STAMP).toString();
//        //LogHandler.getLogger().debug("timeStamp-->" + timeStamp);

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String currentTimestamp = dateFormat.format(date);

        if (fileName == null || fileType == null || fileName.isEmpty() || fileType.isEmpty()) {
            fileName = GVREPORTFORMAT.PARAM_DEFAULT_REPORT_NAME_EXPORT;
            fileType = GVREPORTFORMAT.PARAM_PDF_FILETYPE;
        }

        try {
            JasperPrint jasperPrint = jasperPrintIn;
            if (jasperPrint != null) {

                if (fileType.equals(GVREPORTFORMAT.PARAM_PDF_FILETYPE)) {

                    JasperExportManager.exportReportToPdfFile(jasperPrint, request.getRealPath(GVREPORTFORMAT.PARAM_FOLDER_REPORT_PATH) + "/" + currentTimestamp + fileType);

                } else if (fileType.equals(GVREPORTFORMAT.PARAM_HTML_FILETYPE)) {

                    JRHtmlExporter exporter = new JRHtmlExporter();
                    File outputFile = new File(request.getRealPath(GVREPORTFORMAT.PARAM_FOLDER_REPORT_PATH) + "/" + currentTimestamp + fileType);
                    FileOutputStream fos = new FileOutputStream(outputFile);
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fos);
                    exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);
                    exporter.exportReport();

                } else if (fileType.equals(GVREPORTFORMAT.PARAM_EXCEL_FILETYPE)) {
                    //JasperExportManager.exportReportToHtmlFile(jasperPrint, request.getRealPath("/reports") + "/" + currentTimestamp + fileType);
                    JRXlsExporter exporter = new JRXlsExporter();
                    File outputFile = new File(request.getRealPath(GVREPORTFORMAT.PARAM_FOLDER_REPORT_PATH) + "/" + currentTimestamp + fileType);
                    FileOutputStream fos = new FileOutputStream(outputFile);
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fos); //and output stream
                    //Excel specific parameter
                    exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
                    exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                    exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                    exporter.exportReport();
                } else if (fileType.equals(GVREPORTFORMAT.PARAM_WORD_FILETYPE)) {
                    JRDocxExporter exporter = new JRDocxExporter();
                    File outputFile = new File(request.getRealPath(GVREPORTFORMAT.PARAM_FOLDER_REPORT_PATH) + "/" + currentTimestamp + fileType);
                    FileOutputStream fos = new FileOutputStream(outputFile);
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fos); //and output stream
                    exporter.exportReport();
                } else if (fileType.equals(GVREPORTFORMAT.PARAM_CSV_FILETYPE)) {
                    JRCsvExporter exporter = new JRCsvExporter();
                    File outputFile = new File(request.getRealPath(GVREPORTFORMAT.PARAM_FOLDER_REPORT_PATH) + "/" + currentTimestamp + fileType);
                    FileOutputStream fos = new FileOutputStream(outputFile);
//                    exporter.setParameter(JRCsvExporterParameter.FIELD_DELIMITER,
//                            "|");
//                    exporter.setParameter(JRCsvExporterParameter.RECORD_DELIMITER,
//                            "\n");
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fos); //and output stream
                    exporter.exportReport();
                } else if (fileType.equals(GVREPORTFORMAT.PARAM_RTF_FILETYPE)) {
                    JRRtfExporter exporter = new JRRtfExporter();
                    File outputFile = new File(request.getRealPath(GVREPORTFORMAT.PARAM_FOLDER_REPORT_PATH) + "/" + currentTimestamp + fileType);
                    FileOutputStream fos = new FileOutputStream(outputFile);
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fos); //and output stream
                    exporter.exportReport();
                } else if (fileType.equals(GVREPORTFORMAT.PARAM_ODT_FILETYPE)) {
                    JROdtExporter exporter = new JROdtExporter();
                    File outputFile = new File(request.getRealPath(GVREPORTFORMAT.PARAM_FOLDER_REPORT_PATH) + "/" + currentTimestamp + fileType);
                    FileOutputStream fos = new FileOutputStream(outputFile);
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fos); //and output stream
                    exporter.exportReport();
                } else {
                    //in case of undefine file type or jpg :
                    JRGraphics2DExporter exporter = new JRGraphics2DExporter();
                    BufferedImage bufferedImage = new BufferedImage(jasperPrint.getPageWidth() * 4, jasperPrint.getPageHeight() * 4, BufferedImage.TYPE_INT_RGB);
                    Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
                    exporter.setParameter(JRGraphics2DExporterParameter.GRAPHICS_2D, g);
                    exporter.setParameter(JRGraphics2DExporterParameter.ZOOM_RATIO, Float.valueOf(4));
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.exportReport();
                    g.dispose();
                    File outputFile = new File(request.getRealPath(GVREPORTFORMAT.PARAM_FOLDER_REPORT_PATH) + "/" + currentTimestamp + GVREPORTFORMAT.PARAM_JPG_FILETYPE);
                    ImageIO.write(bufferedImage, "JPEG", outputFile);
                }

                File f = new File(request.getRealPath(GVREPORTFORMAT.PARAM_FOLDER_REPORT_PATH) + "/" + currentTimestamp + fileType);
                FileInputStream fin = new FileInputStream(f);

                ServletOutputStream outStream = response.getOutputStream();




                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment;filename=" + fileName + fileType);
                byte[] buffer = new byte[1024];
                int n = 0;
                while ((n = fin.read(buffer)) != -1) {
                    outStream.write(buffer, 0, n);
                    System.out.println(buffer);
                }
                outStream.flush();
                fin.close();
                outStream.close();
                f.delete();

            } else {
                //LogHandler.getLogger().error("report not print!");
                System.out.println("report not print!");
            }
        } catch (Exception ex) {
            //LogHandler.getLogger().error(ex.getMessage());
            System.out.println(ex.getMessage());
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
