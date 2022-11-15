package com.djuber.djuberbackend.Controllers.Admin;

import com.djuber.djuberbackend.Application.Services.Admin.IAdminService;
import com.djuber.djuberbackend.Application.Services.Admin.Results.AdminResult;
import com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions.UserNotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@Tag(name="Admin API",description = "Provides Admin CRUD end-points.")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "api/admin")
public class AdminController {

    private final IAdminService adminService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Page<AdminResult>> getAdmins(Principal user, Pageable pageable){
        throw new UserNotFoundException("Admin does not exist");
        //return new ResponseEntity<>(adminService.readPageable(pageable), HttpStatus.OK);
    }
}
