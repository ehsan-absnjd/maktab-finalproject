package ir.maktab.finalproject.service;

import ir.maktab.finalproject.entity.Customer;
import ir.maktab.finalproject.entity.Specialist;
import ir.maktab.finalproject.entity.User;
import ir.maktab.finalproject.entity.UserStatus;
import ir.maktab.finalproject.exception.InvalidVerificationMailException;
import ir.maktab.finalproject.exception.UserNotFoundException;
import ir.maktab.finalproject.repository.UserRepository;
import ir.maktab.finalproject.service.dto.output.CustomerOutputDTO;
import ir.maktab.finalproject.service.dto.output.SpecialistOutputDTO;
import ir.maktab.finalproject.service.dto.output.UserOutputDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Transactional
    public UserOutputDTO verify(Long id, String email){
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException());
        if(!user.getEmail().equals(email) || user.getStatus()!= UserStatus.NEW){
            throw new InvalidVerificationMailException();
        }
        user.setStatus(user.getRole().equals("SPECIALIST")?UserStatus.WAITING_APPROVAL: UserStatus.APPROVED);
        User saved = repository.save(user);
        return convertToDTO(saved);
    }

    public UserOutputDTO findById(Long id){
        return convertToDTO(repository.findById(id).orElseThrow(()->new UserNotFoundException()));
    }

    public List<UserOutputDTO> findByParameters(Map<String, String> parameterMap) {
        return repository.findByParameters(parameterMap).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<UserOutputDTO> getReportByParameters(Map<String, String> parameterMap){
        return repository.getReport(parameterMap).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private UserOutputDTO convertToDTO(User user) {
        if (user instanceof Specialist){
            return convertSpecialist(user);
        }else if (user instanceof Customer){
            return convertCustomer(user);
        }else
            return null;
    }

    private UserOutputDTO convertSpecialist(User specialist){
        Specialist input = (Specialist) specialist;
        return SpecialistOutputDTO.builder()
                .id(input.getId())
                .firstName(input.getFirstName())
                .lastName(input.getLastName())
                .email(input.getEmail())
                .registrationDate(input.getRegistrationDate())
                .status(input.getStatus())
                .credit(input.getCredit())
                .photoURL(input.getPhotoURL())
                .points(input.getPoints())
                .role("SPECIALIST")
                .build();
    }

    private UserOutputDTO convertCustomer(User customer){
        Customer input = (Customer) customer;
        return CustomerOutputDTO.builder()
                .id(input.getId())
                .firstName(input.getFirstName())
                .lastName(input.getLastName())
                .email(input.getEmail())
                .registrationDate(input.getRegistrationDate())
                .status(input.getStatus())
                .credit(input.getCredit())
                .role("CUSTOMER")
                .build();
    }
}
