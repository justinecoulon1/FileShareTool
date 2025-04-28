package com.justicou.file.share.tool.rest.users;

import com.justicou.file.share.tool.configuration.annotations.UserEndpoint;
import com.justicou.file.share.tool.db.model.FileShareToolUser;
import com.justicou.file.share.tool.rest.dto.users.CreateUserRequestDto;
import com.justicou.file.share.tool.rest.dto.users.LoginRequestDto;
import com.justicou.file.share.tool.rest.dto.users.LoginResponseDto;
import com.justicou.file.share.tool.rest.dto.users.UserDto;
import com.justicou.file.share.tool.rest.mapper.UserMapper;
import com.justicou.file.share.tool.rest.users.connectedusers.ConnectedUsersService;
import com.justicou.file.share.tool.rest.utils.auth.TokenService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersService usersService;
    private final TokenService tokenService;
    private final UserMapper userMapper;
    private final ConnectedUsersService connectedUsersService;

    public UsersController(
            UsersService usersService,
            TokenService tokenService,
            UserMapper userMapper,
            ConnectedUsersService connectedUsersService
    ) {
        this.usersService = usersService;
        this.tokenService = tokenService;
        this.userMapper = userMapper;
        this.connectedUsersService = connectedUsersService;
    }

    @UserEndpoint()
    @GetMapping
    public List<UserDto> getAllUsers() {
        return userMapper.toDtos(usersService.getAllUsers());
    }

    @UserEndpoint()
    @GetMapping("/connected")
    public List<UserDto> getAllConnectedUsers() {
        List<Long> connectedUsersIds = connectedUsersService.getAllConnectedUserIds();
        return userMapper.toDtos(usersService.getAllUsersByIds(connectedUsersIds));
    }


    @PostMapping
    public UserDto createUser(@RequestBody CreateUserRequestDto createUserRequestDto) {
        FileShareToolUser user = usersService.createUser(createUserRequestDto.name(), createUserRequestDto.email(), createUserRequestDto.password());
        return userMapper.toDto(user);
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto) {
        var user = usersService.getUser(loginRequestDto.email(), loginRequestDto.password());
        String accessToken = tokenService.generateAccessToken(user.getId());
        String refreshToken = tokenService.generateRefreshToken(user.getId());
        return new LoginResponseDto(userMapper.toDto(user), accessToken, refreshToken);
    }
}
