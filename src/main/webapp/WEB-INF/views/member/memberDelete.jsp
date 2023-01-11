<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>memberDelete.jsp</title>
	<jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
	<script>
		'use strict';
		function userDel() {
			let mid = $("#mid").val();
			let res = confirm(mid+"님 정말로 탈퇴하시겠습니까 ?");
			if(!res) {
				return false;
			}
			myform.submit();
		}
	</script>
</head>
<body>
    <jsp:include page="/WEB-INF/views/include/nav.jsp" />
    <jsp:include page="/WEB-INF/views/include/slide2.jsp" />
	<p><br/><p>
	<div class="container text-center">
		<h2>회 원 탈 퇴</h2>
		<form name="myform" method="post">
			<div class="text-center" style="width: 500px; margin: 0px auto;">
				<div class="row mt-3 mb-3">
					<div class="col-3 text-right">아이디</div>
					<div class="col-7"><input type="text" name="mid" id="mid" class="form-control" autofocus required/></div>
				</div>
				<div class="row mb-3">
					<div class="col-3 text-right">비밀번호</div>
					<div class="col-7"><input type="password" name="pwd" id="pwd" class="form-control" required/></div>
				</div>
				<div class="row">
					<div class="col">
						<input type="button" value="회원탈퇴" onclick="userDel()" class="btn btn-danger"/>
						<input type="reset" value="다시입력" class="btn btn-warning"/>
						<input type="button" value="돌아가기" onclick="location.href='${ctp}/';" class="btn btn-info"/>
					</div>
				</div>
			</div>
		</form>
	</div>
	<p><br/><p>
	<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>