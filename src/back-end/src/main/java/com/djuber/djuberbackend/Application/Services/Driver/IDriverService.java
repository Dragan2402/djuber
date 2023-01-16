package com.djuber.djuberbackend.Application.Services.Driver;

import com.djuber.djuberbackend.Application.Services.Driver.Results.DriverResult;
import com.djuber.djuberbackend.Controllers.Admin.Requests.RegisterDriverRequest;
import com.djuber.djuberbackend.Controllers.Driver.Requests.UpdateDriverRequest;
import com.djuber.djuberbackend.Controllers.Driver.Response.DriverUpdateResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDriverService {
    Long registerNewDriver(RegisterDriverRequest request);

    DriverResult getDriverByEmail(String email);

    String getDriverPictureByEmail(String email);

    void updateLoggedDriverPicture(String email, String image);

    void updateLoggedDriver(String email, UpdateDriverRequest request);

    Page<DriverResult> readPageable(Pageable pageable);

    void blockDriver(long driverId);

    void unblockDriver(long driverId);

    Page<DriverResult> readPageableWithFilter(Pageable pageable, String filter);

    void updateDriverNote(long driverId, String note);

    void submitDriverUpdateRequest(String email, UpdateDriverRequest request);

    String getDriverNote(long driverId);

    List<DriverUpdateResponse> getDriverProfileUpdates();

    void declineChangeRequest(long id);

    void acceptChangeRequest(long id);
}
