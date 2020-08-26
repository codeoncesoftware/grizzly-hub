package fr.codeonce.grizzlyhub.team.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fr.codeonce.grizzlyhub.microservices.domain.Microservice;
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
import fr.codeonce.grizzlyhub.microservices.service.SubscriptionService;
import fr.codeonce.grizzlyhub.notification.service.NotificationServiceImpl;
import fr.codeonce.grizzlyhub.team.domain.MemberRepository;
import fr.codeonce.grizzlyhub.team.domain.Membre;
import fr.codeonce.grizzlyhub.team.domain.Organisation;
import fr.codeonce.grizzlyhub.team.domain.Team;
import fr.codeonce.grizzlyhub.team.service.IMembreService;

@RestController
@RequestMapping("/api/member")
public class MemberController {

	@Autowired
	IMembreService memberService;

	private static final Logger log = LoggerFactory.getLogger(MemberController.class);

	@GetMapping("/teamHub")
	public Set<Microservice> getAllMicroservicesInTeamHub(){
		String currentEmail = SecurityContextUtil.getCurrentUserEmail();
		Membre membre = memberService.findMemberByEmail(currentEmail);
		List<Membre> members = memberService.findTeamsMembers(membre.getTeamIds());
		return memberService.findAllMembersSubscriptions(members);
	}

	@GetMapping("/all")
	public List<Membre> getAllMemebers() {
		return memberService.findAllMembres();
	}

	@GetMapping("/find/{id}")
	public Membre getMemeber(@PathVariable String id) {
		return memberService.findById(id);
	}

	@GetMapping("/findByOrganisation/{id}")
	public List<Membre> getMemeberByOrganisation(@PathVariable String id) {
		return memberService.findByOrganisationId(id);
	}

	@GetMapping("/findByTeam/{id}")
	public List<Membre> getMemeberByTeam(@PathVariable String id) {
		return memberService.findByTeamId(id);
	}

	@PostMapping("/add")
	public Membre addMember(@RequestBody Membre membre) {
		return memberService.saveMembre(membre);
	}

	@PutMapping("/update/{id}")
	public Membre updateMember(@RequestBody Membre membre, @PathVariable String id) {
		return memberService.updateMembre(membre, id);
	}
	
	@DeleteMapping("delete/{id}")
	public void deleteMemeber(@PathVariable String id) {
		memberService.deleteMembre(id);
	}

	@GetMapping("/currentUserOrganisation")
	public List<Organisation> getCurrentUserOrganisation() throws Exception {
		String currentEmail = SecurityContextUtil.getCurrentUserEmail();
		
		try {
			log.info("test " + memberService.findOrganisationByEmail(currentEmail));
			return memberService.findOrganisationByEmail(currentEmail);
		} catch (Exception e) {
			return new ArrayList<Organisation>();
		}
	}

	@GetMapping("/currentUserTeam")
	public List<Team> getCurrentUserTeam() throws Exception {
		String currentEmail = SecurityContextUtil.getCurrentUserEmail();
		try {
			return memberService.findTeamByEmail(currentEmail);
		} catch (Exception e) {
			return new ArrayList<Team>();
		}
	}
	
	@GetMapping("/currentUserIsAdmin")
	public boolean checkUserIsAdmin() throws Exception {
		String currentEmail = SecurityContextUtil.getCurrentUserEmail();
		try {
			return memberService.checkUserIsAdmin(currentEmail);
		} catch (Exception e) {
			return false;
		}
	}

	@DeleteMapping("/deleteAllMemebers/organisationId")
	public void deleteAllMEmebers(String organisationId) {
		memberService.deletAllMemebersOfOrganisation(organisationId);
	}
	
	@GetMapping("/membreExistsInMembers/{email}")
	public Boolean membreExistsInMembers(@PathVariable String email) {
		return memberService.membreExistsInMembers(email);
	}
	
	@GetMapping("/membreExistsInUsers/{email}")
	public Boolean membreExistsInUsers(@PathVariable String email) {
		return memberService.membreExistsInUsers(email);
	}
	@GetMapping("/addMemberToTeam/{teamId}/{email}")
	public Membre addMemberToTeam(@PathVariable String teamId,@PathVariable String email) {
		return memberService.addMemberToTeam(teamId, email);
	}
	

}
