package com.songdiary.SongDiary.user.dto;

import lombok.Data;

@Data
public class UserNewPasswordRequest {
  private String presPassword;
  private String newPassword;
  private String newPasswordCheck;
}
