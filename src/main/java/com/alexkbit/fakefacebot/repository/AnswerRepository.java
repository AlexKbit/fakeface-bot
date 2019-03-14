package com.alexkbit.fakefacebot.repository;

import com.alexkbit.fakefacebot.model.Answer;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AnswerRepository extends MongoRepository<Answer, ObjectId> {
}
