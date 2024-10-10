package mirkoabozzi.Abozzi.Market.controllers;

import mirkoabozzi.Abozzi.Market.dto.ShipmentsDTO;
import mirkoabozzi.Abozzi.Market.entities.Shipment;
import mirkoabozzi.Abozzi.Market.exceptions.BadRequestException;
import mirkoabozzi.Abozzi.Market.services.ShipmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/shipments")
public class ShipmentsController {
    @Autowired
    private ShipmentsService shipmentsService;

    //POST
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public Shipment saveShipment(@RequestBody @Validated ShipmentsDTO payload, BindingResult validation) {
        if (validation.hasErrors()) {
            String msg = validation.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining());
            throw new BadRequestException("Payload error: " + msg);
        } else {
            return this.shipmentsService.saveShipment(payload);
        }
    }

    //GET ALL
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public Page<Shipment> getAllShipment(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(defaultValue = "city") String sortBy) {
        return this.shipmentsService.getAllShipments(page, size, sortBy);
    }

    //DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteShipment(@PathVariable UUID id) {
        this.shipmentsService.deleteShipment(id);
    }
}