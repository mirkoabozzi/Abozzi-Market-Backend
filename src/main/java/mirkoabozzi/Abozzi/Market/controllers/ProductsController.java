package mirkoabozzi.Abozzi.Market.controllers;

import mirkoabozzi.Abozzi.Market.dto.ProductDiscountDTO;
import mirkoabozzi.Abozzi.Market.dto.ProductsDTO;
import mirkoabozzi.Abozzi.Market.entities.Product;
import mirkoabozzi.Abozzi.Market.exceptions.BadRequestException;
import mirkoabozzi.Abozzi.Market.services.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductsController {
    @Autowired
    private ProductsService productsService;

    //POST SAVE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Product saveProduct(@RequestBody @Validated ProductsDTO payload, BindingResult validation) {
        if (validation.hasErrors()) {
            String msg = validation.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining());
            throw new BadRequestException("Payload error: " + msg);
        } else {
            return this.productsService.saveProduct(payload);
        }
    }

    // GET ALL
    @GetMapping("/all")
//    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public Page<Product> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "24") int size,
                                        @RequestParam(defaultValue = "lastUpdate") String sortBy) {
        return this.productsService.getAllProducts(page, size, sortBy);
    }

    //GET BY ID
    @GetMapping("/product/{id}")
//    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public Product getProductById(@PathVariable UUID id) {
        return this.productsService.findById(id);
    }

    //UPDATE
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Product updateProduct(@PathVariable UUID id, @RequestBody @Validated ProductsDTO payload, BindingResult validation) {
        if (validation.hasErrors()) {
            String msg = validation.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining());
            throw new BadRequestException("Payload error: " + msg);
        } else {
            return this.productsService.updateProduct(id, payload);
        }
    }

    //DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteCategory(@PathVariable UUID id) {
        this.productsService.delete(id);
    }

    //IMG UPDATE
    @PostMapping("/image/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void imgUpload(@RequestParam("image") MultipartFile img, @PathVariable UUID id) throws IOException, MaxUploadSizeExceededException {
        this.productsService.imgUpload(img, id);
    }

    //GET FIND BY PRODUCTS NAME
    @GetMapping("/name")
//    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public Page<Product> findByProductsContainsName(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "24") int size,
                                                    @RequestParam(defaultValue = "lastUpdate") String sortBy,
                                                    @RequestParam String name) {
        return this.productsService.findByProductsContainsName(page, size, sortBy, name);
    }

    //GET PRODUCTS BY CATEGORY NAME
    @GetMapping("/category")
//    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public Page<Product> findProductsByCategoryName(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "24") int size,
                                                    @RequestParam(defaultValue = "lastUpdate") String sortBy,
                                                    @RequestParam String name) {
        return this.productsService.findProductsByCategoryContainingName(page, size, sortBy, name);
    }

    //GET FIND BY PRICE RANGE
    @GetMapping("/price")
//    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public Page<Product> findByPriceRange(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "24") int size,
                                          @RequestParam(defaultValue = "lastUpdate") String sortBy,
                                          @RequestParam int min,
                                          @RequestParam int max) {
        return this.productsService.findByPriceRange(page, size, sortBy, min, max);
    }

    //GET FIND BY DISCOUNT STATUS
    @GetMapping("/discount")
//    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public Page<Product> findByDiscountStatus(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "24") int size,
                                              @RequestParam(defaultValue = "lastUpdate") String sortBy
    ) {
        return this.productsService.findByDiscountStatus(page, size, sortBy);
    }

    //UPDATE ADD DISCOUNT
    @PutMapping("/add/discount/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Product addDiscount(@PathVariable UUID id, @RequestBody @Validated ProductDiscountDTO payload, BindingResult validation) {
        if (validation.hasErrors()) {
            String msg = validation.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining());
            throw new BadRequestException("Payload error: " + msg);
        } else {
            return this.productsService.addDiscount(id, payload);
        }
    }

    //REMOVE DISCOUNT
    @DeleteMapping("/remove/discount/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Product removeDiscount(@PathVariable UUID id, @RequestBody @Validated ProductDiscountDTO payload, BindingResult validation) {
        if (validation.hasErrors()) {
            String msg = validation.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining());
            throw new BadRequestException("Payload error: " + msg);
        } else {
            return this.productsService.removeDiscount(id, payload);
        }
    }
}