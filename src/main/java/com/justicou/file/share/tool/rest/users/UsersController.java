package com.justicou.file.share.tool.rest.users;

import com.justicou.file.share.tool.configuration.annotations.UserEndpoint;
import com.justicou.file.share.tool.db.model.FileShareToolUser;
import com.justicou.file.share.tool.rest.dto.sharedfiles.SharedFileInfoDto;
import com.justicou.file.share.tool.rest.dto.users.*;
import com.justicou.file.share.tool.rest.mapper.SharedFileInfoMapper;
import com.justicou.file.share.tool.rest.mapper.UserMapper;
import com.justicou.file.share.tool.rest.sharedfiles.SharedFileInfoService;
import com.justicou.file.share.tool.rest.utils.auth.TokenService;
import com.justicou.file.share.tool.websocket.sessions.ConnectedUsersService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersService usersService;
    private final TokenService tokenService;
    private final UserMapper userMapper;
    private final ConnectedUsersService connectedUsersService;
    private final SharedFileInfoService sharedFileInfoService;
    private final SharedFileInfoMapper sharedFileInfoMapper;

    public UsersController(
            UsersService usersService,
            TokenService tokenService,
            UserMapper userMapper,
            ConnectedUsersService connectedUsersService,
            SharedFileInfoService sharedFileInfoService,
            SharedFileInfoMapper sharedFileInfoMapper
    ) {
        this.usersService = usersService;
        this.tokenService = tokenService;
        this.userMapper = userMapper;
        this.connectedUsersService = connectedUsersService;
        this.sharedFileInfoService = sharedFileInfoService;
        this.sharedFileInfoMapper = sharedFileInfoMapper;
    }

    @UserEndpoint
    @GetMapping
    public List<UserDto> getAllUsers() {
        return userMapper.toDtos(usersService.getAllUsers());
    }

    @UserEndpoint
    @GetMapping("/{userId}")
    public UserInfoDto getUserInfo(@PathVariable Long userId) {
        UserDto user = userMapper.toDto(usersService.getUserById(userId));
        List<SharedFileInfoDto> sharedFileInfos = sharedFileInfoMapper.toDtos(sharedFileInfoService.getUserSharedFileInfo(userId));
        return new UserInfoDto(user, sharedFileInfos);
    }

    @UserEndpoint
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
