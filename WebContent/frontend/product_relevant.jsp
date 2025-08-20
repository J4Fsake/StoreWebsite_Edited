<%-- MOVE THIS TO PAGE <head>: <link rel="stylesheet" type="text/css" href="css/card_template.css" /> --%>

<div class="card text-center" style="font-family: 'Roboto', sans-serif">
	<a href="view_product?id=${recProduct.id}">
		<div class="content">
			<img class="image-product" src="${recProduct.image}" alt="${recProduct.name}" />
		</div>
	</a>

	<a href="view_product?id=${recProduct.id}" style="text-decoration: none">
		<div class="content">
			<div style="font-size: 25px">
				<b style="color: #000000">${recProduct.name}</b>
			</div>
		</div>
	</a>

	<a href="view_product?id=${recProduct.id}" style="text-decoration: none">
		<div class="content" style="font-size: 25px; color: #000000">
			<b>From: ${recProduct.brand}</b>
		</div>
	</a>

	<a href="view_product?id=${recProduct.id}" style="text-decoration: none">
		<div class="content" style="font-size: 25px; color: #000000">
			<b>Price: ${recProduct.price}</b>
		</div>
	</a>
</div>