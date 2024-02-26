package com.songdiary.SongDiary.song.service;

import com.songdiary.SongDiary.diary.domain.Diary;
import com.songdiary.SongDiary.diary.dto.DiaryResponseDTO;
import com.songdiary.SongDiary.diary.service.DiaryService;
import com.songdiary.SongDiary.song.domain.Song;
import com.songdiary.SongDiary.song.dto.SongDTO;
import com.songdiary.SongDiary.user.domain.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class SongServiceImplTest {
    @Autowired
    DiaryService diaryService;
    @Autowired SongService songService;
    @Autowired
    EntityManager em;
    @Test
    @Rollback(false)
    public void 노래등록() throws Exception{
        //given
        User user = createUser();
        Diary diary = createDiary(user);
        List<SongDTO> reqs = createSongs();

        //when
        Long diaryId = diaryService.writeDiary(diary);
        songService.createSongs(diaryId, reqs);
        DiaryResponseDTO getDiary = diaryService.findDiaryById(diaryId);
        List<SongDTO> getSongs = songService.findSongsByDiaryId(diaryId);

        //then
        assertThat(getSongs).hasSize(3);
        assertThat(getSongs.get(0).getArtist()).isEqualTo("artist1");
        assertThat(getSongs.get(1).getArtist()).isEqualTo("artist2");
        assertThat(getSongs.get(2).getArtist()).isEqualTo("artist3");
    }

    @Test
    @Rollback(false)
    public void 노래삭제() throws Exception{
        //given
        User user = createUser();
        Diary diary = createDiary(user);
        List<SongDTO> reqs = createSongs();

        //when
        Long diaryId = diaryService.writeDiary(diary);
        songService.createSongs(diaryId, reqs);
        songService.deleteSongs(diaryId);
        DiaryResponseDTO getDiary = diaryService.findDiaryById(diaryId);
        List<SongDTO> getSongs = songService.findSongsByDiaryId(diaryId);

        //then
        assertThat(getSongs).hasSize(0);

    }

    private List<SongDTO> createSongs() {
        List<SongDTO> reqs = new ArrayList<>();
        SongDTO req = new SongDTO();
        req.setTitle("title1");
        req.setArtist("artist1");
        req.setLikes(0L);

        SongDTO req2 = new SongDTO();
        req2.setTitle("title2");
        req2.setArtist("artist2");
        req2.setLikes(100L);

        SongDTO req3 = new SongDTO();
        req3.setTitle("title3");
        req3.setArtist("artist3");
        req3.setLikes(5L);

        reqs.add(req);
        reqs.add(req2);
        reqs.add(req3);

        return reqs;
    }

    private User createUser() {
        User user = new User();
        user.setUserPassword("password");
        user.setUserName("gimijin");
        user.setUserProfileId("inging007");
        em.persist(user);
        return user;
    }
    private Diary createDiary(User user) {
        Diary diary = new Diary();
        diary.setDiaryTitle("test");
        diary.setDiaryContents("hello world");
        diary.setDiaryWriterId(user.getUserId());
        return diary;
    }
}