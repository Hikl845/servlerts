package org.example;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/time") // лише шлях, без хосту і порту
public class TImeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");

        String tzParam = req.getParameter("timezone");
        ZonedDateTime time;

        if (tzParam == null || tzParam.isEmpty()) {
            tzParam = "UTC";
            time = ZonedDateTime.now(ZoneOffset.UTC);
        } else if (tzParam.matches("UTC([+-]\\d+)?")) {
            String offsetStr = tzParam.substring(3);
            if (offsetStr.isEmpty()) offsetStr = "+0";
            int hoursOffset = Integer.parseInt(offsetStr);
            time = ZonedDateTime.now(ZoneOffset.ofHours(hoursOffset));
        } else {
            time = ZonedDateTime.now(ZoneId.of(tzParam));
        }

        String formatedAnswer = time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " " + tzParam;

        String htmlResponse = "<!DOCTYPE html>" +
                "<html>" +
                "<head><title>Current Time</title></head>" +
                "<body>" +
                "<h1>Поточний час:</h1>" +
                "<p>" + formatedAnswer + "</p>" +
                "</body>" +
                "</html>";

        resp.getWriter().write(htmlResponse); // <-- обов'язково потрібно додати
    }
}
