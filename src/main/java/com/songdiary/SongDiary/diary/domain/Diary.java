package com.songdiary.SongDiary.diary.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.songdiary.SongDiary.emotion.domain.Emotion;
import com.songdiary.SongDiary.song.domain.Song;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Data
@Entity
@Table(name="AppDiary")
public class Diary {
  @Column(name="DIARYID")
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long diaryId;

  @Column(name="DIARYWRITERID")
  private Long diaryWriterId;

  @Column(name="DIARYTITLE")
  private String diaryTitle;

  @Column(name="DIARYDATE")
  private LocalDate diaryDate;

  @Column(name="DIARYCONTENTS")
  private String diaryContents;

  @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Song> diarySongs = new ArrayList<>();

  @OneToOne(mappedBy = "diary")
  private Emotion diaryEmotion;

  @Column(name="MOSTEMOTION")
  private String mostEmotion;

  //==연관관계 메서드==//
  public void addDiarySong(Song song){
    diarySongs.add(song);
    song.setDiary(this);
  }

  public void addDiaryEmotion(Emotion e) {
    diaryEmotion = e;
    e.setDiary(this);
  }

}