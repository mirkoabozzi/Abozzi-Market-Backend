package mirkoabozzi.Abozzi.Market.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import mirkoabozzi.Abozzi.Market.dto.request.DiscountsDTO;
import mirkoabozzi.Abozzi.Market.dto.response.DiscountRespDTO;
import mirkoabozzi.Abozzi.Market.entities.Discount;
import mirkoabozzi.Abozzi.Market.exceptions.BadRequestException;
import mirkoabozzi.Abozzi.Market.services.DiscountsService;
import org.modelmapper.ModelMapper;
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
@RequestMapping("/discounts")
@Tag(name = "Discounts")
public class DiscountsController {
    @Autowired
    private DiscountsService discountsService;
    @Autowired
    private ModelMapper modelMapper;

    //POST
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public DiscountRespDTO saveDiscount(@RequestBody @Validated DiscountsDTO payload, BindingResult validation) {
        if (validation.hasErrors()) {
            String msg = validation.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining());
            throw new BadRequestException("Payload error: " + msg);
        } else {
            Discount discount = this.discountsService.saveDiscount(payload);
            return this.modelMapper.map(discount, DiscountRespDTO.class);
        }
    }

    //GET ALL
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<DiscountRespDTO> getAllDiscounts(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "20") int size,
                                                 @RequestParam(defaultValue = "description") String sortBy
    ) {
        Page<Discount> discountPage = this.discountsService.getAllDiscounts(page, size, sortBy);
        return discountPage.map(discount -> modelMapper.map(discount, DiscountRespDTO.class));
    }

    //PUT UPDATE
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public DiscountRespDTO updateDiscount(@PathVariable UUID id, @RequestBody @Validated DiscountsDTO payload, BindingResult validation) {
        if (validation.hasErrors()) {
            String msg = validation.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining());
            throw new BadRequestException("Payload error: " + msg);
        } else {
            Discount discount = this.discountsService.updateDiscount(id, payload);
            return modelMapper.map(discount, DiscountRespDTO.class);
        }
    }

    //DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteDiscount(@PathVariable UUID id) {
        this.discountsService.deleteDiscount(id);
    }
}
