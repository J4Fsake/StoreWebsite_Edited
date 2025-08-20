<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>Carousel Sản phẩm</title>
  <style>
    body {
      background-color: #111;
      font-family: 'Roboto', sans-serif;
    }
    .carousel-container {
      position: relative;
      width: 100%;
      overflow: hidden;
      padding: 20px 0;
    }
    .carousel-track {
      display: flex;
      transition: transform 0.4s ease-in-out;
    }
    .carousel-item {
      flex: 0 0 calc(100% / 3); /* 3 sản phẩm mỗi lần */
      box-sizing: border-box;
      padding: 0 5px;
    }
    .carousel-btn {
      position: absolute;
      top: 50%;
      transform: translateY(-50%);
      background-color: rgba(0, 0, 0, 0.5);
      color: white;
      border: none;
      font-size: 24px;
      padding: 10px 15px;
      cursor: pointer;
      border-radius: 50%;
      z-index: 10;
      transition: background 0.3s ease;
    }
    .carousel-btn:hover {
      background-color: rgba(0, 0, 0, 0.8);
    }
    .carousel-btn.left {
      left: 10px;
    }
    .carousel-btn.right {
      right: 10px;
    }
    .card {
      background-color: #222;
      padding: 10px;
      border-radius: 8px;
      text-align: center;
    }
    .card img {
      max-width: 100%;
      border-radius: 6px;
    }
    .card .content {
      margin-top: 8px;
    }
  </style>
</head>
<body>

<div class="carousel-container">
  <button class="carousel-btn left" id="btnPrev">&#10094;</button>
  <div class="carousel-track" id="recommendTrack">
    <!-- AJAX sẽ load sản phẩm vào đây -->
  </div>
  <button class="carousel-btn right" id="btnNext">&#10095;</button>
</div>

<script>
  let currentIndex = 0;
  const itemsPerPage = 3;
  const track = document.getElementById('recommendTrack');
  let totalItems = 0;

  function renderProducts(data) {
    track.innerHTML = '';

    data.forEach(product => {
      let href = "view_product?id=" + product.id;

      const col = document.createElement('div');
      col.className = 'carousel-item';

      const card = document.createElement('div');
      card.className = 'card';

      // Ảnh
      const linkImg = document.createElement('a');
      linkImg.href = href;
      const img = document.createElement('img');
      img.src = product.image;
      linkImg.appendChild(img);

      // Tên
      const nameDiv = document.createElement('div');
      nameDiv.className = 'content';
      nameDiv.style.fontSize = '20px';
      nameDiv.style.color = '#fff';
      nameDiv.textContent = product.name;

      // Brand
      const brandDiv = document.createElement('div');
      brandDiv.className = 'content';
      brandDiv.style.fontSize = '18px';
      brandDiv.style.color = '#ccc';
      brandDiv.textContent = "From: " + product.brand;

      // Price
      const priceDiv = document.createElement('div');
      priceDiv.className = 'content';
      priceDiv.style.fontSize = '18px';
      priceDiv.style.color = '#ccc';
      priceDiv.textContent = "Price: " + product.price + "$";

      card.appendChild(linkImg);
      card.appendChild(nameDiv);
      card.appendChild(brandDiv);
      card.appendChild(priceDiv);

      col.appendChild(card);
      track.appendChild(col);
    });

    totalItems = data.length;
  }

  function moveSlide(direction) {
    const maxIndex = Math.ceil(totalItems / itemsPerPage) - 1;
    currentIndex += direction;

    if (currentIndex < 0) currentIndex = 0;
    if (currentIndex > maxIndex) currentIndex = maxIndex;

    // Dịch theo %
    const offset = -(currentIndex * 100);
    track.style.transform = `translateX(${offset}%)`;
  }

  document.getElementById('btnPrev').addEventListener('click', () => moveSlide(-1));
  document.getElementById('btnNext').addEventListener('click', () => moveSlide(1));

  function loadRecommendList() {
    const apiUrl = `${window.location.origin}/StoreWebsite/api/recommend`;
    fetch(apiUrl)
            .then(response => {
              if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);
              return response.json();
            })
            .then(data => {
              if (!data || data.length === 0) return;
              renderProducts(data);
            })
            .catch(err => console.error('Error loading recommend list:', err));
  }

  document.addEventListener('DOMContentLoaded', loadRecommendList);
</script>

</body>
</html>
