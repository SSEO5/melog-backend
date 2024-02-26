package com.songdiary.SongDiary.user.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.songdiary.SongDiary.user.domain.User;
import com.songdiary.SongDiary.user.dto.UserInfoRequest;
import com.songdiary.SongDiary.user.dto.UserInfoResponse;
import com.songdiary.SongDiary.user.dto.UserJoinRequest;
import com.songdiary.SongDiary.user.dto.UserLoginRequest;
import com.songdiary.SongDiary.user.dto.UserNewPasswordRequest;
import com.songdiary.SongDiary.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  // 회원가입
  public User join(UserJoinRequest req) {
    User user = new User();

    InputProfileId(req.getProfileId());
    InputPassword(req.getPassword(), req.getPasswordCheck());
    InputName(req.getName());
    
    user.setUserProfileId(req.getProfileId());
    String encodedPassword = passwordEncoder.encode(req.getPassword());
    user.setUserPassword(encodedPassword);
    user.setUserName(req.getName());
    
    userRepository.save(user);
    return user;
  }


  // 로그인
  public void login(UserLoginRequest req) {
    if (req.getProfileId() == null) {
      throw new IllegalStateException("아이디를 입력해주세요.");
    }
    if (req.getPassword() == null) {
      throw new IllegalStateException("비밀번호를 입력해주세요.");
    }

    if (validateId(req.getProfileId())) {
      throw new IllegalStateException("등록되지 않은 사용자입니다.");
    }
    String incodedPassword = userRepository.findByUserProfileId(req.getProfileId()).get().getUserPassword();
    if (!passwordEncoder.matches(req.getPassword(), incodedPassword)) {
      throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
    }
  }
  
  
  // 회원탈퇴
  public void delete(Long userId) {
    Optional<User> user = userRepository.findById(userId);
    if(user.isPresent())
      userRepository.delete(user.get());
    else
      throw new IllegalStateException("탈퇴 처리에 실패했습니다.");
  }

  // 회원정보
  // 회원정보 조회
  public UserInfoResponse userInfo(Long userId) {
    User user = userRepository.findByUserId(userId).get();
    
    UserInfoResponse res = new UserInfoResponse();
    res.setProfileId(user.getUserProfileId());
    res.setName(user.getUserName());

    return res;
  }

  // 회원정보 수정
  public void editUserInfo(Long userId, UserInfoRequest req) {
    User user = userRepository.findById(userId).get();

    InputName(req.getName());
    user.setUserName(req.getName());
    
    userRepository.save(user);
  }

  // 비밀번호 변경
  public void editPassword(Long userId, UserNewPasswordRequest req) {
    User user = userRepository.findById(userId).get();

    String incodedPassword = user.getUserPassword();
    if (!passwordEncoder.matches(req.getPresPassword(), incodedPassword)) {
      throw new IllegalStateException("현재 비밀번호가 일치하지 않습니다.");
    }
    InputPassword(req.getNewPassword(), req.getNewPasswordCheck());

    String encodedPassword = passwordEncoder.encode(req.getNewPassword());
    user.setUserPassword(encodedPassword);

    userRepository.save(user);
  }

  // 사용자 아이디 중복 검사
  public Boolean validateId(String profileId) {
    if (userRepository.findByUserProfileId(profileId).isEmpty()) {
      return true;
    }
    return false;
  }
  // 사용자 비밀번호 입력 검사
  public Boolean validatePassword(String password, String passwordCheck) {
    if (password.equals(passwordCheck)) {
      return true;
    }
    return false;
  }

  // userId 탐색
  public Long findUserId(String profileId) {
    User user = userRepository.findByUserProfileId(profileId).get();
    return user.getUserId();
  }


  // ------------------
  // 사용자 아이디 입력
  private void InputProfileId(String profileId) {
    if(profileId == null) {
      throw new IllegalStateException("아이디를 입력하세요.");
    }
    if(!validateId(profileId)) {
      throw new IllegalStateException("이미 사용 중인 아이디입니다.");
    }
  }
  // 사용자 비밀번호 입력
  private void InputPassword(String password, String passwordCheck) {
    if(password == null) {
      throw new IllegalStateException("비밀번호를 입력하세요.");
    }
    if(passwordCheck == null) {
      throw new IllegalStateException("비밀번호 확인을 입력하세요.");
    }
    if (!validatePassword(password, passwordCheck)) {
      throw new IllegalStateException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
    }
  }
  // 사용자 이름 입력
  private void InputName(String name) {
    if(name == null) {
      throw new IllegalStateException("이름을 입력해주세요.");
    }
  }
}
