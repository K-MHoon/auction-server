package com.kmhoon.common.model.entity.auth.map;

import com.kmhoon.common.model.entity.BaseEntity;
import com.kmhoon.common.model.entity.BaseTimeEntity;
import com.kmhoon.common.model.entity.auth.privilege.Privilege;
import com.kmhoon.common.model.entity.auth.role.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_auth_role_privilege")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class RolePrivilege {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sequence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_seq")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "privilege_seq")
    private Privilege privilege;
}
