package com.jGod.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jGod.reggie.common.BaseContext;
import com.jGod.reggie.entity.AddressBook;
import com.jGod.reggie.mapper.AddressBookMapper;
import com.jGod.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
    @Override
    @Transactional
    public void setDefault(Long id) {
        Long userId = BaseContext.getCurrentId();
        UpdateWrapper<AddressBook> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("is_default",0);
        updateWrapper.eq("user_id",userId);
        update(updateWrapper);
        UpdateWrapper<AddressBook> updateWrapper1 = new UpdateWrapper<>();
        updateWrapper1.set("is_default",1);
        updateWrapper1.eq("id",id);
        update(updateWrapper1);
    }
}
