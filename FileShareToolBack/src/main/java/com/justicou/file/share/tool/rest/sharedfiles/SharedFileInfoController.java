package com.justicou.file.share.tool.rest.sharedfiles;

import com.justicou.file.share.tool.configuration.annotations.CurrentUser;
import com.justicou.file.share.tool.configuration.annotations.UserEndpoint;
import com.justicou.file.share.tool.db.model.FileShareToolUser;
import com.justicou.file.share.tool.rest.dto.sharedfiles.SharedFileInfoDto;
import com.justicou.file.share.tool.rest.dto.sharedfiles.UpdateSharedFileInfoRequestDto;
import com.justicou.file.share.tool.rest.mapper.SharedFileInfoMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shared-files")
public class SharedFileInfoController {

    private final SharedFileInfoService sharedFileInfoService;
    private final SharedFileInfoMapper sharedFileInfoMapper;

    public SharedFileInfoController(SharedFileInfoService sharedFileInfoService, SharedFileInfoMapper sharedFileInfoMapper) {
        this.sharedFileInfoService = sharedFileInfoService;
        this.sharedFileInfoMapper = sharedFileInfoMapper;
    }

    @UserEndpoint
    @GetMapping
    public List<SharedFileInfoDto> getCurrentUserSharedFileInfo(@CurrentUser FileShareToolUser user) {
        return sharedFileInfoMapper.toDtos(sharedFileInfoService.getUserSharedFileInfo(user.getId()));
    }

    @UserEndpoint
    @GetMapping("/{userId}")
    public List<SharedFileInfoDto> getUserSharedFileInfo(@PathVariable Long userId) {
        return sharedFileInfoMapper.toDtos(sharedFileInfoService.getUserSharedFileInfo(userId));
    }

    @UserEndpoint
    @PostMapping
    public List<SharedFileInfoDto> updateUserSharedFileInfo(@CurrentUser FileShareToolUser user, @RequestBody UpdateSharedFileInfoRequestDto updateSharedFileInfoRequestDto) {
        return sharedFileInfoMapper.toDtos(sharedFileInfoService.updateSharedFileInfo(user.getId(), updateSharedFileInfoRequestDto.updatedSharedFileInfoDto()));
    }
}
