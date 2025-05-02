package com.justicou.file.share.tool.rest.dto.users;

import com.justicou.file.share.tool.rest.dto.sharedfiles.SharedFileInfoDto;

import java.util.List;

public record UserInfoDto(UserDto user, List<SharedFileInfoDto> sharedFileInfos) {
}
