package com.yildiz.teamsync.mappers;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.yildiz.teamsync.dto.BoardColumnCreateRequestDTO;
import com.yildiz.teamsync.dto.BoardColumnCreateResponseDTO;
import com.yildiz.teamsync.dto.BoardColumnUpdateRequestDTO;
import com.yildiz.teamsync.dto.BoardColumnUpdateResponseDTO;
import com.yildiz.teamsync.entities.BoardColumn;

@Mapper(componentModel="spring")
public interface BoardColumnMapper {
	
	// CREATE : Request ---> Entity
	BoardColumn toEntity(BoardColumnCreateRequestDTO requestdto);
	
	// CREATE : Entity --> Response
	BoardColumnCreateResponseDTO toResponse(BoardColumn boardColumn);
	
	// UPDATE : Entity ---> Response
	BoardColumnUpdateResponseDTO toUpdateResponse(BoardColumn boardColumn);

	// HILFREICH: Für die Liste im BoardDetailResponseDTO
	// MapStruct generiert die Implementierung für die ganze Liste automatisch!
	List<BoardColumnCreateResponseDTO> toResponseList(List<BoardColumn> boardColumns);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(BoardColumnUpdateRequestDTO updatedto, @MappingTarget BoardColumn entity);
}
