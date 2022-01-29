package ir.maktab.finalproject.util;
import ir.maktab.finalproject.exception.InvalidCaptchaException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
public class CaptchaValidatorUtil implements CaptchaValidator{
    @Override
    public void validate(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String captcha = (String)session.getAttribute("CAPTCHA");
        String param = request.getParameter("captcha");
        if (captcha==null || !captcha.equals(param))
            throw new InvalidCaptchaException();
    }
}
