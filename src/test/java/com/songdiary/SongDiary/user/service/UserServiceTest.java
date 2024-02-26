package com.songdiary.SongDiary.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.songdiary.SongDiary.user.domain.User;
import com.songdiary.SongDiary.user.dto.UserInfoRequest;
import com.songdiary.SongDiary.user.dto.UserInfoResponse;
import com.songdiary.SongDiary.user.dto.UserJoinRequest;
import com.songdiary.SongDiary.user.dto.UserLoginRequest;
import com.songdiary.SongDiary.user.dto.UserNewPasswordRequest;
import com.songdiary.SongDiary.user.repository.UserRepository;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
public class UserServiceTest {

  @Autowired UserService userService;
  @Autowired UserRepository userRepository;

  @Test
  @Rollback(false)
  void 회원가입() {
    // given
    UserJoinRequest form = new UserJoinRequest();
    form.setProfileId("id1");
    form.setPassword("password");
    form.setPasswordCheck("password");
    form.setName("name");

    // when
    userService.join(form);

    // then
    User findUser = userRepository.findByUserProfileId("id1").get();
    assertEquals("id1", findUser.getUserProfileId());
  }

  @Test
  void 회원탈퇴() {
    // given
    String profileId = "id1";
    Long userId = userRepository.findByUserProfileId(profileId).get().getUserId();

    // when
    userService.delete(userId);

    // then
  }

  @Test
  void 로그인() {
    // given
    UserLoginRequest form = new UserLoginRequest();
    form.setProfileId("id1");
    form.setPassword("password");

    // when
    userService.login(form);

    // then
  }

  @Test
  void 프로필조회() {
    // given
    String profileId = "id1";
    User user = userRepository.findByUserProfileId(profileId).get();
    UserInfoResponse rawUser = new UserInfoResponse();
    rawUser.setProfileId(user.getUserProfileId());
    rawUser.setName(user.getUserName());

    // when
    UserInfoResponse checkUser = userService.userInfo(user.getUserId());

    // then
    assertEquals(rawUser, checkUser);
  }

  @Test
  void 프로필수정() {
    // given
    String profileId = "id1";
    User user = userRepository.findByUserProfileId(profileId).get();

    // when
    UserInfoRequest form = new UserInfoRequest();
    form.setName(user.getUserName());

    userService.editUserInfo(user.getUserId(), form);

    // then
    assertEquals(user, userRepository.findByUserId(user.getUserId()).get());
  }

  @Test
  void 비밀번호변경() {
    // given
    String profileId = "id1";
    String password = "newPassword";
    User user = userRepository.findByUserProfileId(profileId).get();
  
    // when
    UserNewPasswordRequest form = new UserNewPasswordRequest();
    form.setPresPassword("password");
    form.setNewPassword(password);
    form.setNewPasswordCheck(password);

    userService.editPassword(user.getUserId(), form);

    // then
  }
}
