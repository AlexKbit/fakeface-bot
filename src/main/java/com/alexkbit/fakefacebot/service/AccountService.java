package com.alexkbit.fakefacebot.service;

import com.alexkbit.fakefacebot.model.Account;
import com.alexkbit.fakefacebot.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Locale;

@Slf4j
@Service
@Transactional
public class AccountService {

    @Autowired
    private AccountRepository repository;

    public Account registration(Integer id, String login, String firstName, String lastName, String langCode, Long chatId) {
        Account account = new Account();
        account.setAccountId(id);
        account.setLogin(login);
        account.setFirstName(firstName);
        account.setLastName(lastName);
        if (StringUtils.isEmpty(langCode)) {
            account.setLangCode(Locale.ENGLISH.getLanguage());
        }
        account.setLangCode(langCode);
        account.setChatId(chatId);
        account.setScore(0L);
        Account result = repository.save(account);
        log.info("Registered: {}", result);
        return result;
    }

    public Account update(Account account) {
        return repository.save(account);
    }

    public Account getByAccountId(Integer accountId) {
        return repository.findByAccountId(accountId);
    }

    public long getPosition(Account account) {
        return repository.countByScoreGreaterThanEqualAndSpendTimeLessThanAndTimestampLessThan(
                account.getScore(), account.getSpendTime(), account.getTimestamp());
    }

    public long getTotal() {
        return repository.count();
    }

    public long getTotalFinished() {
        return repository.countByFinishedTrue();
    }

    public List<Account> getTop() {
        return repository.findTop10ByFinishedTrueOrderByScoreDescSpendTimeAscTimestampAsc();
    }

}
