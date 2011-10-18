/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.sysAdmin;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.awt.*;
import java.util.*;
import javax.servlet.annotation.WebServlet;

/**
 *
 * @author karennyq
 */
@WebServlet(name = "CaptchaServlet", urlPatterns = {"/Captcha.jpg"} )
public class CaptchaServlet extends HttpServlet {

    private int height = 0;
    private int width = 0;
    public static final String CAPTCHA_KEY = "captcha_key_name";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        height = 30;
        width = 120;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response)
            throws IOException, ServletException {
        //Expire response
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Max-Age", 0);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = image.createGraphics();
        Hashtable map = new Hashtable();
        Random r = new Random();
        String token = Long.toString(Math.abs(r.nextLong()), 36);
        String ch = token.substring(0, 5);
        Color c = new Color(1f, 1f, 1f);
        GradientPaint gp = new GradientPaint(50, 50, c, 15, 25, Color.white, true);
        graphics2D.setPaint(gp);
        Font font = new Font("Verdana", Font.CENTER_BASELINE, 18);
        graphics2D.setFont(font);
        graphics2D.drawString(ch, 2, 20);
        graphics2D.drawLine(0, r.nextInt(25)+r.nextInt(10), 100, r.nextInt(25)+r.nextInt(10));
        graphics2D.dispose();

        HttpSession session = req.getSession(true);
        session.setAttribute(CAPTCHA_KEY, ch);

        OutputStream outputStream = response.getOutputStream();
        ImageIO.write(image, "jpeg", outputStream);
        outputStream.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
