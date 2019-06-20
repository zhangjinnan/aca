package controller;

import ga.AcaCore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import com.alibaba.fastjson.JSON;

@EnableAutoConfiguration
@RestController
public class AcaController {
    @RequestMapping("/{iteratorNum}/{antNum}")
    public String sayHello(@PathVariable Integer iteratorNum,@PathVariable Integer antNum) {
        AcaCore ac=new AcaCore();
        HashMap<Integer, List> map=ac.exe(iteratorNum,antNum);
        return JSON.toJSONString(map);
       // return "Hello,World!";
    }
}
