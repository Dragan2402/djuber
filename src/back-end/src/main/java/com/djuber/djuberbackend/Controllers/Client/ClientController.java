package com.djuber.djuberbackend.Controllers.Client;


import com.djuber.djuberbackend.Application.Services.Client.IClientService;
import com.djuber.djuberbackend.Application.Services.Client.Results.ClientResult;
import com.djuber.djuberbackend.Controllers.Admin.Requests.UpdateAdminRequest;
import com.djuber.djuberbackend.Controllers.Client.Requests.UpdateClientRequest;
import com.djuber.djuberbackend.Controllers._Common.Requests.ImageUpdateRequest;
import com.djuber.djuberbackend.Controllers._Common.Responses.ImageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Tag(name="Client API",description = "Provides Client CRUD end-points.")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "api/client")
public class ClientController {

    private final IClientService clientService;

    @GetMapping(value = "loggedClient")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<ClientResult> getLoggedClient(Principal user){
        return new ResponseEntity<>(clientService.getClientByEmail(user.getName()), HttpStatus.OK);
    }

    @GetMapping(value = "loggedClientPicture")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<ImageResponse> getLoggedClientPicture(Principal user){
        return new ResponseEntity<>(new ImageResponse(clientService.getClientPictureByEmail(user.getName())), HttpStatus.OK);
    }

    @PutMapping(value = "updateLoggedClientPicture")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public void updateLoggedClientPicture(Principal user, @RequestBody @Valid ImageUpdateRequest request){
        this.clientService.updateLoggedClientPicture(user.getName(), request.getImage());
    }

    @PutMapping(value = "updateLoggedClient")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public void updateLoggedClient(Principal user, @RequestBody @Valid UpdateClientRequest request){
        this.clientService.updateLoggedClient(user.getName(),request);
    }
}
