package com.songdiary.SongDiary.emotion.service;

import java.util.Optional;

import com.songdiary.SongDiary.emotion.dto.InputModel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.songdiary.SongDiary.diary.domain.Diary;
import com.songdiary.SongDiary.diary.repository.DiaryRepository;
import com.songdiary.SongDiary.emotion.domain.Emotion;
import com.songdiary.SongDiary.emotion.dto.EmotionDTO;
import com.songdiary.SongDiary.emotion.repository.EmotionRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmotionServiceImpl implements EmotionService {

  private final EmotionRepository emotionRepository;
  private final DiaryRepository diaryRepository;
  private final RestTemplate restTemplate;

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

  @SneakyThrows
  public Optional<EmotionDTO> analyzeEmotion(String contents) {
    String url = "http://모델서버/predict";
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<InputModel> entity = new HttpEntity<>(new InputModel(contents), headers);
    ResponseEntity<EmotionDTO> response = restTemplate.postForEntity(url, entity, EmotionDTO.class);
    log.info("Sleep 시작");
    Thread.sleep(1000);
    log.info("Sleep 종료");
    return Optional.ofNullable(response.getBody());
  }

}
