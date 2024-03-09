package step.learning.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.fileupload.FileItem;
import step.learning.dal.NewsDao;
import step.learning.entity.News;
import step.learning.services.form_parse.FormParseResult;
import step.learning.services.form_parse.FormParseService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class NewsServlet extends HttpServlet {
    private final NewsDao newsDao;

    private final FormParseService formParseService;

    private final Logger logger;

    @Inject
    public NewsServlet(NewsDao newsDao, FormParseService formParseService, Logger logger) {
        this.newsDao = newsDao;
        this.formParseService = formParseService;
        this.logger = logger;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("create-status",
                newsDao.installTable()
                        ? "Success"
                        : "Error");
        req.setAttribute("news",newsDao.getAll());
        req.setAttribute("page-body", "news.jsp");
        req.getRequestDispatcher("WEB-INF/_layout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        FormParseResult formParseResult;
        try {
            formParseResult = formParseService.parse(req);
        } catch (ParseException ex) {
            logger.log(Level.SEVERE, ex.getMessage());
            sendRest(resp, "error", "Data composition error");
            return;
        }
        Map<String, String> fields = formParseResult.getFields();
        String newsTitle = fields.get("news-title");
        if (newsTitle == null || newsTitle.isEmpty()) {
            sendRest(resp, "error", "Missing required data: 'news-title'");
            return;
        }
        String newsSpoiler = fields.get("news-spoiler");
        if (newsSpoiler == null || newsSpoiler.isEmpty()) {
            sendRest(resp, "error", "Missing required data: 'news-spoiler'");
            return;
        }
        String newsDate = fields.get("news-date");
        if (newsDate == null || newsDate.isEmpty()) {
            sendRest(resp, "error", "Missing required data: 'news-date'");
            return;
        }
        String newsText = fields.get("news-text");
        if (newsText == null || newsText.isEmpty()) {
            sendRest(resp, "error", "Missing required data: 'news-text'");
            return;
        }


        if (!formParseResult.getFiles().containsKey("user-avatar")) {
            sendRest(resp, "error", "Missing required data: 'news-file'");
            return;
        }
        String[] allowedExtensions = {".jpg", ".jpeg", ".png", ".gif"};

        FileItem fileItem = formParseResult.getFiles().get("news-file");
        String savedFilename = null;
        String fileName = fileItem.getName();
        int dotPosition = fileName.lastIndexOf(".");
        if (dotPosition == -1) {
            sendRest(resp, "error", "File must have extension: 'news-file'");
            return;
        }
        String ext = fileName.substring(dotPosition).toLowerCase();
        boolean isValidExtension = false;


        for (String allowedExt : allowedExtensions) {
            if (allowedExt.equals(ext)) {
                isValidExtension = true;
                break;
            }
        }

        if (!isValidExtension) {
            sendRest(resp, "error", "Недопустиме розширення файлу");
            return;
        } else {

            File savedFile;
            do {
                savedFilename = UUID.randomUUID() + ext;
                savedFile = new File(
                        req.getServletContext().getRealPath("/upload/news"),
                        savedFilename
                );
            } while (savedFile.isFile());

            try {
                fileItem.write(savedFile);
            } catch (Exception e) {
                logger.log(Level.SEVERE,e.getMessage());
                sendRest(resp, "error", "File transfer error: 'news-file'");
                return;
            }


            News news = new News();
            news.setTitle(newsTitle);
            news.setSpoiler(newsSpoiler);
            try {
                news.setCreateDt(new SimpleDateFormat("yyyy-MM-dd").parse(newsDate));
            } catch (ParseException e) {
               sendRest(resp,"error","Invalid data format 'news-date'");
               return;
            }
            news.setText(newsText);
            news.setImageUrl(savedFilename);

            if(newsDao.addNews(news)){
                sendRest(resp,"success","News created");

            }
            else{
                sendRest(resp,"error","Internal error");

            }
        }
    }

    private void sendRest(HttpServletResponse resp, String status, String message) throws IOException {
        Gson gson = new Gson();
        JsonObject res = new JsonObject();
        res.addProperty("status", status);
        res.addProperty("message", message);
        resp.getWriter().print(gson.toJson(res));
    }
}