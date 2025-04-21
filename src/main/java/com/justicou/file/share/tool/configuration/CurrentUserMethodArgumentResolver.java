package com.justicou.file.share.tool.configuration;

import com.justicou.file.share.tool.configuration.annotations.CurrentUser;
import com.justicou.file.share.tool.db.model.FileShareToolUser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class CurrentUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class) && parameter.getParameterType() == FileShareToolUser.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        var nativeRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        if (nativeRequest == null) {
            return null;
        }
        return nativeRequest.getAttribute("user");
    }
}
