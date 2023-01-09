<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>ajaxTest3.jsp</title>
	<jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
	<script>
		'use strict';
		function idCheck() {
			let mid = myform.mid.value;
			if(mid.trim() == "") {
				alert("아이디를 입력하세요!");
				myform.mid.focus();
				return;
			}
			
			$.ajax({
				type : "post",
				url : "${ctp}/study/ajax/ajaxTest3_1",
				data : {mid : mid},
				success : function(vo) {
					let str = '<b>전송결과</b><hr/>';
					if(vo != '') {
						str += '아이디 : ' + vo.name + '<br/>';
						str += '이메일 : ' + vo.email + '<br/>';
						str += '홈페이지 : ' + vo.homePage + '<br/>';
						str += 'IP : ' + vo.hostIp + '<br/>';
					}
					else {
						str +='<font color="red"><b>찾는 자료가 없습니다.</b></font>'
					}
					$("#demo").html(str);
				},
				error : function() {
					alert("전송 에러!!");
				}
			});
		}
		
		function nameCheck() {
			let mid = myform.mid.value;
			let category = myform.category.value;
			if(mid.trim() == "") {
				alert("아이디를 입력하세요!");
				myform.mid.focus();
				return;
			}
			
			let query = {
				mid : mid,
				category : category
			};
			
			$.ajax({
				type : "post",
				url : "${ctp}/study/ajax/ajaxTest3_2",
				data : query,
				success : function(vos) {
					let str = '<b>전송결과</b><hr/>';
					if(vos != '') {
						str += '<table class="table table-hover">';
						str += '<tr class="table-dark text-dark">';
						str += '<th>아이디</th><th>이메일</th><th>홈페이지</th><th>IP</th>';
						str += '</tr>';
						for(let i=0; i<vos.length; i++) {
							str += '<tr class="text-left">';
							str += '<td>' + vos[i].name + '</td>';
							str += '<td>' + vos[i].email + '</td>';
							str += '<td>' + vos[i].homePage + '</td>';
							str += '<td>' + vos[i].hostIp + '</td>';
							str += '</tr>';
						}
						str += '</table>';
						
					}
					else {
						str +='<font color="red"><b>찾는 자료가 없습니다.</b></font>'
					}
					$("#demo").html(str);
				},
				error : function() {
					alert("전송 에러!!");
				}
			});
		}
		
	</script>
</head>
<body>
    <jsp:include page="/WEB-INF/views/include/nav.jsp" />
    <jsp:include page="/WEB-INF/views/include/slide2.jsp" />
	<p><br/><p>
	<div class="container">
		<h2>Ajax를 활용한 '회원 성명' 검색하기</h2>
		<hr/>
		<form name="myform">
			<p>	<select name="category" id="category">
					<option value="name">성명</option>
					<option value="email">이메일</option>
					<option value="homePage">홈페이지</option>
					<option value="hostIp">IP</option>
			    </select>
				<input type="text" name="mid" id="mid" autofocus required/>  &nbsp;
				<input type="button" value="일치검색" onclick="idCheck()" class="btn btn-secondary"/> &nbsp;
				<input type="button" value="부분일치검색" onclick="nameCheck()" class="btn btn-secondary"/> &nbsp;
				<input type="reset" value="다시입력" class="btn btn-warning"/>
				<input type="button" value="돌아가기" onclick="location.href='${ctp}/study/ajax/ajaxMenu'" class="btn btn-info"/> &nbsp;
			</p>
		</form>
		<p id="demo"></p>
	</div>
	<p><br/><p>
	<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>