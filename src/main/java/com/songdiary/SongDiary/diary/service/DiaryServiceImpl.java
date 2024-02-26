package com.songdiary.SongDiary.diary.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.songdiary.SongDiary.diary.dto.DiaryRequestDTO;
import com.songdiary.SongDiary.diary.dto.DiaryResponseDTO;
import com.songdiary.SongDiary.emotion.service.EmotionService;
import com.songdiary.SongDiary.song.service.SongService;
import org.springframework.stereotype.Service;

import com.songdiary.SongDiary.diary.domain.Diary;
import com.songdiary.SongDiary.diary.repository.DiaryRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {
    private final DiaryRepository diaryRepository;
    private final SongService songService;
    private final EmotionService emotionService;
    @Transactional
    public Long writeDiary(Diary diary){
        diaryRepository.save(diary);
        return diary.getDiaryId();
    }
    @Transactional
    public void deleteDiary(Long diaryId){
        Diary diary = diaryRepository.findByDiaryId(diaryId).get();
        if(diary.getDiarySongs()!=null) songService.deleteSongs(diaryId);
        if(diary.getDiaryEmotion()!=null) {
            emotionService.deleteEmotion(diaryId);
            diary.setMostEmotion(null);
        }
        diaryRepository.delete(diary);
    }
    @Transactional
    public void updateDiary(Long diaryId, DiaryRequestDTO req) {
        Optional<Diary> optionalDiary = diaryRepository.findById(diaryId);
        optionalDiary.ifPresent(diary -> {
            diary.setDiaryTitle(req.getTitle());
            diary.setDiaryContents(req.getContents());
        });
    }

    public List<DiaryResponseDTO> findAllDiaries(){
        return diaryRepository.findAll().stream()
                .map(DiaryResponseDTO::from)
                .collect(Collectors.toList());
    }

    public DiaryResponseDTO findDiaryById(Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new EntityNotFoundException("다이어리가 존재하지 않습니다."));
        return DiaryResponseDTO.from(diary);

    }

    public List<DiaryResponseDTO> findDiariesByUserAndDate(Long diaryWriterId, LocalDate diaryDate) {
        List<Diary> diaries = diaryRepository.findByDiaryDateAndDiaryWriterId(diaryDate, diaryWriterId);
        if (diaries == null || diaries.isEmpty()) {
            throw new EntityNotFoundException("해당 날짜의 다이어리가 존재하지 않습니다.");
        }
        return diaries.stream()
                .map(DiaryResponseDTO::from)
                .collect(Collectors.toList());
    }

    public List<DiaryResponseDTO> findDiariesByUser(Long diaryWriterId) {
       return diaryRepository.findByDiaryWriterId(diaryWriterId).stream()
               .map(DiaryResponseDTO::from)
               .collect(Collectors.toList());
    }

    public String findEmotionByDate(Long diaryWriterId, LocalDate diaryDate){
        List<Diary> diaries = diaryRepository.findByDiaryDateAndDiaryWriterId(diaryDate, diaryWriterId);
        if (diaries == null || diaries.isEmpty()) {
            throw new EntityNotFoundException("해당 날짜의 다이어리가 존재하지 않습니다.");
        }
        Diary diary = diaries.get(0);
        return diary.getMostEmotion();
    }

}