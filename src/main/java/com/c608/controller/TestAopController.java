package com.c608.controller;

import com.c608.common.annotation.Log;
import com.c608.entity.UserEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/testAop")
public class TestAopController {


    @Log
    @GetMapping("/get/{id}")
    public UserEntity update(@PathVariable("id") Long id){
        UserEntity entity = new UserEntity();
        entity.setId(id);
        entity.setName("ekko");
        return entity;
    }


}
