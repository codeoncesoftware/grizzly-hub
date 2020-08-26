package fr.codeonce.grizzlyhub.team.service;

import java.util.List;

import fr.codeonce.grizzlyhub.team.domain.Organisation;

public interface IOrganisationService {
	public Organisation saveOrganisation(Organisation organisation);
	public List<Organisation> findAllOrganisations();
	public Organisation findById(String id);
	public Organisation updateOrganisation(Organisation organisation , String id);	
	public void deleteOrganisation(String id);
}
