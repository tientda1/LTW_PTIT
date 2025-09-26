package com.mycompany.ltw_ptit.servlet;

import com.mycompany.ltw_ptit.dao.UserDAO;
import com.mycompany.ltw_ptit.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet xử lý CRUD operations cho User
 */
@WebServlet("/users")
public class UserServlet extends HttpServlet {
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "list":
                listUsers(request, response);
                break;
            case "add":
                showAddForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteUser(request, response);
                break;
            case "view":
                viewUser(request, response);
                break;
            default:
                listUsers(request, response);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "add":
                addUser(request, response);
                break;
            case "edit":
                editUser(request, response);
                break;
            default:
                listUsers(request, response);
                break;
        }
    }
    
    private void listUsers(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        List<User> users = userDAO.getAllUsers();
        request.setAttribute("users", users);
        request.getRequestDispatcher("/users/list.jsp").forward(request, response);
    }
    
    private void showAddForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.getRequestDispatcher("/users/add.jsp").forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/users");
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            User user = userDAO.getUserById(id);
            
            if (user != null) {
                request.setAttribute("user", user);
                request.getRequestDispatcher("/users/edit.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/users");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/users");
        }
    }
    
    private void addUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String role = request.getParameter("role");
        
        // Validate input
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            fullName == null || fullName.trim().isEmpty() ||
            email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin bắt buộc");
            showAddForm(request, response);
            return;
        }
        
        // Check if username already exists
        User existingUser = userDAO.getUserByUsername(username.trim());
        if (existingUser != null) {
            request.setAttribute("error", "Tên đăng nhập đã tồn tại");
            showAddForm(request, response);
            return;
        }
        
        User user = new User();
        user.setUsername(username.trim());
        user.setPassword(password.trim());
        user.setFullName(fullName.trim());
        user.setEmail(email.trim());
        user.setPhone(phone != null ? phone.trim() : null);
        user.setRole(role != null ? role.trim() : "MEMBER");
        
        boolean success = userDAO.createUser(user);
        
        if (success) {
            response.sendRedirect(request.getContextPath() + "/users?message=add_success");
        } else {
            request.setAttribute("error", "Có lỗi xảy ra khi thêm người dùng");
            showAddForm(request, response);
        }
    }
    
    private void editUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String role = request.getParameter("role");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/users");
            return;
        }
        
        // Validate input
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            fullName == null || fullName.trim().isEmpty() ||
            email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin bắt buộc");
            showEditForm(request, response);
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            User user = userDAO.getUserById(id);
            
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/users");
                return;
            }
            
            // Check if username already exists (excluding current user)
            User existingUser = userDAO.getUserByUsername(username.trim());
            if (existingUser != null && existingUser.getId() != id) {
                request.setAttribute("error", "Tên đăng nhập đã tồn tại");
                showEditForm(request, response);
                return;
            }
            
            user.setUsername(username.trim());
            user.setPassword(password.trim());
            user.setFullName(fullName.trim());
            user.setEmail(email.trim());
            user.setPhone(phone != null ? phone.trim() : null);
            user.setRole(role != null ? role.trim() : "MEMBER");
            
            boolean success = userDAO.updateUser(user);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/users?message=edit_success");
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi cập nhật người dùng");
                showEditForm(request, response);
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/users");
        }
    }
    
    private void deleteUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/users");
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            boolean success = userDAO.deleteUser(id);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/users?message=delete_success");
            } else {
                response.sendRedirect(request.getContextPath() + "/users?message=delete_error");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/users");
        }
    }
    
    private void viewUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/users");
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            User user = userDAO.getUserById(id);
            
            if (user != null) {
                request.setAttribute("user", user);
                request.getRequestDispatcher("/users/view.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/users");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/users");
        }
    }
}
