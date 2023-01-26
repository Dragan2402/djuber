package com.djuber.djuberbackend.Application.Services.Client.Implementation;

import com.djuber.djuberbackend.Application.Services.Admin.Results.AdminResult;
import com.djuber.djuberbackend.Application.Services.Client.IClientService;
import com.djuber.djuberbackend.Application.Services.Client.Mapper.ClientMapper;
import com.djuber.djuberbackend.Application.Services.Client.Results.ClientResult;
import com.djuber.djuberbackend.Controllers.Client.Requests.UpdateClientRequest;
import com.djuber.djuberbackend.Domain.Admin.Admin;
import com.djuber.djuberbackend.Domain.Authentication.Identity;
import com.djuber.djuberbackend.Domain.Authentication.UserType;
import com.djuber.djuberbackend.Domain.Client.Client;
import com.djuber.djuberbackend.Domain.Driver.Driver;
import com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions.UserNotFoundException;
import com.djuber.djuberbackend.Infastructure.Repositories.Authentication.IIdentityRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Client.IClientRepository;
import com.djuber.djuberbackend.Infastructure.Util.MediaService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientService implements IClientService {

    final IIdentityRepository identityRepository;

    final IClientRepository clientRepository;

    final MediaService mediaService;

    final ClientMapper clientMapper;

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

    @Override
    public void blockClient(long clientId) {
        Client client = clientRepository.findById(clientId).orElse(null);
        if(client == null){
            throw new UserNotFoundException("Client with provided id does not exist.");
        }
        client.setBlocked(true);
        clientRepository.save(client);
    }

    @Override
    public void unblockClient(long clientId) {
        Client client = clientRepository.findById(clientId).orElse(null);
        if(client == null){
            throw new UserNotFoundException("Client with provided id does not exist.");
        }
        client.setBlocked(false);
        clientRepository.save(client);
    }

    @Override
    public void updateClientNote(long clientId, String note) {
        Client client = clientRepository.findById(clientId).orElse(null);
        if(client == null){
            throw new UserNotFoundException("Client with provided id does not exist.");
        }
        client.setNote(note);
        clientRepository.save(client);
    }

    @Override
    public String getClientNote(long clientId) {
        Client client = clientRepository.findById(clientId).orElse(null);
        if(client == null){
            throw new UserNotFoundException("Client with provided id does not exist.");
        }
        return client.getNote();
    }

    @Override
    public Double getClientBalanceByEmail(String email) {
        Identity identity = identityRepository.findByEmail(email);
        if(identity == null){
            throw new UserNotFoundException("Client with provided email does not exist.");
        }
        Client client = clientRepository.findByIdentityId(identity.getId());
        if(client == null){
            throw new UserNotFoundException("Client with provided email does not exist.");
        }
        return client.getBalance();
    }

    @Override
    public void addLoggedClientFunds(String email, Double amount) {
        Identity identity = identityRepository.findByEmail(email);
        if(identity == null){
            throw new UserNotFoundException("Client with provided email does not exist.");
        }
        Client client = clientRepository.findByIdentityId(identity.getId());
        if(client == null){
            throw new UserNotFoundException("Client with provided email does not exist.");
        }
        client.setBalance(client.getBalance() + amount);
        clientRepository.save(client);
    }

    @Override
    public String checkIfClientsExist(List<String> clientEmails) {
        for (String clientEmail : clientEmails) {
            Identity clientIdentity = identityRepository.findByEmail(clientEmail);
            if (clientIdentity == null) {
                return clientEmail;
            }

            Client client = clientRepository.findByIdentityId(clientIdentity.getId());
            if (client == null) {
                return clientEmail;
            }
        }
        return null;
    }

    @Override
    public String checkIfClientsAreBlocked(List<String> clientEmails) {
        for (String clientEmail : clientEmails) {
            Identity clientIdentity = identityRepository.findByEmail(clientEmail);
            if (clientIdentity == null) {
                throw new UserNotFoundException("Client with email " + clientEmail + " not found.");
            }

            Client client = clientRepository.findByIdentityId(clientIdentity.getId());
            if (client == null) {
                throw new UserNotFoundException("Client with email " + clientEmail + " not found.");
            }

            if (client.getBlocked().equals(true)) {
                return clientEmail;
            }
        }
        return null;
    }

    @Override
    public Page<ClientResult> readPageable(Pageable pageable) {
        return clientMapper.map(clientRepository.findAll(pageable));
    }

    @Override
    public Page<ClientResult> readPageableWithFilter(Pageable pageable, String filter) {
        return clientMapper.map(clientRepository.findAllWithFilter(filter, pageable));
    }
}
