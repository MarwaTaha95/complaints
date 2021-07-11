package com.abc.complaints.repository;

import com.abc.complaints.entity.Complaint;
import com.abc.complaints.entity.ComplaintStatus;

import java.util.List;

public interface ComplaintRepository extends Repository<Complaint, String> {
    List<Complaint> findByPersonId(String personId);

    List<Complaint> findByStatus(ComplaintStatus status);
}
