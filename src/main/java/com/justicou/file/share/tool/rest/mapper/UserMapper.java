package com.justicou.file.share.tool.rest.mapper;

import com.justicou.file.share.tool.db.model.FileShareToolUser;
import com.justicou.file.share.tool.rest.dto.users.UserDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public UserDto toDto(FileShareToolUser user) {
        return new UserDto(user.getId(), user.getName());
    }

    public List<UserDto> toDtos(List<FileShareToolUser> users) {
        return users.stream().map((user) -> toDto(user)).toList();
    }
}
