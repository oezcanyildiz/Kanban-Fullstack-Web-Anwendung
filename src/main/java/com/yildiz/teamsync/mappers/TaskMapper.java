package com.yildiz.teamsync.mappers;

import com.yildiz.teamsync.dto.TaskCreateRequestDTO;
import com.yildiz.teamsync.dto.TaskCreateResponseDTO;
import com.yildiz.teamsync.dto.TaskUpdateRequestDTO;
import com.yildiz.teamsync.dto.TaskUpdateResponseDTO;
import com.yildiz.teamsync.entities.BoardTask;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    // 1. Create: DTO -> Entity
    @Mapping(target = "taskID", ignore = true)
    @Mapping(target = "boardColumn", ignore = true) 
    @Mapping(target = "assignedUser", ignore = true) 
    BoardTask toEntity(TaskCreateRequestDTO dto);

    // 2. Response: Entity -> DTO
    @Mapping(source = "boardColumn.boardColumnID", target = "columnID")
    @Mapping(source = "assignedUser.userID", target = "assigneeID")
    @Mapping(source = "assignedUser.username", target = "assigneeName") 
    TaskCreateResponseDTO toCreateResponse(BoardTask task);

    // 3. Update: Felder vom DTO auf bestehende Entity übertragen
    @Mapping(target = "taskID", ignore = true)
    @Mapping(target = "boardColumn", ignore = true) 
    @Mapping(target = "assignedUser", ignore = true) 
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(TaskUpdateRequestDTO dto, @MappingTarget BoardTask task);

    // 4. Update Response: Entity -> DTO
    @Mapping(source = "boardColumn.boardColumnID", target = "columnID")
    @Mapping(source = "assignedUser.userID", target = "assigneeID")
    @Mapping(source = "assignedUser.username", target = "assigneeName")
    TaskUpdateResponseDTO toUpdateResponse(BoardTask task);
}