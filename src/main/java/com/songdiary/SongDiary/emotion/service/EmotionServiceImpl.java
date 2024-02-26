package com.songdiary.SongDiary.emotion.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.songdiary.SongDiary.diary.domain.Diary;
import com.songdiary.SongDiary.diary.repository.DiaryRepository;
import com.songdiary.SongDiary.emotion.domain.Emotion;
import com.songdiary.SongDiary.emotion.dto.EmotionDTO;
import com.songdiary.SongDiary.emotion.repository.EmotionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmotionServiceImpl implements EmotionService {

  private final EmotionRepository emotionRepository;
  private final DiaryRepository diaryRepository;

  public void createEmotion(Long diaryId, EmotionDTO req) {
    Optional<Diary> diary = diaryRepository.findByDiaryId(diaryId);
    if(diary.isEmpty() || diary.get().getDiaryId() == null) {
      throw new IllegalStateException("다이어리 조회에 실패하였습니다.");
    }
    
    if(diary.get().getDiaryEmotion() != null) {
      throw new IllegalStateException("다이어리 감정 분석 결과가 이미 완료된 상태입니다.");
    }

    Emotion emotion = new Emotion();
    emotion.setHappiness(req.getHappiness());
    emotion.setNeutral(req.getNeutral());
    emotion.setSadness(req.getSadness());
    emotion.setAnger(req.getAnger());
    emotion.setSurprise(req.getSurprise());
    emotion.setFear(req.getFear());

    emotionRepository.save(emotion);
    diary.get().addDiaryEmotion(emotion);
    diary.get().setMostEmotion(emotion.findMostEmotion(req));
    diaryRepository.save(diary.get());
    
  }

  @Override
  public void deleteEmotion(Long diaryId) {
    Optional<Diary> diary = diaryRepository.findByDiaryId(diaryId);
    if(diary.isEmpty()) {
      throw new IllegalStateException("다이어리 조회에 실패하였습니다.");
    }
  
    Emotion emotion = diary.get().getDiaryEmotion();
    if(emotion == null) {
      throw new IllegalStateException("다이어리 감정 분석 결과가 존재하지 않습니다.");
    }
    diary.get().setDiaryEmotion(null);
    diary.get().setMostEmotion(null);
    emotionRepository.delete(emotion);
    diaryRepository.save(diary.get());

  }

  @Override
  public Optional<EmotionDTO> findEmotionByDiaryId(Long diaryId) {
    Optional<Diary> diary = diaryRepository.findByDiaryId(diaryId);
    if(diary.isEmpty()) {
      throw new IllegalStateException("다이어리 조회에 실패하였습니다.");
    }
    Emotion emotion = diary.get().getDiaryEmotion();
    if(emotion == null) {
      throw new IllegalStateException("다이어리 감정 분석 결과가 존재하지 않습니다.");
    }
    
    EmotionDTO res = new EmotionDTO();
    res.setHappiness(emotion.getHappiness());
    res.setNeutral(emotion.getNeutral());
    res.setSadness(emotion.getSadness());
    res.setAnger(emotion.getAnger());
    res.setSurprise(emotion.getSurprise());
    res.setFear(emotion.getFear());
    return Optional.of(res);

  }
  
}
