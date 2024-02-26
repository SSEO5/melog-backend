package com.songdiary.SongDiary.user.dto;

import lombok.Data;

@Data
public class UserJoinRequest {
  private String profileId;
  private String password;
  private String passwordCheck;
  private String name;
}
