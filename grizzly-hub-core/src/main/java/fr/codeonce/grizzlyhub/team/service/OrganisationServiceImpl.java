package fr.codeonce.grizzlyhub.team.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.codeonce.grizzlyhub.auth.service.util.SecurityContextUtil;
import fr.codeonce.grizzlyhub.team.domain.MemberRepository;
import fr.codeonce.grizzlyhub.team.domain.Membre;
import fr.codeonce.grizzlyhub.team.domain.Organisation;
import fr.codeonce.grizzlyhub.team.domain.OrganisationRepository;
import fr.codeonce.grizzlyhub.team.domain.TeamRepository;

@Service
public class OrganisationServiceImpl implements IOrganisationService {

	@Autowired
	OrganisationRepository organisationRepository;
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	TeamRepository teamRepository;
	@Autowired
	IMembreService memberService;

	@Override
	public Organisation saveOrganisation(Organisation organisation) {
        String currentEmail = SecurityContextUtil.getCurrentUserEmail();
        organisation.setTotalMembers(1L);
        organisation.setTotalTeams(0L);
        Organisation savedOrganisation = organisationRepository.save(organisation); 
        memberService.saveMembre(new Membre(currentEmail, "admin", null, savedOrganisation.getId()));
		return savedOrganisation;
	}
	

	@Override
	public List<Organisation> findAllOrganisations() {
		List<Organisation> organisations = organisationRepository.findAll();
		organisations.forEach(organisation -> {
			organisation.setTotalMembers(memberRepository.countByOrganisationId(organisation.getId()));
			organisation.setTotalTeams(teamRepository.countByOrganisationId(organisation.getId()));
		});
		return organisations;
	}

	@Override
	public Organisation findById(String id) {
		Organisation organisation = organisationRepository.findById(id).get();
		organisation.setTotalMembers(memberRepository.countByOrganisationId(organisation.getId()));
		organisation.setTotalTeams(teamRepository.countByOrganisationId(organisation.getId()));
		 return organisation;
	}

	@Override
	public Organisation updateOrganisation(Organisation organisation, String id) {
		organisation.setId(id);
		return organisationRepository.save(organisation);
	}

	@Override
	public void deleteOrganisation(String id) {
		memberService.deletAllMemebersOfOrganisation(id);
		organisationRepository.deleteById(id);
	}

}
