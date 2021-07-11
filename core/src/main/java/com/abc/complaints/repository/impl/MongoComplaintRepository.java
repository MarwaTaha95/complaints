package com.abc.complaints.repository.impl;


import com.abc.complaints.entity.Complaint;
import com.abc.complaints.entity.ComplaintStatus;
import com.abc.complaints.entity.Person;
import com.abc.complaints.repository.ComplaintRepository;
import com.abc.complaints.repository.PersonRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MongoComplaintRepository extends AbstractMongoRepository<Complaint> implements ComplaintRepository {
    public MongoComplaintRepository(MongoTemplate mongoTemplate) {
        super(mongoTemplate, Complaint.class);
    }

    @Override
    public Class<Complaint> getType() {
        return Complaint.class;
    }

    @Override
    public List<Complaint> findByPersonId(String personId) {
        return mongoTemplate.find(Query.query(Criteria.where("personId").is(personId)), Complaint.class);
    }

    @Override
    public List<Complaint> findByStatus(ComplaintStatus status) {
        return mongoTemplate.find(Query.query(Criteria.where("roleType").is(status.name())), Complaint.class);
    }
}
