<%@ page import="step.learning.entity.News" %>
<%@ page import="java.util.Objects" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    News news = (News) request.getAttribute("news_detail");
    String title = news == null
            ? "Запитаної новини не існує"
            : news.getTitle();
    String contextPath = request.getContextPath();
    boolean canUpdate = //(boolean) request.getAttribute("can-update");
            Objects.equals(true, request.getAttribute("can-update"));
%>
<h1 data-editable="true" data-parameter="title" ><%= title %>
</h1>
<% if (news != null) { %>

<% if (canUpdate) {%>
<div data-editable="true" data-parameter="spoiler"><%= news.getSpoiler()%></div>
<button onclick="newsEditClick()" style="position: fixed; right: 5px;top:30vh;" class="btn-floating btn-large waves-effect waves-light right" ><%=news.getSpoiler()%>
</button>
<%}%>
<img data-editable="true"  alt="image" src="<%= contextPath%>/upload/news/<%= news.getImageUrl()%>"/>
<p data-editable="true" data-parameter="text" data-news-edit-id="<%=news.getId()%>"><%= news.getText()%>
</p>
<% } %>