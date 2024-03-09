<%@ page import="step.learning.entity.User" %>
<%@ page import="step.learning.entity.News" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String contextPath = request.getContextPath() ;
    User user = (User) request.getAttribute("auth-user");
    List<News> news = (List<News>) request.getAttribute("news");
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
%>
<h1>Новини</h1>
<% for(News n : news) { %>
<div class="col s12 m7">
    <div class="card horizontal">
        <div class="card-image" style="flex: 1">
            <img src="<%=contextPath%>/upload/news/<%=n.getImageUrl()%>" alt="img" />
        </div>
        <div class="card-stacked" style="flex: 2">
            <div class="card-content">
                <h5><%=n.getTitle()%></h5>
                <p><%=dateFormat.format(n.getCreateDt())%></p>
                <small>
                    <%=n.getSpoiler()%>
                </small>
            </div>
            <div class="card-action">
                <a href="#">читати детальніше...</a>
            </div>
        </div>
    </div>
</div>
<% } %>
<% if(user != null){%>

}
<p>
    Контроль таблиці: <%= request.getAttribute( "create-status" ) %>
</p>
<div class="news-form">
    <div class="row">
        <div class="col s4">
            <div class="file-field input-field">
                <div class="btn indigo">
                    <span>File</span>
                    <input type="file" id="news-file">
                </div>
                <div class="file-path-wrapper">
                    <input id="news-file-path" class="file-path validate" type="text">
                </div>
            </div>
            <div style="width: 100%;" id="news-image-preview" src="<%=contextPath%>/upload/news/no-avatar1.png" style="width: 100%; min-height: 100px; background-color: #bebebe"></div>
        </div>
        <div class="col s8">
            <div class="input-field row">
                <i class="material-icons prefix">mode_edit</i>
                <textarea id="news-title" class="materialize-textarea"></textarea>
                <label for="news-title">Заголовок</label>
            </div>
            <div class="input-field row">
                <i class="material-icons prefix">calendar_month</i>
                <input id="news-date" type="date"/>
                <label for="news-date">Дата публікації</label>
            </div>
            <div class="input-field row">
                <i class="material-icons prefix">preview</i>
                <textarea id="news-spoiler" class="materialize-textarea"></textarea>
                <label for="news-spoiler">Анонс</label>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="input-field col s12">
            <textarea id="news-text" class="materialize-textarea"></textarea>
            <label for="news-text">Textarea</label>
        </div>
    </div>

    <div class="row"><button id="news-submit" class="btn indigo right">Публікувати</button> </div>
</div>
<% } %>