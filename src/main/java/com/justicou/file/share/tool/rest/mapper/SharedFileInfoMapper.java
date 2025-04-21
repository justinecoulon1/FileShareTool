package com.justicou.file.share.tool.rest.mapper;

import com.justicou.file.share.tool.db.model.SharedFileInfo;
import com.justicou.file.share.tool.rest.dto.sharedfiles.SharedFileInfoDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SharedFileInfoMapper {

    public SharedFileInfoDto toDto(SharedFileInfo sharedFileInfo) {
        return new SharedFileInfoDto(sharedFileInfo.getId(), sharedFileInfo.getName(), sharedFileInfo.getByteSize());
    }

    public List<SharedFileInfoDto> toDtos(List<SharedFileInfo> sharedFileInfos) {
        return sharedFileInfos.stream().map((sharedFileInfo) -> toDto(sharedFileInfo)).toList();
    }
}
