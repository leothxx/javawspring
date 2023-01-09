<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>ajaxTest2_3.jsp</title>
	<jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
	<script>
		'use strict';
		$(function(){
			$("#dodo").change(function(){
				let dodo = $(this).val();
				if(dodo == "") {
					alert("지역을 선택하세요!");
					return false;
				}
				//alert("선택한 지역 : "+dodo);
				$.ajax({
					type : "post",
					url : "${ctp}/study/ajax/ajaxTest2_3",
					data : {dodo : dodo},
					success : function(res) {
						//alert("res : "+ res);
						let str = '';
						str += '<option value="">도시선택</option>';
						for(let i=0; i<res.city.length; i++) { //키로 찾아야함 res.city
							str += '<option>'+res.city[i]+'</option>'; //키로 찾아야함 res.city
						}
						$("#city").html(str);
					},
					error : function() {
						alert("전송 에러!!");
					}
				});
			});
		});
		function fCheck() {
			let dodo = $("#dodo").val();
			let city = $("#city").val();
			if(dodo == "" || city == "") {
				alert("지역을 선택하세요!");
				return;
			}
			let str = "선택하신 지역은 "+dodo+" "+city+" 입니다. &nbsp;";
			str += '<input type="button" value="다시검색" onclick="location.reload()" class="btn btn-danger"/>';
			$("#demo").html(str);
		}
	</script>
</head>
<body>
    <jsp:include page="/WEB-INF/views/include/nav.jsp" />
    <jsp:include page="/WEB-INF/views/include/slide2.jsp" />
	<p><br/><p>
	<div class="container">
		<h2>Ajax를 활용한 값의 전달3(HashMap<k,v>처리)</h2>
		<hr/>
		<form name="myform" method="post">
			<h3>도시를 선택하세요</h3>
			<select name="dodo" id="dodo">
				<option value="">지역선택</option>
				<option value="서울특별시">서울특별시</option>
				<option value="충청남도">충청남도</option>
				<option value="경기도">경기도</option>
				<option value="충청북도">충청북도</option>
			</select>
			<select name="city" id="city">
				<option value="">도시선택</option>
			</select>
			<input type="button" value="선택" onclick="fCheck()" class="btn btn-primary"/> &nbsp;
			<input type="button" value="돌아가기" onclick="location.href='${ctp}/study/ajax/ajaxMenu'" class="btn btn-info"/>
		</form>
		<hr/>
		<div id="demo"></div>
	</div>
	<p><br/><p>
	<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>