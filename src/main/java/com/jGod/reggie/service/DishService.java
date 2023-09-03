package com.jGod.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jGod.reggie.dto.DishDto;
import com.jGod.reggie.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    public void saveWithFlavor(DishDto dishDto);
    public DishDto getByIdWithFlavor(Long id);
    public void updateWithFlavor(DishDto dishDto);
    public void changeStatus(Integer action, List<Long> ids);

    public List<DishDto> list(Dish dish);
}
