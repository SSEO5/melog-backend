package com.songdiary.SongDiary.song.controller;

import com.songdiary.SongDiary.diary.service.DiaryService;
import com.songdiary.SongDiary.emotion.dto.EmotionDTO;
import com.songdiary.SongDiary.song.domain.Song;
import com.songdiary.SongDiary.song.dto.SongDTO;
import com.songdiary.SongDiary.song.service.SongService;
import com.songdiary.SongDiary.user.dto.UserSessionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins="*")
@RequiredArgsConstructor
@RequestMapping("/{diaryId}/song")
public class SongController {
    private final SongService songService;
    private final DiaryService diaryService;

    @PostMapping()
    public ResponseEntity<?> createSong(@SessionAttribute(name="user", required=false) UserSessionDTO user, @PathVariable Long diaryId, @RequestBody List<SongDTO> reqs){
        if (user == null || user.getUserId() == null) {
            return new ResponseEntity<>("로그인 후 이용해주세요.", HttpStatus.UNAUTHORIZED);
        }
        try {
            songService.createSongs(diaryId, reqs);
            return new ResponseEntity<>("다이어리 노래 추천이 완료되었습니다.", HttpStatus.CREATED);
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
            List<SongDTO> songs = songService.findSongsByDiaryId(diaryId);
            return ResponseEntity.ok(songs);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping()
    public ResponseEntity<?> deleteSong(@SessionAttribute(name="user", required=false) UserSessionDTO user, @PathVariable Long diaryId) {
        if (user == null || user.getUserId() == null) {
            return new ResponseEntity<>("로그인 후 이용해주세요.", HttpStatus.UNAUTHORIZED);
        }
        try {
            songService.deleteSongs(diaryId);
            return new ResponseEntity<>("추천된 노래가 성공적으로 제거되었습니다.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
