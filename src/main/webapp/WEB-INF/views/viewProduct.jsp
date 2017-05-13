<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="container-wrapper">
	<div class="container">
		<h2>Product Detail</h2>
		<p class="lead">Here is the detail information of the product</p>
		<div class="row">
			<div class="col-md-6">
				<img
					src="<c:url value="/resources/images/${product.imageFilename }"/>"
					alt="image" style="width: 80%" />
			</div>

			<div class="col-md-6">
				<h3>${product.name }</h3>
				<p>${product.description }</p>
				<p>
					<strong>Manufacturer</strong> : ${product.manufacturer }
				</p>
				<p>
					<strong>Category</strong> : ${product.category }
				</p>
				<p>
					<strong>${product.price }원</strong>
				</p>
			</div>
		</div>
	</div>
</div>