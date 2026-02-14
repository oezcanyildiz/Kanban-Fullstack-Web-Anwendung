package com.yildiz.teamsync.services;

import com.yildiz.teamsync.dto.BoardColumnCreateRequestDTO;
import com.yildiz.teamsync.dto.BoardColumnCreateResponseDTO;
import com.yildiz.teamsync.dto.BoardColumnUpdateRequestDTO;
import com.yildiz.teamsync.dto.BoardColumnUpdateResponseDTO;

public interface IBoardColumnService {
	BoardColumnCreateResponseDTO createColumn(BoardColumnCreateRequestDTO createdto);
    BoardColumnUpdateResponseDTO updateColumn(BoardColumnUpdateRequestDTO updatedto);
    void deleteColumn(Long columnID);

}
