const API_BASE = 'http://localhost:8080/api/admin';
const JWT = localStorage.getItem('jwt');

// Helper for API calls
async function fetchWithAuth(url, method = 'GET', body = null) {
    const options = {
        method,
        headers: { 'Authorization': `Bearer ${JWT}`, 'Content-Type': 'application/json' }
    };
    if (body) options.body = JSON.stringify(body);

    const res = await fetch(url, options);
    if (!res.ok) {
        const errorText = await res.text();
        throw new Error(`API failed: ${res.status} - ${errorText}`);
    }

    const contentType = res.headers.get('content-type');
    if (contentType && contentType.includes('application/json')) {
        return res.json();
    } else {
        return res.text();
    }
}

// Users Section
async function fetchUsers() {
    try {
        const users = await fetchWithAuth(`${API_BASE}/users`);
        const tbody = document.querySelector('#usersTable tbody');
        tbody.innerHTML = '';
        users.forEach(user => {
            const tr = document.createElement('tr');
            tr.innerHTML = `<td>${user.mobileNumber}</td><td>${user.username}</td><td>${user.userEmail}</td><td>${user.role}</td><td><button onclick="updateUserRole('${user.mobileNumber}', 'ADMIN')">Make Admin</button><button onclick="deleteUser('${user.mobileNumber}')">Delete</button></td>`;
            tbody.appendChild(tr);
        });
    } catch (error) {
        alert(error.message);
    }
}

async function updateUserRole(mobile, newRole) {
    try {
        await fetchWithAuth(`${API_BASE}/users/${mobile}`, 'PATCH', { role: newRole });
        fetchUsers();
    } catch (error) {
        alert(error.message);
    }
}

async function deleteUser(mobile) {
    if (confirm('Are you sure you want to delete this user?')) {
        try {
            const message = await fetchWithAuth(`${API_BASE}/users/${mobile}`, 'DELETE');
            alert(message);
            fetchUsers();
        } catch (error) {
            alert(error.message);
        }
    }
}

// Services Section
async function fetchServices() {
    try {
        const services = await fetchWithAuth(`${API_BASE}/services`);
        const tbody = document.querySelector('#servicesTable tbody');
        tbody.innerHTML = '';
        services.forEach(service => {
            const safeName = (service.name || '').replace(/'/g, "\\'");
            const safeDesc = (service.description || '').replace(/'/g, "\\'");
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${service.id}</td>
                <td>${service.name}</td>
                <td>${service.description || '-'}</td>
                <td>${service.price}</td>
                <td>
                    <button onclick="editService('${service.id}', '${safeName}', '${safeDesc}', ${service.price})">Edit</button>
                    <button onclick="deleteService('${service.id}')">Delete</button>
                </td>`;
            tbody.appendChild(tr);
        });
    } catch (error) {
        alert(error.message);
    }
}

function showAddServiceModal(id = '', name = '', description = '', price = '') {
    document.getElementById('serviceId').value = id;
    document.getElementById('serviceName').value = name;
    document.getElementById('serviceDescription').value = description;
    document.getElementById('servicePrice').value = price;
    document.getElementById('addServiceModal').style.display = 'block';
}

function editService(id, name, description, price) {
    showAddServiceModal(id, name, description, price);
}

async function saveService() {
    const id = document.getElementById('serviceId').value;
    const name = document.getElementById('serviceName').value;
    const description = document.getElementById('serviceDescription').value;
    const price = parseFloat(document.getElementById('servicePrice').value);

    if (!name || isNaN(price)) {
        alert('Please enter valid name and price');
        return;
    }

    const method = id ? 'PATCH' : 'POST';
    const url = id ? `${API_BASE}/services/${id}` : `${API_BASE}/services`;

    try {
        await fetchWithAuth(url, method, { name, price, description });
        closeModal('addServiceModal');
        fetchServices();
    } catch (error) {
        alert(error.message);
    }
}

async function deleteService(id) {
    if (confirm('Are you sure you want to delete this service?')) {
        try {
            const message = await fetchWithAuth(`${API_BASE}/services/${id}`, 'DELETE');
            alert(message);
            fetchServices();
        } catch (error) {
            alert(error.message);
        }
    }
}

// Add-Ons Section
async function fetchAddOns() {
    try {
        const addOns = await fetchWithAuth(`${API_BASE}/addons`);
        const tbody = document.querySelector('#addOnsTable tbody');
        tbody.innerHTML = '';
        addOns.forEach(addOn => {
            const tr = document.createElement('tr');
            tr.innerHTML = `<td>${addOn.id}</td><td>${addOn.name}</td><td>${addOn.price}</td><td><button onclick="editAddOn('${addOn.id}', '${addOn.name}', ${addOn.price})">Edit</button><button onclick="deleteAddOn('${addOn.id}')">Delete</button></td>`;
            tbody.appendChild(tr);
        });
    } catch (error) {
        alert(error.message);
    }
}

function showAddAddOnModal(id = '', name = '', price = '') {
    document.getElementById('addOnId').value = id;
    document.getElementById('addOnName').value = name;
    document.getElementById('addOnPrice').value = price;
    document.getElementById('addAddOnModal').style.display = 'block';
}

async function saveAddOn() {
    const id = document.getElementById('addOnId').value;
    const name = document.getElementById('addOnName').value;
    const price = parseFloat(document.getElementById('addOnPrice').value);

    if (!name || isNaN(price)) {
        alert('Please enter valid name and price');
        return;
    }

    const method = id ? 'PATCH' : 'POST';
    const url = id ? `${API_BASE}/addons/${id}` : `${API_BASE}/addons`;

    try {
        await fetchWithAuth(url, method, { name, price });
        closeModal('addAddOnModal');
        fetchAddOns();
    } catch (error) {
        alert(error.message);
    }
}

function editAddOn(id, name, price) {
    showAddAddOnModal(id, name, price);
}

async function deleteAddOn(id) {
    if (confirm('Are you sure you want to delete this add-on?')) {
        try {
            const message = await fetchWithAuth(`${API_BASE}/addons/${id}`, 'DELETE');
            alert(message);
            fetchAddOns();
        } catch (error) {
            alert(error.message);
        }
    }
}

// Bookings Section
async function fetchBookings() {
    try {
        const bookings = await fetchWithAuth(`${API_BASE}/bookings`);
        const tbody = document.querySelector('#bookingsTable tbody');
        tbody.innerHTML = '';
        bookings.forEach(booking => {
            const tr = document.createElement('tr');
            const statusOptions = ['PENDING', 'WASHING', 'COMPLETED']
                .map(s => `<option value="${s}" ${booking.status === s ? 'selected' : ''}>${s}</option>`)
                .join('');
            const reviewButton = booking.status === 'COMPLETED'
                ? `<button onclick="viewReview('${booking.id}', '${booking.review || ''}', ${booking.rating || 0})">View/Edit Review</button>`
                : '';
            tr.innerHTML = `<td>${booking.id}</td><td>${booking.userMobileNumber}</td><td>${booking.serviceName || booking.serviceId}</td><td><select onchange="updateBookingStatus('${booking.id}', this.value)">${statusOptions}</select></td><td>${booking.price}</td><td><button onclick="updateBookingPrice('${booking.id}', ${booking.price})">Edit Price</button><button onclick="deleteBooking('${booking.id}')">Delete</button><button onclick="viewBookingDetails('${booking.id}')">View Details</button>${reviewButton}</td>`;
            tbody.appendChild(tr);
        });
    } catch (error) {
        alert(error.message);
    }
}

async function updateBookingStatus(id, status) {
    try {
        await fetch(`${API_BASE}/bookings/${id}/status?status=${status}`, {
            method: 'PATCH',
            headers: { 'Authorization': `Bearer ${JWT}` }
        });
        fetchBookings();
    } catch (error) {
        alert(error.message);
    }
}

async function updateBookingPrice(id, currentPrice) {
    const newPrice = prompt('Enter new price:', currentPrice);
    if (newPrice && !isNaN(parseFloat(newPrice))) {
        try {
            await fetchWithAuth(`${API_BASE}/bookings/${id}`, 'PATCH', { price: parseFloat(newPrice) });
            fetchBookings();
        } catch (error) {
            alert(error.message);
        }
    } else {
        alert('Invalid price');
    }
}

async function deleteBooking(id) {
    if (confirm('Are you sure you want to delete this booking?')) {
        try {
            const message = await fetchWithAuth(`${API_BASE}/bookings/${id}`, 'DELETE');
            alert(message);
            fetchBookings();
        } catch (error) {
            alert(error.message);
        }
    }
}

async function viewBookingDetails(id) {
    try {
        const booking = await fetchWithAuth(`${API_BASE}/bookings/${id}`);
        const content = document.getElementById('bookingDetailsContent');
        content.innerHTML = `
            <p><strong>ID:</strong> ${booking.id}</p>
            <p><strong>User:</strong> ${booking.userMobileNumber}</p>
            <p><strong>Service Name:</strong> ${booking.serviceName || booking.serviceId}</p>
            <p><strong>Status:</strong> ${booking.status}</p>
            <p><strong>Price:</strong> ${booking.price}</p>
            <p><strong>Scheduled:</strong> ${booking.scheduledDateTime}</p>
        `;
        document.getElementById('bookingDetailsModal').style.display = 'block';
    } catch (error) {
        alert(error.message);
    }
}

// Reviews for Completed Bookings
let currentBookingIdForReview = '';

function viewReview(id, currentReview, currentRating) {
    currentBookingIdForReview = id;
    document.getElementById('reviewText').value = currentReview;
    document.getElementById('reviewRating').value = currentRating;
    document.getElementById('reviewModal').style.display = 'block';
}

async function saveReview() {
    const review = document.getElementById('reviewText').value;
    const rating = parseInt(document.getElementById('reviewRating').value);

    if (isNaN(rating) || rating < 1 || rating > 5) {
        alert('Please enter a valid rating (1-5)');
        return;
    }

    try {
        await fetchWithAuth(`${API_BASE}/bookings/${currentBookingIdForReview}/review`, 'PATCH', { review, rating });
        closeModal('reviewModal');
        fetchBookings();
    } catch (error) {
        alert(error.message);
    }
}

async function sendPushToAll() {
    const title = document.getElementById('pushTitle').value.trim();
    const message = document.getElementById('pushMessage').value.trim();

    if (!title || !message) {
        alert('Please enter both title and message');
        return;
    }

    try {
        const res = await fetchWithAuth(`${API_BASE}/notifications/push`, 'POST', {
            title,
            message
        });
        alert(`Push sent to ${res.sent} users`);
        document.getElementById('pushTitle').value = '';
        document.getElementById('pushMessage').value = '';
    } catch (error) {
        alert(error.message);
    }
}

// Analytics Section
async function fetchAnalytics() {
    try {
        const stats = await fetchWithAuth(`${API_BASE}/analytics`);
        const div = document.getElementById('analytics');
        div.innerHTML = `
            Total Users: ${stats.totalUsers}<br>
            Total Bookings: ${stats.totalBookings}<br>
            Total Services: ${stats.totalServices}<br>
            Total Add-Ons: ${stats.totalAddOns}
        `;
    } catch (error) {
        alert(error.message);
    }
}

// Utility Functions
function closeModal(modalId) {
    document.getElementById(modalId).style.display = 'none';
}

function logout() {
    localStorage.removeItem('jwt');
    window.location.href = 'login.html';
}

function searchTable(tableId, query) {
    const table = document.getElementById(tableId);
    const rows = table.querySelectorAll('tbody tr');
    rows.forEach(row => {
        const text = row.textContent.toLowerCase();
        row.style.display = text.includes(query.toLowerCase()) ? '' : 'none';
    });
}

function exportToCSV(tableId, filename) {
    const table = document.getElementById(tableId);
    const rows = Array.from(table.querySelectorAll('tr'));
    const csv = rows
        .map(row => Array.from(row.querySelectorAll('th, td'))
            .map(cell => cell.textContent).join(','))
        .join('\n');
    const blob = new Blob([csv], { type: 'text/csv' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = filename;
    a.click();
    URL.revokeObjectURL(url);
}

// Redirect to login if no JWT
if (!JWT) window.location.href = 'login.html';
