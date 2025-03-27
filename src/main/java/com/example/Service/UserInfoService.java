package com.example.Service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.example.Model.Address;
import com.example.Model.Department;
import com.example.Model.UserInfo;
import com.example.Repository.AddressRepository;
import com.example.Repository.DepartmentRepository;
import com.example.Repository.UserInfoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserInfoService {
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private AddressRepository addressRepository;

    public List<UserInfo> getAllUsers() {
        return userInfoRepository.findAll();
    }

    public UserInfo saveUser(UserInfo userInfo) {
        // Department ko save karna agar wo null nahi hai
        if (userInfo.getDepartment() != null && userInfo.getDepartment().getId() == null) {
            Department savedDept = departmentRepository.save(userInfo.getDepartment());
            userInfo.setDepartment(savedDept);
        }

        // UserInfo save karna 
        UserInfo savedUser = userInfoRepository.save(userInfo);

//        // Address save karna
        if (userInfo.getAddresses() != null) {
            for (Address address : userInfo.getAddresses()) {
                address.setUser(savedUser);
                addressRepository.save(address);
            }
        }

        return savedUser;
    }

    public UserInfo getUserById(Long id) {
        return userInfoRepository.findById(id).orElse(null);
    }

    public void deleteUser(Long id) {
        userInfoRepository.deleteById(id);
    }
    
    
    public UserInfo updateUser(Long id, UserInfo userInfo) {
        UserInfo existingUser = userInfoRepository.findById(id).orElse(null);
        
        if (existingUser == null) {
            return null; // User not found
        }

        // ✅ Update Basic User Info
        existingUser.setUsername(userInfo.getUsername());
        existingUser.setPassword(userInfo.getPassword());
        existingUser.setFirstName(userInfo.getFirstName());
        existingUser.setLastName(userInfo.getLastName());
        existingUser.setMobile(userInfo.getMobile());
        existingUser.setEmail(userInfo.getEmail());
        existingUser.setGender(userInfo.getGender());

        // ✅ Update Department if provided
        if (userInfo.getDepartment() != null) {
            existingUser.setDepartment(userInfo.getDepartment());
        }

        // ✅ Handle Address Updates
        if (userInfo.getAddresses() != null) {
            List<Long> newAddressIds = userInfo.getAddresses().stream()
                    .map(Address::getId)
                    .collect(Collectors.toList());

            // Remove old addresses that are not in the new list
            existingUser.getAddresses().removeIf(address -> 
                !newAddressIds.contains(address.getId())
            );

            // Add or update addresses
            for (Address newAddress : userInfo.getAddresses()) {
                if (newAddress.getId() == null) {
                    // New Address: Set user and save
                    newAddress.setUser(existingUser);
                    addressRepository.save(newAddress);
                } else {
                    // Existing Address: Update fields
                    for (Address existingAddress : existingUser.getAddresses()) {
                        if (existingAddress.getId().equals(newAddress.getId())) {
                            existingAddress.setAddress1(newAddress.getAddress1());
                            existingAddress.setAddress2(newAddress.getAddress2());
                            existingAddress.setCity(newAddress.getCity());
                            existingAddress.setState(newAddress.getState());
                        }
                    }
                }
            }
        }

        return userInfoRepository.save(existingUser);
    }
    
    
    // ✅ Update only Address for a User
    public List<Address> updateUserAddresses(Long userId, List<Address> updatedAddresses) {
        UserInfo user = userInfoRepository.findById(userId).orElse(null);
        if (user == null) {
            return null; // User not found
        }

        List<Long> newAddressIds = updatedAddresses.stream()
                .map(Address::getId)
                .collect(Collectors.toList());

        // ✅ Remove addresses not present in the request
        user.getAddresses().removeIf(address -> !newAddressIds.contains(address.getId()));

        // ✅ Add or update addresses
        for (Address newAddress : updatedAddresses) {
            if (newAddress.getId() == null) {
                // New Address: Set user and save
                newAddress.setUser(user);
                addressRepository.save(newAddress);
            } else {
                // Update Existing Address
                for (Address existingAddress : user.getAddresses()) {
                    if (existingAddress.getId().equals(newAddress.getId())) {
                        existingAddress.setAddress1(newAddress.getAddress1());
                        existingAddress.setAddress2(newAddress.getAddress2());
                        existingAddress.setCity(newAddress.getCity());
                        existingAddress.setState(newAddress.getState());
                    }
                }
            }
        }

        // ✅ Save updated user info
        userInfoRepository.save(user);
        return user.getAddresses();
    }
    
    
    
    
    
    
    
}
