package com.offcn.user.service;

import com.offcn.user.pojo.TMember;
import com.offcn.user.pojo.TMemberAddress;

import java.util.List;


public interface UserService {

    public void registerUser(TMember member);

    public TMember login(String loginacct,String password);

    public TMember findById(Integer id);

    public List<TMemberAddress> findAddressByMemberId(Integer memberId);
}

