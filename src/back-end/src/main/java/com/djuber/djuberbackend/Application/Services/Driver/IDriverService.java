package com.djuber.djuberbackend.Application.Services.Driver;

import com.djuber.djuberbackend.Controllers.Admin.Requests.RegisterDriverRequest;

public interface IDriverService {
    Long registerNewDriver(RegisterDriverRequest request);
}
