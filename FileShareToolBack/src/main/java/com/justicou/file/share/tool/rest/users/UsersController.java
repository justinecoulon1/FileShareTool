package com.justicou.file.share.tool.rest.users;

import com.justicou.file.share.tool.configuration.annotations.UserEndpoint;
import com.justicou.file.share.tool.db.model.FileShareToolUser;
import com.justicou.file.share.tool.rest.dto.users.CreateUserRequestDto;
import com.justicou.file.share.tool.rest.dto.users.LoginRequestDto;
import com.justicou.file.share.tool.rest.dto.users.LoginResponseDto;
import com.justicou.file.share.tool.rest.dto.users.UserDto;
import com.justicou.file.share.tool.rest.mapper.UserMapper;
import com.justicou.file.share.tool.rest.utils.auth.TokenService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersService usersService;
    private final TokenService tokenService;
    private final UserMapper userMapper;

    public UsersController(UsersService usersService, TokenService tokenService, UserMapper userMapper) {
        this.usersService = usersService;
        this.tokenService = tokenService;
        this.userMapper = userMapper;
    }

    @UserEndpoint()
    @GetMapping
    public List<UserDto> getAllUsers() {
        return userMapper.toDtos(usersService.getAllUsers());
    }

    @PostMapping
    public UserDto createUser(@RequestBody CreateUserRequestDto createUserRequestDto) {
        FileShareToolUser user = usersService.createUser(createUserRequestDto.name(), createUserRequestDto.email(), createUserRequestDto.password());
        return userMapper.toDto(user);
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto) {
        FileShareToolUser user = usersService.getUser(loginRequestDto.email(), loginRequestDto.password());
        String accessToken = tokenService.generateAccessToken(user.getId());
        String refreshToken = tokenService.generateRefreshToken(user.getId());

        return new LoginResponseDto(accessToken, refreshToken);
    }
}
