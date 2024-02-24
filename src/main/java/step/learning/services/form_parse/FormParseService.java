package step.learning.services.form_parse;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

public interface FormParseService {
    FormParseResult parse(HttpServletRequest request) throws ParseException;

}
