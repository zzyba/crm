package com.zybtest.crm.settings.controller;

import com.zybtest.crm.commons.constants.Constants;
import com.zybtest.crm.commons.domain.ReturnObject;
import com.zybtest.crm.commons.util.DateUtils;
import com.zybtest.crm.settings.domain.User;
import com.zybtest.crm.settings.service.UserService;
import com.zybtest.crm.settings.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(){
        return "settings/qx/user/login";
    }

    @RequestMapping("/settings/qx/user/login.do")
    @ResponseBody
    public Object Login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request, HttpServletResponse response,  HttpSession session){
//        System.out.println("==================================");
        Map<String,Object> map=new HashMap<>();
        map.put("login_act",loginAct);
        map.put("login_pwd",loginPwd);
        User user = userService.selectUserByLoginActAndPwd(map);
//        System.out.println("=======");
        System.out.println(user);
        ReturnObject returnObject=new ReturnObject();
        if(user==null){
//            登陆失败 账号或密码错误
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("账号或密码错误");
        } else{
            if (DateUtils.formateDateTime(new Date()).compareTo(user.getExpireTime())>0){
//                登陆失败 账号过期
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账号过期");
            }else if ("0".equals(user.getLockState())){
//                登陆失败 账号被锁定
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账号被锁定");
            }else if (!user.getAllowIps().contains(request.getRemoteAddr())){
//                登陆失败 iP受限
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("iP受限");
            }else {
//                登陆成功
                session.setAttribute(Constants.SESSION_SYSTEMUSER,user);
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCEED);
                if("true".equals(isRemPwd)){
                    Cookie c1= new Cookie("loginAct",loginAct);
                    c1.setMaxAge(10*24*60*60);
                    Cookie c2= new Cookie("loginPwd",loginPwd);
                    c2.setMaxAge(10*24*60*60);
                    response.addCookie(c1);
                    response.addCookie(c2);
                }else {
                    Cookie c1= new Cookie("loginAct",loginAct);
                    c1.setMaxAge(0);
                    Cookie c2= new Cookie("loginPwd",loginPwd);
                    c2.setMaxAge(0);
                    response.addCookie(c1);
                    response.addCookie(c2);
                }
            }

        }
        return returnObject;
    }
    @RequestMapping("/settings/qx/user/logout.do")

    public String logout(HttpServletResponse response,HttpSession session){
        Cookie c1= new Cookie("loginAct","1");
        c1.setMaxAge(0);
        Cookie c2= new Cookie("loginPwd","1");
        c2.setMaxAge(0);
        response.addCookie(c1);
        response.addCookie(c2);
        session.invalidate();
        return "redirect:/";
    }

}
