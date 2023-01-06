package com.djuber.djuberbackend.Application.Services.Driver.Implementation;

import com.djuber.djuberbackend.Application.Services.Driver.IDriverService;
import com.djuber.djuberbackend.Controllers.Admin.Requests.RegisterDriverRequest;
import com.djuber.djuberbackend.Domain.Authentication.Identity;
import com.djuber.djuberbackend.Domain.Authentication.Role;
import com.djuber.djuberbackend.Domain.Authentication.UserType;
import com.djuber.djuberbackend.Domain.Driver.Car;
import com.djuber.djuberbackend.Domain.Driver.CarType;
import com.djuber.djuberbackend.Domain.Driver.Driver;
import com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions.CarWithLicensePlateAlreadyExistsException;
import com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions.EmailAlreadyExistsException;
import com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions.UnsupportedCarTypeException;
import com.djuber.djuberbackend.Infastructure.Repositories.Authentication.IIdentityRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Authentication.RoleRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Driver.ICarRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Driver.IDriverRepository;
import com.djuber.djuberbackend.Infastructure.Util.DateCalculator;
import com.djuber.djuberbackend.Infastructure.Util.EmailSenderService;
import com.djuber.djuberbackend.Infastructure.Util.MediaService;
import com.djuber.djuberbackend.Infastructure.Util.RandomStringGenerator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DriverService implements IDriverService {

    final IIdentityRepository identityRepository;

    final IDriverRepository driverRepository;

    final ICarRepository carRepository;

    final PasswordEncoder passwordEncoder;

    final RoleRepository roleRepository;

    final EmailSenderService emailSenderService;

    final MediaService mediaService;

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
        carToSave.setX(45.24533662754101);
        carToSave.setY(19.8430497771721);
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
