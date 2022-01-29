package ir.maktab.finalproject.util;

import javax.servlet.http.HttpServletRequest;

public interface CaptchaValidator {
    public void validate(HttpServletRequest request);
}
