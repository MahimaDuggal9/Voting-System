package com.Genpact.Vote_System.service;

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
    private UserRepository userRepo; // Repository to manage User data

    public void addCandidate(Candidate candidate) {
        System.out.println("Data for SQL: " + candidate);
        candidateRepo.save(candidate);
    }

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

    public boolean updateCandidate(Long id, Candidate updatedCandidate) {
        Optional<Candidate> existingCandidateOptional = candidateRepo.findById(id);
        if (existingCandidateOptional.isPresent()) {
            Candidate existingCandidate = existingCandidateOptional.get();
            existingCandidate.setFullName(updatedCandidate.getFullName());
            existingCandidate.setPartyName(updatedCandidate.getPartyName());
            existingCandidate.setPartyLogo(updatedCandidate.getPartyLogo());
            candidateRepo.save(existingCandidate);
            return true;
        } else {
            return false;
        }
    }

    public void deleteCandidate(Long candidateId) {
        if (candidateRepo.existsById(candidateId)) {
            System.out.println("Deleting candidate with ID: " + candidateId);
            candidateRepo.deleteById(candidateId);
        } else {
            throw new RuntimeException("Candidate with ID " + candidateId + " not found");
        }
    }

    public int getTotalVotes() {
        List<Candidate> candidates = candidateRepo.findAll();
        return candidates.stream().mapToInt(Candidate::getNumberOfVotes).sum();
    }


    public boolean hasUserVoted(Long userId) {
        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.isHasVoted(); // Assuming `User` entity has `hasVoted` boolean field
        }
        throw new RuntimeException("User with ID " + userId + " not found");
    }

    @Transactional
    public boolean voteForCandidate(Long candidateId, Long userId) {
        // Fetch user by ID
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if the user has already voted
        if (user.isHasVoted()) {
            throw new RuntimeException("User has already voted");
        }

        // Retrieve the candidate
        Optional<Candidate> candidateOptional = candidateRepo.findById(candidateId);
        if (candidateOptional.isPresent()) {
            Candidate candidate = candidateOptional.get();

            // Increment the vote count for the candidate
            candidate.setNumberOfVotes(candidate.getNumberOfVotes() + 1);
            candidateRepo.save(candidate);

            // Mark the user as having voted
            user.setHasVoted(true);
            System.out.println("User before save: " + user);  // Debugging output

            // Save the user entity
            userRepo.save(user);

            return true;
        }
        return false;
    }


    public void incrementVoteCount(Long candidateId) {
        Candidate candidate = candidateRepo.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
        candidate.setNumberOfVotes(candidate.getNumberOfVotes() + 1);
        candidateRepo.save(candidate);
    }

    public Candidate saveCandidate(Candidate candidate) {
        return candidateRepo.save(candidate);
    }

    public Candidate findById(Long id) {
        return candidateRepo.findById(id).orElse(null);
    }
}
