package com.songdiary.SongDiary.song.domain;

import com.songdiary.SongDiary.diary.domain.Diary;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="AppSong")
public class Song {

  @Column(name="SONGID")
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long songId;

  @Column(name="SONGTITLE")
  private String songTitle;

  @Column(name="SONGARTIST")
  private String songArtist;

  @Column(name="SONGLIKES")
  private Long songLikes;

  @ManyToOne
  @JoinColumn(name="diaryId")
  private Diary diary;

}