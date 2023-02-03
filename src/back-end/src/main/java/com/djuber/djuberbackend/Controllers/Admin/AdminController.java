package com.djuber.djuberbackend.Controllers.Admin;

import com.djuber.djuberbackend.Application.Services.Admin.IAdminService;
import com.djuber.djuberbackend.Application.Services.Admin.Results.AdminResult;
import com.djuber.djuberbackend.Application.Services.Driver.IDriverService;
import com.djuber.djuberbackend.Application.Services.LiveChat.ILiveChatService;
import com.djuber.djuberbackend.Controllers.Admin.Requests.RegisterDriverRequest;
import com.djuber.djuberbackend.Controllers.Admin.Requests.UpdateAdminRequest;
import com.djuber.djuberbackend.Controllers._Common.Requests.ImageUpdateRequest;
import com.djuber.djuberbackend.Controllers._Common.Responses.ChatResponse;
import com.djuber.djuberbackend.Controllers._Common.Responses.CountResponse;
import com.djuber.djuberbackend.Controllers._Common.Responses.IdResponse;
import com.djuber.djuberbackend.Controllers._Common.Responses.ImageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;


@Tag(name="Admin API",description = "Provides Admin CRUD end-points.")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "api/admin")
public class AdminController {

    private final IAdminService adminService;

    private final IDriverService driverService;

    private final ILiveChatService liveChatService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Page<AdminResult>> getAdmins(Principal user, Pageable pageable){
        return new ResponseEntity<>(adminService.readPageable(pageable), HttpStatus.OK);
    }

    @GetMapping(value = "loggedAdmin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<AdminResult> getLoggedAdmin(Principal user){
        return new ResponseEntity<>(adminService.getAdminByEmail(user.getName()), HttpStatus.OK);
    }

    @GetMapping(value = "loggedAdminPicture")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ImageResponse> getLoggedAdminPicture(Principal user){
        return new ResponseEntity<>(new ImageResponse(adminService.getAdminPictureByEmail(user.getName())), HttpStatus.OK);
    }

    @PutMapping(value = "updateLoggedAdminPicture")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void updateLoggedAdminPicture(Principal user, @RequestBody @Valid ImageUpdateRequest request){
        this.adminService.updateLoggedAdminPicture(user.getName(), request.getImage());
    }

    @PutMapping(value = "updateLoggedAdmin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void updateLoggedAdmin(Principal user, @RequestBody @Valid UpdateAdminRequest request){
        this.adminService.updateLoggedAdmin(user.getName(),request);
    }

    @PostMapping(value = "registerDriver")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<IdResponse> registerDriver(@RequestBody @Valid RegisterDriverRequest request){
        return new ResponseEntity<>(new IdResponse(this.driverService.registerNewDriver(request)),HttpStatus.CREATED);
    }

    @GetMapping(value = "chats")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<ChatResponse>> getChats(){
        return new ResponseEntity<>(liveChatService.getChats(), HttpStatus.OK);
    }

    @GetMapping(value = "numberOfChats")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<CountResponse> getNumberOfChats(){
        return new ResponseEntity<>(new CountResponse(liveChatService.getNumberOfChats()), HttpStatus.OK);
    }

    @DeleteMapping(value = "deleteChat")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void deleteChat(@RequestParam("chatID") Long chatId){
        liveChatService.deleteChatById(chatId);
    }
}
