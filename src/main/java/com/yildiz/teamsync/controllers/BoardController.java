package com.yildiz.teamsync.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yildiz.teamsync.dto.BoardCreateRequestDTO;
import com.yildiz.teamsync.dto.BoardCreateResponseDTO;
import com.yildiz.teamsync.dto.BoardDetailsResponseDTO;
import com.yildiz.teamsync.dto.BoardListResponseDTO;
import com.yildiz.teamsync.services.IBoardService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final IBoardService boardService;

    public BoardController(IBoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping("/create")
    public ResponseEntity<BoardCreateResponseDTO> createBoard(@Valid @RequestBody BoardCreateRequestDTO requestDTO) {
        BoardCreateResponseDTO response = boardService.createBoard(requestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-boards")
    public ResponseEntity<List<BoardListResponseDTO>> getMyBoards() {
        return ResponseEntity.ok(boardService.getMyBoards());
    }

    @GetMapping("/details")
    public ResponseEntity<BoardDetailsResponseDTO> getBoardDetails(@RequestParam(name = "boardID") Long boardID) {
        return ResponseEntity.ok(boardService.getBoardDetails(boardID));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        boardService.deleteBoard(id);
        return ResponseEntity.ok().build();
    }
}
