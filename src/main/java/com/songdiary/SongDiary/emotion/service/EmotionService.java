package com.songdiary.SongDiary.emotion.service;

import java.util.Optional;

import com.songdiary.SongDiary.emotion.dto.EmotionDTO;

public interface EmotionService {
  void createEmotion(Long diaryId, EmotionDTO req);
  void deleteEmotion(Long diaryId);
  Optional<EmotionDTO> findEmotionByDiaryId(Long diaryId);
  
}
