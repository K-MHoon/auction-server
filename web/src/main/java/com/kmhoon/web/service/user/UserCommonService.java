package com.kmhoon.web.service.user;

import com.kmhoon.common.model.entity.auth.user.User;
import com.kmhoon.common.repository.auth.user.UserRepository;
import com.kmhoon.web.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCommonService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User getLoggedInUser() {
        String loggedInUserId = SecurityUtils.getLoggedInUserId();
        return userRepository.findByEmailWithInventory(loggedInUserId).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
    }


}
