package com.justicou.file.share.tool.rest.sharedfiles;

import com.justicou.file.share.tool.db.model.SharedFileInfo;
import com.justicou.file.share.tool.db.repositories.SharedFileInfoRepository;
import com.justicou.file.share.tool.exceptions.NotFoundException;
import com.justicou.file.share.tool.rest.dto.sharedfiles.UpdateSharedFileInfoDto;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class SharedFileInfoService {

    private final SharedFileInfoRepository sharedFileInfoRepository;

    public SharedFileInfoService(SharedFileInfoRepository sharedFileInfoRepository) {
        this.sharedFileInfoRepository = sharedFileInfoRepository;
    }

    public SharedFileInfo getSharedFileInfoById(Long id) {
        return sharedFileInfoRepository.findById(id).orElseThrow(() -> new NotFoundException("Shared file not found"));
    }

    public List<SharedFileInfo> getUserSharedFileInfo(Long userId) {
        return this.sharedFileInfoRepository.findByUserId(userId);
    }

    public List<SharedFileInfo> updateSharedFileInfo(Long userId, List<UpdateSharedFileInfoDto> updateSharedFileInfoDtos) {
        var existingSharedFileInfos = sharedFileInfoRepository.findByUserId(userId);
        var toSaveFileInfos = updateSharedFileInfoDtos.stream()
                .map((updateFileInfo) -> {
                    var existingSharedFileInfo = getSharedFileInfoByName(existingSharedFileInfos, updateFileInfo.name());
                    return existingSharedFileInfo == null
                            ? createSharedFileInfo(updateFileInfo, userId)
                            : updateSharedFileInfo(updateFileInfo, existingSharedFileInfo);
                })
                .toList();
        var toSaveFileInfoNames = toSaveFileInfos.stream().map((fileInfo) -> fileInfo.getName()).toList();
        var toDeleteFileInfos = existingSharedFileInfos.stream()
                .filter((fileInfo) -> !toSaveFileInfoNames.contains(fileInfo.getName()))
                .toList();

        if (!toDeleteFileInfos.isEmpty()) {
            sharedFileInfoRepository.deleteAll(toDeleteFileInfos);
        }

        if (!toSaveFileInfos.isEmpty()) {
            return sharedFileInfoRepository.saveAll(toSaveFileInfos);
        }
        return Collections.emptyList();
    }

    private SharedFileInfo getSharedFileInfoByName(List<SharedFileInfo> sharedFileInfos, String name) {
        return sharedFileInfos.stream()
                .filter((fileInfo) -> Objects.equals(name, fileInfo.getName()))
                .findFirst()
                .orElse(null);
    }

    private SharedFileInfo createSharedFileInfo(UpdateSharedFileInfoDto updateSharedFileInfoDto, Long userId) {
        return new SharedFileInfo(updateSharedFileInfoDto.name(), updateSharedFileInfoDto.byteSize(), userId);
    }

    private SharedFileInfo updateSharedFileInfo(UpdateSharedFileInfoDto updateSharedFileInfoDto, SharedFileInfo toUpdateSharedFileInfo) {
        toUpdateSharedFileInfo.setByteSize(updateSharedFileInfoDto.byteSize());
        return toUpdateSharedFileInfo;
    }
}
