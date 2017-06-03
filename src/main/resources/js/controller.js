var cartApp = angular.module('cartApp', ['ngRoute']);

cartApp.controller("cartCtrl", function($scope, $http) {
	
	$scope.initCartId = function(cartId) {
		$scope.cartId = cartId;
		$scope.refreshCart();
	};
	
	$scope.refreshCart = function() {
		$http.get('/eStore/rest/cart/' + $scope.cartId).then(function successCallback(response) {
			$scope.cart = response.data;
		});
	};
	
	$scope.addToCart = function(productId) {
		
		$scope.setCsrfToken();
		
		$http.put('/eStore/rest/cart/add/' + productId).then(
			function successCallback() {
				alert("Product successfully added to the cart!");
			}, function errorCallback() {
				alert("Adding to the cart failed!")
		});	
	};
	
	$scope.removeFromCart = function(productId) {
		
		$scope.setCsrfToken();
		
		$http({
			method:'DELETE',
			url : '/eStore/rest/cart/cartitem/' + productId
		}).then(function successCallback() {
			$scope.refreshCart();
		}, function errorCallback(response) {
			console.log(response.data);
		});
	};
	
	$scope.clearCart = function() {
		
		$scope.setCsrfToken();
		
		$http({
			method : 'DELETE',
			url : '/eStore/rest/cart/' + $scope.cartId
		}).then(function successCallback() {
			$scope.refreshCart();
		}, function errorCallback(response) {
			console.log(response.data);
		});
	};
	
	$scope.calGrandTotal = function() {
		var grandTotal =0;
		
		for(var i=0; i<$scope.cart.cartItems.length; i++) {
			grandTotal += $scope.cart.cartItems[i].totalPrice;
		}
		
		return grandTotal;
	};
	
	$scope.setCsrfToken = function() {
		var csrfToken = $("meta[name='_csrf']").attr("content");
		var csrfHeader = $("meta[name='_csrf_header']").attr("content");
		
		$http.defaults.headers.common[csrfHeader] = csrfToken;
	};
});