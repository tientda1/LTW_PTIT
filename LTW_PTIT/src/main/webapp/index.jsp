<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang chủ - Hệ thống Quản lý Thư viện</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .hero-section {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 4rem 0;
        }
        .feature-card {
            transition: transform 0.3s ease;
            border: none;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
        }
        .feature-card:hover {
            transform: translateY(-5px);
        }
        .stats-card {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 15px;
        }
        .quick-action-btn {
            border-radius: 50px;
            padding: 12px 24px;
            font-weight: 600;
            transition: all 0.3s ease;
        }
        .quick-action-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
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
                        <a class="nav-link active" href="${pageContext.request.contextPath}/">
                            <i class="fas fa-home"></i> Trang chủ
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/books">
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

    <!-- Hero Section -->
    <section class="hero-section">
        <div class="container text-center">
            <h1 class="display-4 fw-bold mb-4">
                <i class="fas fa-book-open"></i> Hệ thống Quản lý Thư viện PTIT
            </h1>
            <p class="lead mb-4">Quản lý sách, mượn trả và thành viên một cách hiệu quả</p>
            
            <c:choose>
                <c:when test="${sessionScope.user != null}">
                    <div class="mt-4">
                        <h4>Chào mừng, ${sessionScope.user.fullName}!</h4>
                        <p class="mb-3">Vai trò: 
                            <span class="badge bg-light text-dark">
                                <c:choose>
                                    <c:when test="${sessionScope.user.role == 'ADMIN'}">Quản trị viên</c:when>
                                    <c:when test="${sessionScope.user.role == 'LIBRARIAN'}">Thủ thư</c:when>
                                    <c:otherwise>Thành viên</c:otherwise>
                                </c:choose>
                            </span>
                        </p>
                    </div>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login" class="btn btn-light btn-lg quick-action-btn">
                        <i class="fas fa-sign-in-alt"></i> Đăng nhập để sử dụng
                    </a>
                </c:otherwise>
            </c:choose>
        </div>
    </section>

    <!-- Quick Actions -->
    <c:if test="${sessionScope.user != null}">
        <section class="py-5">
            <div class="container">
                <h2 class="text-center mb-5">Thao tác nhanh</h2>
                <div class="row g-4">
                    <div class="col-md-3">
                        <div class="card feature-card h-100 text-center">
                            <div class="card-body">
                                <i class="fas fa-search fa-3x text-primary mb-3"></i>
                                <h5 class="card-title">Tìm kiếm sách</h5>
                                <p class="card-text">Tìm kiếm sách theo tên, tác giả hoặc ISBN</p>
                                <a href="${pageContext.request.contextPath}/books" class="btn btn-primary quick-action-btn">
                                    <i class="fas fa-search"></i> Tìm kiếm
                                </a>
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-md-3">
                        <div class="card feature-card h-100 text-center">
                            <div class="card-body">
                                <i class="fas fa-plus fa-3x text-success mb-3"></i>
                                <h5 class="card-title">Mượn sách</h5>
                                <p class="card-text">Tạo phiếu mượn sách mới</p>
                                <a href="${pageContext.request.contextPath}/borrow?action=add" class="btn btn-success quick-action-btn">
                                    <i class="fas fa-plus"></i> Mượn sách
                                </a>
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-md-3">
                        <div class="card feature-card h-100 text-center">
                            <div class="card-body">
                                <i class="fas fa-undo fa-3x text-warning mb-3"></i>
                                <h5 class="card-title">Trả sách</h5>
                                <p class="card-text">Xử lý trả sách và tính phí</p>
                                <a href="${pageContext.request.contextPath}/borrow" class="btn btn-warning quick-action-btn">
                                    <i class="fas fa-undo"></i> Trả sách
                                </a>
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-md-3">
                        <div class="card feature-card h-100 text-center">
                            <div class="card-body">
                                <i class="fas fa-list fa-3x text-info mb-3"></i>
                                <h5 class="card-title">Sách đã mượn</h5>
                                <p class="card-text">Xem danh sách sách đã mượn</p>
                                <a href="${pageContext.request.contextPath}/borrow?action=my_borrows" class="btn btn-info quick-action-btn">
                                    <i class="fas fa-list"></i> Xem danh sách
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </c:if>

    <!-- Features Section -->
    <section class="py-5 bg-light">
        <div class="container">
            <h2 class="text-center mb-5">Tính năng hệ thống</h2>
            <div class="row g-4">
                <div class="col-md-4">
                    <div class="card feature-card h-100">
                        <div class="card-body text-center">
                            <i class="fas fa-book fa-3x text-primary mb-3"></i>
                            <h5 class="card-title">Quản lý sách</h5>
                            <p class="card-text">Thêm, sửa, xóa và tìm kiếm sách trong thư viện</p>
                        </div>
                    </div>
                </div>
                
                <div class="col-md-4">
                    <div class="card feature-card h-100">
                        <div class="card-body text-center">
                            <i class="fas fa-users fa-3x text-success mb-3"></i>
                            <h5 class="card-title">Quản lý thành viên</h5>
                            <p class="card-text">Quản lý thông tin người dùng và phân quyền</p>
                        </div>
                    </div>
                </div>
                
                <div class="col-md-4">
                    <div class="card feature-card h-100">
                        <div class="card-body text-center">
                            <i class="fas fa-exchange-alt fa-3x text-warning mb-3"></i>
                            <h5 class="card-title">Mượn/Trả sách</h5>
                            <p class="card-text">Xử lý phiếu mượn và trả sách tự động</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Footer -->
    <footer class="bg-dark text-light py-4">
        <div class="container text-center">
            <p>&copy; 2024 Hệ thống Quản lý Thư viện PTIT. Tất cả quyền được bảo lưu.</p>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
