package com.songdiary.SongDiary.user.dto;
import java.io.Serializable;

import lombok.Data;

@Data
public class UserSessionDTO implements Serializable {
  private static final Long serialVersionUID = 1L;
  private Long userId; 
  //private List<String> roles;
}
