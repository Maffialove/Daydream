package org.csu.mypetstore.controller;

import org.csu.mypetstore.domain.Account;
import org.csu.mypetstore.domain.Product;
import org.csu.mypetstore.service.AccountService;
import org.csu.mypetstore.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/account/")
@SessionAttributes({"account", "myList", "authenticated"})
public class AccountController {

    @Autowired
    private AccountService accountService;


    @GetMapping("loginFrom")
    public String logInFrom() {
        return "account/login";
    }

    @Autowired
    private CatalogService catalogService;

    private static final List<String> LANGUAGE_LIST;
    private static final List<String> CATEGORY_LIST;


    static {
        List<String> langList = new ArrayList<String>();
        langList.add("ENGLISH");
        langList.add("CHINESE");
        LANGUAGE_LIST = Collections.unmodifiableList(langList);

        List<String> catList = new ArrayList<String>();
        catList.add("FISH");
        catList.add("DOGS");
        catList.add("REPTILES");
        catList.add("CATS");
        catList.add("BIRDS");

        CATEGORY_LIST = Collections.unmodifiableList(catList);
    }

    //用户登录，检测输入的Username和Password是否能从数据库中得到，
    // 若能则登陆成功，改变登陆状态，写入账户信息。
    @PostMapping("login")
    public String login(String username, String password, Model model) {
        Account account = accountService.getAccount(username, password);

        if (account == null) {
            String msg = "Invalid username or password.";
            model.addAttribute("msg", msg);
            return "account/login";
        } else {
            account.setPassword(null);
            List<Product> myList = catalogService.getProductListByCategory(account.getFavouriteCategoryId());
            boolean authenticated = true;
            model.addAttribute("account", account);
            model.addAttribute("myList", myList);
            model.addAttribute("authenticated", authenticated);
            return "catalog/main";
        }
    }

    //用户登出，改变登陆状态，删除用户信息
    @GetMapping("logout")
    public String logout(Model model) {
        Account loginAccount = new Account();
        List<Product> myList = null;
        boolean authenticated = false;
        model.addAttribute("account", loginAccount);
        model.addAttribute("authenticated", authenticated);
        return "catalog/main";
    }

    //修改用户信息
    @GetMapping("editAccountForm")
    public String editAccountForm(@SessionAttribute("account") Account account, Model model) {
        model.addAttribute("account", account);
        model.addAttribute("LANGUAGE_LIST", LANGUAGE_LIST);
        model.addAttribute("CATEGORY_LIST", CATEGORY_LIST);
        return "account/edit_account";
    }

    @PostMapping("editAccount")
    public String editAccount(Account account, String repeatedPassword, Model model) {
        if (account.getPassword() == null || account.getPassword().length() == 0
                || repeatedPassword == null || repeatedPassword.length() == 0) {
            String msg = "The password cannot be empty";
            model.addAttribute("msg", msg);
            return "account/edit_account";
        } else if (!account.getPassword().equals(repeatedPassword)) {
            String msg = "The two passwords don't match";
            model.addAttribute("msg", msg);
            return "account/edit_account";
        } else {
            accountService.updateAccount(account);
            account = accountService.getAccount(account.getUsername());
            List<Product> myList = catalogService.getProductListByCategory(account.getFavouriteCategoryId());
            boolean authenticated = true;
            model.addAttribute("account", account);
            model.addAttribute("myList", myList);
            model.addAttribute("authenticated", authenticated);
            return "/catalog/main";
        }
    }

    //注册用户
    @GetMapping("newAccountForm")
    public String newAccountForm(Model model){
        model.addAttribute("newAccount",new Account());
        model.addAttribute("LANGUAGE_LIST", LANGUAGE_LIST);
        model.addAttribute("CATEGORY_LIST", CATEGORY_LIST);
        return "account/new_account";
    }

    @PostMapping("newAccount")
    public String newAccount(Account newAccount,String repeatedPassword,Model model){
        if (newAccount.getUsername() ==null||newAccount.getUsername().length() == 0||
                newAccount.getPassword() == null || newAccount.getPassword().length() == 0
                || repeatedPassword == null || repeatedPassword.length() == 0){
            String msg = "The password cannot be empty";
            model.addAttribute("msg", msg);
            return "account/new_account";
        }else if (!newAccount.getPassword().equals(repeatedPassword)) {
            String msg = "The two passwords don't match";
            model.addAttribute("msg", msg);
            return "account/new_account";
        }else {
            accountService.insertAccount(newAccount);
            newAccount = accountService.getAccount(newAccount.getUsername());
            List<Product> myList = catalogService.getProductListByCategory(newAccount.getFavouriteCategoryId());
            boolean authenticated = true;
            model.addAttribute("account", newAccount);
            model.addAttribute("myList", myList);
            model.addAttribute("authenticated", authenticated);
            return "/catalog/main";
        }
    }
}
