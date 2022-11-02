package com.djuber.djuberbackend.Application.Services.Admin.Implementation;

import com.djuber.djuberbackend.Application.Services.Admin.IAdminService;
import com.djuber.djuberbackend.Application.Services.Admin.Mapper.AdminMapper;
import com.djuber.djuberbackend.Application.Services.Admin.Results.AdminResult;
import com.djuber.djuberbackend.Domain.Admin.Admin;
import com.djuber.djuberbackend.Infastructure.Repositories.Admin.IAdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService implements IAdminService {

    private final IAdminRepository adminRepository;

    private final AdminMapper adminMapper;

    @Override
    public List<AdminResult> readAll() {
        return adminMapper.map(adminRepository.findAll());
    }

    @Override
    public Page<AdminResult> readPageable(Pageable pageable) {
        return adminMapper.map(adminRepository.findAll(pageable));
    }
}
