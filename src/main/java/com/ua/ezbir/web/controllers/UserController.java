package com.ua.ezbir.web.controllers;

import com.ua.ezbir.services.UserService;
import com.ua.ezbir.web.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/search")
    public List<UserResponseDto> fetchUsers(
            @RequestParam(value = "prefix_name", required = false) Optional<String> optionalPrefixName
    ) {
        return userService.fetchUsers(optionalPrefixName);
    }

    @PatchMapping("/update")
    public UserResponseDto addInfo(
            @RequestParam(value = "description", required = false) Optional<String> optionalDescription,
            @RequestParam(value = "number", required = false) Optional<Long> optionalNumber
            // another params...
    ) {
        return userService.updateUser(optionalDescription, optionalNumber);
    }

    @GetMapping
    public UserResponseDto getUser(@RequestParam("id") Long id) {
        return userService.getUserResponseDtoById(id);
    }

    @PostMapping("/upload-picture")
    public String uploadPicture(@RequestParam("picture") MultipartFile file) {
        return userService.uploadPicture(file);
    }
}