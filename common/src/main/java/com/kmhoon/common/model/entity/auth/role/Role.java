package com.kmhoon.common.model.entity.auth.role;

import com.kmhoon.common.model.entity.auth.map.RolePrivilege;
import com.kmhoon.common.model.entity.auth.map.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.type.NumericBooleanConverter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_auth_role")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sequence;

    private String name;

    @Convert(converter = NumericBooleanConverter.class)
    private Boolean isUse;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    @Builder.Default
    private List<UserRole> userRoleList = new ArrayList<>();

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    @Builder.Default
    private List<RolePrivilege> rolePrivilegeList = new ArrayList<>();
}
