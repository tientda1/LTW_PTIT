-- Database schema for Library Management System
-- Tạo database
CREATE DATABASE IF NOT EXISTS library_management;
USE library_management;

-- Bảng Users (Người dùng)
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    role ENUM('ADMIN', 'LIBRARIAN', 'MEMBER') DEFAULT 'MEMBER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng Categories (Thể loại sách)
CREATE TABLE categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng Books (Sách)
CREATE TABLE books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(20) UNIQUE,
    publisher VARCHAR(100),
    publication_year INT,
    category_id INT,
    total_copies INT DEFAULT 1,
    available_copies INT DEFAULT 1,
    description TEXT,
    image_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
);

-- Bảng Borrow Records (Phiếu mượn)
CREATE TABLE borrow_records (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    book_id INT NOT NULL,
    borrow_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE NULL,
    status ENUM('BORROWED', 'RETURNED', 'OVERDUE') DEFAULT 'BORROWED',
    fine_amount DECIMAL(10,2) DEFAULT 0.00,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);

-- Insert dữ liệu mẫu
-- Thêm categories
INSERT INTO categories (name, description) VALUES
('Khoa học', 'Sách về khoa học và công nghệ'),
('Văn học', 'Tiểu thuyết, truyện ngắn, thơ ca'),
('Lịch sử', 'Sách về lịch sử và địa lý'),
('Kinh tế', 'Sách về kinh tế và kinh doanh'),
('Tin học', 'Sách về công nghệ thông tin');

-- Thêm users
INSERT INTO users (username, password, full_name, email, phone, role) VALUES
('admin', 'admin123', 'Quản trị viên', 'admin@library.com', '0123456789', 'ADMIN'),
('librarian1', 'lib123', 'Thủ thư Nguyễn Văn A', 'librarian1@library.com', '0987654321', 'LIBRARIAN'),
('member1', 'member123', 'Thành viên Trần Thị B', 'member1@library.com', '0369258147', 'MEMBER');

-- Thêm books
INSERT INTO books (title, author, isbn, publisher, publication_year, category_id, total_copies, available_copies, description) VALUES
('Java Programming', 'Oracle Corporation', '978-0123456789', 'Oracle Press', 2023, 5, 3, 3, 'Hướng dẫn lập trình Java từ cơ bản đến nâng cao'),
('Lịch sử Việt Nam', 'Nguyễn Văn C', '978-1234567890', 'NXB Giáo dục', 2022, 3, 2, 2, 'Lịch sử Việt Nam từ thời cổ đại đến hiện đại'),
('Kinh tế học', 'Adam Smith', '978-2345678901', 'NXB Kinh tế', 2021, 4, 1, 1, 'Nguyên lý kinh tế học cơ bản'),
('Truyện Kiều', 'Nguyễn Du', '978-3456789012', 'NXB Văn học', 2020, 2, 5, 5, 'Tác phẩm kinh điển của văn học Việt Nam'),
('Vật lý đại cương', 'Albert Einstein', '978-4567890123', 'NXB Khoa học', 2023, 1, 2, 2, 'Các nguyên lý cơ bản của vật lý học');
