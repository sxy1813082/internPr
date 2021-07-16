package com.example.test.controller;

import com.example.test.bean.CommitBean;
import com.example.test.bean.UserBean;
import com.example.test.bean.YanBean;
import com.example.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

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

    @RequestMapping("/shenBao")
    public String shenBao(Model model,Integer id,HttpSession session){
        String Id = id.toString();
        System.out.println(Id);
        List<CommitBean> commit = userService.findCommit(Id);
        if (commit != null){
            session.setAttribute("commits",commit);
            return "shenBao";
        }
        else {
            return "login";
        }

    }

    @RequestMapping("/downLoad")
    public String downLoad(Integer id , String src){
        return "redirect:/shenBao";
    }

    @RequestMapping(value = "/loginIn",method = RequestMethod.POST)
    public String login(HttpServletRequest hsr,HttpSession session) {
        String nameIt = hsr.getParameter("name");
        String passwordIt = hsr.getParameter("password");
        System.out.println(nameIt);
        System.out.println(passwordIt);
        UserBean user = userService.loginIn(nameIt,passwordIt);
        if(user!=null){
           // model.addAttribute("user", user);
            session.setAttribute("user",user);
            System.out.println(user.getId());
            return "success";
        }
        else {
            return "login";
        }
    }

    @RequestMapping("toManagement")
    public String toManagement(){
        return "management";
    }


    @RequestMapping(value="/loginInManager",method = RequestMethod.POST)
    public String loginManager(HttpServletRequest hsr,HttpSession session) {
        String nameIt = hsr.getParameter("name");
        String passwordIt = hsr.getParameter("password");
        UserBean userBean = userService.loginInManager(nameIt, passwordIt);
        List<UserBean> users = userService.findByAll();
        List<CommitBean> commits = userService.findAllCommit();
        if (userBean !=null){
            session.setAttribute("users", users);
            System.out.println(userBean.getId());
            session.setAttribute("commits",commits);
            return "userList";
        }
        else {
            return "management";
        }
    }

    @RequestMapping("toRegister")
    public String toRegister(Model model){
        UserBean user = new UserBean();
        model.addAttribute("user", user);
        return "register";
    }

    @RequestMapping("commit")
    @ResponseBody
    public String toCommit(Model model,@RequestParam(value = "file")MultipartFile file,
                           @RequestParam(value = "userId")String userId,
                           @RequestParam(value = "ComName")String ComName,
                           @RequestParam(value = "name")String name,
                           @RequestParam(value = "tel")String tel,
                           @RequestParam(value = "job")String job,
                           @RequestParam(value = "exp")String exp,
                           @RequestParam(value = "state")String state){
        if (file.isEmpty()) {
            System.out.println("文件为空空");
        }
        String fileName = file.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        String filePath = "D://temp-rainy//"; // 上传后的路径
        fileName = UUID.randomUUID() + suffixName; // 新文件名
        File dest = new File(filePath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String filename = "img/" + fileName;
        //插入数据，实例化一个对象
        CommitBean commitBean = new CommitBean();
        commitBean.setFileSrc(filename);
        commitBean.setUserId(userId);
        commitBean.setComName(ComName);
        commitBean.setName(name);
        commitBean.setTel(tel);
        commitBean.setJob(job);
        commitBean.setExp(exp);
        commitBean.setState(state);
        //插入表单数据
        int a = userService.commit(commitBean);
        System.out.println(a);
        //把id传递给查看表格的页面
       // model.addAttribute("id",userId);
        return "success";
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
        //下面这个转移到上一个接口上了
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