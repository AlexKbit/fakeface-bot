package com.alexkbit.fakefacebot.repository;

import com.alexkbit.fakefacebot.model.Account;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface AccountRepository extends MongoRepository<Account, ObjectId> {

    Account findByAccountId(Integer id);

    List<Account> findTop10ByFinishedTrueOrderByScoreDescSpendTimeAscTimestampAsc();

    long countByScoreGreaterThanOrScoreGreaterThanEqualAndSpendTimeLessThan(Long score1, Long score, Long spendTime);

    long countByFinishedTrue();
}
