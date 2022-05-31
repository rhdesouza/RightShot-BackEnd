package rightshot.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import rightshot.entity.User;

@Controller
public class SecurityController {

    @GetMapping(value = "/user-auth")
    @ResponseBody
	/* @Secured({Const.ROLE_CLIENT, Const.ROLE_ADMIN_ADMIN_ADMIN}) */
    public User user() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
