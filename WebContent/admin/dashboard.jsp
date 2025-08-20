<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Dashboard</title>
  <link href="../css/dashboard.css" rel="stylesheet" type="text/css" />
  <jsp:include page="pagehead.jsp"></jsp:include>
  <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/charts.css/dist/charts.min.css">

  <style>
    /* Form Filter */
    .filter-container { margin: auto; max-width: 900px; }
    .form-row { display: flex; gap: 20px; margin-bottom: 15px; }
    .form-row label { min-width: 120px; font-weight: bold; }
    .form-row select, .form-row input[type="date"] { flex: 1; }
    .form-row.single { justify-content: flex-start; }
    button[type="submit"] { padding: 8px 16px; font-size: 14px; border-radius: 6px; border: none; background-color: #007bff; color: white; cursor: pointer; }
    button[type="submit"]:hover { background-color: #0056b3; }

    /* Chart Container */
    .chart-container { margin: 30px auto; max-width: 900px; }
    table.charts-css { height: 400px; }

    /* Tooltip */
    td[data-tooltip] {
      position: relative;
      cursor: pointer;
    }
    td[data-tooltip]::after {
      content: attr(data-tooltip);
      position: absolute;
      bottom: 100%;
      left: 50%;
      transform: translateX(-50%);
      background: rgba(0, 0, 0, 0.75);
      color: #fff;
      padding: 5px 8px;
      border-radius: 4px;
      white-space: nowrap;
      font-size: 12px;
      opacity: 0;
      pointer-events: none;
      transition: opacity 0.2s ease-in-out;
    }
    td[data-tooltip]:hover::after { opacity: 1; }

    /* Print Button Dropdown */
    .dropdown { position: relative; display: inline-block; }
    .print-btn {
      padding: 8px 16px;
      font-size: 14px;
      border: none;
      border-radius: 6px;
      background-color: #28a745;
      color: white;
      cursor: pointer;
      transition: background 0.2s;
    }
    .print-btn:hover { background-color: #1e7e34; }
    .dropdown-content {
      display: none;
      position: absolute;
      right: 0;
      background-color: white;
      min-width: 140px;
      box-shadow: 0px 4px 8px rgba(0,0,0,0.2);
      border-radius: 4px;
      z-index: 1;
    }
    .dropdown-content a {
      display: block;
      padding: 8px 12px;
      color: #333;
      text-decoration: none;
    }
    .dropdown-content a:hover { background-color: #f1f1f1; }
    .dropdown:hover .dropdown-content { display: block; }
  </style>

  <script>
    function updateTitle() {
      const productSelect = document.getElementById('product_id');
      const typeReportSelect = document.getElementById('typeReport');
      const titleElement = document.getElementById('chart-title');
      const selectedProductId = productSelect.value;
      const selectedReportType = typeReportSelect.value;

      let title = (selectedReportType === 'profit') ? "Profits " : "Revenue ";
      if (selectedProductId === "0") {
        title += "For All Product";
      } else {
        const selectedProductName = productSelect.options[productSelect.selectedIndex].text;
        title += "For Product: " + selectedProductName;
      }
      titleElement.textContent = title;
    }

    function toggleFilters() {
      const reportBy = document.getElementById('reportBy').value;
      document.getElementById('product_id').disabled = (reportBy !== "product");
      document.getElementById('category_id').disabled = (reportBy !== "category");
    }

    function loadReport() {
      const data = {
        start_date: $('#start_date').val(),
        end_date: $('#end_date').val(),
        step: $('#step').val(),
        reportBy: $('#reportBy').val(),
        product_id: $('#product_id').val(),
        category_id: $('#category_id').val(),
        typeReport: $('#typeReport').val()
      };

      $.ajax({
        url: '/StoreWebsite/api/reports',
        method: 'GET',
        data: data,
        success: function (res) {
          renderChart(res.listReports, res.maxY);
          $('#total-value').text(res.total);
          updateTitle();
        },
        error: function () {
          Swal.fire("Error", "Could not load report data", "error");
        }
      });
      return false;
    }

    function renderChart(listReports, maxY) {
      const chartTable = document.getElementById('chart-table-body');
      chartTable.innerHTML = "";

      listReports.forEach(item => {
        let ratio = (item.total === 0) ? 0 : item.total / maxY;
        const tr = document.createElement('tr');
        const th = document.createElement('th'); th.scope = "row"; th.textContent = "";
        const td = document.createElement('td'); td.style = "--size: " + ratio;
        td.setAttribute("data-tooltip", item.start_date + "-" +  item.end_date + ":" + item.total);
        tr.appendChild(th); tr.appendChild(td);
        chartTable.appendChild(tr);
      });
    }

    function sendPrintRequest(format) {
      const start_date = document.getElementById('start_date').value;
      const end_date = document.getElementById('end_date').value;
      const step = document.getElementById('step').value;
      const reportBy = document.getElementById('reportBy').value;
      const category_id = document.getElementById('category_id').value;
      const product_id = document.getElementById('product_id').value;
      const typeReport = document.getElementById('typeReport').value;

      const url = 'print_report?' +
              'start_date=' + encodeURIComponent(start_date) +
              '&end_date=' + encodeURIComponent(end_date) +
              '&reportBy=' + encodeURIComponent(reportBy) +
              '&category_id=' + encodeURIComponent(category_id) +
              '&step=' + encodeURIComponent(step) +
              '&product_id=' + encodeURIComponent(product_id) +
              '&typeReport=' + encodeURIComponent(typeReport) +
              '&format=' + encodeURIComponent(format);

      window.location.href = url;
    }

    window.onload = function () {
      updateTitle();
      toggleFilters();
      document.getElementById('product_id').addEventListener('change', updateTitle);
      document.getElementById('typeReport').addEventListener('change', updateTitle);
      document.getElementById('reportBy').addEventListener('change', toggleFilters);
    };
  </script>
</head>
<body>
<jsp:directive.include file="header.jsp"/>

<div class="filter-container">
  <form onsubmit="return loadReport();">
    <!-- Row 1 -->
    <div class="form-row">
      <label for="start_date">Start Date:</label>
      <input type="date" id="start_date" name="start_date" required>
      <label for="end_date">End Date:</label>
      <input type="date" id="end_date" name="end_date" required>
    </div>

    <!-- Row 2 -->
    <div class="form-row">
      <label for="step">Step:</label>
      <select id="step" name="step">
        <option value="1">Ngày</option>
        <option value="7">Tuần</option>
        <option value="30">Tháng</option>
        <option value="365">Năm</option>
      </select>

      <label for="reportBy">Report By:</label>
      <select id="reportBy" name="reportBy">
        <c:forEach var="filter" items="${filterList}">
          <option value="${filter}">${filter}</option>
        </c:forEach>
      </select>
    </div>

    <!-- Row 3 -->
    <div class="form-row">
      <label for="product_id">Product:</label>
      <select id="product_id" name="product_id">
        <option value="0">All</option>
        <c:forEach var="product" items="${productList}">
          <option value="${product.id}">${product.id} - ${product.name}</option>
        </c:forEach>
      </select>

      <label for="category_id">Category:</label>
      <select id="category_id" name="category_id">
        <c:forEach var="category" items="${categoryList}">
          <option value="${category.id}">${category.name}</option>
        </c:forEach>
      </select>
    </div>

    <!-- Row 4 -->
    <div class="form-row single">
      <label for="typeReport">Type Of Report:</label>
      <select id="typeReport" name="typeReport">
        <option value="revenue">Revenue</option>
        <option value="profit">Profits</option>
      </select>
    </div>

    <button type="submit">Apply Filters</button>
  </form>
</div>

<!-- Chart -->
<div class="chart-container">
  <div class="chart-header" style="display: flex; justify-content: space-between; align-items: center;">
    <h2 id="chart-title">Revenue For All Product</h2>

    <div class="dropdown">
      <button class="print-btn">Print &nbsp; ▼</button>
      <div class="dropdown-content">
        <a href="#" onclick="sendPrintRequest('PDF')">Print .PDF</a>
        <a href="#" onclick="sendPrintRequest('XLSX')">Print .XLSX</a>
      </div>
    </div>
  </div>

  <div style="margin: 10px 0;">Total: <span id="total-value">0</span></div>

  <table class="charts-css column show-primary-axis show-data-axes data-spacing-10">
    <caption>Báo cáo</caption>
    <thead>
    <tr>
      <th scope="col">Khoảng</th>
      <th scope="col">Giá trị</th>
    </tr>
    </thead>
    <tbody id="chart-table-body">
    <!-- render bằng JS -->
    </tbody>
  </table>
</div>

</body>
</html>
