package com.abc.complaints.repository.mock;

import com.abc.complaints.entity.Complaint;
import com.abc.complaints.entity.ComplaintStatus;
import com.abc.complaints.entity.Person;
import com.abc.complaints.repository.ComplaintRepository;
import com.abc.complaints.repository.PersonRepository;

import java.util.List;
import java.util.stream.Collectors;

public class MockComplaintRepository extends AbstractMockRepository<Complaint> implements ComplaintRepository {
    @Override
    public Class<Complaint> getType() {
        return Complaint.class;
    }

    @Override
    protected String getId(Complaint entity) {
        return entity.getId();
    }

    @Override
    public List<Complaint> findByPersonId(String personId) {
        return entities.stream().filter(entity -> entity.getPersonId().equals(personId)).collect(Collectors.toList());
    }

    @Override
    public List<Complaint> findByStatus(ComplaintStatus status) {
        return entities.stream().filter(entity -> entity.getStatus().equals(status)).collect(Collectors.toList());
    }
}
