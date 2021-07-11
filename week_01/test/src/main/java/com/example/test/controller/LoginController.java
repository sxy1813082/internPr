package com.example.test.controller;

import com.example.test.bean.UserBean;
import com.example.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.ui.Model;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

@Controller
public class LoginController {
    private String yanNamber;

    //将Service注入Web层
    @Autowired
    UserService userService;

    @RequestMapping("/login")
    public String show(){
        return "login";
    }

    @RequestMapping(value = "/loginIn",method = RequestMethod.POST)
    public String login(Model model,String name,String password){
        UserBean userBean = userService.loginIn(name,password);
        if(userBean!=null){
            model.addAttribute("user", userBean);
            return "success";
        }else {
            return "error";
        }
    }

    @RequestMapping("toManagement")
    public String toManagement(){
        return "management";
    }


    @RequestMapping("/loginInManager")
    public String loginManager(Model model,String name, String password) {
        UserBean userBean = userService.loginInManager(name, password);
        List<UserBean> users = userService.findByAll();
        model.addAttribute("users", users);
        if (userBean !=null){
            return "userList";
        }
        else {
            return "error";
        }
    }

    @RequestMapping("toRegister")
    public String toRegister(Model model){
        UserBean user = new UserBean();
        model.addAttribute("user", user);
        return "register";
    }

    @RequestMapping("/Register")
    public String Register(UserBean user){
        UserBean userEmail = userService.findUserByEmail(user.getEmail());
        if (userEmail != null){
            System.out.println("already 存在");
            return "userHave";
        }
        else {
            userService.Register(user);
            return "login";
        }
    }

    @RequestMapping("/toForgetPassword")
    public String toForgetPassword(Model model){
        UserBean user = new UserBean();
        model.addAttribute("user", user);
        return "forget";
    }

    @RequestMapping(value = "/sendMessage",method=RequestMethod.POST)
    public String email(Model model,String email,String name,String tel,String yanZheng)throws Exception {
        System.out.println(email);
        UserBean user = new UserBean();
        user.setName(name);
        user.setTel(tel);
        user.setEmail(email);
        model.addAttribute("user", user);
        UserBean userEmail = userService.findUserByEmail(email);
        if (userEmail != null){
            System.out.println("already 存在");
            return "userHave";
        }
        if (email == null){
            return "errorEmail";
        }
        else if (userEmail == null && email != null){
            Properties properties = new Properties();
            properties.setProperty("mail.transport.protocol", "smtp");//发送邮件协议
            properties.setProperty("mail.smtp.auth", "true");//需要验证
            //properties.setProperty("mail.debug", "true");//设置debug模式 后台输出邮件发送的过程
            Session session = Session.getInstance(properties);
            session.setDebug(true);//debug模式
            //邮件信息
            Message messgae = new MimeMessage(session);
            messgae.setFrom(new InternetAddress("2574507872@qq.com"));//设置发送人
            messgae.setText("你的验证码为：" + yanZheng + "。请注意，验证码有效时间为2分钟！！！");//设置邮件内容
            messgae.setSubject("邮箱验证");//设置邮件主题
            //发送邮件
            Transport tran = session.getTransport();
            //tran.connect("smtp.qq.com", 25, "邮箱账户", "邮箱授权码");//连接到新浪邮箱服务器
            tran.connect("smtp.qq.com", 587, "2574507872@qq.com", "stwadydrnjstecgc");//连接到QQ邮箱服务器
            tran.sendMessage(messgae, new Address[]{new InternetAddress(email)});//设置邮件接收人
            tran.close();
            return "register";
        }
        return "register";
    }

    @RequestMapping("/findEmail")
    public String findEmail(Model model,UserBean user){
        UserBean userEmail = userService.findUserByEmail(user.getEmail());
        if(userEmail == null){
            return "noUser";
        }
        else {
            model.addAttribute("user",userEmail);
            return "forgetTwo";
        }
    }

    @RequestMapping("/reEdit")
    public String reEdit(UserBean user){
        userService.updateUser(user);
        return "registerTwo";
    }

    @RequestMapping(value = "/sendMessageTwo",method=RequestMethod.POST)
    public String emailTwo(Model model,String email,String yanZheng)throws Exception {
        System.out.println(email);
        UserBean user = new UserBean();
        user.setEmail(email);
        model.addAttribute("user", user);
        UserBean userEmail = userService.findUserByEmail(email);
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");//发送邮件协议
        properties.setProperty("mail.smtp.auth", "true");//需要验证
        //properties.setProperty("mail.debug", "true");//设置debug模式 后台输出邮件发送的过程
        Session session = Session.getInstance(properties);
        session.setDebug(true);//debug模式
        //邮件信息
        Message messgae = new MimeMessage(session);
        messgae.setFrom(new InternetAddress("2574507872@qq.com"));//设置发送人
        messgae.setText("你的验证码为：" + yanZheng + "。请注意，验证码有效时间为2分钟！！！");//设置邮件内容
        messgae.setSubject("邮箱验证");//设置邮件主题
        //发送邮件
        Transport tran = session.getTransport();
        //tran.connect("smtp.qq.com", 25, "邮箱账户", "邮箱授权码");//连接到新浪邮箱服务器
        tran.connect("smtp.qq.com", 587, "2574507872@qq.com", "stwadydrnjstecgc");//连接到QQ邮箱服务器
        tran.sendMessage(messgae, new Address[]{new InternetAddress(email)});//设置邮件接收人
        tran.close();
        return "forget";
    }
}