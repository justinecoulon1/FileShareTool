package com.justicou.file.share.tool.rest.users;

import com.justicou.file.share.tool.db.model.FileShareToolUser;
import com.justicou.file.share.tool.db.repositories.UsersRepository;
import com.justicou.file.share.tool.exceptions.ForbiddenException;
import com.justicou.file.share.tool.exceptions.NotFoundException;
import com.justicou.file.share.tool.rest.utils.auth.PasswordService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final PasswordService passwordService;

    public UsersService(UsersRepository usersRepository, PasswordService passwordService) {
        this.usersRepository = usersRepository;
        this.passwordService = passwordService;
    }

    public List<FileShareToolUser> getAllUsers() {
        return usersRepository.findAll();
    }

    public FileShareToolUser createUser(String name, String email, String password) {
        FileShareToolUser newUser = new FileShareToolUser(name, email, passwordService.hashPassword(password));
        return usersRepository.save(newUser);
    }

    public FileShareToolUser getUser(String email, String password) {
        FileShareToolUser user = usersRepository.findByEmail(email);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        if (!passwordService.verifyPassword(password, user.getPassword())) {
            throw new ForbiddenException("Wrong credentials");
        }
        return user;
    }
}
