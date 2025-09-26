package com.mycompany.ltw_ptit.servlet;

import com.mycompany.ltw_ptit.dao.CategoryDAO;
import com.mycompany.ltw_ptit.model.Category;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet xử lý CRUD operations cho Category
 */
@WebServlet("/categories")
public class CategoryServlet extends HttpServlet {
    private CategoryDAO categoryDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        categoryDAO = new CategoryDAO();
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
                listCategories(request, response);
                break;
            case "add":
                showAddForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteCategory(request, response);
                break;
            case "view":
                viewCategory(request, response);
                break;
            default:
                listCategories(request, response);
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
                addCategory(request, response);
                break;
            case "edit":
                editCategory(request, response);
                break;
            default:
                listCategories(request, response);
                break;
        }
    }
    
    private void listCategories(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        List<Category> categories = categoryDAO.getAllCategories();
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/categories/list.jsp").forward(request, response);
    }
    
    private void showAddForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.getRequestDispatcher("/categories/add.jsp").forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/categories");
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            Category category = categoryDAO.getCategoryById(id);
            
            if (category != null) {
                request.setAttribute("category", category);
                request.getRequestDispatcher("/categories/edit.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/categories");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/categories");
        }
    }
    
    private void addCategory(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        
        // Validate input
        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập tên thể loại");
            showAddForm(request, response);
            return;
        }
        
        Category category = new Category();
        category.setName(name.trim());
        category.setDescription(description != null ? description.trim() : null);
        
        boolean success = categoryDAO.createCategory(category);
        
        if (success) {
            response.sendRedirect(request.getContextPath() + "/categories?message=add_success");
        } else {
            request.setAttribute("error", "Có lỗi xảy ra khi thêm thể loại");
            showAddForm(request, response);
        }
    }
    
    private void editCategory(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/categories");
            return;
        }
        
        // Validate input
        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập tên thể loại");
            showEditForm(request, response);
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            Category category = categoryDAO.getCategoryById(id);
            
            if (category == null) {
                response.sendRedirect(request.getContextPath() + "/categories");
                return;
            }
            
            category.setName(name.trim());
            category.setDescription(description != null ? description.trim() : null);
            
            boolean success = categoryDAO.updateCategory(category);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/categories?message=edit_success");
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi cập nhật thể loại");
                showEditForm(request, response);
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/categories");
        }
    }
    
    private void deleteCategory(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/categories");
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            boolean success = categoryDAO.deleteCategory(id);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/categories?message=delete_success");
            } else {
                response.sendRedirect(request.getContextPath() + "/categories?message=delete_error");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/categories");
        }
    }
    
    private void viewCategory(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/categories");
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            Category category = categoryDAO.getCategoryById(id);
            
            if (category != null) {
                request.setAttribute("category", category);
                request.getRequestDispatcher("/categories/view.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/categories");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/categories");
        }
    }
}
