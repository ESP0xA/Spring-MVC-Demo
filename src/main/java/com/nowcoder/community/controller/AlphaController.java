package com.nowcoder.community.controller;

import org.apache.tomcat.util.digester.ArrayStack;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping("/alpha")
public class AlphaController {

    // HttpServletRequest 和 HttpServletResponse对象分别获取浏览器请求数据和服务器返回数据

    @RequestMapping("/http")
    // 没有返回值是因为 httpResponse 对象可以直接向浏览器输出数据，不需要依赖返回值
    // Request和Response对象是两个接口，常用的接口是：HttpServletRequest和HttpServletResponse
    // 直接在方法上声明这两个对象，DispatcherServlet 在调用方法时就会自动将它们传递进来
    public void http(HttpServletRequest request,
                     HttpServletResponse response)  {

        // request获取请求数据

        System.out.println(request.getMethod());                        // 请求方法
        System.out.println(request.getServletPath());                   // 请求路径
        Enumeration<String> enumeration = request.getHeaderNames();     // 所有请求头的key
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();                    // 当前header的key
            String value = request.getHeader(name);                     // 当前header的value
            System.out.println(name + ": " + value);
        }
        // 请求体
        System.out.println(request.getParameter("code"));

        // response返回响应数据

        // 设置返回类型
        response.setContentType("text/html;charset=utf-8");
        // 通过response封装的输出流向浏览器输出
        try(
                PrintWriter printWriter = response.getWriter();         // 写在小括号里，编译的时候会自动加finally然后close
        ) {
            // 向浏览器打印网页
            printWriter.write("<h1>Spring MVC Demo</h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 比HttpServletRequest和HttpServletResponse更简便的方式获取数据

    // GET请求

    // 参数写在url参数值中
    @RequestMapping(path = "/students", method = RequestMethod.GET) // 指定get请求
    @ResponseBody
    // 可以直接在方法括号中写上请求参数的参数名，DispatcherServlet会自动匹配传参: (int current, int limit)
    public String getStudent(@RequestParam(name = "current", required = false, defaultValue = "1") int current,
                             @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) {
        System.out.println(current);
        System.out.println(limit);
        return "some students";
    }

    // 参数写在url路径中
    @RequestMapping(path = "/student/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(@PathVariable("id") int id) {
        System.out.println(id);
        return "a student";
    }

    // POST请求

    @RequestMapping(path = "/student",method = RequestMethod.POST)
    @ResponseBody
    // 直接在方法括号内写入和html中属性值对应的参数，完成POST请求中参数的传递（front-end to back-end）
    public String saveStudent(String name, int age) {
        System.out.println(name);
        System.out.println(age);
        return "success!";
    }

    // 向浏览器响应HTML数据（模板）的两种方法: 返回ModelAndView或View

    @RequestMapping(path = "/teacher", method = RequestMethod.GET)
    // 不加@ResponseBody注解默认返回html
    // ModelAndView表示Controller给DispatcherServlet返回Model和View两种数据
    public ModelAndView getTeacher() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("name", "张三");
        modelAndView.addObject("age", 30);
        // 模板的路径和名字(从templates起始)
        modelAndView.setViewName("/demo/view");  // thymeleaf 默认模板后缀为html，所以这里不用写后缀
        return modelAndView;
    }

    @RequestMapping(path = "/school", method = RequestMethod.GET)
    // Controller给DispatcherServlet返回Model，而View数据直接返回
    public String getSchool(Model model) {              // DispatcherServlet在调用方法时，自动实例化model对象并且传进方法中
                                                        // DispatcherServlet持有model这个bean的引用，所以在方法中给model注入数据
                                                        // DispatcherServlet可以得到
        model.addAttribute("name", "北京大学");
        model.addAttribute("age", 122);

        return "/demo/view";    // 返回一个路径
    }

    // 向浏览器响应json数据（通常在异步请求中）

    // java对象 -> js对象(浏览器使用)                    X
    // java对象 -> json字符串 -> js对象(浏览器使用)       √

    // 返回一个Map<String, Object>
    @RequestMapping(path = "/employee", method = RequestMethod.GET)
    // 为了返回Json对象，需要加上@ResponseBody
    @ResponseBody
    // DispatcherServlet在调用这个方法的时候，会自动将Map<String, Object>类型返回值转换为Json字符串
    public Map<String, Object> getEmp() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "张三");
        map.put("age", 23);
        map.put("salary", 8000.00);
        return map;
    }

    // 返回一个List<Map<String, Object>>
    @RequestMapping(path = "/employees", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getEmps() {
        List<Map<String, Object>> list = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();
        map.put("name", "张三");
        map.put("age", 23);
        map.put("salary", 8000.00);
        list.add(map);

        map = new HashMap<>();
        map.put("name", "李四");
        map.put("age", 24);
        map.put("salary", 9000.00);
        list.add(map);

        map = new HashMap<>();
        map.put("name", "王五");
        map.put("age", 25);
        map.put("salary", 10000.00);
        list.add(map);

        return list;
    }

}
