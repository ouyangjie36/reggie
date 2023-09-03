package com.jGod.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jGod.reggie.common.CustomException;
import com.jGod.reggie.dto.SetmealDto;
import com.jGod.reggie.entity.Dish;
import com.jGod.reggie.entity.Setmeal;
import com.jGod.reggie.entity.SetmealDish;
import com.jGod.reggie.mapper.SetmealMapper;
import com.jGod.reggie.service.SetmealDishService;
import com.jGod.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);
        List<SetmealDish> list = setmealDto.getSetmealDishes();
        Long setmealId = setmealDto.getId();
        list = list.stream().map((item)->{
            item.setSetmealId(setmealId);
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(list);
    }

    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Setmeal::getId,ids).eq(Setmeal::getStatus,1);
        int count = count(lambdaQueryWrapper);
        if(count>0){
            throw new CustomException("套餐正在售卖中，不能删除");
        }
        removeByIds(ids);
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(lambdaQueryWrapper1);
    }

    @Override
    public SetmealDto getByIdWithDish(Long id) {
        Setmeal setmeal = getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SetmealDish::getSetmealId,setmeal.getId());
        lambdaQueryWrapper.orderByAsc(SetmealDish::getSort).orderByDesc(SetmealDish::getUpdateTime);
        setmealDto.setSetmealDishes(setmealDishService.list(lambdaQueryWrapper));
        return setmealDto;
    }

    @Override
    public void changeStatus(Integer action,List<Long> ids) {
        for (Long id : ids) {
            UpdateWrapper<Setmeal> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",id);
            updateWrapper.set("status",action);
            update(updateWrapper);
        }
    }
}
