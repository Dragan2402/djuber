package com.djuber.djuberbackend.Application.Services.Driver.Implementation;

import com.djuber.djuberbackend.Application.Services.Driver.IDriverService;
import com.djuber.djuberbackend.Application.Services.Driver.Mapper.DriverMapper;
import com.djuber.djuberbackend.Application.Services.Driver.Results.DriverResult;
import com.djuber.djuberbackend.Application.Services.LiveChat.Results.MessageResult;
import com.djuber.djuberbackend.Controllers.Admin.Requests.RegisterDriverRequest;
import com.djuber.djuberbackend.Controllers.Driver.Requests.UpdateDriverRequest;
import com.djuber.djuberbackend.Controllers.Driver.Response.AvailableDriverResponse;
import com.djuber.djuberbackend.Controllers.Driver.Response.DriverUpdateResponse;
import com.djuber.djuberbackend.Domain.Authentication.Identity;
import com.djuber.djuberbackend.Domain.Authentication.Role;
import com.djuber.djuberbackend.Domain.Authentication.UserType;
import com.djuber.djuberbackend.Domain.Driver.Car;
import com.djuber.djuberbackend.Domain.Driver.CarType;
import com.djuber.djuberbackend.Domain.Driver.Driver;
import com.djuber.djuberbackend.Domain.Driver.DriverDataChange;
import com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions.*;
import com.djuber.djuberbackend.Infastructure.Repositories.Authentication.IIdentityRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Authentication.RoleRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Driver.ICarRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Driver.IDriverDataChange;
import com.djuber.djuberbackend.Infastructure.Repositories.Driver.IDriverRepository;
import com.djuber.djuberbackend.Infastructure.Util.EmailSenderService;
import com.djuber.djuberbackend.Infastructure.Util.MediaService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DriverService implements IDriverService {

    final IIdentityRepository identityRepository;

    final IDriverRepository driverRepository;

    final IDriverDataChange driverDataChangeRepository;

    final ICarRepository carRepository;

    final PasswordEncoder passwordEncoder;

    final RoleRepository roleRepository;

    final EmailSenderService emailSenderService;

    final MediaService mediaService;

    final DriverMapper driverMapper;

    @Override
    public Long registerNewDriver(RegisterDriverRequest request) {
        Car car = carRepository.findCarByLicensePlate(request.getLicensePlate());
        if(car != null){
            throw new CarWithLicensePlateAlreadyExistsException("Car with provided license plate already registered");
        }

        Car carToSave = new Car();
        carToSave.setDeleted(false);
        carToSave.setLicensePlate(request.getLicensePlate());
        carToSave.setCarType(getCarType(request.getCarType()));
        carToSave.setLat(45.24533662754101);
        carToSave.setLon(19.8430497771721);
        carToSave.setAdditionalServices(request.getAdditionalServices());
        Car carSaved = carRepository.save(carToSave);

        Identity identity = identityRepository.findByEmail(request.getEmail());
        if(identity != null) throw new EmailAlreadyExistsException("User with provided email already exists");
        Identity identityToSave = new Identity();

        identityToSave.setEmail(request.getEmail());
        identityToSave.setPassword(passwordEncoder.encode(request.getPassword()));
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("ROLE_DRIVER"));
        identityToSave.setRoles(roles);
        identityToSave.setDeleted(false);
        identityToSave.setUserType(UserType.DRIVER);

        Identity identitySaved = identityRepository.save(identityToSave);


        Driver driverToSave = new Driver();
        driverToSave.setFirstName(request.getFirstName());
        driverToSave.setLastName(request.getLastName());
        driverToSave.setCity(request.getCity());
        driverToSave.setPhoneNumber(request.getPhoneNumber());
        driverToSave.setDeleted(false);
        driverToSave.setInRide(false);
        driverToSave.setBlocked(false);
        driverToSave.setActive(false);
        driverToSave.setDurationActive(Duration.ZERO);
        driverToSave.setIdentity(identitySaved);

        Driver saved = driverRepository.save(driverToSave);

        saved.setCar(carSaved);
        driverRepository.save(saved);

        if(request.getPicture()!=null){
            mediaService.saveBase64AsPicture(saved.getId(),identitySaved.getUserType(),request.getPicture());
        }else{
            mediaService.setUserDefaultPicture(saved.getId(),identitySaved.getUserType());
        }

        emailSenderService.sendDriverRegistrationEmail(identitySaved.getEmail(),saved.getFirstName()+" "+saved.getLastName());

        return identitySaved.getId();
    }

    @Override
    public DriverResult getDriverByEmail(String email) {
        Identity identity = identityRepository.findByEmail(email);
        if(identity == null){
            throw new UserNotFoundException("Driver with provided email does not exist.");
        }
        return new DriverResult(driverRepository.findByIdentityId(identity.getId()));
    }

    @Override
    public String getDriverPictureByEmail(String email) {
        Identity identity = identityRepository.findByEmail(email);
        if(identity == null){
            throw new UserNotFoundException("Driver with provided email does not exist.");
        }
        return mediaService.readUserPictureAsBase64String(driverRepository.findByIdentityId(identity.getId()).getId(), identity.getUserType());
    }

    @Override
    public void updateLoggedDriverPicture(String email, String image) {
        Identity identity = identityRepository.findByEmail(email);
        if(identity == null){
            throw new UserNotFoundException("Driver with provided email does not exist.");
        }
        Long driverId = driverRepository.findByIdentityId(identity.getId()).getId();
        UserType userType = identity.getUserType();
        mediaService.deleteUserPreviousPicture(driverId, userType);
        mediaService.saveBase64AsPicture(driverId,userType,image);
    }

    @Override
    public void updateLoggedDriver(String email, UpdateDriverRequest request) {
        Identity identity = identityRepository.findByEmail(email);
        if(identity == null){
            throw new UserNotFoundException("Driver with provided email does not exist.");
        }
        Driver driver = driverRepository.findByIdentityId(identity.getId());
        driver.setFirstName(request.getFirstName());
        driver.setLastName(request.getLastName());
        driver.setPhoneNumber(request.getPhoneNumber());
        driver.setCity(request.getCity());
        driver.getCar().setLicensePlate(request.getLicensePlate());
        driver.getCar().setCarType(getCarType(request.getCarType()));
        driver.getCar().setAdditionalServices(request.getAdditionalServices());
        driverRepository.save(driver);
    }

    @Override
    public void submitDriverUpdateRequest(String email, UpdateDriverRequest request){
        Identity identity = identityRepository.findByEmail(email);
        if(identity == null){
            throw new UserNotFoundException("Driver with provided email does not exist.");
        }
        Driver driver = driverRepository.findByIdentityId(identity.getId());

        DriverDataChange existing = driverDataChangeRepository.findByDriverId(driver.getId());
        if(existing != null){
            throw new RequestAlreadyExistsException("Request already submitted by this driver.");
        }

        DriverDataChange driverDataChange = new DriverDataChange();
        driverDataChange.setDriver(driver);
        driverDataChange.setCarType(getCarType(request.getCarType()));
        driverDataChange.setFirstName(request.getFirstName());
        driverDataChange.setLastName(request.getLastName());
        driverDataChange.setAdditionalServices(request.getAdditionalServices());
        driverDataChange.setLicensePlate(request.getLicensePlate());
        driverDataChange.setCity(request.getCity());
        driverDataChange.setPhoneNumber(request.getPhoneNumber());

        driverDataChangeRepository.save(driverDataChange);
    }

    @Override
    public Page<DriverResult> readPageable(Pageable pageable) {
        return driverMapper.map(driverRepository.findAll(pageable));
    }

    @Override
    public void blockDriver(long driverId) {
        Driver driver = driverRepository.findById(driverId).orElse(null);
        if(driver == null){
            throw new UserNotFoundException("Driver with provided id does not exist.");
        }
        driver.setBlocked(true);
        driverRepository.save(driver);
    }

    @Override
    public void unblockDriver(long driverId) {
        Driver driver = driverRepository.findById(driverId).orElse(null);
        if(driver == null){
            throw new UserNotFoundException("Driver with provided id does not exist.");
        }
        driver.setBlocked(false);
        driverRepository.save(driver);
    }

    @Override
    public Page<DriverResult> readPageableWithFilter(Pageable pageable, String filter) {
        return driverMapper.map(driverRepository.findAllWithFilter(filter, pageable));
    }

    @Override
    public void updateDriverNote(long driverId, String note) {
        Driver driver = driverRepository.findById(driverId).orElse(null);
        if(driver == null){
            throw new UserNotFoundException("Driver with provided id does not exist.");
        }
        driver.setNote(note);
        driverRepository.save(driver);
    }

    @Override
    public String getDriverNote(long driverId) {
        Driver driver = driverRepository.findById(driverId).orElse(null);
        if(driver == null){
            throw new UserNotFoundException("Driver with provided id does not exist.");
        }
        return driver.getNote();
    }

    @Override
    public List<DriverUpdateResponse> getDriverProfileUpdates() {
        List<DriverDataChange> driversDataChanges = driverDataChangeRepository.findAll();
        List<DriverUpdateResponse> driverUpdateResponses = new ArrayList<>();
        for(DriverDataChange dataChange : driversDataChanges){
            driverUpdateResponses.add(new DriverUpdateResponse(dataChange));
        }
        return driverUpdateResponses;
    }

    @Override
    public void declineChangeRequest(long id) {
        DriverDataChange existing = driverDataChangeRepository.findById(id).orElse(null);
        if(existing == null){
            throw new UserNotFoundException("Request not found.");
        }
        driverDataChangeRepository.delete(existing);
    }

    @Override
    public void acceptChangeRequest(long id) {
        DriverDataChange existing = driverDataChangeRepository.findById(id).orElse(null);
        if(existing == null){
            throw new UserNotFoundException("Request not found.");
        }
        Driver driver = driverRepository.findById(existing.getDriver().getId()).orElse(null);
        if(driver == null){
            throw new UserNotFoundException("Driver not found.");
        }
        driver.setFirstName(existing.getFirstName());
        driver.setLastName(existing.getLastName());
        driver.setPhoneNumber(existing.getPhoneNumber());
        driver.setCity(existing.getCity());
        driver.getCar().setLicensePlate(existing.getLicensePlate());
        driver.getCar().setCarType(existing.getCarType());
        Set<String> additionalServices = new HashSet<>(existing.getAdditionalServices());
        driver.getCar().setAdditionalServices(additionalServices);
        driverRepository.save(driver);
        carRepository.save(driver.getCar());
        driverDataChangeRepository.delete(existing);

    }

    @Override
    public List<AvailableDriverResponse> getAvailableDrivers() {
        return driverMapper.mapAvailableDrivers(driverRepository.getAvailableDrivers());
    }

    private CarType getCarType(String type){
        return switch (type) {
            case "Sedan" -> CarType.SEDAN;
            case "Station wagon" -> CarType.STATION_WAGON;
            case "Van" -> CarType.VAN;
            case "Transporter" -> CarType.TRANSPORTER;
            default -> throw new UnsupportedCarTypeException("The provided car type is not supported.");
        };
    }
}
