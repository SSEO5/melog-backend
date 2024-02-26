package com.songdiary.SongDiary.diary.service;

import java.time.LocalDate;
import java.util.List;

import com.songdiary.SongDiary.diary.domain.Diary;
import com.songdiary.SongDiary.diary.dto.DiaryRequestDTO;
import com.songdiary.SongDiary.diary.dto.DiaryResponseDTO;

public interface DiaryService {
    Long writeDiary(Diary diary);
    void deleteDiary(Long diaryId);
    void updateDiary(Long diaryId, DiaryRequestDTO req);
    List<DiaryResponseDTO> findAllDiaries();
    DiaryResponseDTO findDiaryById(Long diaryId);
    List<DiaryResponseDTO> findDiariesByUserAndDate(Long userId, LocalDate diaryDate);
    List<DiaryResponseDTO> findDiariesByUser(Long userId);
    String findEmotionByDate(Long userId, LocalDate diaryDate);
}