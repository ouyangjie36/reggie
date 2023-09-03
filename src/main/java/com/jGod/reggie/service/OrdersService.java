package com.jGod.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jGod.reggie.entity.Orders;
import org.springframework.transaction.annotation.Transactional;

public interface OrdersService extends IService<Orders> {
    @Transactional
    public void submit(Orders orders);
}
