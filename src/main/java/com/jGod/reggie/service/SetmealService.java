package com.jGod.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jGod.reggie.dto.SetmealDto;
import com.jGod.reggie.entity.Setmeal;
import lombok.With;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    public void saveWithDish(SetmealDto setmealDto);

    public void removeWithDish(List<Long> ids);

    public SetmealDto getByIdWithDish(Long id);

    public void changeStatus(Integer action,List<Long> ids);
}
