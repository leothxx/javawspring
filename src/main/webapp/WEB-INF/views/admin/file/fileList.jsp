<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>fileList.jsp</title>
	<jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
	<script>
		'use strict';
		$(function(){
			$("#allSelect").change(function(){
				if($("#allSelect").is(":checked")) {
					$("input[name=checkBox]").prop("checked", true);
				}
				else {
					$("input[name=checkBox]").prop("checked", false);
				}
			});
		});
		
		function fileDelete(files) {
			let file = "";
			for(let i=0; i<files; i++) {
				if($("#checkBox"+i).is(":checked")) {
					file += $("#checkBox"+i).val()+"/";
				}
			}
			
			location.href="${ctp}/admin/file/fileDelete?file="+file;
		}
	</script>
</head>
<body>
	<p><br/><p>
	<div class="container">
		<h2>서버 파일 리스트</h2>
		<hr/>
		<p>서버의 파일 경로 : ${ctp}/data/ckeditor/~~~파일명</p>
		<input type="checkbox" name="allSelect" id="allSelect" value="전체선택"/>&nbsp;&nbsp;전체선택
		<input type="button" value="삭제" onclick="fileDelete(${fn:length(files)})" class="btn btn-danger"/>
		<hr/>
		<c:forEach var="file" items="${files}" varStatus="st">
			<div id="file${st.index}"><input type="checkbox" name="checkBox" id="checkBox${st.index}" value="${file}">&nbsp;&nbsp;<img src="${ctp}/resources/data/ckeditor/${file}" width="150px"/></div><hr/>
		</c:forEach>
	</div>
	<p><br/><p>
</body>
</html>