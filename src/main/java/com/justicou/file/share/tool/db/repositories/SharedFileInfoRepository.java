package com.justicou.file.share.tool.db.repositories;

import com.justicou.file.share.tool.db.model.SharedFileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SharedFileInfoRepository extends JpaRepository<SharedFileInfo, Long> {

    List<SharedFileInfo> findByUserId(Long userId);
}