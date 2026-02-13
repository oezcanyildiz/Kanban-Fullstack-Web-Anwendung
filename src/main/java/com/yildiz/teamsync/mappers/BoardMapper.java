package com.yildiz.teamsync.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.yildiz.teamsync.dto.BoardCreateRequestDTO;
import com.yildiz.teamsync.dto.BoardCreateResponseDTO;
import com.yildiz.teamsync.dto.BoardListResponseDTO;
import com.yildiz.teamsync.dto.BoardUpdateRequestDTO;
import com.yildiz.teamsync.dto.BoardUpdateResponseDTO;
import com.yildiz.teamsync.entities.Board;

@Mapper(componentModel="spring")
public interface BoardMapper {
	
	//request --> entity
	Board toEntity(BoardCreateRequestDTO requestdto);
	
	
	// entity ----> response
	BoardCreateResponseDTO toCreateResponse(Board board);
	
	
	//request --> entity
	Board toEntity(BoardUpdateRequestDTO requestdto);
	
	
	@Mapping(target = "boardName", source = "newBoardName")
	@Mapping(target = "boardID", ignore = true) // Die ID soll sich nie ändern
	void updateEntityFromDto(BoardUpdateRequestDTO dto, @MappingTarget Board board);

	BoardUpdateResponseDTO toUpdateResponse(Board board);
	
	@Mapping(target = "teamName", source = "team.teamName")
	BoardListResponseDTO toListResponse(Board board);

	List<BoardListResponseDTO> toListResponseList(List<Board> boards);
}
