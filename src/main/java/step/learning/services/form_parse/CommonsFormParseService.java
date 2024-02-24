package step.learning.services.form_parse;

import com.google.inject.Inject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class CommonsFormParseService implements FormParseService{
    private final static int MEMORY_LIMIT = 10 * 1024 * 1024;
    private final static int MAX_FILE_SIZE = 10 * 1024 * 1024;
    private final static int MAX_FORM_SIZE = 20 * 1024 * 1024;
    private final ServletFileUpload fileUpload;

    @Inject
    public CommonsFormParseService(){
        DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
        fileItemFactory.setSizeThreshold(MEMORY_LIMIT);
        fileItemFactory.setRepository(new File(
                System.getProperty("java.io.tmpdir")
        ));
        fileUpload = new ServletFileUpload(fileItemFactory);
        fileUpload.setFileSizeMax(MAX_FILE_SIZE);
        fileUpload.setSizeMax(MAX_FORM_SIZE);
    }

    @Override
    public FormParseResult parse(HttpServletRequest request) throws ParseException {
        Map<String,String> fields = new HashMap<>();
        Map<String, FileItem> files = new HashMap<>();

        if(request.getHeader("Content-Type") != null && request.getHeader("Content-Type").startsWith("multipart/form-data")){
            try {
                for(FileItem part : fileUpload.parseRequest(request)){
                    if(part.isFormField()){
                        fields.put(part.getFieldName(),part.getString("UTF-8"));
                    }
                    else{
                        files.put(part.getFieldName(),part);
                    }
                }
            } catch (FileUploadException | UnsupportedEncodingException e) {
                throw new ParseException(e.getMessage(),0);
            }
        }
        else{ // x-www-form-urlencoded
            for(Map.Entry<String,String[]> entry : request.getParameterMap().entrySet()){
                fields.put(entry.getKey(),entry.getValue()[0]);
            }
        }


        return new FormParseResult() {
            @Override
            public Map<String, String> getFields() {
                return fields;
            }
            @Override
            public Map<String, FileItem> getFiles() {
                return files;
            }
        };
    }
}
