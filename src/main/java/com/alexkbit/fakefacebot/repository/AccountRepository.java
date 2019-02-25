package com.alexkbit.fakefacebot.repository;

import com.alexkbit.fakefacebot.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface AccountRepository extends MongoRepository<Account, Integer> {

    Account findByAccountId(Integer id);

    List<Account> findTop10ByOrderByScoreDescTimestampAsc();

    long countByScoreGreaterThanEqualAndTimestampLessThan(Long score, Date timestamp);

    long countByFinishedTrue();
}
