package com.workbaseai.server.user.service;

import com.workbaseai.server.user.dao.UserDO;
import com.workbaseai.server.user.dto.UserResponse;
import com.workbaseai.server.user.dto.UserWriteRequest;
import com.workbaseai.server.user.manager.UserManager;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserManager userManager;

    public UserService(UserManager userManager) {
        this.userManager = userManager;
    }

    public UserResponse get(long id) {
        UserDO user = userManager.findById(id);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return toResponse(user);
    }

    public void create(UserWriteRequest request) {
        try {
            userManager.insert(toDataObject(null, request));
        } catch (DuplicateKeyException exception) {
            throw new UsernameAlreadyExistsException(exception);
        }
    }

    @Transactional
    public void update(long id, UserWriteRequest request) {
        try {
            if (!userManager.update(toDataObject(id, request))) {
                throw new UserNotFoundException();
            }
        } catch (DuplicateKeyException exception) {
            throw new UsernameAlreadyExistsException(exception);
        }
    }

    public void delete(long id) {
        if (!userManager.deleteById(id)) {
            throw new UserNotFoundException();
        }
    }

    private static UserDO toDataObject(Long id, UserWriteRequest request) {
        return new UserDO(id, request.username().strip(), request.displayName().strip(), request.email(),
                request.status());
    }

    private static UserResponse toResponse(UserDO user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getDisplayName(), user.getEmail(),
                user.getStatus(), user.getCreatedAt(), user.getUpdatedAt());
    }

    public static final class UserNotFoundException extends RuntimeException {

        private UserNotFoundException() {
            super("用户不存在");
        }
    }

    public static final class UsernameAlreadyExistsException extends RuntimeException {

        private UsernameAlreadyExistsException(DuplicateKeyException cause) {
            super("用户名已存在", cause);
        }
    }
}
