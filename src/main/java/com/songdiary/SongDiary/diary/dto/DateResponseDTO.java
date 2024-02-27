package com.songdiary.SongDiary.diary.dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class DateResponseDTO {
    LocalDate date;
    String mostEmotion;
}
