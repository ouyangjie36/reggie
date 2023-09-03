package com.jGod.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jGod.reggie.common.BaseContext;
import com.jGod.reggie.common.R;
import com.jGod.reggie.entity.AddressBook;
import com.jGod.reggie.service.AddressBookService;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    @PostMapping
    public R<String> save(@RequestBody AddressBook addressBook){
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookService.save(addressBook);
        return R.success("添加地址成功");
    }

    @GetMapping("/list")
    public R<List<AddressBook>> list(){
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<AddressBook> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AddressBook::getUserId,userId);
        return R.success(addressBookService.list(lambdaQueryWrapper));
    }

    @PutMapping("/default")
    public R<String> setDefault(@RequestBody AddressBook addressBook){
        addressBookService.setDefault(addressBook.getId());
        return R.success("设置默认地址成功");
    }

    @GetMapping("/default")
    public R<AddressBook> getDefault(){
        LambdaQueryWrapper<AddressBook> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        lambdaQueryWrapper.eq(AddressBook::getIsDefault,1);
        return R.success(addressBookService.getOne(lambdaQueryWrapper));
    }
}
