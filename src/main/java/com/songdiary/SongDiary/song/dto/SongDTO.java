package com.songdiary.SongDiary.song.dto;

import lombok.Data;

@Data
public class SongDTO {
    private String title;
    private String artist;
    private Long likes;
}