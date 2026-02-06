package com.yildiz.teamsync.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="board_columns")

public class BoardColumn extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long boardColumnID;
	
	@Column(nullable = false, columnDefinition = "TEXT")
	private String columnTitle ;
	
	@Column(nullable=false)
	private Integer columnPosition;
	
	@Column(name = "wip_limit")
    private Integer wipLimit;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="board_id",nullable=false)
	private Board board;
}
