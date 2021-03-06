package com.test.controller;

import com.test.model.UserEntity;
import com.test.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Administrator on 2018/9/15.
 */
@Controller
public class MainController {

    // @Autowired自动装配数据库接口，不需要再写原始的Connection来操作数据库
    @Autowired
    UserRepository userRepository;

    //首页
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getIndex() {
        System.out.println("run...");
        return "index";
    }

    @RequestMapping(value = "/json", method = RequestMethod.GET)
    @ResponseBody
    public UserEntity getInJSON() {
        System.out.println("-----请求json数据--------");
        List<UserEntity> userList = userRepository.findAll();
        if (userList != null && userList.size()>0){
            userList.get(0).setBlogById(null);//
            return userList.get(0);
        }
        return new UserEntity();
    }



    //查询所有用户
    @RequestMapping(value = "/admin/users", method = RequestMethod.GET)
    public String getUsers(ModelMap modelMap) {
        List<UserEntity> userList = userRepository.findAll();  // 查询user表中所有记录
        modelMap.addAttribute("userList", userList);  // 将所有记录传递给要返回的jsp页面，放在userList当中
        return "admin/users";  // 返回pages目录下的admin/users.jsp页面给客户端（浏览器）
    }

    //添加用户
    @RequestMapping(value = "/admin/users/add", method = RequestMethod.GET)
    public String addUser() {
        return "admin/addUser";  //注意：addUser.jsp表单中的名称必须和Entity类的名称一致。
    }

    // post请求，处理添加用户请求，并重定向到用户管理页面
    @RequestMapping(value = "/admin/users/addP", method = RequestMethod.POST)
    public String addUserPost(@ModelAttribute("user") UserEntity userEntity) {
        // 注意此处，post请求传递过来的是一个UserEntity对象，里面包含了该用户的信息
        // 通过@ModelAttribute()注解可以获取传递过来的'user'，并创建这个对象
        //userRepository.save(userEntity);  // 数据库中添加一个用户，该步暂时不会刷新缓存
        userRepository.saveAndFlush(userEntity);  // 数据库中添加一个用户，并立即刷新缓存
        return "redirect:/admin/users";  // 重定向到用户管理页面，方法为 redirect:url
    }

    // 查看用户详情
    // @PathVariable可以收集url中的变量，需匹配的变量用{}括起来
    // 例如：访问 localhost:8080/admin/users/show/1 ，将匹配 id = 1
    @RequestMapping(value = "/admin/users/show/{id}", method = RequestMethod.GET)
    public String showUser(@PathVariable("id") Integer userId, ModelMap modelMap) {
        UserEntity userEntity = userRepository.findOne(userId);  // 找到userId所表示的用户
        modelMap.addAttribute("user", userEntity);
        return "admin/userDetail";
    }

    // 更新用户信息 页面
    @RequestMapping(value = "/admin/users/update/{id}", method = RequestMethod.GET)
    public String updateUser(@PathVariable("id") Integer userId, ModelMap modelMap) {
        UserEntity userEntity = userRepository.findOne(userId);
        modelMap.addAttribute("user", userEntity);
        return "admin/updateUser";
    }

    // 更新用户信息 操作
    @RequestMapping(value = "/admin/users/updateP", method = RequestMethod.POST)
    public String updateUserPost(@ModelAttribute("user") UserEntity user) {
        userRepository.updateUser(user.getNickname(), user.getFirstName(), user.getLastName(), user.getPassword(), user.getId());
        userRepository.flush(); // 刷新缓冲区
        return "redirect:/admin/users";
    }

    // 删除用户
    @RequestMapping(value = "/admin/users/delete/{id}", method = RequestMethod.GET)
    public String deleteUser(@PathVariable("id") Integer userId) {
        userRepository.delete(userId);
        userRepository.flush();
        return "redirect:/admin/users";
    }

}

