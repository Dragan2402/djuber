package com.djuber.djuberbackend.Application.Services.Admin.Mapper;

import com.djuber.djuberbackend.Application.Services.Admin.Results.AdminResult;
import com.djuber.djuberbackend.Domain.Admin.Admin;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class AdminMapper {

    public List<AdminResult> map(List<Admin> admins){
        List<AdminResult> adminResults = new ArrayList<>();
        for(Admin admin : admins){
            adminResults.add(new AdminResult(admin));
        }
        return adminResults;
    }

    public Page<AdminResult> map(Page<Admin> adminPage){
        return adminPage.map(AdminResult::new);
    }
}
