package com.tweetapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.tweetapp.model.DbSequence;

import java.util.Objects;

@Service
public class SequenceGeneratorService {

    @Autowired
    private MongoOperations mongoOperations;

    public int getSequenceNumber(String seqName){
        //get sequence number
        Query query = new Query(Criteria.where("id").is(seqName));

        //update the sequence
        Update update = new Update().inc("seq", 1);

        //modify in document
        DbSequence counter = mongoOperations.findAndModify(query,
                update, FindAndModifyOptions.options().returnNew(true).upsert(true),
                DbSequence.class);

        return !Objects.isNull(counter) ? counter.getSeq() : 1;
    }
}
