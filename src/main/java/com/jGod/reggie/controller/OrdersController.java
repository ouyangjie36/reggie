package com.jGod.reggie.controller;

import com.jGod.reggie.common.BaseContext;
import com.jGod.reggie.common.R;
import com.jGod.reggie.entity.Orders;
import com.jGod.reggie.service.OrderDetailService;
import com.jGod.reggie.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        ordersService.submit(orders);
        return R.success("提交订单成功");
    }
}
