package com.justicou.file.share.tool.db.model;

import jakarta.persistence.*;

@Entity
@Table(name = "shared_file_info")
public class SharedFileInfo {

    @Id
    @Column(name = "shared_file_info_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long byteSize;
    private Long userId;

    /**
     * Constructor necessary for Hibernate
     */
    @Deprecated()
    public SharedFileInfo() {
    }

    public SharedFileInfo(String name, Long byteSize, Long userId) {
        this.name = name;
        this.byteSize = byteSize;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getByteSize() {
        return byteSize;
    }

    public void setByteSize(Long byteSize) {
        this.byteSize = byteSize;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
