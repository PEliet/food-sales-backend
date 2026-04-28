package com.foodsales.controller;

import com.foodsales.model.Address;
import com.foodsales.service.AddressService;
import com.foodsales.util.ResponseResult;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired private AddressService addressService;

    @GetMapping
    public ResponseResult<List<Address>> list(HttpServletRequest request) {
        return ResponseResult.success(addressService.getUserAddresses((Integer)request.getAttribute("userId")));
    }

    @PostMapping("/add")
    public ResponseResult<Address> add(@RequestBody Address address, HttpServletRequest request) {
        address.setUserId((Integer)request.getAttribute("userId"));
        return ResponseResult.success(addressService.addAddress(address));
    }

    @PutMapping("/update")
    public ResponseResult<Address> update(@RequestBody Address address) {
        return ResponseResult.success(addressService.updateAddress(address));
    }

    @DeleteMapping("/{id}")
    public ResponseResult<Void> delete(@PathVariable Integer id, HttpServletRequest request) {
        addressService.deleteAddress(id, (Integer)request.getAttribute("userId"));
        return ResponseResult.success(null);
    }

    @PutMapping("/default")
    public ResponseResult<Void> setDefault(@RequestParam Integer addressId, HttpServletRequest request) {
        addressService.setDefault(addressId, (Integer)request.getAttribute("userId"));
        return ResponseResult.success(null);
    }
}