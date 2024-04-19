package com.kmhoon.common.model.entity.auth.map;

import com.kmhoon.common.model.entity.BaseEntity;
import com.kmhoon.common.model.entity.BaseTimeEntity;
import com.kmhoon.common.model.entity.auth.role.Role;
import com.kmhoon.common.model.entity.auth.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_auth_user_role")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class UserRole extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sequence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_seq")
    private Role role;
}
