package com.chatting.domains.chatting.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
public class ChattingController {

    @RequestMapping("/chat")
    public String chat(Model model){
        model.addAttribute("name","aejeong");
        return "chat";
    }

}
