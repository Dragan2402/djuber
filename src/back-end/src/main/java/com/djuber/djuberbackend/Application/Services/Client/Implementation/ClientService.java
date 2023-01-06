package com.djuber.djuberbackend.Application.Services.Client.Implementation;

import com.djuber.djuberbackend.Application.Services.Admin.Results.AdminResult;
import com.djuber.djuberbackend.Application.Services.Client.IClientService;
import com.djuber.djuberbackend.Application.Services.Client.Results.ClientResult;
import com.djuber.djuberbackend.Controllers.Client.Requests.UpdateClientRequest;
import com.djuber.djuberbackend.Domain.Admin.Admin;
import com.djuber.djuberbackend.Domain.Authentication.Identity;
import com.djuber.djuberbackend.Domain.Authentication.UserType;
import com.djuber.djuberbackend.Domain.Client.Client;
import com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions.UserNotFoundException;
import com.djuber.djuberbackend.Infastructure.Repositories.Authentication.IIdentityRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Client.IClientRepository;
import com.djuber.djuberbackend.Infastructure.Util.MediaService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientService implements IClientService {

    final IIdentityRepository identityRepository;

    final IClientRepository clientRepository;

    final MediaService mediaService;

    @Override
    public ClientResult getClientByEmail(String email) {
        Identity identity = identityRepository.findByEmail(email);
        if(identity == null){
            throw new UserNotFoundException("Client with provided email does not exist.");
        }
        return new ClientResult(clientRepository.findByIdentityId(identity.getId()));
    }

    @Override
    public String getClientPictureByEmail(String email) {
        Identity identity = identityRepository.findByEmail(email);
        if(identity == null){
            throw new UserNotFoundException("Client with provided email does not exist.");
        }
        return mediaService.readUserPictureAsBase64String(clientRepository.findByIdentityId(identity.getId()).getId(), identity.getUserType());
    }

    @Override
    public void updateLoggedClientPicture(String email, String image) {
        Identity identity = identityRepository.findByEmail(email);
        if(identity == null){
            throw new UserNotFoundException("Client with provided email does not exist.");
        }
        Long clientId = clientRepository.findByIdentityId(identity.getId()).getId();
        UserType userType = identity.getUserType();
        mediaService.deleteUserPreviousPicture(clientId, userType);
        mediaService.saveBase64AsPicture(clientId,userType,image);
    }

    @Override
    public void updateLoggedClient(String email, UpdateClientRequest request) {
        Identity identity = identityRepository.findByEmail(email);
        if(identity == null){
            throw new UserNotFoundException("Client with provided email does not exist.");
        }
        Client client = clientRepository.findByIdentityId(identity.getId());
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setPhoneNumber(request.getPhoneNumber());
        client.setCity(request.getCity());
        clientRepository.save(client);
    }
}
