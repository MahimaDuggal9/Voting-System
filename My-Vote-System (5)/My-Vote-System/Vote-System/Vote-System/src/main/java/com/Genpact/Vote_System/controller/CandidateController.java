package com.Genpact.Vote_System.controller;

import com.Genpact.Vote_System.entity.Candidate;
import com.Genpact.Vote_System.entity.User;
import com.Genpact.Vote_System.service.CandidateService;
import com.Genpact.Vote_System.service.UserService;
import com.Genpact.Vote_System.service.VotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;



@Controller
@RequestMapping("/candidates")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private VotingService votingService;

    @Autowired
    private UserService userService;

    @GetMapping("/addcandidate")
    public String addCandidateForm(Model model) {
        model.addAttribute("candidate", new Candidate());
        return "addcandidate";
    }

    @PostMapping("/addcandidate")
    public String registerCandidate(@RequestParam String fullName,
                                    @RequestParam String dateOfBirth,
                                    @RequestParam String nationality,
                                    @RequestParam String partyName,
                                    @RequestParam String partyLogo,
                                    RedirectAttributes redirectAttributes) {

        LocalDate localDateOfBirth;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            localDateOfBirth = LocalDate.parse(dateOfBirth, formatter);
        } catch (DateTimeParseException e) {
            redirectAttributes.addFlashAttribute("error", "Invalid Date of Birth format.");
            return "redirect:/candidates/addcandidate";
        }

        Candidate candidate = new Candidate();
        candidate.setFullName(fullName);
        candidate.setDateOfBirth(localDateOfBirth);
        candidate.setNationality(nationality);
        candidate.setPartyName(partyName);
        candidate.setPartyLogo(partyLogo);
        candidate.setNumberOfVotes(0);

        candidateService.saveCandidate(candidate);
        redirectAttributes.addFlashAttribute("message", "Candidate registered successfully.");
        return "redirect:/candidates/index";
    }


    @GetMapping("/adminpage")
    public String getCandidates(Model model) {
        List<Candidate> candidates = candidateService.getAllCandidates();
        Long totalVotes = candidateService.getTotalVotes();
        candidates.sort(Comparator.comparing(Candidate::getFullName));
        model.addAttribute("candidates", candidates);
        model.addAttribute("totalVotes", totalVotes);
        return "adminpage";
    }

    @GetMapping("/index")
    public String getCandidatesForIndex(Model model) {
        List<Candidate> candidates = candidateService.getAllCandidates();
        Long totalVotes = candidateService.getTotalVotes();
        candidates.sort(Comparator.comparing(Candidate::getFullName));
        model.addAttribute("candidates", candidates);
        model.addAttribute("totalVotes", totalVotes);
        return "index";
    }


    @GetMapping("/delete/{id}")
    public String deleteCandidates(@PathVariable("id") Long candidateId, HttpSession session) {
        Candidate candidate = candidateService.getCandidateById(candidateId);


        Long totalVotes = candidateService.getTotalVotes();


        if (candidate.getNumberOfVotes() < (totalVotes / 2)) {
            candidateService.deleteCandidate(candidateId);
            session.setAttribute("msg", "Candidate deleted successfully!");
        } else {
            session.setAttribute("msg", "Cannot delete candidate as they have received more than half of the total votes.");
        }

        return "redirect:/candidates/adminpage";
    }



    @PostMapping("/vote/{id}")
    public String voteForCandidates(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String aadharNumber = authentication.getName();


            User user = userService.findByAadharNumber(aadharNumber);
            if (user.getVotecount() >= 1) {
                redirectAttributes.addFlashAttribute("message", "You have already voted.");
                return "redirect:/candidates/index"; // Redirect to the index page
            }

            candidateService.voteForCandidate(id);


            userService.incrementVoteCount(user.getId());


            Candidate candidate = candidateService.getCandidateById(id);


            redirectAttributes.addFlashAttribute("candidate", candidate);
            redirectAttributes.addFlashAttribute("user", user); // Add user details
            redirectAttributes.addFlashAttribute("message", "Vote recorded successfully");


            return "redirect:/candidates/success";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while recording your vote.");
            return "redirect:/error";
        }
    }



    @GetMapping("/success")
    public String voteSuccess(Model model, HttpServletRequest request) {

        return "success";
    }


    @GetMapping("/edit/{id}")
    public String showEditPage(@PathVariable("id") long id, Model model) {
        Candidate candidate = candidateService.findById(id);
        model.addAttribute("candidate", candidate);
        return "editcandidate";
    }

    @PostMapping("/update")
    public String updateCandidate(@ModelAttribute @Valid Candidate candidate, BindingResult result) {
        if (result.hasErrors()) {
            return "editcandidate";
        }
        candidateService.updateCandidate(candidate);
        return "redirect:/candidates/adminpage";
    }





}
