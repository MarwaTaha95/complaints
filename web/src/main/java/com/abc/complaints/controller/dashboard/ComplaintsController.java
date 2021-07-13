package com.abc.complaints.controller.dashboard;


import com.abc.complaints.entity.*;
import com.abc.complaints.exception.entity.AlreadyAuthenticatedException;
import com.abc.complaints.repository.ComplaintRepository;
import com.abc.complaints.rest.request.ComplaintRequest;
import com.abc.complaints.rest.request.UpdateRequest;
import com.abc.complaints.service.SessionService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * This API is for retrieving and managing complaints for a user
 */
@RestController
@RequestMapping("/api/v1/rest/complaints")
public class ComplaintsController {
    private final ComplaintRepository complaints;
    private final Session session;
    private final SessionService sessions;

    public ComplaintsController(ComplaintRepository complaints, Session session, SessionService sessions) {
        this.complaints = complaints;
        this.session = session;
        this.sessions = sessions;
    }

    /**
     * Get all complaints for the user in session
     */
    @GetMapping(value = "/get")
    public Object get(HttpServletRequest httpServletRequest) throws Exception {
        Person existingPerson = sessions.getPerson(session);
        if (existingPerson == null) {
            throw new AlreadyAuthenticatedException("User not logged in");
        }

        return complaints.findByPersonId(existingPerson.getId());
    }

    /**
     * Create a new complaint for the user in session.
     *
     * @param request, complaint's data to be created
     */
    @PostMapping(value = "/create")
    public Object create(@RequestBody ComplaintRequest request, HttpServletRequest httpServletRequest) throws Exception {
        Person existingPerson = sessions.getPerson(session);
        if (existingPerson == null) {
            throw new AlreadyAuthenticatedException("User not logged in");
        }

        Complaint complaint = new Complaint();
        complaint.setTitle(request.getTitle());
        complaint.setDescription(request.getDescription());
        complaint.setPersonId(existingPerson.getId());
        complaint.setPriority(Priority.getByCode(request.getPriority()));
        complaint.setClient(Client.getByCode(request.getClient()));
        complaint.setStatus(ComplaintStatus.PENDING);
        complaints.save(complaint);
        return complaint;
    }

    /**
     * Get all pending complaints, from all users.
     * Accessible for admins only
     */
    @GetMapping(value = "/getAll")
    public Object getAll(HttpServletRequest httpServletRequest) throws Exception {
        Person existingPerson = sessions.getPerson(session);
        if (existingPerson == null || !existingPerson.getRoleType().equals(RoleType.ADMIN)) {
            throw new AlreadyAuthenticatedException("User cannot access this info");
        }

        return complaints.findByStatus(ComplaintStatus.PENDING);
    }

    /**
     * Update the status of a complaint.
     * Only accessible by admins
     */
    @PostMapping(value = "/update")
    public Object update(@RequestBody UpdateRequest request, HttpServletRequest httpServletRequest) throws Exception {
        Person existingPerson = sessions.getPerson(session);
        if (existingPerson == null || !existingPerson.getRoleType().equals(RoleType.ADMIN)) {
            throw new AlreadyAuthenticatedException("User cannot access this info");
        }

        Complaint complaint = complaints.findById(request.getId()).orElse(null);
        if (complaint != null) {
            complaint.setStatus(ComplaintStatus.getByCode(request.getStatus()));
            complaints.save(complaint);
        }
        return complaint;
    }
}
