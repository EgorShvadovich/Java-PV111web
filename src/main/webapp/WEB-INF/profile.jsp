<%@ page import="step.learning.entity.User" %>
<%@ page import="step.learning.entity.Role" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String contextPath = request.getContextPath() ;
    User user = (User) request.getAttribute("profile-user");
%>
<% if(user == null) { %>
<h1>Запитаного профілю не знайдено</h1>
<% } else { %>
<h1>Профіль користувача <%=user.getName()%></h1>
<img src="<%=contextPath%>/upload/avatar/<%=user.getAvatar()%>" alt="avatar" />
<% for( Role role : user.getRoles() ) { %>
<p>Має роль <%=role.getName()%></p>
<% } %>
<% } %>