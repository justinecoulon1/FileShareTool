package com.justicou.file.share.tool.db.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class FileShareToolUser {

    @Id
    @Column(name = "user_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;

    /**
     * Constructor necessary for Hibernate
     */
    @Deprecated()
    public FileShareToolUser() {
    }

    public FileShareToolUser(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
