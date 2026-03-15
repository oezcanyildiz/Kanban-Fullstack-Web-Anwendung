package com.yildiz.teamsync.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardColumnDTO {
    private Long boardColumnID;
    private String columnTitle;
    private Integer columnPosition;
    private Integer wipLimit;
    private List<TaskDTO> tasks;
}
