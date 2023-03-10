<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>boardList.jsp</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
  <script>
  	'use strict';
  	function pageCheck() {
  		let pageSize = document.getElementById("pageSize").value;
  		location.href="${ctp}/board/boardList?pageSize="+pageSize+"&pag=${pageVO.pag}";
  	}
  	// 검색
  	function searchCheck() {
  		let searchString = $("#searchString").val();
  		if(searchString.trim() == "") {
  			alert("찾고자 하는 검색어를 입력하세요!");
  			searchForm.searchString.focus();
  		}
  		else {
  			searchForm.submit();
  		}
  	}
  </script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp" />
<jsp:include page="/WEB-INF/views/include/slide2.jsp" />
<p><br/></p>
<div class="container">
  <h2 class="text-center">게 시 판 리 스 트</h2>
  <br/>
  <table class="table table-borderless">
    <tr>
      <td class="text-left p-0"><c:if test="${sLevel != 4}"><a href="${ctp}/board/boardInput?pag=${pageVO.pag}&pageSize=${pageVO.pageSize}" class="btn btn-secondary btn-sm">글쓰기</a>&nbsp;&nbsp;</c:if><a href="${ctp}/board/boardList" class="btn btn-sm btn-primary">전체보기</a></td>
      <td class="text-right p-0">
        <select name="pageSize" id="pageSize" onchange="pageCheck()">
          <option value="5"  ${pageSize==5  ? 'selected' : ''}>5건</option>
          <option value="10" ${pageSize==10 ? 'selected' : ''}>10건</option>
          <option value="15" ${pageSize==15 ? 'selected' : ''}>15건</option>
          <option value="20" ${pageSize==20 ? 'selected' : ''}>20건</option>
        </select>
      </td>
    </tr>
  </table>
  <table class="table table-hover text-center">
    <tr class="table-dark text-dark">
      <th>글번호</th>
      <th>글제목</th>
      <th>글쓴이</th>
      <th>글쓴날짜</th>
      <th>조회수</th>
      <th>좋아요</th>
    </tr>
  	<c:set var="curScrStartNo" value="${pageVO.curScrStartNo}"/>
    <c:forEach var="vo" items="${vos}">
    	<tr>
    	  <td>${curScrStartNo}</td>
    	  <td class="text-left">
    	  	<a href="${ctp}/board/boardContent?idx=${vo.idx}&pag=${pageVO.pag}&pageSize=${pageVO.pageSize}" class="title">${vo.title}
    	  		<c:if test="${vo.replyCount != 0}">[${vo.replyCount}]</c:if>
    	  		<c:if test="${vo.hour_diff <= 24}"><img src="${ctp}/images/new.gif"/></c:if>
   	  		</a>
    	  </td>
    	  <td>${vo.nickName}</td>
    	  <%-- <td>${fn:substring(vo.wDate,0,10)}(${vo.day_diff})</td> --%>
    	  <%-- <td>${vo.day_diff > 24 ? fn:substring(vo.wDate,0,10) : fn:substring(vo.wDate,11,16)}</td> --%>
    	  <td>
    	  	<c:if test="${vo.hour_diff > 24}">${fn:substring(vo.WDate,0,10)}</c:if>
    	  	<c:if test="${vo.hour_diff < 24 && vo.day_diff < 1}">${fn:substring(vo.WDate,11,16)}</c:if>
    	  	<c:if test="${vo.hour_diff < 24 && vo.day_diff >= 1}">${fn:substring(vo.WDate,0,16)}</c:if>
    	  </td>
    	  <td>${vo.readNum}</td>
    	  <td>${vo.good}</td>
    	</tr>
    	<c:set var="curScrStartNo" value="${curScrStartNo-1}"/>
    </c:forEach>
    <tr><td colspan="6" class="m-0 p-0"></td></tr>
  </table>
</div>

<!-- 블록 페이지 시작 -->
<div class="text-center">
  <ul class="pagination justify-content-center">
    <c:if test="${pageVO.pag > 1}">
      <li class="page-item"><a class="page-link text-secondary" href="${ctp}/board/boardList?pageSize=${pageVO.pageSize}&pag=1&search=${search}&searchString=${searchString}">첫페이지</a></li>
    </c:if>
    <c:if test="${pageVO.curBlock > 0}">
      <li class="page-item"><a class="page-link text-secondary" href="${ctp}/board/boardList?pageSize=${pageVO.pageSize}&pag=${(pageVO.curBlock-1)*pageVO.blockSize + 1}&search=${search}&searchString=${searchString}">이전블록</a></li>
    </c:if>
    <c:forEach var="i" begin="${(pageVO.curBlock)*pageVO.blockSize + 1}" end="${(pageVO.curBlock)*pageVO.blockSize + pageVO.blockSize}" varStatus="st">
      <c:if test="${i <= pageVO.totPage && i == pageVO.pag}">
    		<li class="page-item active"><a class="page-link bg-secondary border-secondary" href="${ctp}/board/boardList?pageSize=${pageVO.pageSize}&pag=${i}&search=${search}&searchString=${searchString}">${i}</a></li>
    	</c:if>
      <c:if test="${i <= pageVO.totPage && i != pageVO.pag}">
    		<li class="page-item"><a class="page-link text-secondary" href="${ctp}/board/boardList?pageSize=${pageVO.pageSize}&pag=${i}&search=${search}&searchString=${searchString}">${i}</a></li>
    	</c:if>
    </c:forEach>
    <c:if test="${pageVO.curBlock < pageVO.lastBlock}">
      <li class="page-item"><a class="page-link text-secondary" href="${ctp}/board/boardList?pageSize=${pageVO.pageSize}&pag=${(pageVO.curBlock+1)*pageVO.blockSize + 1}&search=${search}&searchString=${searchString}">다음블록</a></li>
    </c:if>
    <c:if test="${pageVO.pag < pageVO.totPage}">
      <li class="page-item"><a class="page-link text-secondary" href="${ctp}/board/boardList?pageSize=${pageVO.pageSize}&pag=${pageVO.totPage}&search=${search}&searchString=${searchString}">마지막페이지</a></li>
    </c:if>
  </ul>
</div>
<!-- 블록 페이지 끝 -->
<br/>
<!--  검색기 처리 시작 -->
<div class="cantainer text-center">
	<form name="searchForm" method="post" action="${ctp}/board/boardSearch">
		<b>검색 : </b>
		<select name="search">
			<option value="title">제목</option>
			<option value="nickName">작성자</option>
			<option value="content">내용</option>
		</select>
		<input type="text" name="searchString" id="searchString"/>
		<input type="button" value="검색" onclick="searchCheck()" class="btn btn-secondary btn-sm"/>
		<input type="hidden" name="pag" value="${pageVO.pag}"/>
		<input type="hidden" name="pageSize" value="${pageVO.pageSize}"/>
	</form>
</div>
<!-- 검색기 처리 끝 -->
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>