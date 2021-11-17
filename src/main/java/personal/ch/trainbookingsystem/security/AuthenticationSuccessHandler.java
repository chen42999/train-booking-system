package personal.ch.trainbookingsystem.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = getTargetURL(authentication);
        redirectStrategy.sendRedirect(request,response,targetUrl);
    }

    protected String getTargetURL(Authentication authentication){
        String url = "";
        // 获得当前登录用户的权限（角色）集合
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> role = new ArrayList<>();
        for (GrantedAuthority au : authorities){
            role.add(au.getAuthority());
        }


        // 判断不同角色的用户跳转到不同的url
        if (role.contains("ROLE_USER")){
            url = "/index";
        } else if (role.contains("ROLE_ADMIN")){
            url = "/adminIndex";
        }
        return url;
    }
}
