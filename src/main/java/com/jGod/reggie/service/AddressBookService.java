package com.jGod.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jGod.reggie.entity.AddressBook;


public interface AddressBookService extends IService<AddressBook> {

    public void setDefault(Long id);
}
