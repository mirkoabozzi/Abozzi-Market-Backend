package mirkoabozzi.Abozzi.Market.services;

import mirkoabozzi.Abozzi.Market.dto.ShipmentsDTO;
import mirkoabozzi.Abozzi.Market.entities.Shipment;
import mirkoabozzi.Abozzi.Market.entities.User;
import mirkoabozzi.Abozzi.Market.exceptions.NotFoundException;
import mirkoabozzi.Abozzi.Market.repositories.ShipmentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ShipmentsService {
    @Autowired
    private ShipmentsRepository shipmentsRepository;
    @Autowired
    private UsersService usersService;

    //POST SAVE
    public Shipment saveShipment(ShipmentsDTO payload, UUID authenticatedUser) {
        User userFound = this.usersService.findById(authenticatedUser);
        Shipment newShipment = new Shipment(payload.address(), payload.number(), payload.city(), payload.zipCode(), userFound);
        return this.shipmentsRepository.save(newShipment);
    }

    //FIND BY ID
    public Shipment findById(UUID id) {
        return this.shipmentsRepository.findById(id).orElseThrow(() -> new NotFoundException("Shipment whit ID " + id + " not found"));
    }

    //GET MY
    public Page<Shipment> getMyShipments(int page, int size, String sortBy, UUID authenticatedUserId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.shipmentsRepository.findByUserId(pageable, authenticatedUserId);
    }

    //UPDATE
    public Shipment updateShipment(UUID id, ShipmentsDTO payload) {
        Shipment shipmentFound = this.findById(id);
        shipmentFound.setAddress(payload.address());
        shipmentFound.setNumber(payload.number());
        shipmentFound.setCity(payload.city());
        shipmentFound.setZipCode(payload.zipCode());
        return this.shipmentsRepository.save(shipmentFound);
    }

    //DELETE MY SHIPMENT ADDRESS
    public void deleteShipment(UUID id, UUID authenticatedUserId) {
        this.shipmentsRepository.delete(this.shipmentsRepository.findByIdAndUserId(id, authenticatedUserId).orElseThrow(() -> new NotFoundException("Shipment with id " + id + " not found")));
    }
}
