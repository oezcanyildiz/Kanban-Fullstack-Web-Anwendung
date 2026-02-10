package com.yildiz.teamsync.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.yildiz.teamsync.entities.Organization;
import com.yildiz.teamsync.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUserEmail(String userEmail);
	
	long countByOrganization(Organization organization);

    List<User> findAllByOrganization_OrganizationID(Long organizationID);

    boolean existsByUserIDAndOrganization_OrganizationID(Long userID, Long organizationID);

}
