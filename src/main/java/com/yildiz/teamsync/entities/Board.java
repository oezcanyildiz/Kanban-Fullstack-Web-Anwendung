package com.yildiz.teamsync.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="boards")

public class Board extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long boardID;
	
	@Column(nullable= false)
	private String boardName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="team_id", nullable = false)
	private Team team;
	
	
}
