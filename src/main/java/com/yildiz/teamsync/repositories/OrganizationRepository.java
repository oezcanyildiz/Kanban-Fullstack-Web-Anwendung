package com.yildiz.teamsync.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yildiz.teamsync.entities.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long>{
	
	Optional<Organization> findByinvitationCode(String invitationCode);
	
	Optional<Organization> findByOrganizationEmail(String organizationEmail);

}
