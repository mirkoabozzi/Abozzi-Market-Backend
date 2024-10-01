package mirkoabozzi.Abozzi.Market.services;

import mirkoabozzi.Abozzi.Market.dto.ShipmentsDTO;
import mirkoabozzi.Abozzi.Market.entities.Shipment;
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

    //POST SAVE
    public Shipment saveShipment(ShipmentsDTO payload) {
        Shipment newShipment = new Shipment(payload.address(), payload.number(), payload.city(), payload.zipCode());
        return this.shipmentsRepository.save(newShipment);
    }

    public Shipment findById(UUID id) {
        return this.shipmentsRepository.findById(id).orElseThrow(() -> new NotFoundException("Shipment whit ID " + id + " not found"));
    }

    //GET ALL
    public Page<Shipment> getAllShipments(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.shipmentsRepository.findAll(pageable);
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

    //DELETE
    public void deleteShipment(UUID id) {
        this.shipmentsRepository.delete(this.findById(id));
    }
}
