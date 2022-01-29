package ir.maktab.finalproject.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CaptchaUtil implements Captcha{
    private Random ranNum = new Random();

    public String generateCaptcha(int captchaLength){
        String saltCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        int saltLength = saltCharacters.length();
        StringBuffer captchaStrBuff = new StringBuffer();
        while (captchaStrBuff.length() < captchaLength){
            int index = ranNum.nextInt(saltLength);
            captchaStrBuff.append(saltCharacters.charAt(index));
        }
        return captchaStrBuff.toString();
    }
}
