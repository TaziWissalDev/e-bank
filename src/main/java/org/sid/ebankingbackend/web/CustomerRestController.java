package org.sid.ebankingbackend.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.ebankingbackend.dtos.CustomerDTO;
import org.sid.ebankingbackend.entities.BankAccount;
import org.sid.ebankingbackend.entities.Customer;
import org.sid.ebankingbackend.exceptions.CustomerNotFountException;
import org.sid.ebankingbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class CustomerRestController {
    private BankAccountService bankAccountService;

    @GetMapping("/customers")
    public List<CustomerDTO> customerList(){
        return bankAccountService.getCustomers();
    }

    @GetMapping("/customers/search")
    public List<CustomerDTO> searchCustomers(@RequestParam(name = "keyword",defaultValue = "") String keyword){
        return bankAccountService.searchCustomers("%"+keyword+"%");
    }
    @GetMapping("/customer/{id}")
    public CustomerDTO getCustomer(@PathVariable(name = "id") Long customerID) throws CustomerNotFountException {
        return bankAccountService.getCustomer(customerID);
    }

    @GetMapping("/customers/{id}/accounts")
    public List<BankAccount> getBankAccountsByCustomerId(@PathVariable(name = "id") Long customerID) {
        return bankAccountService.getBankAccountByCustomerId(customerID);
    }

    @PostMapping("/customers")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO request){
        return bankAccountService.saveCustomer(request);
    }

    @PutMapping("/customers/{customerID}")
    public CustomerDTO updateCustomer(@PathVariable Long customerID,@RequestBody CustomerDTO customerDTO){
        customerDTO.setId(customerID);
        return bankAccountService.updateCustomer(customerDTO);
    }

    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable Long id){
        bankAccountService.deleteCustomer(id);
    }


}
