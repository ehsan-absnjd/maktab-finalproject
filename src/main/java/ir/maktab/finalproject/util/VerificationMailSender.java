package ir.maktab.finalproject.util;

import ir.maktab.finalproject.service.dto.output.UserOutputDTO;

public interface VerificationMailSender {
    public void sendVerificationMail(UserOutputDTO userOutputDTO);
}
