package com.songdiary.SongDiary.diary.controller;

import java.time.LocalDate;
import java.util.List;

import com.songdiary.SongDiary.diary.dto.DateRequestDTO;
import com.songdiary.SongDiary.diary.dto.DiaryResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.songdiary.SongDiary.diary.domain.Diary;
import com.songdiary.SongDiary.diary.dto.DiaryRequestDTO;
import com.songdiary.SongDiary.diary.service.DiaryService;
import com.songdiary.SongDiary.user.dto.UserSessionDTO;

@RestController
@CrossOrigin(origins="*")
@RequiredArgsConstructor
public class DiaryController {
  
  private final DiaryService diaryService;

  @PostMapping("/diary")
  public ResponseEntity<?> createDiary(@SessionAttribute(name="user", required=false) UserSessionDTO user, @RequestBody DiaryRequestDTO req){
    if (user == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 후 이용해주세요.");
    }
    
    Diary diary = new Diary();
    diary.setDiaryWriterId(user.getUserId());
    diary.setDiaryTitle(req.getTitle());
    diary.setDiaryContents(req.getContents());
    diary.setDiaryDate(req.getDate());

    try {
      diaryService.writeDiary(diary);
      return new ResponseEntity<>("게시물이 정상적으로 작성되었습니다.", HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/diary")
  public ResponseEntity<?> getDiaryByDate(@SessionAttribute(name="user", required=false) UserSessionDTO user, @RequestParam(name="date", required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
    if (user == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 후 이용해주세요.");
    }

    LocalDate diaryDate = (date == null)?LocalDate.now():date;
    try {
      List<DiaryResponseDTO> res = diaryService.findDiariesByUserAndDate(user.getUserId(), diaryDate);
      return ResponseEntity.ok(res);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
  @GetMapping("/diary/emotion")
  public ResponseEntity<?> getEmotionByDate(@SessionAttribute(name="user", required=false) UserSessionDTO user, @RequestParam(name="date", required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
    if (user == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 후 이용해주세요.");
    }

    LocalDate diaryDate = (date == null)?LocalDate.now():date;
    try {
      String res = diaryService.findEmotionByDate(user.getUserId(), diaryDate);
      return ResponseEntity.ok(res);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
  @GetMapping("/main")
  public ResponseEntity<?> getMainDiary(@SessionAttribute(name="user", required=false) UserSessionDTO user){
    if (user == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 후 이용해주세요.");
    }
    try {
      List<DiaryResponseDTO> res = diaryService.findDiariesByUser(user.getUserId());
      return ResponseEntity.ok(res);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
  @GetMapping("/diary/{diaryId}")
  public ResponseEntity<?> getDiary(@SessionAttribute(name="user", required=false) UserSessionDTO user, @PathVariable Long diaryId){
    if (user == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 후 이용해주세요.");
    }
    try {
      DiaryResponseDTO res = diaryService.findDiaryById(diaryId);
      return ResponseEntity.ok(res);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
  @DeleteMapping("/diary/{diaryId}")
  public ResponseEntity<?> deleteDiary(@SessionAttribute(name="user", required=false) UserSessionDTO user, @PathVariable Long diaryId){
    if (user == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 후 이용해주세요.");
    }
    try {
      diaryService.deleteDiary(diaryId);
      return new ResponseEntity<>("게시물이 정상적으로 삭제되었습니다.", HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping("/diary/{diaryId}")
  public ResponseEntity<?> updateDiary(@PathVariable Long diaryId, @RequestBody DiaryRequestDTO req){

    try {
      diaryService.updateDiary(diaryId, req);
      return new ResponseEntity<>("게시물이 정상적으로 수정되었습니다.", HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
