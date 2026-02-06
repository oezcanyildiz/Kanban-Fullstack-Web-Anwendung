package com.yildiz.teamsync.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yildiz.teamsync.entities.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long>{

}
