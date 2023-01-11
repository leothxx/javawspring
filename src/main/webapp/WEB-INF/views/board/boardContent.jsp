<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<% pageContext.setAttribute("newLine", "\n"); %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>boardContent.jsp</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
  <script>
    'use strict';
    
    function goodCheck() {
    	$.ajax({
    		type  : "post",
    		url   : "${ctp}/board/boardGood",
    		data  : {idx : ${vo.idx}},
    		success:function(res) {
    			if(res == "0") alert("이미 좋아요 버튼을 클릭하셨습니다.");
    			else location.reload();
    		},
    		error : function() {
    			alert("전송 오류~~");
    		}
    	});
    }
    
    // 게시글 삭제처리
    function boardDelCheck() {
    	let ans = confirm("현 게시글을 삭제하시겠습니까?");
    	if(ans) location.href = "${ctp}/board/boardDelete?idx=${vo.idx}&pag=${pag}&pageSize=${pageSize}&mid=${vo.mid}";
    }
    
    // 댓글 달기
    function replyCheck() {
    	let content = $("#content").val();
    	if(content.trim() == "") {
    		alert("댓글을 입력하세요");
    		$("#content").focus();
    		return false;
    	}
    	let query = {
    			boardIdx  : ${vo.idx},
    			mid		: '${sMid}',
    			nickName  : '${sNickName}',
    			content   : content,
    			hostIp    : '${pageContext.request.remoteAddr}'
    	}
    	
    	$.ajax({
    		type : "post",
    		url  : "${ctp}/board/boardReplyInput",
    		data : query,
    		success:function(res) {
    			if(res == "1") {
    				alert("댓글이 입력되었습니다.");
    				location.reload();
    			}
    			else {
    				alert("댓글 입력 실패~~~");
    			}
    		},
  			error  : function() {
  				alert("전송 오류!!");
  			}
    	});
    }
    
    // 댓글 삭제하기
    function replyDelCheck(idx) {
    	let ans = confirm("현재 댓글을 삭제하시겠습니까?");
    	if(!ans) return false;
    	
    	$.ajax({
    		type  : "post",
    		url   : "${ctp}/board/boardReplyDelete",
    		data  : {idx : idx},
    		success:function(res) {
    			if(res == "1") {
    				alert("댓글이 삭제되었습니다.");
    				location.reload();
    			}
    			else {
    				alert("댓글이 삭제 실패~~");
    			}
    		},
    		error  : function() {
    			alert("전송 오류~~");
    		}
    	});
    }
  </script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp" />
<jsp:include page="/WEB-INF/views/include/slide2.jsp" />
<p><br/></p>
<div class="container">
  <h2 class="text-center">글 내 용 보 기</h2>
  <br/>
  <table class="table table-borderless">
    <tr>
      <td class="text-right">hostIp : ${vo.hostIp}</td>
    </tr>
  </table>
  <table class="table table-bordered">
    <tr>
      <th>글쓴이${vo.idx}</th>
      <td>${vo.nickName}</td>
      <th>글쓴날짜</th>
      <td>${fn:substring(vo.WDate,0,fn:length(vo.WDate)-2)}</td>
    </tr>
    <tr>
      <th>글제목</th>
      <td colspan="3">${vo.title}</td>
    </tr>
    <tr>
      <th>전자메일</th>
      <td>${vo.email}</td>
      <th>조회수</th>
      <td>${vo.readNum}</td>
    </tr>
    <tr>
      <th>홈페이지</th>
      <td>${vo.homePage}</td>
      <th>좋아요</th>
      <td><a href="javascript:goodCheck()">
            <c:if test="${sSw == '1'}"><font color="red">❤</font></c:if>
            <c:if test="${sSw != '1'}">❤</c:if>
          </a>
          ${vo.good}
          <a href="javascript:goodDBCheck()">
            <c:if test="${goodVo.good == 'Y'}"><font color="red">❤</font></c:if>
            <c:if test="${goodVo.good != 'Y'}">❤</c:if>
          </a>
      </td>
    </tr>
    <tr>
      <th>글내용</th>
      <td colspan="3" style="height:220px">${fn:replace(vo.content, newLine, "<br/>")}</td>
    </tr>
    <tr>
      <td colspan="4" class="text-center">
        <c:if test="${flag == 'search'}"><input type="button" value="돌아가기" onclick="location.href='${ctp}/board/boardSearch?search=${search}&searchString=${searchString}&pageSize=${pageSize}&pag=${pag}';" class="btn btn-secondary"/></c:if>
        <c:if test="${flag != 'search'}">
          <input type="button" value="돌아가기" onclick="location.href='${ctp}/board/boardList?pageSize=${pageSize}&pag=${pag}';" class="btn btn-secondary"/>
	        <c:if test="${sMid == vo.mid || sLevel == 0 || sLevel == 1}">
		        <input type="button" value="수정하기" onclick="location.href='${ctp}/board/boardUpdate?idx=${vo.idx}&pageSize=${pageSize}&pag=${pag}';" class="btn btn-success"/>
		        <input type="button" value="삭제하기" onclick="boardDelCheck()" class="btn btn-danger"/>
	        </c:if>
        </c:if>
      </td>
    </tr>
  </table>
  
  <c:if test="${flag != 'search'}">
	  <!-- 이전글/다음글 처리 -->
	  <table class="table table-borderless">
	    <tr>
	      <td>
	         <c:if test="${vo.idx < pnVos[0].idx}">👈 다음글 : </c:if><c:if test="${vo.idx > pnVos[0].idx}">👈 이전글 : </c:if><a href="${ctp}/board/boardContent?idx=${pnVos[0].idx}&pageSize=${pageSize}&pag=${pag}">${pnVos[0].title}</a></br/>
	         <c:if test="${!empty pnVos[1]}">👉 다음글 : <a href="${ctp}/board/boardContent?idx=${pnVos[1].idx}&pageSize=${pageSize}&pag=${pag}">${pnVos[1].title}</a></br/></c:if>
	      </td>
	    </tr>
	  </table>
  </c:if>
</div>
<br/>

<!-- 댓글 리스트보여주기 -->
<div class="container">
	<table class="table table-hover text-left">
	  <tr style="background-color:#eee">
	    <th> &nbsp;작성자</th>
	    <th>댓글내용</th>
	    <th class="text-center">작성일자</th>
	    <th class="text-center">접속IP</th>
	  </tr>
	  <c:forEach var="replyVo" items="${replyVos}">
	    <tr>
	      <td>${replyVo.nickName}
	        <c:if test="${sMid == replyVo.mid || sLevel == 0}">
	          (<a href="javascript:replyDelCheck(${replyVo.idx})" title="삭제하기">x</a>)
	        </c:if>
	      </td>
	      <td>
	        ${fn:replace(replyVo.content, newLine, "<br/>")}
	      </td>
	      <td class="text-center">${replyVo.WDate}</td>
	      <td class="text-center">${replyVo.hostIp}</td>
	    </tr>
	  </c:forEach>
	</table>
	<!-- 댓글 입력창 -->
	<%-- <form name="replyForm" method="post" action="${ctp}/boReplyInput.bo"> --%>
	<form name="replyForm">
	  <table class="table text-center">
	    <tr>
	      <td style="width:85%" class="text-left">
	        글내용 :
	        <textarea rows="4" name="content" id="content" class="form-control"></textarea>
	      </td>
	      <td style="width:15%">
	        <br/>
	        <p>작성 : ${sNickName}</p>
	        <p>
	          <input type="button" value="댓글달기" onclick="replyCheck()" class="btn btn-info btn-sm"/>
	        </p>
	      </td>
	    </tr>
	  </table>
	  <%-- 
	  <input type="hidden" name="boardIdx" value="${vo.idx}"/>
	  <input type="hidden" name="hostIp" value="${pageContext.request.remoteAddr}"/>
	  <input type="hidden" name="mid" value="${sMid}"/>
	  <input type="hidden" name="nickName" value="${sNickName}"/>
	   --%>
	</form>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>