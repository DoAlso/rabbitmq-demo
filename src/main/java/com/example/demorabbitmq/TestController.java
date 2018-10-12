package com.example.demorabbitmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@RestController
public class TestController {

    @Autowired
    private RestTemplate restTemplate;


    //当前会话状态API
    @GetMapping("/test1")
    public String test1(){
        Map<String,Object> map = new HashMap<>();
        String string = restTemplate.getForEntity("http://49.4.65.228//v2/audio/call_status.json",String.class,map).getBody();
        return string;
    }

    //注册终端的状态信息
    @GetMapping("/test2")
    public String test2(@RequestParam("connected_eq") String param){
        String string = restTemplate.getForEntity("http://49.4.65.228/v2/audio/terminals_status.json",String.class,param).getBody();
        return string;
    }


    //发起监听
    @GetMapping("/test3")
    public String test3(@RequestParam("send_terminal") String sendNo, @RequestParam("recive_terminal")String reciveNo){
        String string = restTemplate.getForEntity("http://49.4.65.228/v2/audio/monitor.json",String.class,sendNo,reciveNo).getBody();
        return string;
    }


    //监听流链接
    @GetMapping("/test4")
    public String test4(@RequestParam("send_terminal") String sendNo, @RequestParam("recive_terminal")String reciveNo){
        String string = restTemplate.getForEntity("http://49.4.65.228/v2/audio/monitor_stream_url.json",String.class,sendNo,reciveNo).getBody();
        return string;
    }

    //发起广播喊话(一键广播)
    @GetMapping("/test5")
    public String test5(@RequestParam("send_terminal") String sendNo, @RequestParam("recive_terminal")String reciveNo){
        String string = restTemplate.getForEntity("http://49.4.65.228/v2/audio/call.json",String.class,sendNo,reciveNo).getBody();
        return string;
    }

    //发起对讲会话
    @GetMapping("/test6")
    public String test6(@RequestParam("send_terminal") String sendNo, @RequestParam("recive_terminal")String reciveNo){
        String string = restTemplate.getForEntity("http://49.4.65.228/v2/audio/talk.json",String.class,sendNo,reciveNo).getBody();
        return string;
    }

    //会话强拆,为用户提供结束会话状态功能。
    @GetMapping("/test7")
    public String test7(@RequestParam("id") String callid){
        String string = restTemplate.getForEntity("http://49.4.65.228/v2/audio/call_close.json",String.class,callid).getBody();
        return string;
    }

    //开启消息队列状态发送
    @GetMapping("/test8")
    public String test8(){
        Map<String,Object> map = new HashMap<>();
        String string = restTemplate.getForEntity("http://49.4.65.228/v2/audio/start_get_terminal_status.json",String.class,map).getBody();
        return string;
    }

    //关闭消息队列状态发送
    @GetMapping("/test9")
    public String test9(){
        Map<String,Object> map = new HashMap<>();
        String string = restTemplate.getForEntity("http://49.4.65.228/v2/audio/stop_get_terminal_status.json",String.class,map).getBody();
        return string;
    }

    //对讲转接
    @GetMapping("/test10")
    public String test10(@RequestParam("uuid") String uuid){
        String string = restTemplate.getForEntity("http://49.4.65.228/v2/audio/call_transfer.json",String.class,uuid).getBody();
        return string;
    }


    //查询指定注册终端信息
    @GetMapping("/test11")
    public String test11(@RequestParam("query_flag") String flag, @RequestParam("query_var") String var){
        String string = restTemplate.getForEntity("http://49.4.65.228/v2/audio/terminals.json",String.class,flag,var).getBody();
        return string;
    }


}
