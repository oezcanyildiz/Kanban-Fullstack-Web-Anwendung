package com.yildiz.teamsync.mappers;

import org.mapstruct.Mapper;

import com.yildiz.teamsync.dto.OrganizationRequestDTO;
import com.yildiz.teamsync.dto.OrganizationResponseDTO;
import com.yildiz.teamsync.entities.Organization;

@Mapper(componentModel="spring")
public interface OrganizationMapper {
	
	//Request --> Entitiy 
	Organization toEntity(OrganizationRequestDTO requestdto);
	
	//Entity ---> Response 
	OrganizationResponseDTO toRegisterResponse (Organization organization);

}
