package org.csu.mypetstore.persistence;

import org.csu.mypetstore.domain.Account;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountMapper {

    Account getAccountByUsername(String username);

    //用于验证登陆信息
    Account getAccountByUsernameAndPassword(Account account);

    void insertAccount(Account account);

    void insertProfile(Account account);

    void insertLogin(Account account);

    void updateAccount(Account account);

    void updateProfile(Account account);

    void updateLogin(Account account);
}
