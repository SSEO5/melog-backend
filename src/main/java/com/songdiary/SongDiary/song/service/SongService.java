package com.songdiary.SongDiary.song.service;

import com.songdiary.SongDiary.song.domain.Song;
import com.songdiary.SongDiary.song.dto.SongDTO;

import java.util.List;

public interface SongService {

  void createSongs(Long diaryId, List<SongDTO> reqs);
  void deleteSongs(Long diaryId);
  List<SongDTO> findSongsByDiaryId(Long diaryId);
}
