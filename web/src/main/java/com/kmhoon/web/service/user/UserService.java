package com.kmhoon.web.service.user;

import com.kmhoon.common.enums.CouponStatus;
import com.kmhoon.common.enums.CouponType;
import com.kmhoon.common.model.entity.auth.map.UserRole;
import com.kmhoon.common.model.entity.auth.role.Role;
import com.kmhoon.common.model.entity.auth.user.User;
import com.kmhoon.common.model.entity.service.inventory.Coupon;
import com.kmhoon.common.model.entity.service.inventory.Inventory;
import com.kmhoon.common.repository.auth.map.UserRoleRepository;
import com.kmhoon.common.repository.auth.role.RoleRepository;
import com.kmhoon.common.repository.auth.user.UserRepository;
import com.kmhoon.common.repository.service.inventory.CouponRepository;
import com.kmhoon.common.repository.service.inventory.InventoryRepository;
import com.kmhoon.web.controller.dto.user.request.CreateUserControllerRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final InventoryRepository inventoryRepository;
    private final CouponRepository couponRepository;

    @Transactional
    public void register(CreateUserControllerRequest request) {
        if(userRepository.findByEmailWithRoles(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일 입니다.");
        }
        Role role = getRole();
        User savedUser = getNewUser(request);
        createUserRole(role, savedUser);
        createInventory(savedUser);
    }

    private void createInventory(User savedUser) {

        Inventory inventory = Inventory.builder()
                .money(1000000L)
                .owner(savedUser)
                .build();

        Inventory savedInventory = inventoryRepository.save(inventory);

        List<Coupon> defaultCouponList = IntStream.rangeClosed(0, 5).mapToObj(c -> Coupon.builder()
                        .inventory(savedInventory)
                        .type(CouponType.AUCTION)
                        .status(CouponStatus.UNUSED)
                        .endDate(null)
                        .build())
                .collect(Collectors.toList());

        couponRepository.saveAll(defaultCouponList);
    }

    private void createUserRole(Role role, User savedUser) {
        UserRole userRole = UserRole.builder()
                .user(savedUser)
                .role(role)
                .build();

        userRoleRepository.save(userRole);
    }

    private Role getRole() {
        return roleRepository.findByName("ROLE_USER").orElseThrow(() -> new IllegalArgumentException("역할이 존재하지 않습니다."));
    }

    private User getNewUser(CreateUserControllerRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .isUse(Boolean.TRUE)
                .isLock(Boolean.FALSE)
                .build();

        return userRepository.save(user);
    }
}
