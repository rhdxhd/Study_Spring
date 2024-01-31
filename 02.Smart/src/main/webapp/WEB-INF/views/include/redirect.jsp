<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  



<form method="post" action="${url}">
<input type="hidden" name="id" value="${vo.id }">
<input type="hidden" name="curPage" value="${vo.curPage }">
<input type="hidden" name="search" value="${vo.search }">
<input type="hidden" name="keyword" value="${vo.keyword }">
<input type="hidden" name="pageList" value="${vo.pageList }">
</form>

<script>
$("form").submit()
</script>