package com.songdiary.SongDiary.emotion.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.songdiary.SongDiary.emotion.dto.EmotionDTO;
import com.songdiary.SongDiary.emotion.service.EmotionService;
import com.songdiary.SongDiary.user.dto.UserSessionDTO;


@RestController
@CrossOrigin(origins="*")
@RequestMapping("/{diaryId}/emotion")
public class EmotionController {

  private final EmotionService emotionService;

  @Autowired
  public EmotionController(EmotionService emotionService) {
    this.emotionService = emotionService;
  }

  @PostMapping()
  public ResponseEntity<?> createEmotion(@SessionAttribute(name="user", required=false) UserSessionDTO user, @PathVariable Long diaryId, @RequestBody EmotionDTO req) {
    if (user == null || user.getUserId() == null) {
      return new ResponseEntity<>("로그인 후 이용해주세요.", HttpStatus.UNAUTHORIZED);
    }

    if (req == null) {
      return new ResponseEntity<>("분석이 정상적으로 수행되지 않았습니다. 다시 시도해주세요.", HttpStatus.NOT_FOUND);
    }


    try {
      emotionService.createEmotion(diaryId, req);
      return new ResponseEntity<>("다이어리 감정 분석이 완료되었습니다.", HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping()
  public ResponseEntity<?> findEmotion(@SessionAttribute(name="user", required=false) UserSessionDTO user, @PathVariable Long diaryId) {
    if (user == null || user.getUserId() == null) {
      return new ResponseEntity<>("로그인 후 이용해주세요.", HttpStatus.UNAUTHORIZED);
    }

    try {
      Optional<EmotionDTO> emotion = emotionService.findEmotionByDiaryId(diaryId);
      return ResponseEntity.ok(emotion.get());
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping()
  public ResponseEntity<?> deleteEmotion(@SessionAttribute(name="user", required=false) UserSessionDTO user, @PathVariable Long diaryId) {
    if (user == null || user.getUserId() == null) {
      return new ResponseEntity<>("로그인 후 이용해주세요.", HttpStatus.UNAUTHORIZED);
    }
    try {
      emotionService.deleteEmotion(diaryId);
      return new ResponseEntity<>("분석된 감정이 성공적으로 제거되었습니다.", HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
  

  
}
