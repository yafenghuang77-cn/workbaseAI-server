package com.workbaseai.server.user.controller;

import com.alibaba.cola.dto.SingleResponse;
import com.workbaseai.server.user.dto.UserResponse;
import com.workbaseai.server.user.dto.UserWriteRequest;
import com.workbaseai.server.user.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/detail")
    public SingleResponse<UserResponse> get(@RequestParam("id") @Positive long id) {
        return SingleResponse.of(userService.get(id));
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public SingleResponse<Boolean> create(@Valid @RequestBody UserWriteRequest request) {
        userService.create(request);
        return SingleResponse.of(true);
    }

    @PutMapping("/update")
    public SingleResponse<Boolean> update(@RequestParam("id") @Positive long id,
            @Valid @RequestBody UserWriteRequest request) {
        userService.update(id, request);
        return SingleResponse.of(true);
    }

    @DeleteMapping("/delete")
    public SingleResponse<Boolean> delete(@RequestParam("id") @Positive long id) {
        userService.delete(id);
        return SingleResponse.of(true);
    }
}
