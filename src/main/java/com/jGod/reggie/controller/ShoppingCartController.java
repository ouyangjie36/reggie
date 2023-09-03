package com.jGod.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jGod.reggie.common.BaseContext;
import com.jGod.reggie.common.R;
import com.jGod.reggie.dto.DishDto;
import com.jGod.reggie.entity.Dish;
import com.jGod.reggie.entity.ShoppingCart;
import com.jGod.reggie.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,userId);
        lambdaQueryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        return R.success(shoppingCartService.list(lambdaQueryWrapper));
    }

    @PostMapping("/add")
    public R<ShoppingCart> save(@RequestBody ShoppingCart shoppingCart){
        shoppingCart.setUserId(BaseContext.getCurrentId());
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,shoppingCart.getUserId());
        lambdaQueryWrapper.eq(shoppingCart.getDishId()!=null,ShoppingCart::getDishId,shoppingCart.getDishId());
        lambdaQueryWrapper.eq(shoppingCart.getSetmealId()!=null,ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        ShoppingCart shoppingCart1 = shoppingCartService.getOne(lambdaQueryWrapper);
        if(shoppingCart1 == null){
            shoppingCart1 = shoppingCart;
            shoppingCart1.setNumber(1);
            shoppingCart1.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart1);
        }else{
            shoppingCart1.setNumber(shoppingCart1.getNumber()+1);
            shoppingCartService.updateById(shoppingCart1);
        }
        return R.success(shoppingCart1);
    }

    @PostMapping("/sub")
    public R<ShoppingCart> remove(@RequestBody ShoppingCart shoppingCart){
        shoppingCart.setUserId(BaseContext.getCurrentId());
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,shoppingCart.getUserId());
        lambdaQueryWrapper.eq(shoppingCart.getDishId()!=null,ShoppingCart::getDishId,shoppingCart.getDishId());
        lambdaQueryWrapper.eq(shoppingCart.getSetmealId()!=null,ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        ShoppingCart shoppingCart1 = shoppingCartService.getOne(lambdaQueryWrapper);
        if(shoppingCart1 == null){
            R.error("菜品或套餐不存在");
        }else{
            shoppingCart1.setNumber(shoppingCart1.getNumber()-1);
            if(shoppingCart1.getNumber()==0){
                shoppingCartService.removeById(shoppingCart1.getId());
            }else{
                shoppingCartService.updateById(shoppingCart1);
            }
        }
        return R.success(shoppingCart1);
    }

    @DeleteMapping("/clean")
    public R<String> clean(){
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,userId);
        shoppingCartService.remove(lambdaQueryWrapper);
        return R.success("清空购物车成功");
    }
}
