package com.Genpact.Emp_System.controller;

import com.Genpact.Emp_System.entity.Candidate;
import com.Genpact.Emp_System.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/candidates")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @PostMapping("/addCandidate")
    public String addCandidate(@RequestParam String fullName,
                               @RequestParam String dateOfBirth,
                               @RequestParam String nationality,
                               @RequestParam String partyName,
                               @RequestParam String partyLogo) {
        Candidate candidate = new Candidate();
        candidate.setFullName(fullName);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDateOfBirth = LocalDate.parse(dateOfBirth, formatter);
        candidate.setDateOfBirth(localDateOfBirth);

        candidate.setNationality(nationality);
        candidate.setPartyName(partyName);
        candidate.setPartyLogo(partyLogo);
        candidate.setNumberOfVotes(0);

        candidateService.saveCandidate(candidate);

        return "redirect:/candidates/index";
    }

    @GetMapping("/addcandidate")
    public String addCandidateForm(Model model) {
        model.addAttribute("candidate", new Candidate());
        return "addcandidate";
    }

    @PostMapping("/addcandidate")
    public String registerCandidate(@ModelAttribute Candidate candidate, RedirectAttributes redirectAttributes) {
        LocalDate today = LocalDate.now();
        LocalDate birthDate = candidate.getDateOfBirth();

        if (birthDate == null) {
            redirectAttributes.addFlashAttribute("error", "Birthdate is required.");
            return "redirect:/candidates/addcandidate";
        }

        int age = Period.between(birthDate, today).getYears();

        if (birthDate.isAfter(today)) {
            redirectAttributes.addFlashAttribute("error", "Birthdate cannot be in the future.");
            return "redirect:/candidates/addcandidate";
        }

        if (age < 35) {
            redirectAttributes.addFlashAttribute("error", "Candidate must be at least 35 years old.");
            return "redirect:/candidates/addcandidate";
        }

        candidateService.addCandidate(candidate);
        redirectAttributes.addFlashAttribute("message", "Candidate registered successfully.");
        return "redirect:/candidates/adminpage";
    }

    @GetMapping("/adminpage")
    public String getCandidates(Model model) {
        List<Candidate> candidates = candidateService.getAllCandidates();
        model.addAttribute("candidates", candidates);
        return "adminpage";
    }

    @GetMapping("/index")
    public String getCandidatesForIndex(Model model) {
        List<Candidate> candidates = candidateService.getAllCandidates();
        model.addAttribute("candidates", candidates);
        return "index";
    }

    @GetMapping("/confirmVote/{id}")
    public String confirmVoteForm(@PathVariable("id") Long candidateId, Model model) {
        model.addAttribute("candidateId", candidateId);
        return "confirm_vote";
    }

    @GetMapping("/delete/{id}")
    public String deleteCandidates(@PathVariable("id") Long candidateId, HttpSession session) {
        candidateService.deleteCandidate(candidateId);
        session.setAttribute("msg", "Candidate deleted successfully!");
        return "redirect:/candidates/adminpage";
    }

    @GetMapping("/candidates/vote/{id}")
    public String voteForCandidate(@PathVariable Long id) {
        candidateService.incrementVoteCount(id);
        return "redirect:/candidates/index";
    }

    @GetMapping("/editcandidate/{id}")
    public String editCandidateForm(@PathVariable("id") Long candidateId, Model model) {
        Candidate candidate = candidateService.getCandidateById(candidateId);
        model.addAttribute("candidate", candidate);
        return "edit_candidate";
    }

    @PostMapping("/updatecandidate")
    public String updateCandidate(@ModelAttribute Candidate candidate) {
        Long id = candidate.getId();
        candidateService.updateCandidate(id, candidate);
        return "redirect:/candidates/adminpage";
    }


}
