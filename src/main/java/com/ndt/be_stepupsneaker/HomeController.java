package com.ndt.be_stepupsneaker;

import com.ndt.be_stepupsneaker.util.ResponseHelper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("")
    public Object home(){
        return ResponseHelper.getResponse("Đồ Án Tốt Nghiệp StepUpSneaker của 4 anh em Nguyên Tuấn Đức Duy", HttpStatus.OK);
    }
}
