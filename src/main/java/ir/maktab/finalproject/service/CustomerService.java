package ir.maktab.finalproject.service;

import ir.maktab.finalproject.entity.Specialist;
import ir.maktab.finalproject.exception.SpecialistNotFoundException;
import ir.maktab.finalproject.service.dto.input.CustomerInputDTO;
import ir.maktab.finalproject.service.dto.output.CustomerOutputDTO;
import ir.maktab.finalproject.entity.Customer;
import ir.maktab.finalproject.entity.UserStatus;
import ir.maktab.finalproject.exception.CustomerNotFoundException;
import ir.maktab.finalproject.repository.CustomerRepository;
import ir.maktab.finalproject.service.dto.output.SpecialistOutputDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService{
    @Autowired
    private CustomerRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Transactional
    public CustomerOutputDTO save(CustomerInputDTO inputDTO){
        Customer customer = convertFromDTO(inputDTO);
        Customer saved = repository.save(customer);
        return convertToDTO(saved);
    }

    @Transactional(readOnly = true)
    public Customer getById(Long customerId){
        Customer customer = repository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException());
        return customer;
    }

    @Transactional(readOnly = true)
    public CustomerOutputDTO findById(Long customerId){
        Customer customer = repository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException());
        return convertToDTO(customer);
    }

    @Transactional(readOnly = true)
    public List<CustomerOutputDTO> findAll(Pageable pageable){
        return repository.findAll(pageable).get().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CustomerOutputDTO> findAll(){
        return repository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional
    public CustomerOutputDTO changePassword(Long customerId , String password){
        Customer customer = repository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException());
        customer.setPassword(encoder.encode(password));
        repository.save(customer);
        return convertToDTO(customer);
    }

    @Transactional
    public CustomerOutputDTO changeStatus(Long customerId , UserStatus status){
        Customer customer = repository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException());
        customer.setStatus(status);
        repository.save(customer);
        return convertToDTO(customer);
    }

    @Transactional
    public CustomerOutputDTO update(Long customerId , CustomerInputDTO inputDTO){
        Customer customer = repository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException());
        customer.setFirstName(inputDTO.getFirstName());
        customer.setLastName(inputDTO.getLastName());
        customer.setEmail(inputDTO.getEmail());
        customer.setPassword(inputDTO.getPassword());
        repository.save(customer);
        return convertToDTO(customer);
    }


    @Transactional
    public CustomerOutputDTO addCredit(Long customerId , Double credit){
        Customer customer = repository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException());
        customer.setCredit(customer.getCredit() + credit );
        Customer saved = repository.save(customer);
        return convertToDTO(saved);
    }

    @Transactional
    public void removeById(Long customerId){
        Customer customer = repository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException());
        repository.delete(customer);
    }

    public Customer convertFromDTO(CustomerInputDTO inputDTO){
        return Customer.builder()
                .firstName(inputDTO.getFirstName())
                .lastName(inputDTO.getLastName())
                .email(inputDTO.getEmail())
                .password(encoder.encode(inputDTO.getPassword()))
                .registrationDate(new Date())
                .status(UserStatus.NEW)
                .build();
    }

    public CustomerOutputDTO convertToDTO(Customer input){
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
