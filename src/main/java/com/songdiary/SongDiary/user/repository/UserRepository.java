package com.songdiary.SongDiary.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.songdiary.SongDiary.user.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUserId(Long userId);
  Optional<User> findByUserProfileId(String userProfileId);
}
