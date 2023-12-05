package com.ead.authuser.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
/* RefreshScope serve para atualizar as informações do banco sem ter reiniciar a api.*/
@RefreshScope
public class RefreshScopeController {

    //recebe o valor que está definido no arquivo de configuração.
    @Value("${authuser.refreshscope.name}")
    private String name;

    @RequestMapping("/refreshscope")
    public String refreshscope() {
        return this.name;
    }

}
