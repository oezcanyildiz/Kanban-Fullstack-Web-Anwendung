package com.yildiz.teamsync.services.impl;

import org.springframework.stereotype.Service;

import com.yildiz.teamsync.dto.OrganizationRequestDTO;
import com.yildiz.teamsync.dto.OrganizationResponseDTO;
import com.yildiz.teamsync.entities.Organization;
import com.yildiz.teamsync.mappers.OrganizationMapper;
import com.yildiz.teamsync.repositories.OrganizationRepository;
import com.yildiz.teamsync.services.IOrganizationRegisterService;

@Service
public class OrganizationRegisterService implements IOrganizationRegisterService {
	
	private final OrganizationRepository organizationRepository;
	private final OrganizationMapper organizationMapper;
	

	public OrganizationRegisterService (OrganizationRepository organizationRepository, OrganizationMapper organizationMapper) {
		this.organizationMapper=organizationMapper;
		this.organizationRepository= organizationRepository;
	};
	
	///////////////////////////////
    ///							///
    ///     	REGISTER  		///
    ///							///
    ///////////////////////////////
	@Override
	public OrganizationResponseDTO registerOrganization(OrganizationRequestDTO requestdto) {
		String requestEmail=requestdto.getOrganizationEmail().toLowerCase().trim();
		
		if(organizationRepository.findByOrganizationEmail(requestEmail).isPresent()){
			throw new IllegalArgumentException("Mit dieser Email existiert schon ein Organization.");
		}
		Organization organization = organizationMapper.toEntity(requestdto);
		
		// Wir nehmen die ersten 3 Buchstaben der Firma + eine Zufallszahl (oder UUID)
		String cleanName = organization.getOrganizationName().replaceAll("\\s+", "").toUpperCase();
		String prefix = cleanName.length() >= 3 ? cleanName.substring(0, 3) : cleanName;
		String generatedCode = prefix + "-" + java.util.UUID.randomUUID().toString().substring(0, 5).toUpperCase();
				
		organization.setInvitationCode(generatedCode);
		
		Organization savedOrganization = organizationRepository.save(organization);
		
		return organizationMapper.toRegisterResponse(savedOrganization);
	}
}
