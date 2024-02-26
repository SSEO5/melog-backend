package com.songdiary.SongDiary.user.dto;

import lombok.Data;

@Data
public class UserLoginRequest {
  private String profileId;
  private String password;
}
