package com.workbaseai.server.config;

import com.alibaba.cola.dto.Response;
import com.workbaseai.server.user.service.UserService.UserNotFoundException;
import com.workbaseai.server.user.service.UserService.UsernameAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class UserExceptionAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Response userNotFound(UserNotFoundException exception) {
        return Response.buildFailure("USER_NOT_FOUND", exception.getMessage());
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    Response duplicateUsername(UsernameAlreadyExistsException exception) {
        return Response.buildFailure("USER_USERNAME_EXISTS", exception.getMessage());
    }
}
