package com.kmhoon.common.model.entity.auth.user;

import com.kmhoon.common.model.entity.BaseTimeEntity;
import com.kmhoon.common.model.entity.auth.map.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.type.NumericBooleanConverter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_auth_user")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sequence;

    @Column(nullable = false, updatable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Convert(converter = NumericBooleanConverter.class)
    @Column(nullable = false)
    @Builder.Default
    private Boolean isUse = Boolean.TRUE;

    @Builder.Default
    private Long passwordWrongCount = 0L;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isLock = Boolean.FALSE;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private List<UserRole> userRoleList = new ArrayList<>();
}
