package com.mycompany.ltw_ptit.servlet;

import com.mycompany.ltw_ptit.dao.BookDAO;
import com.mycompany.ltw_ptit.dao.CategoryDAO;
import com.mycompany.ltw_ptit.model.Book;
import com.mycompany.ltw_ptit.model.Category;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Servlet xử lý CRUD operations cho Book
 */
@WebServlet("/books")
public class BookServlet extends HttpServlet {
    private BookDAO bookDAO;
    private CategoryDAO categoryDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        bookDAO = new BookDAO();
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
                listBooks(request, response);
                break;
            case "add":
                showAddForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteBook(request, response);
                break;
            case "search":
                searchBooks(request, response);
                break;
            case "view":
                viewBook(request, response);
                break;
            default:
                listBooks(request, response);
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
                addBook(request, response);
                break;
            case "edit":
                editBook(request, response);
                break;
            default:
                listBooks(request, response);
                break;
        }
    }
    
    private void listBooks(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        List<Book> books = bookDAO.getAllBooks();
        List<Category> categories = categoryDAO.getAllCategories();
        
        request.setAttribute("books", books);
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/books/list.jsp").forward(request, response);
    }
    
    private void showAddForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        List<Category> categories = categoryDAO.getAllCategories();
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/books/add.jsp").forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/books");
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            Book book = bookDAO.getBookById(id);
            
            if (book != null) {
                List<Category> categories = categoryDAO.getAllCategories();
                request.setAttribute("book", book);
                request.setAttribute("categories", categories);
                request.getRequestDispatcher("/books/edit.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/books");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/books");
        }
    }
    
    private void addBook(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String isbn = request.getParameter("isbn");
        String publisher = request.getParameter("publisher");
        String publicationYearStr = request.getParameter("publicationYear");
        String categoryIdStr = request.getParameter("categoryId");
        String totalCopiesStr = request.getParameter("totalCopies");
        String description = request.getParameter("description");
        String imageUrl = request.getParameter("imageUrl");
        
        // Validate input
        if (title == null || title.trim().isEmpty() || 
            author == null || author.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin bắt buộc");
            showAddForm(request, response);
            return;
        }
        
        try {
            Book book = new Book();
            book.setTitle(title.trim());
            book.setAuthor(author.trim());
            book.setIsbn(isbn != null ? isbn.trim() : null);
            book.setPublisher(publisher != null ? publisher.trim() : null);
            
            if (publicationYearStr != null && !publicationYearStr.trim().isEmpty()) {
                book.setPublicationYear(Integer.parseInt(publicationYearStr.trim()));
            }
            
            if (categoryIdStr != null && !categoryIdStr.trim().isEmpty()) {
                book.setCategoryId(Integer.parseInt(categoryIdStr.trim()));
            }
            
            if (totalCopiesStr != null && !totalCopiesStr.trim().isEmpty()) {
                int totalCopies = Integer.parseInt(totalCopiesStr.trim());
                book.setTotalCopies(totalCopies);
                book.setAvailableCopies(totalCopies);
            } else {
                book.setTotalCopies(1);
                book.setAvailableCopies(1);
            }
            
            book.setDescription(description != null ? description.trim() : null);
            book.setImageUrl(imageUrl != null ? imageUrl.trim() : null);
            
            boolean success = bookDAO.createBook(book);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/books?message=add_success");
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi thêm sách");
                showAddForm(request, response);
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Dữ liệu nhập vào không hợp lệ");
            showAddForm(request, response);
        }
    }
    
    private void editBook(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String isbn = request.getParameter("isbn");
        String publisher = request.getParameter("publisher");
        String publicationYearStr = request.getParameter("publicationYear");
        String categoryIdStr = request.getParameter("categoryId");
        String totalCopiesStr = request.getParameter("totalCopies");
        String availableCopiesStr = request.getParameter("availableCopies");
        String description = request.getParameter("description");
        String imageUrl = request.getParameter("imageUrl");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/books");
            return;
        }
        
        // Validate input
        if (title == null || title.trim().isEmpty() || 
            author == null || author.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin bắt buộc");
            showEditForm(request, response);
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            Book book = bookDAO.getBookById(id);
            
            if (book == null) {
                response.sendRedirect(request.getContextPath() + "/books");
                return;
            }
            
            book.setTitle(title.trim());
            book.setAuthor(author.trim());
            book.setIsbn(isbn != null ? isbn.trim() : null);
            book.setPublisher(publisher != null ? publisher.trim() : null);
            
            if (publicationYearStr != null && !publicationYearStr.trim().isEmpty()) {
                book.setPublicationYear(Integer.parseInt(publicationYearStr.trim()));
            }
            
            if (categoryIdStr != null && !categoryIdStr.trim().isEmpty()) {
                book.setCategoryId(Integer.parseInt(categoryIdStr.trim()));
            }
            
            if (totalCopiesStr != null && !totalCopiesStr.trim().isEmpty()) {
                book.setTotalCopies(Integer.parseInt(totalCopiesStr.trim()));
            }
            
            if (availableCopiesStr != null && !availableCopiesStr.trim().isEmpty()) {
                book.setAvailableCopies(Integer.parseInt(availableCopiesStr.trim()));
            }
            
            book.setDescription(description != null ? description.trim() : null);
            book.setImageUrl(imageUrl != null ? imageUrl.trim() : null);
            
            boolean success = bookDAO.updateBook(book);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/books?message=edit_success");
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi cập nhật sách");
                showEditForm(request, response);
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Dữ liệu nhập vào không hợp lệ");
            showEditForm(request, response);
        }
    }
    
    private void deleteBook(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/books");
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            boolean success = bookDAO.deleteBook(id);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/books?message=delete_success");
            } else {
                response.sendRedirect(request.getContextPath() + "/books?message=delete_error");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/books");
        }
    }
    
    private void searchBooks(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String keyword = request.getParameter("keyword");
        List<Book> books;
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            books = bookDAO.searchBooks(keyword.trim());
            request.setAttribute("keyword", keyword.trim());
        } else {
            books = bookDAO.getAllBooks();
        }
        
        List<Category> categories = categoryDAO.getAllCategories();
        request.setAttribute("books", books);
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/books/list.jsp").forward(request, response);
    }
    
    private void viewBook(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/books");
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            Book book = bookDAO.getBookById(id);
            
            if (book != null) {
                request.setAttribute("book", book);
                request.getRequestDispatcher("/books/view.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/books");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/books");
        }
    }
}
