<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="container-wrapper">
	<div class="container">
		<h2>Login with Username and Password</h2>

		<c:if test="${not empty logout }">
			<div style="color:#0000ff">
				<h3>${logout }</h3>
			</div>
		</c:if>
		
		<c:if test="${not empty error }">
			<div style="color:#ff0000">
				<h3>${error }</h3>
			</div>
		</c:if>
		
		<form action="<c:url value="/login"/>" method="post">
			<div class="form-group">
				<label for="username">Username:</label> <input type="text"
					class="form-control" name="username" style="width: 50%">
			</div>

			<div class="form-group">
				<label for="password">Password:</label> <input type="password"
					class="form-control" name="password" style="width: 50%">
			</div>

			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" /> <input type="submit" value="확인"
				class="btn btn-default">
		</form>
	</div>
</div>