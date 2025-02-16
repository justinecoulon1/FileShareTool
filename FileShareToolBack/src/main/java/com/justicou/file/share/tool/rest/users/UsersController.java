package com.justicou.file.share.tool.rest.users;

import com.justicou.file.share.tool.configuration.annotations.UserEndpoint;
import com.justicou.file.share.tool.db.model.FileShareToolUser;
import com.justicou.file.share.tool.rest.dto.CreateUserRequestDto;
import com.justicou.file.share.tool.rest.dto.LoginRequestDto;
import com.justicou.file.share.tool.rest.dto.LoginResponseDto;
import com.justicou.file.share.tool.rest.utils.auth.TokenService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersService usersService;
    private final TokenService tokenService;

    public UsersController(UsersService usersService, TokenService tokenService) {
        this.usersService = usersService;
        this.tokenService = tokenService;
    }

    @UserEndpoint()
    @GetMapping
    public List<FileShareToolUser> getAllUsers() {
        return usersService.getAllUsers();
    }

    @PostMapping
    public FileShareToolUser createUser(@RequestBody CreateUserRequestDto createUserRequestDto) {
        return usersService.createUser(createUserRequestDto.getName(), createUserRequestDto.getEmail(), createUserRequestDto.getPassword());
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto) {
        FileShareToolUser user = usersService.getUser(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        String accessToken = tokenService.generateAccessToken(user.getId());
        String refreshToken = tokenService.generateRefreshToken(user.getId());

        return new LoginResponseDto(accessToken, refreshToken);
    }
}
