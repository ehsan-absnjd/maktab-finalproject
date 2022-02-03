package ir.maktab.finalproject.util;

public interface RequestAuthorizer {
    public void authorizeCustomer(Long requestId);
    public void authorizeSpecialist(Long requestId);
}
