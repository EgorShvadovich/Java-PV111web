package step.learning.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.fileupload.FileItem;
import step.learning.dal.NewsDao;
import step.learning.entity.News;
import step.learning.entity.User;
import step.learning.services.form_parse.FormParseResult;
import step.learning.services.form_parse.FormParseService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class NewsServlet  extends HttpServlet {
    private final NewsDao newsDao ;
    private final FormParseService formParseService;
    private final Logger logger ;

    @Inject
    public NewsServlet(NewsDao newsDao, FormParseService formParseService, Logger logger) {
        this.newsDao = newsDao;
        this.formParseService = formParseService;
        this.logger = logger;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean canDelete = false ;
        req.setAttribute( "create-status",
                newsDao.installTable()
                        ? "Success"
                        : "Error" ) ;
        User user = (User) req.getAttribute("auth-user");
        if( user != null ) {
            req.setAttribute("can-create",
                    user.getRoles().stream().anyMatch(role -> role.getCanCreate() == 1));
            req.setAttribute("can-update",
                    user.getRoles().stream().anyMatch(role -> role.getCanUpdate() == 1));
            canDelete = user.getRoles().stream().anyMatch(role -> role.getCanDelete() == 1);
            req.setAttribute("can-delete", canDelete);
        }
        String pathInfo = req.getPathInfo();
        if( "/".equals(pathInfo) ) {   // All news page
            req.setAttribute("news", newsDao.getAll(canDelete) );
            req.setAttribute("page-body", "news.jsp");
        }
        else {   // Single news page
            News news = newsDao.getById( pathInfo.substring(1) ) ;
            if( news != null ) {
                req.setAttribute("news_detail", news);
            }
            req.setAttribute("page-body", "news_detail.jsp");
        }

        req.getRequestDispatcher( "../WEB-INF/_layout.jsp" ).forward(req, resp);
    }
    /*                    getContextPath()      getServletPath()    getPathInfo()
    /JavaWeb111/news/       /JavaWeb111             /news                /
    /JavaWeb111/news/123    /JavaWeb111             /news                /123

    Д.З. Розширити таблицю News, додати посилання на автора (користувача)
    який її опублікував. Реалізувати передачу ID авторизованого
    користувача до бекенду, пересвідчитись у тому, що ці дані потрапляють
    до БД.
    * У картку новин додати відомості про автора (e-mail або name або інші дані)
     */

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        FormParseResult formParseResult;
        try {
            formParseResult = formParseService.parse(req);
        }catch(ParseException ex){
            logger.log(Level.SEVERE,ex.getMessage());
            sendRest(resp,"error","Data composition error");
            return;
        }
        Map<String,String> fields = formParseResult.getFields();
        sendRest(resp,String.join(",",fields.keySet()),String.join(",",fields.keySet()));


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        FormParseResult formParseResult ;
        try {
            formParseResult = formParseService.parse(req) ;
        }
        catch (ParseException ex) {
            logger.log(Level.SEVERE, ex.getMessage());
            sendRest(resp, "error", "Data composition error");
            return;
        }
        Map<String,String> fields = formParseResult.getFields() ;
        String newsTitle = fields.get( "news-title" ) ;
        if( newsTitle == null || newsTitle.isEmpty() ) {
            sendRest(resp, "error", "Missing required data: 'news-title'");
            return;
        }
        String newsDate = fields.get( "news-date" ) ;
        if( newsDate == null || newsDate.isEmpty() ) {
            sendRest(resp, "error", "Missing required data: 'news-date'");
            return;
        }
        String newsSpoiler = fields.get( "news-spoiler" ) ;
        if( newsSpoiler == null || newsSpoiler.isEmpty() ) {
            sendRest(resp, "error", "Missing required data: 'news-spoiler'");
            return;
        }
        String newsText = fields.get( "news-text" ) ;
        if( newsText == null || newsText.isEmpty() ) {
            sendRest(resp, "error", "Missing required data: 'news-text'");
            return;
        }
        if( ! formParseResult.getFiles().containsKey("news-file") ) {
            sendRest(resp, "error", "Missing required data: 'news-file'");
            return;
        }
        FileItem fileItem = formParseResult.getFiles().get("news-file");
        String savedFilename = null;
        String fileName = fileItem.getName();
        int dotPosition = fileName.lastIndexOf(".");
        if( dotPosition == -1 ) {
            sendRest(resp, "error", "File must have extension: 'news-file'");
            return;
        }
        String ext = fileName.substring(dotPosition);
        File savedFile ;
        do {
            savedFilename = UUID.randomUUID() + ext;
            savedFile = new File(
                    req.getServletContext().getRealPath("/upload/news"),
                    savedFilename
            );
        } while( savedFile.isFile() ) ;

        try {
            fileItem.write( savedFile ) ;
        }
        catch (Exception ex) {
            logger.log( Level.SEVERE, ex.getMessage() );
            sendRest(resp, "error", "File transfer error: 'news-file'");
            return;
        }
        News news = new News();
        news.setTitle(newsTitle);
        news.setSpoiler(newsSpoiler);

        try {
            news.setCreateDt(new SimpleDateFormat("yyyy-MM-dd").parse(newsDate));
        }
        catch (ParseException e) {
            sendRest(resp, "error", "Invalid data format: 'news-date'. YYYY-MM-DD expected");
            return;
        }
        news.setText(newsText);
        news.setImageUrl(savedFilename);

        if( newsDao.addNews(news) ) {
            sendRest(resp, "success", "News created");
        }
        else {
            sendRest(resp, "error", "Internal error.");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String newsId = req.getParameter("id");
        if( newsId == null || newsId.isEmpty() ) {
            sendRest(resp, "error", "Missing required data: 'id'");
            return;
        }
        if( newsDao.deleteNews(newsId) ) {
            sendRest(resp, "success", "News deleted");
        }
        else {
            sendRest(resp, "error", "Internal error.");
        }
    }

    private void sendRest(HttpServletResponse resp, String status, String message) throws IOException {
        Gson gson = new Gson();
        JsonObject res = new JsonObject() ;
        res.addProperty("status", status);
        res.addProperty("message", message);
        resp.getWriter().print( gson.toJson(res) );
    }
}
/*
Д.З. Поглибити валідацію даних, які надходять для створення
новини
- назва: не коротше 10 символів
- анонс: не коротше 10 слів
- дата: відповідає формату yyyy-mm-dd
- контент: не коротше 300 символів
- файл-картинка: має графічний тип (розширення файлу)
 */