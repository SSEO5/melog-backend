package com.songdiary.SongDiary.emotion.domain;

import com.songdiary.SongDiary.diary.domain.Diary;
import com.songdiary.SongDiary.emotion.dto.EmotionDTO;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Data
@Entity
@Table(name="AppEmotion")
public class Emotion {

  @Column(name="EMOTIONID")
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long emotionId;

  @Column(name="HAPPINESS")
  private Long happiness;

  @Column(name="NEUTRAL")
  private Long neutral;

  @Column(name="SADNESS")
  private Long sadness;

  @Column(name="ANGER")
  private Long anger;

  @Column(name="SURPRISE")
  private Long surprise;

  @Column(name="FEAR")
  private Long fear;

  @OneToOne
  @JoinColumn(name="diaryId")
  private Diary diary;

  //==비지니스 로직==//
  public String findMostEmotion(EmotionDTO req){
    Map<String, Long> emotions = new HashMap<>();
    emotions.put("happiness", req.getHappiness());
    emotions.put("neutral", req.getNeutral());
    emotions.put("sadness", req.getSadness());
    emotions.put("anger", req.getAnger());
    emotions.put("surprise", req.getSurprise());
    emotions.put("fear", req.getFear());
    return Collections.max(emotions.entrySet(), Map.Entry.comparingByValue()).getKey();
  }


}