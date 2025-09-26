// JavaScript for Library Management System

// Wait for DOM to be fully loaded
document.addEventListener('DOMContentLoaded', function() {
    // Initialize tooltips
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Initialize popovers
    var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });

    // Auto-hide alerts after 5 seconds
    setTimeout(function() {
        var alerts = document.querySelectorAll('.alert');
        alerts.forEach(function(alert) {
            var bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        });
    }, 5000);

    // Form validation
    initializeFormValidation();
    
    // Search functionality
    initializeSearch();
    
    // Book management
    initializeBookManagement();
    
    // Borrow management
    initializeBorrowManagement();
});

// Form Validation
function initializeFormValidation() {
    const forms = document.querySelectorAll('.needs-validation');
    
    Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    });
}

// Search Functionality
function initializeSearch() {
    const searchInput = document.getElementById('searchInput');
    if (searchInput) {
        // Debounce search input
        let searchTimeout;
        searchInput.addEventListener('input', function() {
            clearTimeout(searchTimeout);
            searchTimeout = setTimeout(() => {
                performSearch(this.value);
            }, 300);
        });
    }
}

function performSearch(query) {
    if (query.length < 2) return;
    
    // Show loading spinner
    showLoadingSpinner();
    
    // Simulate search (replace with actual AJAX call)
    setTimeout(() => {
        hideLoadingSpinner();
        // Update search results
        updateSearchResults(query);
    }, 500);
}

function updateSearchResults(query) {
    // This would typically make an AJAX call to the server
    console.log('Searching for:', query);
}

// Book Management
function initializeBookManagement() {
    // Book availability check
    const borrowButtons = document.querySelectorAll('.btn-borrow');
    borrowButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            const bookId = this.dataset.bookId;
            const availableCopies = parseInt(this.dataset.availableCopies);
            
            if (availableCopies <= 0) {
                e.preventDefault();
                showAlert('Sách này hiện không có sẵn để mượn!', 'warning');
                return false;
            }
        });
    });

    // Book deletion confirmation
    const deleteButtons = document.querySelectorAll('.btn-delete');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            if (!confirm('Bạn có chắc chắn muốn xóa sách này? Hành động này không thể hoàn tác.')) {
                e.preventDefault();
                return false;
            }
        });
    });
}

// Borrow Management
function initializeBorrowManagement() {
    // Due date calculation
    const borrowDateInput = document.getElementById('borrowDate');
    const dueDateInput = document.getElementById('dueDate');
    
    if (borrowDateInput && dueDateInput) {
        borrowDateInput.addEventListener('change', function() {
            calculateDueDate(this.value);
        });
    }

    // Return book functionality
    const returnButtons = document.querySelectorAll('.btn-return');
    returnButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            const recordId = this.dataset.recordId;
            showReturnModal(recordId);
        });
    });
}

function calculateDueDate(borrowDate) {
    if (!borrowDate) return;
    
    const borrowDateObj = new Date(borrowDate);
    const dueDateObj = new Date(borrowDateObj);
    dueDateObj.setDate(dueDateObj.getDate() + 14); // 14 days loan period
    
    const dueDateInput = document.getElementById('dueDate');
    if (dueDateInput) {
        dueDateInput.value = dueDateObj.toISOString().split('T')[0];
    }
}

function showReturnModal(recordId) {
    // Create and show return modal
    const modal = createReturnModal(recordId);
    document.body.appendChild(modal);
    const bsModal = new bootstrap.Modal(modal);
    bsModal.show();
    
    // Remove modal after hiding
    modal.addEventListener('hidden.bs.modal', function() {
        document.body.removeChild(modal);
    });
}

function createReturnModal(recordId) {
    const modal = document.createElement('div');
    modal.className = 'modal fade';
    modal.innerHTML = `
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Trả sách</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form id="returnForm">
                        <input type="hidden" name="id" value="${recordId}">
                        <div class="mb-3">
                            <label for="returnDate" class="form-label">Ngày trả</label>
                            <input type="date" class="form-control" id="returnDate" name="returnDate" required>
                        </div>
                        <div class="mb-3">
                            <label for="fineAmount" class="form-label">Phí phạt (VNĐ)</label>
                            <input type="number" class="form-control" id="fineAmount" name="fineAmount" min="0" step="1000">
                        </div>
                        <div class="mb-3">
                            <label for="notes" class="form-label">Ghi chú</label>
                            <textarea class="form-control" id="notes" name="notes" rows="3"></textarea>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="button" class="btn btn-primary" onclick="submitReturn()">Trả sách</button>
                </div>
            </div>
        </div>
    `;
    
    // Set today's date as default return date
    const today = new Date().toISOString().split('T')[0];
    modal.querySelector('#returnDate').value = today;
    
    return modal;
}

function submitReturn() {
    const form = document.getElementById('returnForm');
    const formData = new FormData(form);
    
    // Show loading
    showLoadingSpinner();
    
    // Submit form (replace with actual AJAX call)
    setTimeout(() => {
        hideLoadingSpinner();
        showAlert('Trả sách thành công!', 'success');
        
        // Close modal
        const modal = bootstrap.Modal.getInstance(document.querySelector('.modal'));
        modal.hide();
        
        // Reload page or update table
        location.reload();
    }, 1000);
}

// Utility Functions
function showAlert(message, type = 'info') {
    const alertContainer = document.getElementById('alertContainer') || createAlertContainer();
    
    const alert = document.createElement('div');
    alert.className = `alert alert-${type} alert-dismissible fade show`;
    alert.innerHTML = `
        <i class="fas fa-${getAlertIcon(type)}"></i> ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    alertContainer.appendChild(alert);
    
    // Auto-hide after 5 seconds
    setTimeout(() => {
        const bsAlert = new bootstrap.Alert(alert);
        bsAlert.close();
    }, 5000);
}

function createAlertContainer() {
    const container = document.createElement('div');
    container.id = 'alertContainer';
    container.className = 'position-fixed top-0 end-0 p-3';
    container.style.zIndex = '9999';
    document.body.appendChild(container);
    return container;
}

function getAlertIcon(type) {
    const icons = {
        'success': 'check-circle',
        'danger': 'exclamation-circle',
        'warning': 'exclamation-triangle',
        'info': 'info-circle'
    };
    return icons[type] || 'info-circle';
}

function showLoadingSpinner() {
    const spinner = document.createElement('div');
    spinner.id = 'loadingSpinner';
    spinner.className = 'spinner';
    spinner.style.position = 'fixed';
    spinner.style.top = '50%';
    spinner.style.left = '50%';
    spinner.style.transform = 'translate(-50%, -50%)';
    spinner.style.zIndex = '9999';
    document.body.appendChild(spinner);
}

function hideLoadingSpinner() {
    const spinner = document.getElementById('loadingSpinner');
    if (spinner) {
        spinner.remove();
    }
}

// Data Table Enhancement
function initializeDataTable() {
    const tables = document.querySelectorAll('.data-table');
    tables.forEach(table => {
        // Add sorting functionality
        const headers = table.querySelectorAll('th[data-sort]');
        headers.forEach(header => {
            header.style.cursor = 'pointer';
            header.addEventListener('click', function() {
                sortTable(table, this.dataset.sort);
            });
        });
    });
}

function sortTable(table, column) {
    const tbody = table.querySelector('tbody');
    const rows = Array.from(tbody.querySelectorAll('tr'));
    
    rows.sort((a, b) => {
        const aVal = a.querySelector(`td[data-sort="${column}"]`).textContent;
        const bVal = b.querySelector(`td[data-sort="${column}"]`).textContent;
        
        if (isNaN(aVal) || isNaN(bVal)) {
            return aVal.localeCompare(bVal);
        } else {
            return parseFloat(aVal) - parseFloat(bVal);
        }
    });
    
    rows.forEach(row => tbody.appendChild(row));
}

// Export Functions
function exportToCSV(tableId, filename) {
    const table = document.getElementById(tableId);
    const rows = table.querySelectorAll('tr');
    let csv = [];
    
    rows.forEach(row => {
        const cells = row.querySelectorAll('td, th');
        const rowData = Array.from(cells).map(cell => `"${cell.textContent.trim()}"`);
        csv.push(rowData.join(','));
    });
    
    const csvContent = csv.join('\n');
    const blob = new Blob([csvContent], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    
    const link = document.createElement('a');
    link.href = url;
    link.download = filename || 'export.csv';
    link.click();
    
    window.URL.revokeObjectURL(url);
}

// Print Functions
function printPage() {
    window.print();
}

function printTable(tableId) {
    const table = document.getElementById(tableId);
    const printWindow = window.open('', '_blank');
    
    printWindow.document.write(`
        <html>
            <head>
                <title>In bảng</title>
                <style>
                    body { font-family: Arial, sans-serif; }
                    table { width: 100%; border-collapse: collapse; }
                    th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
                    th { background-color: #f2f2f2; }
                </style>
            </head>
            <body>
                ${table.outerHTML}
            </body>
        </html>
    `);
    
    printWindow.document.close();
    printWindow.print();
}

// Responsive Functions
function handleResize() {
    const width = window.innerWidth;
    
    if (width < 768) {
        // Mobile adjustments
        document.body.classList.add('mobile-view');
    } else {
        document.body.classList.remove('mobile-view');
    }
}

window.addEventListener('resize', handleResize);
handleResize(); // Initial call
