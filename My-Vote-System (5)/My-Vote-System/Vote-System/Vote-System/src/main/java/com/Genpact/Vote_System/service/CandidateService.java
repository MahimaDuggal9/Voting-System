package com.Genpact.Vote_System.service;

import com.Genpact.Vote_System.dto.CandidateDto;
import com.Genpact.Vote_System.entity.Candidate;
import com.Genpact.Vote_System.entity.User; // Assuming you have a User entity
import com.Genpact.Vote_System.repository.CandidateRepository;
import com.Genpact.Vote_System.repository.UserRepository; // Assuming you have a User repository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepo;

    @Autowired
    private UserRepository userRepo;


    public List<Candidate> getAllCandidates() {
        List<Candidate> candidates = candidateRepo.findAll();
        System.out.println("Data from SQL: " + candidates);
        return candidates;
    }

    public Candidate getCandidateById(Long id) {
        Optional<Candidate> candidate = candidateRepo.findById(id);
        System.out.println("Candidate with ID " + id + ": " + candidate.orElse(null));
        return candidate.orElse(null);
    }


    public void deleteCandidate(Long candidateId) {
        if (candidateRepo.existsById(candidateId)) {
            System.out.println("Deleting candidate with ID: " + candidateId);
            candidateRepo.deleteById(candidateId);
        } else {
            throw new RuntimeException("Candidate with ID " + candidateId + " not found");
        }
    }



    public Candidate saveCandidate(Candidate candidate) {

        return candidateRepo.save(candidate);
    }

    public Candidate findById(Long id) {

        return candidateRepo.findById(id).orElse(null);
    }


    public void updateCandidate(Candidate candidate) {

        Optional<Candidate> existingCandidateOpt = candidateRepo.findById(candidate.getId());

        if (existingCandidateOpt.isPresent()) {
            Candidate existingCandidate = existingCandidateOpt.get();

            existingCandidate.setFullName(candidate.getFullName());
            existingCandidate.setDateOfBirth(candidate.getDateOfBirth());
            existingCandidate.setPartyName(candidate.getPartyName());
            existingCandidate.setPartyLogo(candidate.getPartyLogo());

            candidateRepo.save(existingCandidate);
        } else {
            throw new RuntimeException("Candidate not found");
        }
    }

    public void voteForCandidate(Long id) {
        Candidate candidate = candidateRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
        candidate.setNumberOfVotes(candidate.getNumberOfVotes() + 1);
        candidateRepo.save(candidate);

    }
    public Long getTotalVotes() {
        // Assuming you have a repository for candidates
        return candidateRepo.findAll()
                .stream()
                .mapToLong(Candidate::getNumberOfVotes)
                .sum();
    }

}
