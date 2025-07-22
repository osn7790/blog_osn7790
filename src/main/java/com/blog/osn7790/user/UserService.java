package com.blog.osn7790.user;

import com.blog.osn7790._core.errors.exception.Exception400;
import com.blog.osn7790._core.errors.exception.Exception404;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {

    private final UserJpaRepository userJpaRepository;


    // 회원가입
    @Transactional
    public User join(UserRequest.JoinDTO joinDTO) {
        userJpaRepository.findByUsername(joinDTO.getUsername())
                .ifPresent(user1 -> {
                    throw new Exception400("이미 존재하는 사용자명입니다");
                });
            return userJpaRepository.save(joinDTO.toEntity());
        }

    // 로그인
    public User login(UserRequest.LoginDTO loginDTO) {
        return userJpaRepository
                .findByUsernameAndPassword(loginDTO.getUsername(), loginDTO.getPassword())
                .orElseThrow(() -> {
                    return new Exception400("사용자명 또는 비밀번호가 일치하지 않습니다");
                });
    }

    // 사용자 정보 조회

    public User findById(Long id) {
        return userJpaRepository.findById(id).orElseThrow(() -> {

            log.warn("사용자 조회 실패 - ID {}", id);
            return new Exception404("사용자를 찾을 수 없습니다");
        });
    }

    // 회원정보 수정
    @Transactional
    public User updateById(Long userId, UserRequest.UpdateDTO updateDTO) {
        User user = findById(userId);
        user.setPassword(updateDTO.getPassword());
        return user;
    }


    }


