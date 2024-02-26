package com.songdiary.SongDiary.song.repository;

import java.util.List;
import java.util.Optional;

import com.songdiary.SongDiary.diary.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.songdiary.SongDiary.song.domain.Song;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
  Optional<Song> findBySongId(Long songId);
  List<Song> findByDiaryDiaryId(Long diaryId);
  void deleteByDiary(Diary diary);
}
