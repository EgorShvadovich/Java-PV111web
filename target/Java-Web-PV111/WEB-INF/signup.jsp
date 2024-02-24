<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    Map<String, String> errorMessages = (Map<String, String>) request.getAttribute("errorMessages");
    if(errorMessages == null) {
        errorMessages = new HashMap<>();
    }
%>
<h1>Зареєструватись на сайті</h1>
<div class="row">
    <form class="col s12" method="post" enctype="multipart/form-data">
        <div class="row">
            <div class="input-field col s6">
                <i class="material-icons prefix">account_circle</i>
                <input id="icon_prefix" type="text" name="user-name"
                       class="<%= (errorMessages.containsKey("user-name")) ? "invalid" : "" %>" >
                <label for="icon_prefix">П.І.Б.</label>
                <span class="helper-text"
                      data-error="<%=errorMessages.getOrDefault("user-name", "") %>"
                      data-success="Правильно">Прізвище, ім'я, по-батькові</span>
            </div>
            <div class="input-field col s6">
                <i class="material-icons prefix">phone</i>
                <input id="icon_telephone" type="tel"  name="user-phone" inputmode="tel"
                       class="<%= (errorMessages.containsKey("user-phone")) ? "invalid" : "" %>" >
                <label for="icon_telephone">Телефон</label>
                <span class="helper-text"
                      data-error="<%=errorMessages.getOrDefault("user-phone", "") %>"
                      data-success="Правильно">Мобільний або домашній телефон</span>
            </div>
        </div>
        <div class="row">
            <div class="input-field col s6">
                <i class="material-icons prefix">lock</i>
                <input  id="icon_password" type="password" name="user-password"
                        class="<%= (errorMessages.containsKey("user-password")) ? "invalid" : "" %>">
                <label for="icon_password">Пароль</label>
                <span class="helper-text"
                      data-error="<%=errorMessages.getOrDefault("user-password", "") %>"
                      data-success="Припустимо">Придумайте пароль</span>
            </div>
            <div class="input-field col s6">
                <i class="material-icons prefix">mail</i>
                <input  id="icon_email" type="tel"  name="user-email"
                        class="<%= (errorMessages.containsKey("user-email")) ? "invalid" : "" %>">
                <label for="icon_email">E-mail</label>
                <span class="helper-text"
                      data-error="<%=errorMessages.getOrDefault("user-email", "") %>"
                      data-success="Правильно">Адреса електронної пошти</span>
            </div>
        </div>
        <div class="row">
            <div class="file-field input-field col s6">
                <div class="btn indigo">
                    <i class="material-icons">photo</i>
                    <input type="file" name="user-avatar"   >

                </div>
                <div class="file-path-wrapper">
                    <input class="file-path <%= (errorMessages.containsKey("user-avatar")) ? "invalid" : "" %>"
                           type="text" placeholder="Аватарка" >
                    <span class="helper-text"
                          data-error="<%=errorMessages.getOrDefault("user-avatar", "") %>"
                          data-success="Припустимо">Виберіть аватарку</span>
                </div>
            </div>
            <div class="input-field col s6">
                <button class="btn indigo right"><i class="material-icons left">task_alt</i>Реєстрація</button>
            </div>
        </div>
    </form>
</div>