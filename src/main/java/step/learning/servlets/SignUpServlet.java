package step.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.fileupload.FileItem;
import step.learning.dal.UserDao;
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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class SignUpServlet extends HttpServlet {
    private final FormParseService formParseService ;
    private final UserDao userDao ;
    private final Logger logger;

    @Inject
    public SignUpServlet(FormParseService formParseService, UserDao userDao, Logger logger) {
        this.formParseService = formParseService;
        this.userDao = userDao;
        this.logger = logger;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.log(Level.WARNING,"Demo warning from Signup");

        HttpSession session = req.getSession();
        Map<String, String> errorMessages = (Map<String, String>) session.getAttribute( "form-status" ) ;
        if( errorMessages != null ) {
            req.setAttribute("errorMessages", errorMessages ) ;
            session.removeAttribute( "form-status" ) ;
        }
        // userDao.installTable();
        req.setAttribute( "page-body", "signup.jsp" ) ;
        req.getRequestDispatcher( "WEB-INF/_layout.jsp" ).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // API - JSON
        // Web - Redirect
        Map<String, String> errorMessages = new HashMap<>() ;
        FormParseResult formParseResult ;
        HttpSession session = req.getSession();
        try {
            formParseResult = formParseService.parse(req) ;
        }
        catch (ParseException ex) {
            errorMessages.put("parse", ex.getMessage());
            session.setAttribute( "form-status", errorMessages );
            resp.sendRedirect( req.getRequestURI() );
            return;
        }
        Map<String,String> fields = formParseResult.getFields() ;
        boolean isValid = true;
        String userName = fields.get( "user-name" ) ;
        if( userName == null || userName.isEmpty() ) {
            errorMessages.put( "user-name", "Не може бути порожнім name" ) ;
            isValid = false ;
        }
        if (userName.length() == 1) {
            errorMessages.put("user-name", "Має бути довшим ніж 1 символ");
        }

        Pattern pattern = Pattern.compile("[A-Z][a-z]*");
        Matcher matcher = pattern.matcher(userName);

        if (!matcher.matches()) {
            errorMessages.put("user-name", "Перша літера має бути велика,а інші - маленькі");
        }

        String userPhone = fields.get( "user-phone" ) ;
        if( userPhone == null || userPhone.isEmpty() ) {
            errorMessages.put( "user-phone", "Не може бути порожнім phone" ) ;
            isValid = false ;
        }

        String userPassword = fields.get( "user-password" ) ;
        if( userPassword == null || userPassword.isEmpty() ) {
            errorMessages.put( "user-password", "Не може бути порожнім password" ) ;
            isValid = false ;
        }

        String userEmail = fields.get( "user-email" ) ;
        if( userEmail == null || userEmail.isEmpty() ) {
            errorMessages.put( "user-email", "Не може бути порожнім email" ) ;
            isValid = false ;
        }
        String[] allowedExtensions = {".jpg", ".jpeg", ".png", ".gif"};
        String savedFilename = null;
        if (formParseResult.getFiles().containsKey("user-avatar")) {
            FileItem fileItem = formParseResult.getFiles().get("user-avatar");
            String fileName = fileItem.getName();
            int dotPosition = fileName.lastIndexOf(".");
            if (dotPosition == -1) {
                errorMessages.put("user-avatar", "Файли без розширення не допускаються");
                isValid = false;
            } else {
                String ext = fileName.substring(dotPosition).toLowerCase();
                boolean isValidExtension = false;


                for (String allowedExt : allowedExtensions) {
                    if (allowedExt.equals(ext)) {
                        isValidExtension = true;
                        break;
                    }
                }

                if (!isValidExtension) {
                    errorMessages.put("user-avatar", "Недопустиме розширення файлу");
                } else {

                    File savedFile;
                    do {
                        savedFilename = UUID.randomUUID() + ext;
                        savedFile = new File(
                                req.getServletContext().getRealPath("/upload/avatar"),
                                savedFilename
                        );
                    } while (savedFile.isFile());

                    try {
                        fileItem.write(savedFile);
                    } catch (Exception e) {
                        throw new IOException(e);
                    }
                }
            }
        }

        if( isValid ) {
            userDao.signupUser(userName, userPhone, userPassword, userEmail, savedFilename);
        }

        session.setAttribute( "form-status", errorMessages );
        resp.sendRedirect( req.getRequestURI() );
    }
}
