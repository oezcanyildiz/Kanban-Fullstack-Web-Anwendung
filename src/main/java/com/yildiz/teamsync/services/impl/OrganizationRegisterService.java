package com.yildiz.teamsync.services.impl;

import org.springframework.stereotype.Service;

import com.yildiz.teamsync.dto.OrganizationRequestDTO;
import com.yildiz.teamsync.dto.OrganizationResponseDTO;
import com.yildiz.teamsync.entities.Organization;
import com.yildiz.teamsync.exceptions.ConflictException;
import com.yildiz.teamsync.repositories.OrganizationRepository;
import com.yildiz.teamsync.services.IOrganizationRegisterService;

@Service
public class OrganizationRegisterService implements IOrganizationRegisterService {
	
	private final OrganizationRepository organizationRepository;
	public OrganizationRegisterService (OrganizationRepository organizationRepository) {
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
			throw new ConflictException("Mit dieser Email existiert schon ein Organization.");
		}
		Organization organization = new Organization();
		organization.setOrganizationName(requestdto.getOrganizationName());
		organization.setOrganizationEmail(requestdto.getOrganizationEmail());
		
		// Wir nehmen die ersten 3 Buchstaben der Firma + eine Zufallszahl (oder UUID)
		String cleanName = organization.getOrganizationName().replaceAll("\\s+", "").toUpperCase();
		String prefix = cleanName.length() >= 3 ? cleanName.substring(0, 3) : cleanName;
		String generatedCode = prefix + "-" + java.util.UUID.randomUUID().toString().substring(0, 5).toUpperCase();
				
		organization.setInvitationCode(generatedCode);
		
		Organization savedOrganization = organizationRepository.save(organization);
		
		OrganizationResponseDTO responseDTO = new OrganizationResponseDTO();
		responseDTO.setOrganizationName(savedOrganization.getOrganizationName());
		responseDTO.setOrganizationEmail(savedOrganization.getOrganizationEmail());
		responseDTO.setInvitationCode(savedOrganization.getInvitationCode());
		
		return responseDTO;
	}
}
