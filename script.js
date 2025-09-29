// Data Management
let books = [
    { 
        id: 1, 
        title: "Java Programming", 
        author: "Oracle Corporation", 
        isbn: "9787134685991", 
        publisher: "Oracle Press", 
        year: 2020, 
        quantity: 5, 
        borrowed: 0, 
        description: "Tài liệu về lập trình Java" 
    },
    { 
        id: 2, 
        title: "Clean Code", 
        author: "Robert C. Martin", 
        isbn: "9787132350884", 
        publisher: "Prentice Hall", 
        year: 2008, 
        quantity: 3, 
        borrowed: 0, 
        description: "Hướng dẫn viết code sạch" 
    },
    { 
        id: 3, 
        title: "Design Patterns", 
        author: "Gang of Four", 
        isbn: "9787201633610", 
        publisher: "Addison-Wesley", 
        year: 1994, 
        quantity: 2, 
        borrowed: 0, 
        description: "Các mẫu thiết kế phần mềm" 
    }
];

let borrows = [];

// Initialize app when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    loadDataFromStorage();
    updateBooksTable();
    updateBorrowsTable();
    updateStats();
});

// Data Persistence Functions
function saveDataToStorage() {
    localStorage.setItem('libraryBooks', JSON.stringify(books));
    localStorage.setItem('libraryBorrows', JSON.stringify(borrows));
}

function loadDataFromStorage() {
    try {
        const savedBooks = localStorage.getItem('libraryBooks');
        if (savedBooks) {
            books = JSON.parse(savedBooks);
        }
        
        const savedBorrows = localStorage.getItem('libraryBorrows');
        if (savedBorrows) {
            borrows = JSON.parse(savedBorrows);
        }
    } catch (error) {
        console.error('Error loading data from storage:', error);
    }
}

// Tab Management
function showTab(tab, action = null) {
    // Hide all tabs
    document.querySelectorAll('[id$="-tab"]').forEach(t => t.classList.add('d-none'));
    
    // Show selected tab
    const selectedTab = document.getElementById(`${tab}-tab`);
    if (selectedTab) {
        selectedTab.classList.remove('d-none');
    }
    
    // Execute action if specified
    if (action === 'add') {
        if (tab === 'books') showAddBookForm();
        else if (tab === 'borrows') showAddBorrowForm();
    }
}

// Book Management Functions
function showAddBookForm() {
    const form = document.getElementById("add-book-form");
    if (form) {
        form.classList.remove('d-none');
        // Reset form
        form.querySelectorAll("input, textarea").forEach(input => {
            input.value = "";
            input.classList.remove('error');
        });
        
        // Reset submit button text
        const submitBtn = form.querySelector("button[type='submit']");
        if (submitBtn) {
            submitBtn.innerHTML = "💾 Lưu sách";
            form.onsubmit = addBook;
        }
    }
}

function hideAddBookForm() {
    const form = document.getElementById("add-book-form");
    if (form) {
        form.classList.add('d-none');
    }
}

function addBook(event) {
    event.preventDefault();
    
    try {
        const newBook = {
            id: Date.now(),
            title: document.getElementById("book-title").value.trim(),
            author: document.getElementById("book-author").value.trim(),
            isbn: document.getElementById("book-isbn").value.trim(),
            publisher: document.getElementById("book-publisher").value.trim(),
            year: parseInt(document.getElementById("book-year").value) || null,
            quantity: parseInt(document.getElementById("book-quantity").value),
            borrowed: 0,
            description: document.getElementById("book-description").value.trim()
        };
        
        // Validate required fields
        if (!newBook.title || !newBook.author || !newBook.quantity) {
            alert("Vui lòng điền đầy đủ thông tin bắt buộc!");
            return;
        }
        
        books.push(newBook);
        saveDataToStorage();
        updateBooksTable();
        updateBorrowsTable();
        updateStats();
        hideAddBookForm();
        
        alert("✅ Đã thêm sách thành công!");
    } catch (error) {
        console.error('Error adding booking:', error);
        alert("❌ Có lỗi xảy ra khi thêm sách!");
    }
}

function editBook(id) {
    const book = books.find(b => b.id == id);
    if (book) {
        // Fill form with book data
        document.getElementById("book-title").value = book.title;
        document.getElementById("book-author").value = book.author;
        document.getElementById("book-isbn").value = book.isbn || "";
        document.getElementById("book-publisher").value = book.publisher || "";
        document.getElementById("book-year").value = book.year || "";
        document.getElementById("book-quantity").value = book.quantity;
        document.getElementById("book-description").value = book.description || "";
        
        // Change submit button to update mode
        const submitBtn = document.querySelector("#add-book-form button[type='submit']");
        if (submitBtn) {
            submitBtn.innerHTML = "💾 Cập nhật sách";
            document.querySelector("#add-book-form form").onsubmit = function(e) { 
                updateBook(e, id); 
            };
        }
        
        showAddBookForm();
    }
}

function updateBook(event, id) {
    event.preventDefault();
    
    try {
        const bookIndex = books.findIndex(b => b.id == id);
        if (bookIndex !== -1) {
            books[bookIndex].title = document.getElementById("book-title").value.trim();
            books[bookIndex].author = document.getElementById("book-author").value.trim();
            books[bookIndex].isbn = document.getElementById("book-isbn").value.trim();
            books[bookIndex].publisher = document.getElementById("book-publisher").value.trim();
            books[bookIndex].year = parseInt(document.getElementById("book-year").value) || null;
            books[bookIndex].quantity = parseInt(document.getElementById("book-quantity").value);
            books[bookIndex].description = document.getElementById("book-description").value.trim();
            
            saveDataToStorage();
            updateBooksTable();
            updateBorrowsTable();
            updateStats();
            hideAddBookForm();
            
            alert("✅ Đã cập nhật sách thành công!");
        }
    } catch (error) {
        console.error('Error updating book:', error);
        alert("❌ Có lỗi xảy ra khi cập nhật sách!");
    }
}

function deleteBook(id) {
    if (confirm("⚠️ Bạn có chắc chắn muốn xóa sách này không?\n\nThao tác này sẽ xóa tất cả phiếu mượn liên quan!")) {
        try {
            books = books.filter(b => b.id != id);
            borrows = borrows.filter(b => b.bookId != id);
            
            saveDataToStorage();
            updateBooksTable();
            updateBorrowsTable();
            updateStats();
            
            alert("✅ Đã xóa sách thành công!");
        } catch (error) {
            console.error('Error deleting book:', error);
            alert("❌ Có lỗi xảy ra khi xóa sách!");
        }
    }
}

function updateBooksTable() {
    const table = document.getElementById("books-table");
    if (!table) return;
    
    table.innerHTML = "";

    if (books.length === 0) {
        const alert = document.getElementById("no-books-menu");
        if (alert) alert.classList.remove('d-none');
        return;
    }

    const alert = document.getElementById("no-books-menu");
    if (alert) alert.classList.add('d-none');
    
    table.innerHTML = `
        <thead>
            <tr>
                <th>Tiêu đề</th>
                <th>Tác giả</th>
                <th>ISBN</th>
                <th>Nhà xuất bản</th>
                <th>Tình trạng</th>
                <th>Còn lại</th>
                <th>Thao tác</th>
            </tr>
        </thead>
        <tbody></tbody>
    `;

    const tbody = table.querySelector("tbody");
    books.forEach(book => {
        const borrowedCount = borrows.filter(b => b.bookId == book.id && b.status === "Đang mượn").length;
        const available = book.quantity - borrowedCount;
        const status = available > 0 ? "Có sẵn" : "Hết sách";
        const statusClass = available > 0 ? "success" : "danger";
        
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${book.title}</td>
            <td>${book.author}</td>
            <td>${book.isbn || 'N/A'}</td>
            <td>${book.publisher || 'N/A'}</td>
            <td><span class="badge badge-${statusClass}">${status}</span></td>
            <td>${available}</td>
            <td>
                <button class="btn btn-primary me-1" onclick="editBook(${book.id})" style="padding: 0.25rem 0.5rem;">✏️</button>
                <button class="btn btn-danger" onclick="deleteBook(${book.id})" style="padding: 0.25rem 0.5rem;">🗑️</button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

function filterBooks(searchInput = null) {
    try {
        let searchFilter = "";
        let statusFilter = "";
        
        if (searchInput) {
            searchFilter = searchInput.value.toLowerCase().trim();
        } else {
            const searchElement = document.getElementById("search-books");
            if (searchElement) {
                searchFilter = searchElement.value.toLowerCase().trim();
            }
        }
        
        const statusFilterElement = document.getElementById("book-status-filter");
        if (statusFilterElement) {
            statusFilter = statusFilterElement.value;
        }
        
        const rows = document.querySelectorAll("#books-table tbody tr");
        
        rows.forEach(row => {
            const text = row.textContent.toLowerCase();
            const availableCell = row.cells[5];
            const availableText = availableCell ? availableCell.textContent : "";
            
            const matchesSearch = !searchFilter || text.includes(searchFilter);
            let matchesStatus = true;
            
            if (statusFilter === "available") {
                matchesStatus = !availableText.includes("0");
            } else if (statusFilter === "unavailable") {
                matchesStatus = availableText.includes("0");
            }
            
            row.style.display = (matchesSearch && matchesStatus) ? "" : "none";
        });
    } catch (error) {
        console.error('Error filtering books:', error);
    }
}

// Borrow Management Functions
function showAddBorrowForm() {
    const form = document.getElementById("add-borrow-form");
    if (form) {
        form.classList.remove('d-none');
        document.getElementById("borrow-user").value = "";
        document.getElementById("borrow-date").value = "";  
        document.getElementById("borrow-due").value = "";
        updateBookDropdown();
    }
}

function hideAddBorrowForm() {
    const form = document.getElementById("add-borrow-form");
    if (form) {
        form.classList.add('d-none');
    }
}

function updateBookDropdown() {
    const dropdown = document.getElementById("borrow-book");
    if (!dropdown) return;
    
    dropdown.innerHTML = "";
    
    books.forEach(book => {
        const borrowedCount = borrows.filter(b => b.bookId == book.id && b.status === "Đang mượn").length;
        const available = book.quantity - borrowedCount;
        if (available > 0) {
            dropdown.innerHTML += `<option value="${book.id}">${book.title} - ${available} còn lại</option>`;
        }
    });
}

function addBorrow(event) {
    event.preventDefault();
    
    try {
        const bookId = parseInt(document.getElementById("borrow-book").value);
        const userName = document.getElementById("borrow-user").value.trim();
        const borrowDate = document.getElementById("borrow-date").value;
        const dueDate = document.getElementById("borrow-due").value;
        
        if (!userName || !borrowDate || !dueDate) {
            alert("Vui lòng điền đầy đủ thông tin!");
            return;
        }
        
        if (new Date(dueDate) <= new Date(borrowDate)) {
            alert("Ngày hẹn trả phải sau ngày mượn!");
            return;
        }
        
        const book = books.find(b => b.id == bookId);
        if (!book) {
            alert("Không tìm thấy sách được chọn!");
            return;
        }
        
        const newBorrow = {
            id: Date.now(),
            userName: userName,
            bookId: bookId,
            bookTitle: book.title,
            borrowDate: borrowDate,
            dueDate: dueDate,
            returnDate: null,
            status: "Đang mượn"
        };
        
        borrows.push(newBorrow);
        
        saveDataToStorage();
        updateBorrowsTable();
        updateBooksTable();
        updateStats();
        hideAddBorrowForm();
        
        alert("✅ Đã tạo phiếu mượn thành công!");
    } catch (error) {
        console.error('Error adding borrow:', error);
        alert("❌ Có lỗi xảy ra khi tạo phiếu mượn!");
    }
}

function returnBook(id) {
    if (confirm("Xác nhận trả sách này?")) {
        try {
            const borrow = borrows.find(b => b.id == id);
            if (borrow) {
                borrow.status = "Đã trả";
                borrow.returnDate = new Date().toISOString().split('T')[0];
                
                saveDataToStorage();
                updateBorrowsTable();
                updateBooksTable();
                updateStats();
                
                alert("✅ Đã cập nhật trạng thái trả sách!");
            }
        } catch (error) {
            console.error('Error returning book:', error);
            alert("❌ Có lỗi xảy ra khi trả sách!");
        }
    }
}

function updateBorrowsTable() {
    const table = document.getElementById("borrows-table");
    if (!table) return;
    
    table.innerHTML = "";

    if (borrows.length === 0) {
        const alert = document.getElementById("no-borrows-menu");
        if (alert) alert.classList.remove('d-none');
        return;
    }

    const alert = document.getElementById("no-borrows-menu");
    if (alert) alert.classList.add('d-none');
    
    table.innerHTML = `
        <thead>
            <tr>
                <th>Người mượn</th>
                <th>Sách</th>
                <th>Ngày mượn</th>
                <th>Ngày hẹn</th>
                <th>Trạng thái</th>
                <th>Thao tác</th>
            </tr>
        </thead>
        <tbody></tbody>
    `;

    const tbody = table.querySelector("tbody");
    borrows.forEach(borrow => {
        const canReturn = borrow.status === "Đang mượn";
        const isOverdue = canReturn && new Date(borrow.dueDate) < new Date();
        
        const row = document.createElement('tr');
        if (isOverdue) {
            row.style.backgroundColor = '#fff3cd';
        }
        
        row.innerHTML = `
            <td>${borrow.userName}</td>
            <td>${borrow.bookTitle}</td>
            <td>${borrow.borrowDate}</td>
            <td>${borrow.dueDate}</td>
            <td>
                ${borrow.status}
                ${isOverdue ? ' <span class="badge badge-danger">Quá hạn</span>' : ''}
            </td>
            <td>
                ${canReturn ? `<button class="btn btn-success" onclick="returnBook(${borrow.id})">✅ Trả sách</button>` : ''}
            </td>
        `;
        tbody.appendChild(row);
    });
}

// Statistics Update
function updateStats() {
    try {
        const totalBooksElement = document.getElementById("total-books");
        const totalUsersElement = document.getElementById("total-users");
        
        if (totalBooksElement) {
            totalBooksElement.textContent = books.reduce((sum, book) => sum + book.quantity, 0);
        }
        
        if (totalUsersElement) {
            const uniqueUsers = borrows.length > 0 ? [...new Set(borrows.map(b => b.userName))].length : 0;
            totalUsersElement.textContent = uniqueUsers;
        }
    } catch (error) {
        console.error('Error updating stats:', error);
    }
}

// Data Export/Import Functions
function exportData() {
    try {
        const data = {
            books: books,
            borrows: borrows,
            exportDate: new Date().toISOString(),
            version: "1.0",
            totalBooks: books.reduce((sum, book) => sum + book.quantity, 0),
            totalBorrows: borrows.length,
            activeBorrows: borrows.filter(b => b.status === "Đang mượn").length
        };
        
        const dataStr = JSON.stringify(data, null, 2);
        const dataBlob = new Blob([dataStr], {type: 'application/json'});
        
        const link = document.createElement('a');
        link.href = URL.createObjectURL(dataBlob);
        link.download = `thu-vien-backup-${new Date().toISOString().split('T')[0]}.json`;
        link.click();
        
        alert("✅ Đã xuất dữ liệu thành công!");
    } catch (error) {
        console.error('Error exporting data:', error);
        alert("❌ Có lỗi xảy ra khi xuất dữ liệu!");
    }
}

function importData() {
    try {
        const input = document.createElement('input');
        input.type = 'file';
        input.accept = '.json';
        
        input.onchange = function(e) {
            const file = e.target.files[0];
            if (!file) return;
            
            const reader = new FileReader();
            reader.onload = function(e) {
                try {
                    const data = JSON.parse(e.target.result);
                    if (data.books && data.borrows) {
                        if (confirm(`⚠️ Nhập dữ liệu này sẽ thay thế tất cả dữ liệu hiện tại.\n\nDữ liệu backup:\n- ${data.books.length} sách\n- ${data.borrows.length} phiếu mượn\n\nTiếp tục?`)) {
                            books = data.books;
                            borrows = data.borrows;
                            saveDataToStorage();
                            updateBooksTable();
                            updateBorrowsTable();
                            updateStats();
                            alert("✅ Đã nhập dữ liệu thành công!");
                        }
                    } else {
                        alert("❌ File không đúng định dạng!");
                    }
                } catch (error) {
                    console.error('Error parsing imported data:', error);
                    alert("❌ File không hợp lệ!");
                }
            };
            reader.readAsText(file);
        };
        
        input.click();
    } catch (error) {
        console.error('Error importing data:', error);
        alert("❌ Có lỗi xảy ra khi nhập dữ liệu!");
    }
}

// Utility Functions
function formatDate(dateString) {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleDateString('vi-VN');
}

function showNotification(message, type = 'success') {
    // Simple notification system
    const alertClass = type === 'success' ? 'alert-success' : 'alert-danger';
    console.log(`[${type.toUpperCase()}] ${message}`);
}

// Keyboard shortcuts
document.addEventListener('keydown', function(e) {
    // Ctrl+S to save data (export)
    if (e.ctrlKey && e.key === 's') {
        e.preventDefault();
        exportData();
    }
});

// Auto-save functionality
setInterval(function() {
    saveDataToStorage();
}, 30000); // Auto-save every 30 seconds

console.log('📚 Thư viện PTIT đã sẵn sàng!');
