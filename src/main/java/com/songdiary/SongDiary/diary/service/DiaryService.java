package com.songdiary.SongDiary.diary.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import com.songdiary.SongDiary.diary.domain.Diary;
import com.songdiary.SongDiary.diary.dto.DateResponseDTO;
import com.songdiary.SongDiary.diary.dto.DiaryRequestDTO;
import com.songdiary.SongDiary.diary.dto.DiaryResponseDTO;

public interface DiaryService {
    Long writeDiary(Diary diary);
    void deleteDiary(Long diaryId);
    void updateDiary(Long diaryId, DiaryRequestDTO req);
    DiaryResponseDTO findDiaryById(Long diaryId);
    List<DiaryResponseDTO> findDiariesByUserAndDate(Long userId, LocalDate diaryDate);
    List<DiaryResponseDTO> findDiariesByUser(Long userId);
    List<DateResponseDTO> findEmotionByDate(Long userId, YearMonth diaryDate);
}