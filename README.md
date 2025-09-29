# Hệ thống quản lý thư viện đơn giản

## Mô tả
Đây là một ứng dụng web quản lý thư viện đơn giản được phát triển hoàn toàn bằng HTML, CSS và JavaScript thuần túy. Không cần server backend hoặc cơ sở dữ liệu phức tạp.

## Công nghệ sử dụng
- **HTML5**: Cấu trúc trang web
- **CSS3**: Thiết kế giao diện 
- **JavaScript**: Logic xử lý các chức năng
- **Bootstrap 5**: Framework CSS để thiết kế responsive
- **Font Awesome**: Icon đẹp mắt
- **localStorage**: Lưu trữ dữ liệu trực tiếp trên trình duyệt

## Tính năng chính

### 1. Quản lý sách (CRUD)
- ✅ **Thêm sách mới**: Nhập thông tin chi tiết về sách
- ✅ **Xem danh sách sách**: Hiển thị tất cả sách trong hệ thống
- ✅ **Sửa thông tin sách**: Cập nhật thông tin sách đã có
- ✅ **Xóa sách**: Loại bỏ sách khỏi hệ thống
- ✅ **Tìm kiếm sách**: Tìm theo tiêu đề, tác giả, ISBN, nhà xuất bản
- ✅ **Lọc theo trạng thái**: Còn sẵn hoặc hết sách

### 2. Quản lý mượn trả
- ✅ **Tạo phiếu mượn**: Tạo phiếu mượn sách cho thành viên
- ✅ **Xem lịch sử mượn**: Danh sách tất cả phiếu mượn
- ✅ **Trả sách**: Cập nhật trạng thái đã trả sách
- ✅ **Theo dõi quá hạn**: Phát hiện tự động sách quá hạn

### 3. Báo cáo thống kê
- ✅ **Dashboard tổng quan**: Thống kê nhanh các số liệu quan trọng
- ✅ **Thống kê real-time**: Cập nhật số liệu theo thời gian thực
- ✅ **Biểu đồ trực quan**: Hiển thị thống kê bằng biểu đồ

### 4. Quản lý dữ liệu
- ✅ **Lưu trữ tự động**: Dữ liệu được lưu vào localStorage
- ✅ **Xuất dữ liệu**: Tải dữ liệu về dưới dạng file JSON
- ✅ **Nhập dữ liệu**: Khôi phục dữ liệu từ file JSON

## Hướng dẫn sử dụng

### Cách chạy ứng dụng
1. **Tải về**: Clone hoặc tải project về máy
2. **Mở file**: Double-click vào file `src/main/webapp/index.html`
3. **Hoặc**: Mở trình duyệt và kéo file `index.html` vào

### Sử dụng ứng dụng
1. **Trang chủ**: Xem tổng quan và điều hướng nhanh
2. **Quản lý sách**: 
   - Click "Thêm sách mới" để thêm sách
   - Click icon ✏️ để sửa thông tin sách
   - Click icon 🗑️ để xóa sách
   - Sử dụng thanh tìm kiếm để lọc sách
3. **Quản lý mượn trả**:
   - Click "Tạo phiếu mượn mới" để mượn sách
   - Chọn sách từ danh sách còn sẵn
   - Click "Trả sách" để hoàn tất trả sách

## Cấu trúc file
```
src/main/webapp/
├── index.html          # Trang chính của ứng dụng
├── css/
│   └── style.css       # CSS tùy chỉnh
└── images/             # Thư mục hình ảnh (tùy chọn)
```

## Dữ liệu mẫu
Ứng dụng có sẵn dữ liệu mẫu gồm:
- **3 cuốn sách**: Java Programming, Clean Code, Design Patterns
- **Thông tin chi tiết**: ISBN, nhà xuất bản, năm xuất bản, mô tả

## Lưu trữ dữ liệu
- Dữ liệu được lưu trực tiếp vào localStorage của trình duyệt
- Dữ liệu sẽ được giữ lại khi tải lại trang
- Có thể xuất/nhập dữ liệu để sao lưu hoặc chia sẻ

## Responsive Design
- ✅ Tương thích với máy tính để bàn
- ✅ Tương thích với máy tính bảng  
- ✅ Tương thích với điện thoại thông minh
- ✅ Giao diện tự động điều chỉnh theo kích thước màn hình

## Browser Support
- ✅ Chrome (khuyến nghị)
- ✅ Firefox
- ✅ Safari
- ✅ Edge

## Tương lai có thể mở rộng
- Thêm tính năng đăng nhập người dùng
- Ghép với cơ sở dữ liệu thật
- Thêm API REST
- Tích hợp QR Code cho sách
- Thêm tính năng phạt quá hạn tự động
- Thêm bình luận và đánh giá sách

## Lưu ý quan trọng
- Đây là ứng dụng frontend đơn giản, không cần server
- Dữ liệu chỉ tồn tại trên trình duyệt hiện tại
- Để chia sẻ dữ liệu giữa các thiết bị, sử dụng tính năng xuất/nhập file
- Phù hợp cho bài tập học tập hoặc demo nhỏ

## Nhóm phát triển
Được phát triển bởi nhóm 5 người như yêu cầu bài tập.

---
**Chúc bạn sử dụng ứng dụng thành công! 📚**