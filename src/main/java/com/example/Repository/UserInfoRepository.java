package com.example.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Model.UserInfo;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
}
