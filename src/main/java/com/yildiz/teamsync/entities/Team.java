package com.yildiz.teamsync.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

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

	@Column(name = "deleted")
	private Boolean deleted = false;

	// Team-Mitglieder Beziehung
	@OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
	private List<TeamMember> members;

    public boolean isDeleted() {
        return deleted != null && deleted;
    }
}
