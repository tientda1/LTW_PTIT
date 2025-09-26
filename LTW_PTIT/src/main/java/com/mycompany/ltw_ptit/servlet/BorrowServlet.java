package com.mycompany.ltw_ptit.servlet;

import com.mycompany.ltw_ptit.dao.BorrowRecordDAO;
import com.mycompany.ltw_ptit.dao.BookDAO;
import com.mycompany.ltw_ptit.dao.UserDAO;
import com.mycompany.ltw_ptit.model.BorrowRecord;
import com.mycompany.ltw_ptit.model.Book;
import com.mycompany.ltw_ptit.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Servlet xử lý CRUD operations cho BorrowRecord
 */
@WebServlet("/borrow")
public class BorrowServlet extends HttpServlet {
    private BorrowRecordDAO borrowRecordDAO;
    private BookDAO bookDAO;
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        borrowRecordDAO = new BorrowRecordDAO();
        bookDAO = new BookDAO();
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
                listBorrowRecords(request, response);
                break;
            case "add":
                showAddForm(request, response);
                break;
            case "return":
                returnBook(request, response);
                break;
            case "view":
                viewBorrowRecord(request, response);
                break;
            case "my_borrows":
                listMyBorrows(request, response);
                break;
            default:
                listBorrowRecords(request, response);
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
                addBorrowRecord(request, response);
                break;
            case "return":
                processReturn(request, response);
                break;
            default:
                listBorrowRecords(request, response);
                break;
        }
    }
    
    private void listBorrowRecords(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        List<BorrowRecord> records = borrowRecordDAO.getAllBorrowRecords();
        request.setAttribute("records", records);
        request.getRequestDispatcher("/borrow/list.jsp").forward(request, response);
    }
    
    private void listMyBorrows(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        List<BorrowRecord> records = borrowRecordDAO.getBorrowRecordsByUser(user.getId());
        request.setAttribute("records", records);
        request.getRequestDispatcher("/borrow/my_borrows.jsp").forward(request, response);
    }
    
    private void showAddForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        List<Book> books = bookDAO.getAllBooks();
        List<User> users = userDAO.getAllUsers();
        
        request.setAttribute("books", books);
        request.setAttribute("users", users);
        request.getRequestDispatcher("/borrow/add.jsp").forward(request, response);
    }
    
    private void addBorrowRecord(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String userIdStr = request.getParameter("userId");
        String bookIdStr = request.getParameter("bookId");
        String borrowDateStr = request.getParameter("borrowDate");
        String dueDateStr = request.getParameter("dueDate");
        String notes = request.getParameter("notes");
        
        // Validate input
        if (userIdStr == null || userIdStr.trim().isEmpty() ||
            bookIdStr == null || bookIdStr.trim().isEmpty() ||
            borrowDateStr == null || borrowDateStr.trim().isEmpty() ||
            dueDateStr == null || dueDateStr.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin");
            showAddForm(request, response);
            return;
        }
        
        try {
            int userId = Integer.parseInt(userIdStr);
            int bookId = Integer.parseInt(bookIdStr);
            LocalDate borrowDate = LocalDate.parse(borrowDateStr);
            LocalDate dueDate = LocalDate.parse(dueDateStr);
            
            // Check if book is available
            Book book = bookDAO.getBookById(bookId);
            if (book == null || book.getAvailableCopies() <= 0) {
                request.setAttribute("error", "Sách không có sẵn để mượn");
                showAddForm(request, response);
                return;
            }
            
            BorrowRecord record = new BorrowRecord(userId, bookId, borrowDate, dueDate);
            record.setNotes(notes != null ? notes.trim() : null);
            
            boolean success = borrowRecordDAO.createBorrowRecord(record);
            
            if (success) {
                // Update available copies
                bookDAO.updateAvailableCopies(bookId, book.getAvailableCopies() - 1);
                response.sendRedirect(request.getContextPath() + "/borrow?message=add_success");
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi tạo phiếu mượn");
                showAddForm(request, response);
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Dữ liệu nhập vào không hợp lệ");
            showAddForm(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            showAddForm(request, response);
        }
    }
    
    private void returnBook(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/borrow");
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            BorrowRecord record = borrowRecordDAO.getBorrowRecordById(id);
            
            if (record != null && "BORROWED".equals(record.getStatus())) {
                request.setAttribute("record", record);
                request.getRequestDispatcher("/borrow/return.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/borrow");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/borrow");
        }
    }
    
    private void processReturn(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        String returnDateStr = request.getParameter("returnDate");
        String fineAmountStr = request.getParameter("fineAmount");
        String notes = request.getParameter("notes");
        
        if (idStr == null || idStr.trim().isEmpty() ||
            returnDateStr == null || returnDateStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/borrow");
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            LocalDate returnDate = LocalDate.parse(returnDateStr);
            
            BigDecimal fineAmount = BigDecimal.ZERO;
            if (fineAmountStr != null && !fineAmountStr.trim().isEmpty()) {
                fineAmount = new BigDecimal(fineAmountStr.trim());
            }
            
            BorrowRecord record = borrowRecordDAO.getBorrowRecordById(id);
            if (record == null) {
                response.sendRedirect(request.getContextPath() + "/borrow");
                return;
            }
            
            boolean success = borrowRecordDAO.returnBook(id, returnDate, fineAmount, notes);
            
            if (success) {
                // Update available copies
                Book book = bookDAO.getBookById(record.getBookId());
                if (book != null) {
                    bookDAO.updateAvailableCopies(record.getBookId(), book.getAvailableCopies() + 1);
                }
                
                response.sendRedirect(request.getContextPath() + "/borrow?message=return_success");
            } else {
                response.sendRedirect(request.getContextPath() + "/borrow?message=return_error");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/borrow");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/borrow?message=return_error");
        }
    }
    
    private void viewBorrowRecord(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/borrow");
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            BorrowRecord record = borrowRecordDAO.getBorrowRecordById(id);
            
            if (record != null) {
                request.setAttribute("record", record);
                request.getRequestDispatcher("/borrow/view.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/borrow");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/borrow");
        }
    }
}
