package com.yildiz.teamsync.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import com.yildiz.teamsync.dto.BoardCreateRequestDTO;
import com.yildiz.teamsync.dto.BoardCreateResponseDTO;
import com.yildiz.teamsync.dto.BoardUpdateRequestDTO;
import com.yildiz.teamsync.dto.BoardUpdateResponseDTO;
import com.yildiz.teamsync.dto.BoardDetailsResponseDTO;
import com.yildiz.teamsync.dto.BoardListResponseDTO;
import com.yildiz.teamsync.services.IBoardService;

@RestController
@RequestMapping("/board")
public class BoardController {

    private final IBoardService boardService;

    public BoardController(IBoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping("/create")
    public ResponseEntity<BoardCreateResponseDTO> createBoard(@RequestBody BoardCreateRequestDTO request) {
        return ResponseEntity.ok(boardService.createBoard(request));
    }

    @PatchMapping("/update")
    public ResponseEntity<BoardUpdateResponseDTO> updateBoard(@RequestBody BoardUpdateRequestDTO request) {
        return ResponseEntity.ok(boardService.updateBoard(request));
    }

    @GetMapping("/details")
    public ResponseEntity<BoardDetailsResponseDTO> getBoardDetails(@RequestParam Long boardID) {
        return ResponseEntity.ok(boardService.getBoardDetails(boardID));
    }

    @GetMapping("/my-boards")
    public ResponseEntity<List<BoardListResponseDTO>> getMyBoards() {
        return ResponseEntity.ok(boardService.getMyBoards());
    }

}
