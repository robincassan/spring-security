package com.example.demo.controller;

import com.example.demo.models.JobOffer;
import com.example.demo.models.UserApp;
import com.example.demo.repositories.JobOfferRepository;
import com.example.demo.repositories.UserAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/jobs")
public class JobOfferController {

    @Autowired
    private JobOfferRepository jobOfferRepository;

    @Autowired
    private UserAppRepository userAppRepository;

    // 1. Accessible à tous : lister les offres
    @GetMapping
    public List<JobOffer> getAll() {
        return jobOfferRepository.findAll();
    }

    // 2. Accessible aux connectés : ajouter une offre
    @PostMapping
    public ResponseEntity<?> createJob(@RequestBody JobOffer job, @AuthenticationPrincipal UserApp user) {
        job.setCreator(user);
        return ResponseEntity.ok(jobOfferRepository.save(job));
    }

    // 3. Supprimer une offre
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Long id, @AuthenticationPrincipal UserApp currentUser) {
        try {
            JobOffer job = jobOfferRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Job offer with ID " + id + " not found"));
            if (job.getCreator().getUsername().equals(currentUser.getUsername())
                    || currentUser.getRole().equals("ADMIN")) {
                jobOfferRepository.delete(job);
                return ResponseEntity.ok("Job deleted");
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden: You do not have permission to delete this job");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}