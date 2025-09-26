<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh sách sách - Hệ thống Quản lý Thư viện</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .book-card {
            transition: transform 0.3s ease;
            border: none;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
        }
        .book-card:hover {
            transform: translateY(-5px);
        }
        .search-box {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 15px;
            padding: 2rem;
        }
        .btn-action {
            border-radius: 20px;
            padding: 8px 16px;
            font-size: 0.9rem;
        }
        .status-badge {
            font-size: 0.8rem;
            padding: 6px 12px;
        }
    </style>
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/">
                <i class="fas fa-book"></i> Thư viện PTIT
            </a>
            
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/">
                            <i class="fas fa-home"></i> Trang chủ
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="${pageContext.request.contextPath}/books">
                            <i class="fas fa-book"></i> Sách
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/borrow">
                            <i class="fas fa-exchange-alt"></i> Mượn/Trả
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/categories">
                            <i class="fas fa-tags"></i> Thể loại
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/users">
                            <i class="fas fa-users"></i> Người dùng
                        </a>
                    </li>
                </ul>
                
                <ul class="navbar-nav">
                    <c:choose>
                        <c:when test="${sessionScope.user != null}">
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown">
                                    <i class="fas fa-user"></i> ${sessionScope.user.fullName}
                                </a>
                                <ul class="dropdown-menu">
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/borrow?action=my_borrows">
                                        <i class="fas fa-list"></i> Sách đã mượn
                                    </a></li>
                                    <li><hr class="dropdown-divider"></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/logout">
                                        <i class="fas fa-sign-out-alt"></i> Đăng xuất
                                    </a></li>
                                </ul>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/login">
                                    <i class="fas fa-sign-in-alt"></i> Đăng nhập
                                </a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Main Content -->
    <div class="container-fluid py-4">
        <!-- Alert Messages -->
        <c:if test="${not empty message}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <c:choose>
                    <c:when test="${message == 'add_success'}">
                        <i class="fas fa-check-circle"></i> Thêm sách thành công!
                    </c:when>
                    <c:when test="${message == 'edit_success'}">
                        <i class="fas fa-check-circle"></i> Cập nhật sách thành công!
                    </c:when>
                    <c:when test="${message == 'delete_success'}">
                        <i class="fas fa-check-circle"></i> Xóa sách thành công!
                    </c:when>
                    <c:otherwise>
                        <i class="fas fa-info-circle"></i> ${message}
                    </c:otherwise>
                </c:choose>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <!-- Search Section -->
        <div class="search-box mb-4">
            <div class="row align-items-center">
                <div class="col-md-8">
                    <h3 class="mb-3">
                        <i class="fas fa-search"></i> Tìm kiếm sách
                    </h3>
                    <form action="${pageContext.request.contextPath}/books" method="get" class="d-flex">
                        <input type="hidden" name="action" value="search">
                        <input type="text" class="form-control me-2" name="keyword" 
                               placeholder="Nhập tên sách, tác giả hoặc ISBN..." 
                               value="${keyword}">
                        <button type="submit" class="btn btn-light">
                            <i class="fas fa-search"></i> Tìm kiếm
                        </button>
                    </form>
                </div>
                <div class="col-md-4 text-end">
                    <c:if test="${sessionScope.user != null && (sessionScope.user.role == 'ADMIN' || sessionScope.user.role == 'LIBRARIAN')}">
                        <a href="${pageContext.request.contextPath}/books?action=add" class="btn btn-light btn-lg">
                            <i class="fas fa-plus"></i> Thêm sách mới
                        </a>
                    </c:if>
                </div>
            </div>
        </div>

        <!-- Books Grid -->
        <div class="row g-4">
            <c:choose>
                <c:when test="${not empty books}">
                    <c:forEach var="book" items="${books}">
                        <div class="col-md-6 col-lg-4">
                            <div class="card book-card h-100">
                                <c:if test="${not empty book.imageUrl}">
                                    <img src="${book.imageUrl}" class="card-img-top" alt="${book.title}" style="height: 200px; object-fit: cover;">
                                </c:if>
                                <div class="card-body">
                                    <h5 class="card-title">${book.title}</h5>
                                    <p class="card-text">
                                        <strong>Tác giả:</strong> ${book.author}<br>
                                        <strong>Nhà xuất bản:</strong> ${book.publisher}<br>
                                        <strong>Năm xuất bản:</strong> ${book.publicationYear}<br>
                                        <strong>Thể loại:</strong> ${book.categoryName}<br>
                                        <strong>ISBN:</strong> ${book.isbn}
                                    </p>
                                    
                                    <div class="mb-3">
                                        <span class="badge bg-primary status-badge">
                                            <i class="fas fa-copy"></i> Tổng: ${book.totalCopies}
                                        </span>
                                        <span class="badge bg-success status-badge">
                                            <i class="fas fa-check-circle"></i> Có sẵn: ${book.availableCopies}
                                        </span>
                                    </div>
                                    
                                    <c:if test="${not empty book.description}">
                                        <p class="card-text">
                                            <small class="text-muted">${book.description}</small>
                                        </p>
                                    </c:if>
                                </div>
                                
                                <div class="card-footer bg-transparent">
                                    <div class="btn-group w-100" role="group">
                                        <a href="${pageContext.request.contextPath}/books?action=view&id=${book.id}" 
                                           class="btn btn-outline-primary btn-action">
                                            <i class="fas fa-eye"></i> Xem
                                        </a>
                                        
                                        <c:if test="${sessionScope.user != null && (sessionScope.user.role == 'ADMIN' || sessionScope.user.role == 'LIBRARIAN')}">
                                            <a href="${pageContext.request.contextPath}/books?action=edit&id=${book.id}" 
                                               class="btn btn-outline-warning btn-action">
                                                <i class="fas fa-edit"></i> Sửa
                                            </a>
                                            <a href="${pageContext.request.contextPath}/books?action=delete&id=${book.id}" 
                                               class="btn btn-outline-danger btn-action"
                                               onclick="return confirm('Bạn có chắc chắn muốn xóa sách này?')">
                                                <i class="fas fa-trash"></i> Xóa
                                            </a>
                                        </c:if>
                                        
                                        <c:if test="${sessionScope.user != null && book.availableCopies > 0}">
                                            <a href="${pageContext.request.contextPath}/borrow?action=add&bookId=${book.id}" 
                                               class="btn btn-outline-success btn-action">
                                                <i class="fas fa-plus"></i> Mượn
                                            </a>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="col-12">
                        <div class="text-center py-5">
                            <i class="fas fa-book fa-3x text-muted mb-3"></i>
                            <h4 class="text-muted">Không tìm thấy sách nào</h4>
                            <p class="text-muted">
                                <c:choose>
                                    <c:when test="${not empty keyword}">
                                        Không có sách nào phù hợp với từ khóa "${keyword}"
                                    </c:when>
                                    <c:otherwise>
                                        Chưa có sách nào trong thư viện
                                    </c:otherwise>
                                </c:choose>
                            </p>
                            <c:if test="${sessionScope.user != null && (sessionScope.user.role == 'ADMIN' || sessionScope.user.role == 'LIBRARIAN')}">
                                <a href="${pageContext.request.contextPath}/books?action=add" class="btn btn-primary">
                                    <i class="fas fa-plus"></i> Thêm sách đầu tiên
                                </a>
                            </c:if>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <!-- Footer -->
    <footer class="bg-dark text-light py-4 mt-5">
        <div class="container text-center">
            <p>&copy; 2024 Hệ thống Quản lý Thư viện PTIT. Tất cả quyền được bảo lưu.</p>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
