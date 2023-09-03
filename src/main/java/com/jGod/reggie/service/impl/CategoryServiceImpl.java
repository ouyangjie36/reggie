package com.jGod.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jGod.reggie.common.CustomException;
import com.jGod.reggie.entity.Category;
import com.jGod.reggie.entity.Dish;
import com.jGod.reggie.entity.Setmeal;
import com.jGod.reggie.mapper.CategoryMapper;
import com.jGod.reggie.service.CategoryService;
import com.jGod.reggie.service.DishService;
import com.jGod.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(Long id) {
        //查询当前分类是否关联菜品，关联则抛出业务异常
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(Dish::getCategoryId,id);
        if(dishService.count(lambdaQueryWrapper)>0){
            throw new CustomException("当前分类已关联菜品，不能删除");
        }
        //查询当前分类是否关联套餐，关联则抛出业务异常
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(Setmeal::getCategoryId,id);
        if(setmealService.count(lambdaQueryWrapper1)>0){
            throw new CustomException("当前分类已关联套餐，不能删除");
        }
        super.removeById(id);
    }
}
