package com.songdiary.SongDiary.diary.service;

import com.songdiary.SongDiary.diary.domain.Diary;
import com.songdiary.SongDiary.diary.dto.DiaryRequestDTO;
import com.songdiary.SongDiary.diary.dto.DiaryResponseDTO;
import com.songdiary.SongDiary.user.domain.User;
import com.songdiary.SongDiary.user.repository.UserRepository;
import com.songdiary.SongDiary.user.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class DiaryServiceTest {
    @Autowired
    DiaryService diaryService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    EntityManager em;

    @Test
    @Rollback(false)
    public void 일기작성() throws Exception{
        //given
        User user = createUser();
        Diary diary = createDiary(user);

        //when
        Long diaryId = diaryService.writeDiary(diary);

        //then
        DiaryResponseDTO getDiary = diaryService.findDiaryById(diaryId);

        assertEquals("test",getDiary.getTitle()); //check title
        assertEquals("hello world",getDiary.getContents()); //check contents
    }
    @Test
    @Rollback(false)
    public void 일기수정() throws Exception{
        //given
        User user = createUser();
        Diary diary = createDiary(user);

        Long diaryId = diaryService.writeDiary(diary);
        String newTitle = "edit Title";
        String newContents = "edit Contents";
        DiaryRequestDTO request = new DiaryRequestDTO();
        request.setTitle(newTitle);
        request.setContents(newContents);

        //when
        diaryService.updateDiary(diaryId, request);
        DiaryResponseDTO res = diaryService.findDiaryById(diaryId);

        //then
        assertEquals(res.getTitle(), "edit Title");
        assertEquals(res.getContents(), "edit Contents");
    }

    @Test
    @Rollback(false)
    public void 일기검색() throws Exception{
        //given
        User user = createUser();
        Diary diary = createDiary(user);
        Diary diary1 = createDiary(user);

        User user2 = createUser();
        Diary diary2 = createDiary(user2);

        //when
        diaryService.writeDiary(diary);
        diaryService.writeDiary(diary1);
        diaryService.writeDiary(diary2);

        List<DiaryResponseDTO> diaries = diaryService.findDiariesByUserAndDate(user.getUserId(), LocalDate.now());
        List<DiaryResponseDTO> diaries1 = diaryService.findDiariesByUser(user.getUserId());
        List<DiaryResponseDTO> diaries2 = diaryService.findDiariesByUserAndDate(user2.getUserId(), LocalDate.now());

        //then
        //test findByUserAndDate
        assertThat(diaries).hasSize(2);
        assertThat(diaries).hasSize(2);
        assertThat(diaries2).hasSize(1);

    }

    @Test
    public void 일기삭제() throws Exception{
        //given
        User user = createUser();
        Diary diary = createDiary(user);
        Long diaryId = diaryService.writeDiary(diary);

        //when
        diaryService.deleteDiary(diaryId);

        //then
        assertThrows(EntityNotFoundException.class, ()->{
            diaryService.findDiaryById(diaryId);
        });
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