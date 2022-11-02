package com.djuber.djuberbackend.Application.Services.Admin;

import com.djuber.djuberbackend.Application.Services.Admin.Results.AdminResult;
import com.djuber.djuberbackend.Domain.Admin.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAdminService {

    List<AdminResult> readAll();

    Page<AdminResult> readPageable(Pageable pageable);
}
