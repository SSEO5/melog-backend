package com.songdiary.SongDiary.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.songdiary.SongDiary.user.domain.User;
import com.songdiary.SongDiary.user.dto.UserInfoRequest;
import com.songdiary.SongDiary.user.dto.UserInfoResponse;
import com.songdiary.SongDiary.user.dto.UserJoinRequest;
import com.songdiary.SongDiary.user.dto.UserLoginRequest;
import com.songdiary.SongDiary.user.dto.UserNewPasswordRequest;
import com.songdiary.SongDiary.user.dto.UserSessionDTO;
import com.songdiary.SongDiary.user.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;



@RestController
@CrossOrigin(origins="*")
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
  
  private final UserService userService;

  // 회원가입
  @PostMapping("join")
  public ResponseEntity<?> userSignUp(@SessionAttribute(name="user", required=false) UserSessionDTO user, @RequestBody UserJoinRequest req) {
    if (user != null) {
      return new ResponseEntity<>("로그인 상태에서는 사용할 수 없는 기능입니다. 로그아웃 후 시도해주세요.", HttpStatus.FORBIDDEN);
    }

    if (req == null) {
      return new ResponseEntity<>("입력이 정상적으로 수행되지 않았습니다. 다시 시도해주세요.", HttpStatus.NOT_FOUND);
    }

    try {
      User registeredUser = userService.join(req);
      return new ResponseEntity<>(registeredUser.getUserName()+"("+registeredUser.getUserProfileId()+") 님 계정이 생성되었습니다.", HttpStatus.CREATED);
    } catch (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  // 로그인
  @PostMapping("login")
  public ResponseEntity<?> userLogin(@SessionAttribute(name="user", required=false) UserSessionDTO user, @RequestBody UserLoginRequest req, HttpServletRequest sessionReq) {
    if (user != null) {
      return new ResponseEntity<>("로그인 상태에서는 사용할 수 없는 기능입니다. 로그아웃 후 시도해주세요.", HttpStatus.FORBIDDEN);
    }

    if (req == null) {
      return new ResponseEntity<>("입력이 정상적으로 수행되지 않았습니다. 다시 시도해주세요.", HttpStatus.NOT_FOUND);
    }

    try {
      userService.login(req);
      Long userId = userService.findUserId(req.getProfileId());

      //세션
      UserSessionDTO userSessionDTO = new UserSessionDTO();
      userSessionDTO.setUserId(userId);
      //userSessionDto.setRoles(Arrays.asList("USER"));
      HttpSession session = sessionReq.getSession();
      session.setAttribute("user", userSessionDTO);

      return new ResponseEntity<>(req.getProfileId()+" 님, 환영합니다. 로그인이 완료되었습니다.", HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
  
  // 로그아웃
  @PostMapping("logout")
  public ResponseEntity<?> userLogout(HttpServletRequest req) {
    HttpSession session = req.getSession(false);
    if (session != null) {
      session.invalidate();
      return new ResponseEntity<>("로그아웃이 완료되었습니다.", HttpStatus.OK);
    }
    return new ResponseEntity<>("로그인 후 이용해주세요.", HttpStatus.UNAUTHORIZED);
  }
  
  // 회원탈퇴
  @DeleteMapping("delete")
  public ResponseEntity<?> userDelete(@SessionAttribute(name="user", required=false) UserSessionDTO user, HttpServletRequest req) {
    if (user.getUserId() != null) {
      HttpSession session = req.getSession(false);
      if (session != null) {
        session.invalidate();
      }
      userService.delete(user.getUserId());
      return new ResponseEntity<>("회원 탈퇴가 완료되었습니다.", HttpStatus.OK);
    }
    else {
      return new ResponseEntity<>("로그인 후 이용해주세요.", HttpStatus.UNAUTHORIZED);
    }
  }

  // 회원정보 조회
  @GetMapping("info/check")
  public UserInfoResponse userInfo(@SessionAttribute(name="user", required=false) UserSessionDTO user) {
    if (user.getUserId() != null) {
      return userService.userInfo(user.getUserId());
    }
    else {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 후 이용해주세요.");
    }
  }
  
  // 회원정보 수정
  @PostMapping("info/edit/profile")
  public ResponseEntity<?> userInfoEdit(@SessionAttribute(name="user", required=false) UserSessionDTO user, @RequestBody UserInfoRequest req) {
    if (user.getUserId() != null) {
      if (req != null) {
        try {
          userService.editUserInfo(user.getUserId(), req);
          return new ResponseEntity<>("프로필이 업데이트되었습니다.", HttpStatus.OK);
        }
        catch (Exception e) {
          return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
      }
      else {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "입력이 정상적으로 수행되지 않았습니다. 다시 시도해주세요.");
      }
    }
    else {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 후 이용해주세요.");
    }
  }

  // 비밀번호 변경
  @PostMapping("info/edit/password")
  public ResponseEntity<?> postMethodName(@SessionAttribute(name="user", required=false) UserSessionDTO user, @RequestBody UserNewPasswordRequest req) {
    if (user.getUserId() != null) {
      if (req != null) {
        try {
          userService.editPassword(user.getUserId(), req);
          return new ResponseEntity<>("비밀번호가 변경되었습니다.", HttpStatus.OK);
        }
        catch (Exception e) {
          return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
      }
      else {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "입력이 정상적으로 수행되지 않았습니다. 다시 시도해주세요.");
      }
    }
    else {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 후 이용해주세요.");
    }
  }
  
}
