package com.blog.osn7790._core.incerceptor;

import com.blog.osn7790._core.errors.exception.Exception401;
import com.blog.osn7790.user.User;
import com.blog.osn7790.util.Define;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class LoginInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        System.out.println("인터셉터 동작 확인 : " + request.getRequestURI());
        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);
        if (sessionUser == null) {
            throw new Exception401("로그인 먼저 해주세요");
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }


}

