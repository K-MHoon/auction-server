package com.kmhoon.common.model.entity.auth.privilege;

import com.kmhoon.common.model.entity.auth.map.RolePrivilege;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.type.NumericBooleanConverter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_auth_privilege")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sequence;

    private String name;

    @Convert(converter = NumericBooleanConverter.class)
    private Boolean isUse;

    @OneToMany(mappedBy = "privilege", fetch = FetchType.LAZY)
    @Builder.Default
    private List<RolePrivilege> rolePrivilegeList = new ArrayList<>();
}
