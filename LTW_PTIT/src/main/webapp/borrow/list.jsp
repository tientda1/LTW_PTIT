<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh sách phiếu mượn - Hệ thống Quản lý Thư viện</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
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
                        <a class="nav-link" href="${pageContext.request.contextPath}/books">
                            <i class="fas fa-book"></i> Sách
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="${pageContext.request.contextPath}/borrow">
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
                        <i class="fas fa-check-circle"></i> Tạo phiếu mượn thành công!
                    </c:when>
                    <c:when test="${message == 'return_success'}">
                        <i class="fas fa-check-circle"></i> Trả sách thành công!
                    </c:when>
                    <c:otherwise>
                        <i class="fas fa-info-circle"></i> ${message}
                    </c:otherwise>
                </c:choose>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <!-- Header Section -->
        <div class="search-box mb-4">
            <div class="row align-items-center">
                <div class="col-md-8">
                    <h3 class="mb-3">
                        <i class="fas fa-exchange-alt"></i> Quản lý Mượn/Trả sách
                    </h3>
                    <p class="mb-0">Theo dõi và quản lý các phiếu mượn sách trong thư viện</p>
                </div>
                <div class="col-md-4 text-end">
                    <c:if test="${sessionScope.user != null && (sessionScope.user.role == 'ADMIN' || sessionScope.user.role == 'LIBRARIAN')}">
                        <a href="${pageContext.request.contextPath}/borrow?action=add" class="btn btn-light btn-lg">
                            <i class="fas fa-plus"></i> Tạo phiếu mượn
                        </a>
                    </c:if>
                </div>
            </div>
        </div>

        <!-- Borrow Records Table -->
        <div class="card">
            <div class="card-header">
                <h5 class="mb-0">
                    <i class="fas fa-list"></i> Danh sách phiếu mượn
                </h5>
            </div>
            <div class="card-body">
                <c:choose>
                    <c:when test="${not empty records}">
                        <div class="table-responsive">
                            <table class="table table-hover">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Người mượn</th>
                                        <th>Tên sách</th>
                                        <th>Ngày mượn</th>
                                        <th>Hạn trả</th>
                                        <th>Ngày trả</th>
                                        <th>Trạng thái</th>
                                        <th>Phí phạt</th>
                                        <th>Thao tác</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="record" items="${records}">
                                        <tr>
                                            <td>${record.id}</td>
                                            <td>${record.userName}</td>
                                            <td>${record.bookTitle}</td>
                                            <td>
                                                <fmt:formatDate value="${record.borrowDate}" pattern="dd/MM/yyyy"/>
                                            </td>
                                            <td>
                                                <fmt:formatDate value="${record.dueDate}" pattern="dd/MM/yyyy"/>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty record.returnDate}">
                                                        <fmt:formatDate value="${record.returnDate}" pattern="dd/MM/yyyy"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-muted">Chưa trả</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${record.status == 'BORROWED'}">
                                                        <span class="badge bg-warning">Đang mượn</span>
                                                    </c:when>
                                                    <c:when test="${record.status == 'RETURNED'}">
                                                        <span class="badge bg-success">Đã trả</span>
                                                    </c:when>
                                                    <c:when test="${record.status == 'OVERDUE'}">
                                                        <span class="badge bg-danger">Quá hạn</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-secondary">${record.status}</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${record.fineAmount > 0}">
                                                        <span class="text-danger">
                                                            <fmt:formatNumber value="${record.fineAmount}" type="currency" currencyCode="VND"/>
                                                        </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-success">0 VNĐ</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <div class="btn-group" role="group">
                                                    <a href="${pageContext.request.contextPath}/borrow?action=view&id=${record.id}" 
                                                       class="btn btn-outline-primary btn-sm">
                                                        <i class="fas fa-eye"></i>
                                                    </a>
                                                    
                                                    <c:if test="${record.status == 'BORROWED' && sessionScope.user != null && (sessionScope.user.role == 'ADMIN' || sessionScope.user.role == 'LIBRARIAN')}">
                                                        <a href="${pageContext.request.contextPath}/borrow?action=return&id=${record.id}" 
                                                           class="btn btn-outline-success btn-sm">
                                                            <i class="fas fa-undo"></i>
                                                        </a>
                                                    </c:if>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="text-center py-5">
                            <i class="fas fa-exchange-alt fa-3x text-muted mb-3"></i>
                            <h4 class="text-muted">Chưa có phiếu mượn nào</h4>
                            <p class="text-muted">Chưa có phiếu mượn sách nào trong hệ thống</p>
                            <c:if test="${sessionScope.user != null && (sessionScope.user.role == 'ADMIN' || sessionScope.user.role == 'LIBRARIAN')}">
                                <a href="${pageContext.request.contextPath}/borrow?action=add" class="btn btn-primary">
                                    <i class="fas fa-plus"></i> Tạo phiếu mượn đầu tiên
                                </a>
                            </c:if>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <footer class="bg-dark text-light py-4 mt-5">
        <div class="container text-center">
            <p>&copy; 2024 Hệ thống Quản lý Thư viện PTIT. Tất cả quyền được bảo lưu.</p>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>
