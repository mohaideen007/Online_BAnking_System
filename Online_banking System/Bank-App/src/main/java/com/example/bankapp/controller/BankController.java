package com.example.bankapp.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.bankapp.model.Account;
import com.example.bankapp.service.AccountService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class BankController {

    @Autowired
    private AccountService accountService;


    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
    
        Account account = accountService.findAccountByUsername(username);
        model.addAttribute("account", account);
        return "dashboard";

}
    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @PostMapping("/register")
    public String registerAccount(@RequestParam String username,
                                  @RequestParam String password,
                                  Model model) {
      try{
        accountService.registerAccount(username, password);
        return "register:/login";
      }
      catch (RuntimeException e){
        model.addAttribute("error", e.getMessage());
        return "register";
      }
    }
    

    @GetMapping("/login")
    public String login() {
        return "login";
    }



    @PostMapping("/deposit")
    public String deposit(@RequestParam BigDecimal amount){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountService.findAccountByUsername(username);
        accountService.deposit(account, amount);
        return "redirect:/dashboard";
    }                     


    @PostMapping("/withdraw")
    public String withdraw(@RequestParam BigDecimal amount,Model model){
        
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountService.findAccountByUsername(username);
        try{
            accountService.withdraw(account, amount);
        }
        catch (RuntimeException e){
            model.addAttribute("error", e.getMessage());
            model.addAttribute("account", account);
            return "dashboard";
}

return "redirect:/dashboard";
    }


    @GetMapping("/transactions")
    public String transactionsHistory(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountService.findAccountByUsername(username);
        model.addAttribute("transactions", accountService.getTransactionHistroy(account));
        return "transactions";
}



@PostMapping("/transfer")
public String transferAmount(@RequestParam String toUsername,
                        @RequestParam BigDecimal amount,
                        Model model) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    Account fromAccount = accountService.findAccountByUsername(username);


    try {
        accountService.transferAmount(fromAccount, toUsername, amount);
    } catch (RuntimeException e) {
        model.addAttribute("error", e.getMessage());
        model.addAttribute("account", fromAccount);
        return "dashboard";


    }
    return "redirect:/dashboard";
}

}
    