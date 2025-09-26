# Hệ thống Quản lý Thư viện PTIT

## Mô tả dự án

Đây là một ứng dụng web quản lý thư viện được xây dựng bằng Java EE với các công nghệ:
- **Backend**: JSP + Servlet + JDBC + MySQL
- **Frontend**: HTML5 + CSS3 + JavaScript + Bootstrap 5
- **Database**: MySQL

## Tính năng chính

### 1. Quản lý Sách
- ✅ Thêm, sửa, xóa sách
- ✅ Tìm kiếm sách theo tên, tác giả, ISBN
- ✅ Phân loại sách theo thể loại
- ✅ Quản lý số lượng sách có sẵn

### 2. Quản lý Người dùng
- ✅ Đăng ký, đăng nhập hệ thống
- ✅ Phân quyền: Admin, Thủ thư, Thành viên
- ✅ Quản lý thông tin người dùng

### 3. Quản lý Mượn/Trả sách
- ✅ Tạo phiếu mượn sách
- ✅ Theo dõi trạng thái mượn/trả
- ✅ Tính phí phạt khi trả muộn
- ✅ Quản lý hạn trả sách

### 4. Quản lý Thể loại
- ✅ Thêm, sửa, xóa thể loại sách
- ✅ Phân loại sách theo thể loại

## Cấu trúc dự án

```
LTW_PTIT/
├── src/main/java/com/mycompany/ltw_ptit/
│   ├── model/                 # Các model classes
│   │   ├── User.java
│   │   ├── Book.java
│   │   ├── Category.java
│   │   └── BorrowRecord.java
│   ├── dao/                   # Data Access Objects
│   │   ├── DatabaseConnection.java
│   │   ├── UserDAO.java
│   │   ├── BookDAO.java
│   │   ├── CategoryDAO.java
│   │   └── BorrowRecordDAO.java
│   └── servlet/               # Servlets xử lý request
│       ├── LoginServlet.java
│       ├── LogoutServlet.java
│       ├── BookServlet.java
│       ├── UserServlet.java
│       ├── BorrowServlet.java
│       └── CategoryServlet.java
├── src/main/webapp/
│   ├── css/
│   │   └── style.css          # Custom CSS
│   ├── js/
│   │   └── script.js         # Custom JavaScript
│   ├── books/                # JSP pages cho quản lý sách
│   │   ├── list.jsp
│   │   └── add.jsp
│   ├── borrow/                # JSP pages cho mượn/trả
│   │   └── list.jsp
│   ├── login.jsp             # Trang đăng nhập
│   ├── index.jsp             # Trang chủ
│   └── WEB-INF/
│       ├── web.xml           # Cấu hình servlet
│       └── layout/
│           └── layout.jsp    # Layout chung
├── database/
│   └── library_schema.sql    # Script tạo database
└── pom.xml                   # Maven configuration
```

## Cài đặt và Chạy dự án

### 1. Yêu cầu hệ thống
- Java 8 hoặc cao hơn
- Apache Tomcat 9.0+
- MySQL 8.0+
- Maven 3.6+

### 2. Cài đặt Database
1. Tạo database MySQL:
```sql
CREATE DATABASE library_management;
```

2. Chạy script SQL:
```bash
mysql -u root -p library_management < database/library_schema.sql
```

3. Cập nhật thông tin kết nối database trong `DatabaseConnection.java`:
```java
private static final String URL = "jdbc:mysql://localhost:3306/library_management?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8";
private static final String USERNAME = "root";
private static final String PASSWORD = "your_password"; // Thay đổi password
```

### 3. Build và Deploy
1. Build project:
```bash
mvn clean package
```

2. Deploy file WAR vào Tomcat:
```bash
cp target/LTW_PTIT-1.0-SNAPSHOT.war $TOMCAT_HOME/webapps/
```

3. Khởi động Tomcat và truy cập:
```
http://localhost:8080/LTW_PTIT-1.0-SNAPSHOT/
```

## Tài khoản Demo

| Vai trò | Username | Password | Mô tả |
|---------|----------|----------|-------|
| Admin | admin | admin123 | Quản trị viên hệ thống |
| Thủ thư | librarian1 | lib123 | Quản lý sách và phiếu mượn |
| Thành viên | member1 | member123 | Mượn sách và xem thông tin |

## Hướng dẫn sử dụng

### 1. Đăng nhập hệ thống
- Truy cập trang chủ và click "Đăng nhập"
- Sử dụng một trong các tài khoản demo ở trên

### 2. Quản lý sách (Admin/Thủ thư)
- Vào menu "Sách" để xem danh sách
- Click "Thêm sách mới" để thêm sách
- Sử dụng chức năng tìm kiếm để tìm sách
- Click "Sửa" hoặc "Xóa" để quản lý sách

### 3. Mượn sách
- Vào menu "Mượn/Trả" để xem phiếu mượn
- Click "Tạo phiếu mượn" để mượn sách mới
- Chọn người mượn và sách cần mượn
- Hệ thống tự động tính ngày hạn trả (14 ngày)

### 4. Trả sách
- Tìm phiếu mượn có trạng thái "Đang mượn"
- Click nút "Trả" để xử lý trả sách
- Nhập ngày trả và phí phạt (nếu có)
- Hệ thống tự động cập nhật số lượng sách có sẵn

## Tính năng Responsive

Ứng dụng được thiết kế responsive và hoạt động tốt trên:
- ✅ Desktop (1200px+)
- ✅ Tablet (768px - 1199px)
- ✅ Mobile (< 768px)

## Công nghệ sử dụng

### Backend
- **Java EE 8**: Servlet, JSP, JSTL
- **JDBC**: Kết nối và thao tác database
- **MySQL**: Cơ sở dữ liệu
- **Maven**: Quản lý dependencies

### Frontend
- **HTML5**: Cấu trúc trang web
- **CSS3**: Styling và responsive design
- **JavaScript**: Tương tác và validation
- **Bootstrap 5**: Framework CSS
- **Font Awesome**: Icons

## API Endpoints

| Method | URL | Mô tả |
|--------|-----|-------|
| GET | `/login` | Hiển thị trang đăng nhập |
| POST | `/login` | Xử lý đăng nhập |
| GET | `/logout` | Đăng xuất |
| GET | `/books` | Danh sách sách |
| GET | `/books?action=add` | Form thêm sách |
| POST | `/books?action=add` | Xử lý thêm sách |
| GET | `/books?action=edit&id={id}` | Form sửa sách |
| POST | `/books?action=edit` | Xử lý sửa sách |
| GET | `/books?action=delete&id={id}` | Xóa sách |
| GET | `/borrow` | Danh sách phiếu mượn |
| GET | `/borrow?action=add` | Form tạo phiếu mượn |
| POST | `/borrow?action=add` | Xử lý tạo phiếu mượn |
| GET | `/borrow?action=return&id={id}` | Form trả sách |
| POST | `/borrow?action=return` | Xử lý trả sách |

## Troubleshooting

### 1. Lỗi kết nối Database
- Kiểm tra MySQL đã chạy chưa
- Kiểm tra username/password trong `DatabaseConnection.java`
- Kiểm tra database `library_management` đã tạo chưa

### 2. Lỗi 404 - Page not found
- Kiểm tra URL mapping trong `web.xml`
- Kiểm tra servlet class đã compile chưa
- Restart Tomcat server

### 3. Lỗi encoding
- Đảm bảo database sử dụng UTF-8
- Kiểm tra connection string có `characterEncoding=UTF-8`

## Đóng góp

Để đóng góp vào dự án:
1. Fork repository
2. Tạo feature branch
3. Commit changes
4. Push to branch
5. Tạo Pull Request

## License

Dự án này được phát hành dưới MIT License.

## Liên hệ

Nếu có thắc mắc hoặc cần hỗ trợ, vui lòng liên hệ:
- Email: support@ptit.edu.vn
- Website: https://ptit.edu.vn
