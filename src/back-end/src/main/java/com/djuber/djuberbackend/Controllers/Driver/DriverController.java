package com.djuber.djuberbackend.Controllers.Driver;

import com.djuber.djuberbackend.Application.Services.Client.IClientService;
import com.djuber.djuberbackend.Application.Services.Client.Results.ClientResult;
import com.djuber.djuberbackend.Application.Services.Driver.IDriverService;
import com.djuber.djuberbackend.Application.Services.Driver.Results.DriverResult;
import com.djuber.djuberbackend.Controllers.Client.Requests.UpdateClientRequest;
import com.djuber.djuberbackend.Controllers.Driver.Requests.UpdateDriverRequest;
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

@Tag(name="Driver API",description = "Provides Driver CRUD end-points.")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "api/driver")
public class DriverController {

    private final IDriverService driverService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Page<DriverResult>> getDrivers(Pageable pageable, @RequestParam @Nullable String filter){
        if(filter == null){
            return new ResponseEntity<>(driverService.readPageable(pageable), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(driverService.readPageableWithFilter(pageable, filter), HttpStatus.OK);
        }
    }

    @PutMapping(value = "updateDriverNote")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void updateDriverNote(@RequestBody @Valid NoteUpdateRequest request){
        driverService.updateDriverNote(request.getId(), request.getNote());
    }

    @GetMapping(value = "getDriverNote")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<NoteResponse> getDriverNote(@RequestParam long id) {
        return new ResponseEntity<>(new NoteResponse(driverService.getDriverNote(id)),HttpStatus.OK);
    }

    @PutMapping(value = "blockDriver")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void blockDriver(@RequestBody @Valid IdRequest request){
       driverService.blockDriver(request.getId());
    }

    @PutMapping(value = "unblockDriver")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void unblockDriver(@RequestBody @Valid IdRequest request){
        driverService.unblockDriver(request.getId());
    }

    @GetMapping(value = "loggedDriver")
    @PreAuthorize("hasAnyRole('DRIVER')")
    public ResponseEntity<DriverResult> getLoggedDriver(Principal user){
        return new ResponseEntity<>(driverService.getDriverByEmail(user.getName()), HttpStatus.OK);
    }

    @GetMapping(value = "loggedDriverPicture")
    @PreAuthorize("hasAnyRole('DRIVER')")
    public ResponseEntity<ImageResponse> getLoggedDriverPicture(Principal user){
        return new ResponseEntity<>(new ImageResponse(driverService.getDriverPictureByEmail(user.getName())), HttpStatus.OK);
    }

    @PutMapping(value = "updateLoggedDriverPicture")
    @PreAuthorize("hasAnyRole('DRIVER')")
    public void updateLoggedDriverPicture(Principal user, @RequestBody @Valid ImageUpdateRequest request){
        this.driverService.updateLoggedDriverPicture(user.getName(), request.getImage());
    }

    @PutMapping(value = "updateLoggedDriver")
    @PreAuthorize("hasAnyRole('DRIVER')")
    public void updateLoggedDriver(Principal user, @RequestBody @Valid UpdateDriverRequest request){
        this.driverService.updateLoggedDriver(user.getName(),request);
    }
}
