package com.foodsales.service;

import com.foodsales.dto.*;
import com.foodsales.model.*;
import com.foodsales.dto.PageDTO;
import java.util.List;
import java.util.Map;

public interface AddressService {
    java.util.List<Address> getUserAddresses(Integer userId);
    Address addAddress(Address address);
    Address updateAddress(Address address);
    void deleteAddress(Integer addressId, Integer userId);
    void setDefault(Integer addressId, Integer userId);
}