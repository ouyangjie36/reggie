package com.jGod.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jGod.reggie.common.CustomException;
import com.jGod.reggie.common.R;
import com.jGod.reggie.dto.DishDto;
import com.jGod.reggie.entity.Category;
import com.jGod.reggie.entity.Dish;
import com.jGod.reggie.entity.SetmealDish;
import com.jGod.reggie.service.CategoryService;
import com.jGod.reggie.service.DishService;
import com.jGod.reggie.service.SetmealDishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SetmealDishService setmealDishService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        dishService.saveWithFlavor(dishDto);
        return R.success("录入菜品成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<Dish> pageInfo = new Page(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(name != null, Dish::getName, name);
        lambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo, lambdaQueryWrapper);

        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
        List<DishDto> list = pageInfo.getRecords().stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            long categoryId = item.getCategoryId();
            Category c = categoryService.getById(categoryId);
            if (c != null) dishDto.setCategoryName(c.getName());
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
        return R.success("录入菜品成功");
    }

    @PostMapping("/status/{action}")
    public R<String> changeStatus(@PathVariable Integer action,@RequestParam List<Long> ids){
        dishService.changeStatus(action,ids);
        return R.success(action==0?"停售":"起售"+"成功");
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getDishId,ids);
        lambdaQueryWrapper.eq(SetmealDish::getIsDeleted,0);
        int num = setmealDishService.count(lambdaQueryWrapper);
        if(num>0){
            throw new CustomException("菜品关联套餐");
        }
        dishService.removeByIds(ids);
        return R.success("删除成功");
    }

    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        return R.success(dishService.list(dish));
    }

}
