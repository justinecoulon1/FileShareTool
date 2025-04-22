package com.justicou.file.share.tool.rest.dto.users;

public record LoginResponseDto(UserDto user, String accessToken, String refreshToken) {

}
