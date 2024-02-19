package com.xmonster.howtaxing.model;

import com.xmonster.howtaxing.type.Role;
import com.xmonster.howtaxing.type.SocialType;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "USERS")
public class User extends BaseEntity {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String userId;

    @Column(length = 50)
    private String userName;

    @Column(length = 50)
    private String userEmail;

    private String password; // 비밀번호

    @Column(columnDefinition = "ENUM('KAKAO','NAVER','GOOGLE','APPLE','NORMAL') DEFAULT 'NORMAL'")
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    private String refreshToken;

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }
}
