package com.Genpact.Vote_System.service;

import com.Genpact.Vote_System.entity.Candidate;
import com.Genpact.Vote_System.entity.User;
import com.Genpact.Vote_System.repository.CandidateRepository;
import com.Genpact.Vote_System.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VotingService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private UserRepository userRepository;

    public void voteForCandidate(Long candidateId, User user) {
        Optional<Candidate> candidateOptional = candidateRepository.findById(candidateId);
        if (candidateOptional.isPresent()) {
            Candidate candidate = candidateOptional.get();
            if (!user.getVotedCandidateIds().contains(candidateId)) {
                candidate.setNumberOfVotes(candidate.getNumberOfVotes() + 1);
                candidateRepository.save(candidate);
                user.getVotedCandidateIds().add(candidateId);
                user.incrementVoteCount();
                userRepository.save(user);
            } else {
                throw new IllegalStateException("You have already voted for this candidate.");
            }
        } else {
            throw new IllegalArgumentException("Candidate not found.");
        }
    }
}
