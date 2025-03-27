package com.example.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.Model.Address;
import com.example.Model.UserInfo;
import com.example.Service.UserInfoService;

import java.util.List;

@RestController
@RequestMapping("/api/userinfo")
@CrossOrigin(origins = "http://localhost:4200/") // Angular ke liye
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    @GetMapping
    public List<UserInfo> getAllUsers() {
        return userInfoService.getAllUsers();
    }

    @PostMapping
    public ResponseEntity<UserInfo> createUser(@RequestBody UserInfo userInfo) {
        UserInfo savedUser = userInfoService.saveUser(userInfo);
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserInfo> getUserById(@PathVariable Long id) {
        UserInfo user = userInfoService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userInfoService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
    
    @PutMapping("/{id}")
    public ResponseEntity<UserInfo> updateUser(@PathVariable Long id, @RequestBody UserInfo userInfo) {
        UserInfo updatedUser = userInfoService.updateUser(id, userInfo);
        return updatedUser != null ? ResponseEntity.ok(updatedUser) : ResponseEntity.notFound().build();
    }
    
    
    // ✅ Update only Address for a specific user
    @PutMapping("/{userId}/addresses")
    public ResponseEntity<List<Address>> updateUserAddresses(
            @PathVariable Long userId, @RequestBody List<Address> addresses) {
        List<Address> updatedAddresses = userInfoService.updateUserAddresses(userId, addresses);
        return updatedAddresses != null ? ResponseEntity.ok(updatedAddresses) : ResponseEntity.notFound().build();
    }
}
