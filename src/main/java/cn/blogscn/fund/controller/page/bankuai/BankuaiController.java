package cn.blogscn.fund.controller.page.bankuai;

import cn.blogscn.fund.entity.bankuai.Bankuai;
import cn.blogscn.fund.service.bankuai.BankuaiService;
import cn.blogscn.fund.util.JsonResult;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bk")
public class BankuaiController {

    @Autowired
    private BankuaiService bankuaiService;

    @GetMapping("/list")
    public JsonResult<List<Bankuai>> list() {
        return JsonResult.success(bankuaiService.listByDegreeDesc());
    }

    @GetMapping("/updateStartAndEndDay")
    public JsonResult<String> updateStartAndEndDay() {
        return bankuaiService.updateStartAndEndDay() ? JsonResult.success("success")
                : JsonResult.error("false");
    }
}
