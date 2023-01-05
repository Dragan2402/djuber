package com.djuber.djuberbackend.Application.Services.Admin.Implementation;

import com.djuber.djuberbackend.Application.Services.Admin.IAdminService;
import com.djuber.djuberbackend.Application.Services.Admin.Mapper.AdminMapper;
import com.djuber.djuberbackend.Application.Services.Admin.Results.AdminResult;
import com.djuber.djuberbackend.Controllers.Admin.Requests.UpdateAdminRequest;
import com.djuber.djuberbackend.Domain.Admin.Admin;
import com.djuber.djuberbackend.Domain.Authentication.Identity;
import com.djuber.djuberbackend.Domain.Authentication.UserType;
import com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions.UserNotFoundException;
import com.djuber.djuberbackend.Infastructure.Repositories.Admin.IAdminRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Authentication.IIdentityRepository;
import com.djuber.djuberbackend.Infastructure.Util.MediaService;
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

    private final IIdentityRepository identityRepository;

    private final MediaService mediaService;

    @Override
    public List<AdminResult> readAll() {
        return adminMapper.map(adminRepository.findAll());
    }

    @Override
    public Page<AdminResult> readPageable(Pageable pageable) {
        return adminMapper.map(adminRepository.findAll(pageable));
    }

    @Override
    public AdminResult getAdminByEmail(String email) {
        Identity identity = identityRepository.findByEmail(email);
        if(identity == null){
            throw new UserNotFoundException("Admin with provided email does not exist.");
        }
        return new AdminResult(adminRepository.findByIdentityId(identity.getId()));
    }

    @Override
    public String getAdminPictureByEmail(String email) {
        Identity identity = identityRepository.findByEmail(email);
        if(identity == null){
            throw new UserNotFoundException("Admin with provided email does not exist.");
        }
        return mediaService.readUserPictureAsBase64String(adminRepository.findByIdentityId(identity.getId()).getId(), identity.getUserType());
    }

    @Override
    public void updateLoggedAdminPicture(String email, String image) {
        Identity identity = identityRepository.findByEmail(email);
        if(identity == null){
            throw new UserNotFoundException("Admin with provided email does not exist.");
        }
        Long adminId = adminRepository.findByIdentityId(identity.getId()).getId();
        UserType userType = identity.getUserType();
        mediaService.deleteUserPreviousPicture(adminId, userType);
        mediaService.saveBase64AsPicture(adminId,userType,image);
    }

    @Override
    public void updateLoggedAdmin(String email, UpdateAdminRequest request) {
        Identity identity = identityRepository.findByEmail(email);
        if(identity == null){
            throw new UserNotFoundException("Admin with provided email does not exist.");
        }
        Admin admin = adminRepository.findByIdentityId(identity.getId());
        admin.setFirstName(request.getFirstName());
        admin.setLastName(request.getLastName());
        admin.setPhoneNumber(request.getPhoneNumber());
        admin.setCity(request.getCity());
        adminRepository.save(admin);
    }
}
