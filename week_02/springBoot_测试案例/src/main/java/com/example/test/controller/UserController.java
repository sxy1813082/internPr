package com.example.test.controller;

import com.example.test.bean.User;
import com.example.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    //查询所有的user数据
    @RequestMapping("/findByAll")
    public String index(Model model) {
        List<User> list = userService.findByAll();
        model.addAttribute("users",list);
        return "userList";
    }

    @RequestMapping("/manager")
    public String list(Model model) {
        List<User> users = userService.findByAll();
        model.addAttribute("users", users);
        return "userList";
    }

    //保存user数据
    @RequestMapping("/toAdd")
    public String toAdd() {
        return "userAdd";
    }

    @RequestMapping("/add")
    public String add(User user) {
        userService.saveUser(user);
        return "redirect:/manager";
    }

    @RequestMapping("toRegister")
    public String toRegister(){
        return "register";
    }

    @RequestMapping("/Register")
    public String Register(User user){
        userService.Register(user);
        return "success";
    }

    //修改user数据
    @RequestMapping("/toEdit")
    public String toEdit(Model model, Integer id) {
        User user = userService.findUserById(id);
        model.addAttribute("user", user);
        return "userEdit";
    }

    @RequestMapping("/edit")
    //@ResponseBody
    public String edit(User user) {
        userService.updateUser(user);
        return "redirect:/manager";
    }

    //删除user数据
    @RequestMapping("/delete")
    public String delete(Integer id) {
        userService.deleteUser(id);
        return "redirect:/manager";
    }


}
