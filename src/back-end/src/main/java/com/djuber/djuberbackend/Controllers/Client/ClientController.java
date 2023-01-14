package com.djuber.djuberbackend.Controllers.Client;


import com.djuber.djuberbackend.Application.Services.Admin.Results.AdminResult;
import com.djuber.djuberbackend.Application.Services.Client.IClientService;
import com.djuber.djuberbackend.Application.Services.Client.Results.ClientResult;
import com.djuber.djuberbackend.Controllers.Admin.Requests.UpdateAdminRequest;
import com.djuber.djuberbackend.Controllers.Client.Requests.UpdateClientRequest;
import com.djuber.djuberbackend.Controllers._Common.Requests.IdRequest;
import com.djuber.djuberbackend.Controllers._Common.Requests.ImageUpdateRequest;
import com.djuber.djuberbackend.Controllers._Common.Requests.NoteUpdateRequest;
import com.djuber.djuberbackend.Controllers._Common.Responses.ImageResponse;
import com.djuber.djuberbackend.Controllers._Common.Responses.NoteResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
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

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Page<ClientResult>> getClients(Pageable pageable, @RequestParam @Nullable String filter) {
        if (filter == null) {
            return new ResponseEntity<>(clientService.readPageable(pageable), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(clientService.readPageableWithFilter(pageable, filter), HttpStatus.OK);
        }
    }

    @GetMapping(value = "getClientNote")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<NoteResponse> getClientNote(@RequestParam long id) {
        return new ResponseEntity<>(new NoteResponse(clientService.getClientNote(id)),HttpStatus.OK);
    }

    @PutMapping(value = "blockClient")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void blockClient(@RequestBody @Valid IdRequest request){
        clientService.blockClient(request.getId());
    }

    @PutMapping(value = "unblockClient")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void unblockClient(@RequestBody @Valid IdRequest request){
        clientService.unblockClient(request.getId());
    }

    @PutMapping(value = "updateClientNote")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void updateClientNote(@RequestBody @Valid NoteUpdateRequest request){
        clientService.updateClientNote(request.getId(), request.getNote());
    }

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
