package com.nowcoder.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

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


}
