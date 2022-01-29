package ir.maktab.finalproject.util;

import ir.maktab.finalproject.exception.UnauthorizedCustomerException;
import ir.maktab.finalproject.service.RequestService;
import ir.maktab.finalproject.service.dto.output.RequestOutputDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class RequestAuthorizerUtil implements RequestAuthorizer{
    @Autowired
    RequestService requestService;
    @Override
    public void authorize(Long requestId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long customerId = Long.valueOf(authentication.getName());
        RequestOutputDTO request = requestService.findById(requestId);
        if (!request.getCustomerId().equals(customerId))
            throw new UnauthorizedCustomerException();
    }
}
