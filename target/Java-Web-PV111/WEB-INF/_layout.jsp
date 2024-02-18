<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String pageBody = (String) request.getAttribute( "page-body" ) ;
    if( pageBody == null ) {
        pageBody = "home.jsp" ;   // default page
    }
    String contextPath = request.getContextPath() ;
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
            <li><a href="#">Components</a></li>
            <li><a href="#">JavaScript</a></li>
            <li><a href="<%=contextPath%>/privacy">Privacy</a></li>
            <li><a href="<%=contextPath%>/signup"><i class="material-icons">person_add</i></a></li>

        </ul>

    </div>
</nav>
    <div class="container">
        <jsp:include page="<%= pageBody %>" />
    </div>
<div style="flex: max-content"></div>



    <!-- Compiled and minified JavaScript -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>

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
</html>
