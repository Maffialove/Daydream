package org.csu.mypetstore.controller;

import org.csu.mypetstore.domain.Account;
import org.csu.mypetstore.domain.Product;
import org.csu.mypetstore.service.AccountService;
import org.csu.mypetstore.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@Controller
@RequestMapping("/account/")
public class AccountController {

    @Autowired
    private AccountService accountService;

//    @Autowired
//    private CatalogService catalogService;

    @GetMapping("loginFrom")
    public String logInFrom(){
        return "account/login";
    }

    @PostMapping("login")
    public String logIn(String username, String password, Model model){
        Account account = accountService.getAccount(username, password);
        if(account == null){
            String msg = "Invalid username or password.";
            model.addAttribute("msg", msg);
            return "account/login";
        }else {
            account.setPassword(null);
            //List<Product> myList = catalogService.getProductListByCategory(account.getFavouriteCategoryId());
            boolean authenticated = true;
            model.addAttribute("account", account);
           // model.addAttribute("myList", myList);
            model.addAttribute("authenticated", authenticated);
            return "catalog/main";
        }
    }

    @GetMapping("logout")
    public String logout(Model model) {
        Account loginAccount = new Account();
        //List<Product> myList = null;
        boolean authenticated = false;
        model.addAttribute("account", loginAccount);
        //model.addAttribute("myList", myList);
        model.addAttribute("authenticated", authenticated);
        return "catalog/main";
    }



}