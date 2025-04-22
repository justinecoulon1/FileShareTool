package com.justicou.file.share.tool.configuration;

import com.justicou.file.share.tool.configuration.annotations.UserEndpoint;
import com.justicou.file.share.tool.db.model.FileShareToolUser;
import com.justicou.file.share.tool.db.repositories.UsersRepository;
import com.justicou.file.share.tool.exceptions.ForbiddenException;
import com.justicou.file.share.tool.rest.utils.auth.TokenInformation;
import com.justicou.file.share.tool.rest.utils.auth.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class UserValidationInterceptor implements HandlerInterceptor {

    private final TokenService tokenService;
    private final UsersRepository usersRepository;

    public UserValidationInterceptor(TokenService tokenService, UsersRepository usersRepository) {
        this.tokenService = tokenService;
        this.usersRepository = usersRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        String accessToken = request.getHeader("accessToken");
        boolean requiresUser = handlerMethod.hasMethodAnnotation(UserEndpoint.class);

        if (accessToken == null && requiresUser) {
            throw new ForbiddenException("Missing token");
        } else if (accessToken == null) {
            return true;
        }

        TokenInformation accessTokenInformation = tokenService.getTokenInformation(accessToken);
        FileShareToolUser user = usersRepository.findById(accessTokenInformation.getUserId())
                .orElseThrow(ForbiddenException::new);

        request.setAttribute("user", user);
        return true;
    }
}
