package ir.maktab.finalproject.util;
import ir.maktab.finalproject.exception.InvalidCaptchaException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
public class CaptchaValidatorUtil implements CaptchaValidator{
    @Override
    public void validate(HttpServletRequest request , String captcha) {
        HttpSession session = request.getSession();
        String validCaptcha = (String)session.getAttribute("CAPTCHA");
        if (validCaptcha==null || !validCaptcha.equals(captcha))
            throw new InvalidCaptchaException();
    }
}
