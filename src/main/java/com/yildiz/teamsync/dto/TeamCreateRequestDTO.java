package com.yildiz.teamsync.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamCreateRequestDTO {
    
    @NotBlank(message = "Der Team-Name darf nicht leer sein.")
    private String teamName;

}
