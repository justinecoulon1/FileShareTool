package com.justicou.file.share.tool.db.repositories;

import com.justicou.file.share.tool.db.model.FileShareToolUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsersRepository extends JpaRepository<FileShareToolUser, Long> {

    FileShareToolUser findByEmail(String email);

    List<FileShareToolUser> findAllByIdIn(List<Long> id);
}
