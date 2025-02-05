package mirkoabozzi.Abozzi.Market.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import mirkoabozzi.Abozzi.Market.dto.ShipmentsDTO;
import mirkoabozzi.Abozzi.Market.entities.Shipment;
import mirkoabozzi.Abozzi.Market.entities.User;
import mirkoabozzi.Abozzi.Market.exceptions.BadRequestException;
import mirkoabozzi.Abozzi.Market.services.ShipmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/shipments")
@Tag(name = "Shipments")
public class ShipmentsController {
    @Autowired
    private ShipmentsService shipmentsService;

    //POST
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Shipment saveShipment(@RequestBody @Validated ShipmentsDTO payload, @AuthenticationPrincipal User authenticatedUser, BindingResult validation) {
        if (validation.hasErrors()) {
            String msg = validation.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining());
            throw new BadRequestException("Payload error: " + msg);
        } else {
            return this.shipmentsService.saveShipment(payload, authenticatedUser.getId());
        }
    }

    //GET MY ADDRESS
    @GetMapping("/me")
    public Page<Shipment> getMyShipment(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "20") int size,
                                        @RequestParam(defaultValue = "city") String sortBy,
                                        @AuthenticationPrincipal User authenticatedUser
    ) {
        return this.shipmentsService.getMyShipments(page, size, sortBy, authenticatedUser.getId());
    }

    //UPDATE SHIPMENT ADDRESS
    @PutMapping("/me/{id}")
    public Shipment updateShipment(@PathVariable UUID id, @RequestBody @Validated ShipmentsDTO payload, BindingResult validation) {
        if (validation.hasErrors()) {
            String msg = validation.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining());
            throw new BadRequestException("Payload error: " + msg);
        } else {
            return this.shipmentsService.updateShipment(id, payload);
        }
    }

    //DELETE
    @DeleteMapping("/me/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteShipment(@PathVariable UUID id, @AuthenticationPrincipal User authenticatedUser) {
        this.shipmentsService.deleteShipment(id, authenticatedUser.getId());
    }
}