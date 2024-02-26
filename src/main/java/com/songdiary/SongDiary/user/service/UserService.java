package com.songdiary.SongDiary.user.service;

import com.songdiary.SongDiary.user.domain.User;
import com.songdiary.SongDiary.user.dto.UserInfoRequest;
import com.songdiary.SongDiary.user.dto.UserInfoResponse;
import com.songdiary.SongDiary.user.dto.UserJoinRequest;
import com.songdiary.SongDiary.user.dto.UserLoginRequest;
import com.songdiary.SongDiary.user.dto.UserNewPasswordRequest;

public interface UserService {

  public User join(UserJoinRequest req);
  public void delete(Long userId);
  public void login(UserLoginRequest req);
  public UserInfoResponse userInfo(Long userId);
  public void editUserInfo(Long userId, UserInfoRequest req);
  public void editPassword(Long userId, UserNewPasswordRequest req);
  
  public Boolean validateId(String profileId);
  public Boolean validatePassword(String password, String passwordCheck);

  public Long findUserId(String profileId);
}
