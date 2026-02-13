package com.yildiz.teamsync.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="teams")

public class Team extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long teamID;
	
	@Column(nullable=false)
	private String teamName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="owner_id", nullable=false)
	private User owner;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="organization_id",nullable = false)
    private Organization organization;
	

}
