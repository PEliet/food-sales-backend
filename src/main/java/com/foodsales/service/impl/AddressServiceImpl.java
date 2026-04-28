package com.foodsales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.foodsales.mapper.AddressMapper;
import com.foodsales.model.Address;
import com.foodsales.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired private AddressMapper addressMapper;

    @Override
    public List<Address> getUserAddresses(Integer userId) {
        return addressMapper.selectList(new LambdaQueryWrapper<Address>().eq(Address::getUserId, userId));
    }

    @Override @Transactional
    public Address addAddress(Address address) {
        if (address.getIsDefault() == 1) {
            addressMapper.update(null, new LambdaUpdateWrapper<Address>()
                    .eq(Address::getUserId, address.getUserId()).set(Address::getIsDefault, 0));
        }
        address.setCreateTime(LocalDateTime.now());
        addressMapper.insert(address);
        return address;
    }

    @Override @Transactional
    public Address updateAddress(Address address) {
        if (address.getIsDefault() == 1) {
            addressMapper.update(null, new LambdaUpdateWrapper<Address>()
                    .eq(Address::getUserId, address.getUserId())
                    .ne(Address::getAddressId, address.getAddressId())
                    .set(Address::getIsDefault, 0));
        }
        addressMapper.updateById(address);
        return address;
    }

    @Override public void deleteAddress(Integer addressId, Integer userId) {
        addressMapper.delete(new LambdaQueryWrapper<Address>().eq(Address::getAddressId, addressId).eq(Address::getUserId, userId));
    }

    @Override @Transactional
    public void setDefault(Integer addressId, Integer userId) {
        addressMapper.update(null, new LambdaUpdateWrapper<Address>()
                .eq(Address::getUserId, userId).set(Address::getIsDefault, 0));
        Address addr = addressMapper.selectById(addressId);
        if (addr != null) { addr.setIsDefault(1); addressMapper.updateById(addr); }
    }
}
