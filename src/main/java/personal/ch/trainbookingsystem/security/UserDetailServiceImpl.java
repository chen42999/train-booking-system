package personal.ch.trainbookingsystem.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import personal.ch.trainbookingsystem.entity.Users;
import personal.ch.trainbookingsystem.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 获取用户的基本信息
        Users user = userService.findByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException("用户名不存在");
        }

        // 获取用户的权限信息
        String userAuthority = userService.findAuthorityByUsername(user.getId());

        // GrantedAuthority 代表赋予当前用户的权限（认证权限）
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userAuthority));
        System.out.println(new SimpleGrantedAuthority(userAuthority).getAuthority());
//        System.out.println(authorities.get(authorities.size()-1).getAuthority());

//        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("user,ROLE_user");
        // 获得当前用户的权限保存为用户的认证权限


        // 将获得用户名与密码传给security的User
        return new User(user.getUsername(), user.getPassword(), authorities);
    }
}
