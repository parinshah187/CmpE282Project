<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="utf-8">
	<title> Products-CMPE-282 Cloud Services </title>
	<link type="text/css" rel="stylesheet" href="../assets/css/bootstrap.css"/>
</head>


<body>
<img src="../assets/img/cloudServices1.jpg" class="span2" />
<div class="container">
	<h1><a href="#">CMPE 282 Cloud Services</a></h1>
		<div class="navbar">
              <div class="navbar-inner">
                <div class="container">
                  <ul class="nav">
                    <li class="span2"><a href="../view/products_home.html">CMPE282.com</a></li>
					<li class="active span6"></li>
					<li class="span2"><a href="/CloudServices/rest/users/cart"> My Cart </a></li> <!-- Sign In -->
                    <li><a href="#"> Sign Out </a></li>
                  </ul>
                </div>
              </div>
            </div>
</div>	
<div>
<ul class="nav nav-list span3">
    <li class="nav-header">What we sell?</li>
    <!--  <li class="active"><a href="#">What we sell?</a></li> -->
    <li><a href="/CloudServices/rest/users/comp">Computers</a></li>
    <li><a href="/CloudServices/rest/users/elec">Electronics</a></li>
    <li><a href="/CloudServices/rest/users/musnmov">Music & Movies</a></li>
    <!--  <li><a href="#">Contact Us</a></li>  -->
    <li class="nav-header">Join Us</li>
    <li><a href="#">Google+</a></li>
    <li><a href="#">Facebook</a></li>
    <li><a href="#">Twitter</a></li>
    <li><a href="#">Pinterest</a></li>
    <li><a href="#">My Space</a></li>
</ul>
	
</div>
<!--  Displaying details of a product here.-->

<table border="0">
<tr>
<td align="center">
	<img src=${product.image} width="200" height="200">
</td>
<td aligh="center">
	<h2> ${product.product_name} </h2><br />
	<h3 style="color:red"> $${product.price} </h3> <br />
	<button id="addtocart" onclick="AddToCart('${product.product_id}')"><img src="/CloudServices/assets/img/addtocartamazon.jpg" border="0" width="150" height="75"> </button> <br />
	Category : ${product.category} <br />
	Product ID : ${product.product_id} <br />
</td>
</table>

<!-- Product Display ends here -->
<!-- Footer starts here -->
<script src="../assets/js/addcart.js"></script>

</div>
</body>
</html>