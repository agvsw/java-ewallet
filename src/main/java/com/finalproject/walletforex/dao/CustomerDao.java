package com.finalproject.walletforex.dao;

import com.finalproject.walletforex.dto.CustomerDto;
import com.finalproject.walletforex.exception.AccountNotFoundException;
import com.finalproject.walletforex.exception.InvalidUsernameOrPasswordException;
import com.finalproject.walletforex.exception.UserAlreadyException;
import com.finalproject.walletforex.model.Customer;

import java.util.List;

public interface CustomerDao {
    Customer registerCustomer(CustomerDto dto) throws UserAlreadyException;
    Customer login(CustomerDto dto) throws InvalidUsernameOrPasswordException;
    Customer findById(String id) throws AccountNotFoundException, InterruptedException;
    List<Customer> findAll();
}
