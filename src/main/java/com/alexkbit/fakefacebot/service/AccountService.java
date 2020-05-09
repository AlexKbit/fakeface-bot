package com.alexkbit.fakefacebot.service;

import com.alexkbit.fakefacebot.model.Account;
import com.alexkbit.fakefacebot.model.Answer;
import com.alexkbit.fakefacebot.model.AnswerCount;
import com.alexkbit.fakefacebot.repository.AccountRepository;
import com.alexkbit.fakefacebot.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;
    private final AnswerRepository answerRepository;
    private final MongoTemplate mongoTemplate;

    public Account registration(Integer id, String login, String firstName, String lastName, String langCode, Long chatId) {
        Account account = new Account();
        account.setAccountId(id);
        account.setLogin(login);
        account.setFirstName(firstName);
        account.setLastName(lastName);
        account.setLangCode(Locale.ENGLISH.getLanguage());
        if ("en".equals(langCode) || "ru".equals(langCode)) {
            account.setLangCode(langCode);
        }
        account.setChatId(chatId);
        account.setScore(0L);
        Account result = repository.save(account);
        log.info("Registered: {}", result);
        return result;
    }

    public Account update(Account account) {
        answerRepository.saveAll(account.getAnswers());
        return repository.save(account);
    }

    public Account getByAccountId(Integer accountId) {
        return repository.findByAccountId(accountId);
    }

    public long getPosition(Account account) {
        return repository.countByScoreGreaterThanOrScoreGreaterThanEqualAndSpendTimeLessThan(
                account.getScore(), account.getScore(), account.getSpendTime());
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

    public List<AnswerCount> getTopIncorrectAnswer() {
        Aggregation agg = newAggregation(
                match(Criteria.where("valid").is(false)),
                group("qId").count().as("total"),
                project("total").and("qId").previousOperation(),
                sort(Sort.Direction.DESC, "total")
        );
        AggregationResults<AnswerCount> groupResults = mongoTemplate.aggregate(agg, Answer.class, AnswerCount.class);
        return groupResults.getMappedResults();
    }

    public Page<Account> getPage(int pageNumber, int pageSize) {
        return repository.findAll(PageRequest.of(pageNumber, pageSize));
    }


}
