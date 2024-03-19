<%@ page import="step.learning.entity.User" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String pageBody = (String) request.getAttribute( "page-body" ) ;
    if( pageBody == null ) {
        pageBody = "home.jsp" ;   // default page
    }
    pageBody = "/WEB-INF/" + pageBody;
    String contextPath = request.getContextPath() ;
    User user = (User) request.getAttribute("auth-user");
%>
<!doctype html>
<html>
<head>
    <!--Import Google Icon Font-->
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <!-- Compiled and minified CSS -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
    <link rel="stylesheet" href="<%=contextPath%>/css/site.css" />
    <title>Java web</title>
</head>
<body style="display: flex;flex-direction: column;justify-content: space-between;min-height: 100vh;">
<nav>
    <div class="nav-wrapper indigo">
        <a href="#" class="brand-logo left">Logo</a>
        <ul id="nav-mobile" class="right ">
            <li><a href="<%=contextPath%>/ioc"><i class="material-icons">sync</i>IoC</a></li>
            <li><a href="<%=contextPath%>/news/">Новини</a></li>
            <li><a href="#">JavaScript</a></li>
            <li><a href="<%=contextPath%>/privacy">Privacy</a></li>
            <li><a href="<%=contextPath%>/signup"><i class="material-icons">person_add</i></a></li>
            <% if(user == null){
                %>
            <li><a href="#modal-auth" class="modal-trigger"><i class="material-icons">key</i></a></li>
            <%
            }else{%>
            <li><a href="<%=contextPath%>/profile/<%=user.getId()%>" style="vertical-align: middle;"><img alt="avatar"
                                                                                                          src="<%=contextPath%>/upload/avatar/<%=user.getAvatar()%>"
                                                                                                          style="max-height: 40px;border-radius: 50%"/></a></li>
            <li><a href="?logout"><i class="material-icons">logout</i></a></li>
            <%}%>


        </ul>

    </div>
</nav>
    <div class="container">
        <jsp:include page="<%= pageBody %>" />
    </div>
<div style="flex: max-content"></div>



    <!-- Compiled and minified JavaScript -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
    <script src="<%=contextPath%>/js/site.js"></script>
</body>
<footer class="page-footer indigo">
    <div class="container">
        <div class="row">
            <div class="col l6 s12">
                <h5 class="white-text">Footer Content</h5>
                <p class="grey-text text-lighten-4">You can use rows and columns here to organize your footer content.</p>
            </div>
            <div class="col l4 offset-l2 s12">
                <h5 class="white-text">Links</h5>
                <ul>
                    <li><a class="grey-text text-lighten-3" href="#!">Link 1</a></li>
                    <li><a class="grey-text text-lighten-3" href="#!">Link 2</a></li>
                    <li><a class="grey-text text-lighten-3" href="#!">Link 3</a></li>
                    <li><a class="grey-text text-lighten-3" href="#!">Link 4</a></li>
                </ul>
            </div>
        </div>
    </div>
    <div class="footer-copyright">
        <div class="container">
            © 2024 Copyright Text
            <a class="grey-text text-lighten-4 right" href="#!">More Links</a>
        </div>
    </div>
</footer>

<div id="modal-auth" class="modal">
    <div class="modal-content">

        <h4>Автентифікація</h4>
        <div class="row">
            <div class="input-field col s11">
                <i class="material-icons prefix">mail</i>
                <input  id="auth-email" type="email">
                <label for="auth-email">E-mail</label>
            </div>
            <div class="input-field col s11">
                <i class="material-icons prefix">lock</i>
                <input  id="auth-password" type="password">
                <label for="auth-password">Пароль</label>
            </div>
        </div>
    </div>
    <div class="modal-footer">
        <div class="auth-message"></div>
        <div>
            <button class="modal-close btn-flat grey">Закрити</button>
            <button id="auth-button" class="btn-flat indigo white-text waves-effect">Вхід</button>
        </div>

    </div>
</div>
</html>
