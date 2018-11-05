package com.jesse.onecake.biz;

import com.jesse.onecake.biz.base.BaseBiz;
import com.jesse.onecake.common.config.security.UserUtils;
import com.jesse.onecake.entity.Cart;
import com.jesse.onecake.entity.CartDetail;
import com.jesse.onecake.entity.User;
import com.jesse.onecake.mapper.CartDetailMapper;
import com.jesse.onecake.mapper.CartMapper;
import com.jesse.onecake.mapper.UserMapper;
import com.jesse.onecake.service.generator.id.provider.IdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class CartBiz extends BaseBiz<CartMapper,Cart> {
    @Autowired private UserMapper userMapper;
    @Autowired private CartDetailMapper cartDetailMapper;
    @Autowired private IdService idService;

    @Transactional
    public void addCart(String cakeId, Integer quantity) {
        String userName = UserUtils.getUserName();
        User user = userMapper.findByName(userName);
        Cart cart = this.mapper.findCartByUserId(user.getId().toString());
        //购物车不为空
        if (cart != null) {
            //查询购物车详情
            List<CartDetail>  cartDetail = cartDetailMapper.findCartDetailByUserId(user.getId().toString());
            boolean alreadyExist = false;
            //判断是否包含将要添加商品的id
            for (CartDetail detail : cartDetail) {
                //包含
                if (detail.getCakeId() != null && cakeId.equals(detail.getCakeId())) {
                    detail.setQuantity(detail.getQuantity() + quantity);
                    detail.setUpdateDate(new Date());
                    alreadyExist = true;
                    try{
                        cartDetailMapper.updateByPrimaryKeySelective(detail);
                    }catch (RuntimeException e){
                        throw new RuntimeException(e);
                    }
                    break;
                }
            }
            //不包含，新建一条
            if (!alreadyExist) {
                CartDetail extenalDetail = new CartDetail();
                extenalDetail.setCakeId(cakeId);
                extenalDetail.setQuantity(quantity);
                extenalDetail.setCartId(cart.getId().toString());
                extenalDetail.setId(idService.genId());
                extenalDetail.setCreateDate(new Date());
                try{
                    cartDetailMapper.insertSelective(extenalDetail);
                }catch (RuntimeException e){
                    throw new RuntimeException(e);
                }
            }
            //购物车为空
        } else {
            Cart externalCart = new Cart();
            externalCart.setId(idService.genId());
            externalCart.setUserId(user.getId().toString());
            externalCart.setCreateDate(new Date());
            externalCart.setCreateUser(userName);
            CartDetail externalDetail = new CartDetail();
            externalDetail.setId(idService.genId());
            externalDetail.setCartId(externalCart.getId().toString());
            externalDetail.setCakeId(cakeId);
            externalDetail.setQuantity(quantity);
            externalDetail.setCreateDate(new Date());
            try{
                this.mapper.insertSelective(externalCart);
                cartDetailMapper.insertSelective(externalDetail);
            }catch (RuntimeException e){
                throw new RuntimeException(e);
            }
        }

    }
}
