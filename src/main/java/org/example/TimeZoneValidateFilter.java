package org.example;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.TimeZone;

@WebFilter("/time")
public class TimeZoneValidateFilter implements Filter{

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse resp = (HttpServletResponse) response;

            String tzParam = req.getParameter("timezone");

            // Якщо параметр не передано — пропускаємо далі (буде UTC)
            if (tzParam == null || tzParam.isEmpty()) {
                chain.doFilter(request, response);
                return;
            }

            // Перевіряємо формат "UTC", "UTC+2", "UTC-3" або стандартну зону
            boolean valid = false;

            if (tzParam.matches("UTC([+-]\\d+)?")) {
                valid = true; // валідний формат UTC+/-N
            } else {
                // перевірка стандартного ID часової зони
                String[] availableIDs = TimeZone.getAvailableIDs();
                for (String id : availableIDs) {
                    if (id.equals(tzParam)) {
                        valid = true;
                        break;
                    }
                }
            }

            if (valid) {
                // Параметр валідний — передаємо запит далі до сервлета
                chain.doFilter(request, response);
            } else {
                // Параметр некоректний — віддаємо 400
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.setContentType("text/html;charset=UTF-8");
                resp.getWriter().write("<!DOCTYPE html>" +
                        "<html><head><title>Invalid timezone</title></head>" +
                        "<body><h1>Invalid timezone</h1></body></html>");
            }
    }
}
