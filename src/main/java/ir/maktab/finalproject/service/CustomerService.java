package ir.maktab.finalproject.service;

import ir.maktab.finalproject.dto.input.CustomerInputDTO;
import ir.maktab.finalproject.dto.output.CustomerOutputDTO;
import ir.maktab.finalproject.entity.Customer;
import ir.maktab.finalproject.entity.UserStatus;
import ir.maktab.finalproject.exception.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService extends BaseService<Customer,Long>{

    @Autowired
    @Qualifier("customerRepository")
    protected void setRepository(JpaRepository<Customer, Long> repository) {
        this.repository = repository;
    }

    @Transactional
    public CustomerOutputDTO save(CustomerInputDTO inputDTO){
        Customer customer = convertFromDTO(inputDTO);
        Customer saved = repository.save(customer);
        return convertToDTO(saved);
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
        customer.setPassword(password);
        repository.save(customer);
        return convertToDTO(customer);
    }

    public CustomerOutputDTO update(Long customerId , CustomerInputDTO inputDTO){
        Customer customer = repository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException());
        customer.setFirstName(inputDTO.getFirstName());
        customer.setLastName(inputDTO.getLastName());
        customer.setEmail(inputDTO.getEmail());
        customer.setPassword(inputDTO.getPassword());
        customer.setCredit(inputDTO.getCredit());
        customer.setStatus(inputDTO.getStatus());
        repository.save(customer);
        return convertToDTO(customer);
    }

    public Customer convertFromDTO(CustomerInputDTO inputDTO){
        return Customer.builder()
                .firstName(inputDTO.getFirstName())
                .lastName(inputDTO.getLastName())
                .email(inputDTO.getEmail())
                .password(inputDTO.getPassword())
                .credit(inputDTO.getCredit())
                .registrationDate(new Date())
                .status(UserStatus.NEW)
                .build();
    }

    public CustomerOutputDTO convertToDTO(Customer input){
        return CustomerOutputDTO.builder()
                .id(input.getId())
                .firstName(input.getFirstName())
                .lastName(input.getLastName())
                .email(input.getPassword())
                .registrationDate(input.getRegistrationDate())
                .status(input.getStatus())
                .credit(input.getCredit())
                .build();
    }
}
