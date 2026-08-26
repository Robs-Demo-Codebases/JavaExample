package com.blackduck.example.example;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {}
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean success = false;
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/user", "root", "root")) {

            Statement stmt = null;

            String username = request.getParameter("username");
            String password = request.getParameter("password");
            // UNSAFE 
            // String query = "select * from tbluser where username='" + username + "' and password = '" + password + "'";
            // stmt = conn.createStatement();
            // rs = stmt.executeQuery(query);

            // SAFE
            String safeQuery = "select * from tbluser where username=? and password = ?";
            try (PreparedStatement prepStmt = conn.prepareStatement(safeQuery)) {
                prepStmt.setString(1,username);
                prepStmt.setString(2,password);
                try (ResultSet rs = prepStmt.executeQuery()) {



                    if (rs.next()) {
                        // Login Successful if match is found
                        success = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (success) {
            response.sendRedirect("home.html");
        } else {
            response.sendRedirect("login.html?error=1");
        }
    }
}
