package fr.codeonce.grizzlyhub.team.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.codeonce.grizzlyhub.auth.service.util.SecurityContextUtil;
import fr.codeonce.grizzlyhub.team.domain.MemberRepository;
import fr.codeonce.grizzlyhub.team.domain.Membre;
import fr.codeonce.grizzlyhub.team.domain.Organisation;
import fr.codeonce.grizzlyhub.team.domain.OrganisationRepository;
import fr.codeonce.grizzlyhub.team.domain.Team;
import fr.codeonce.grizzlyhub.team.domain.TeamRepository;
import fr.codeonce.grizzlyhub.team.service.IMembreService;
import fr.codeonce.grizzlyhub.team.service.IOrganisationService;

@RestController
@RequestMapping("/api/organisation")
public class OrganisationController {

	private static final Logger log = LoggerFactory.getLogger(OrganisationController.class);
	@Autowired
	IOrganisationService organisationService;
	@Autowired
	OrganisationRepository organisationRepository;
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	TeamRepository teamRepository;
	@Autowired
	IMembreService memberService;

	@GetMapping("/all")
	public List<Organisation> getAllOrganisations() {
		return organisationService.findAllOrganisations();
	}

	@GetMapping("/find/{id}")
	public Organisation getOrganisation(@PathVariable String id) {
		return organisationService.findById(id);
	}

	@PostMapping("/add")
	public Organisation addOrganisation(@RequestBody Organisation organisation) {
		return organisationService.saveOrganisation(organisation);
	}

	@PutMapping("/update/{id}")
	public Organisation updateOrganisation(@RequestBody Organisation organisation, @PathVariable String id) {
		return organisationService.updateOrganisation(organisation, id);
	}

	@DeleteMapping("/delete/{id}")
	public void deleteOrganisation(@PathVariable String id) {
		organisationService.deleteOrganisation(id);
	}

	@GetMapping("/only/{name}")
	public Boolean onlyOrganisation(@PathVariable String name) {
		return organisationRepository.existsByNameIgnoreCase(name);
	}
	
	@GetMapping("onlyOrganisation/{email}")
	public Boolean onlyOrganisationByEmail(@PathVariable String email) {
		return memberService.checkMembreInOrganisation(email);
	}
	@GetMapping("onlyOrganisationAndEmail/{organisationId}/{email}")
	public Boolean onlyOrganisationByEmailAndOrganisationID(@PathVariable String organisationId ,@PathVariable String email) {
		return memberRepository.existsByOrganisationIdAndEmail(organisationId, email);
	}
	@GetMapping("/myOrganisationTeams")
	public List<Team> teamsOfMyOrganisation() {
		return teamRepository.findByOrganisationId(memberRepository.findByEmail(SecurityContextUtil.getCurrentUserEmail()).getOrganisationId());
	}
}
