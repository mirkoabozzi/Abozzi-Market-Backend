package mirkoabozzi.Abozzi.Market.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import mirkoabozzi.Abozzi.Market.dto.request.ProductDiscountDTO;
import mirkoabozzi.Abozzi.Market.dto.request.ProductsDTO;
import mirkoabozzi.Abozzi.Market.dto.response.ProductRespDTO;
import mirkoabozzi.Abozzi.Market.entities.Product;
import mirkoabozzi.Abozzi.Market.exceptions.BadRequestException;
import mirkoabozzi.Abozzi.Market.services.ProductsService;
import org.modelmapper.ModelMapper;
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
@Tag(name = "Products")
public class ProductsController {
    @Autowired
    private ProductsService productsService;
    @Autowired
    private ModelMapper modelMapper;

    //POST SAVE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ProductRespDTO saveProduct(@RequestBody @Validated ProductsDTO payload, BindingResult validation) {
        if (validation.hasErrors()) {
            String msg = validation.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining());
            throw new BadRequestException("Payload error: " + msg);
        } else {
            Product product = this.productsService.saveProduct(payload);
            return this.modelMapper.map(product, ProductRespDTO.class);
        }
    }

    // GET ALL
    @GetMapping("/all")
    public Page<ProductRespDTO> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "24") int size,
                                               @RequestParam(defaultValue = "lastUpdate") String sortBy
    ) {
        Page<Product> productPage = this.productsService.getAllProducts(page, size, sortBy);
        return productPage.map(product -> this.modelMapper.map(product, ProductRespDTO.class));
    }

    //GET BY ID
    @GetMapping("/product/{id}")
    public ProductRespDTO getProductById(@PathVariable UUID id) {
        Product product = this.productsService.findById(id);
        return this.modelMapper.map(product, ProductRespDTO.class);
    }

    //UPDATE
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ProductRespDTO updateProduct(@PathVariable UUID id, @RequestBody @Validated ProductsDTO payload, BindingResult validation) {
        if (validation.hasErrors()) {
            String msg = validation.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining());
            throw new BadRequestException("Payload error: " + msg);
        } else {
            Product product = this.productsService.updateProduct(id, payload);
            return this.modelMapper.map(product, ProductRespDTO.class);
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
    public Page<ProductRespDTO> findByProductsContainsName(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "24") int size,
                                                           @RequestParam(defaultValue = "lastUpdate") String sortBy,
                                                           @RequestParam String name
    ) {
        Page<Product> productPage = this.productsService.findByProductsContainsName(page, size, sortBy, name);
        return productPage.map(product -> this.modelMapper.map(product, ProductRespDTO.class));
    }

    //GET PRODUCTS BY CATEGORY NAME
    @GetMapping("/category")
    public Page<ProductRespDTO> findProductsByCategoryName(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "24") int size,
                                                           @RequestParam(defaultValue = "lastUpdate") String sortBy,
                                                           @RequestParam String name
    ) {
        Page<Product> productPage = this.productsService.findProductsByCategoryContainingName(page, size, sortBy, name);
        return productPage.map(product -> this.modelMapper.map(product, ProductRespDTO.class));
    }

    //GET FIND BY PRICE RANGE
    @GetMapping("/price")
    public Page<ProductRespDTO> findByPriceRange(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "24") int size,
                                                 @RequestParam(defaultValue = "lastUpdate") String sortBy,
                                                 @RequestParam int min,
                                                 @RequestParam int max
    ) {
        Page<Product> productPage = this.productsService.findByPriceRange(page, size, sortBy, min, max);
        return productPage.map(product -> this.modelMapper.map(product, ProductRespDTO.class));
    }

    //GET FIND BY DISCOUNT STATUS
    @GetMapping("/discount")
    public Page<ProductRespDTO> findByDiscountStatus(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "24") int size,
                                                     @RequestParam(defaultValue = "lastUpdate") String sortBy
    ) {
        Page<Product> productPage = this.productsService.findByDiscountStatus(page, size, sortBy);
        return productPage.map(product -> this.modelMapper.map(product, ProductRespDTO.class));
    }

    //UPDATE ADD DISCOUNT
    @PutMapping("/add/discount/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ProductRespDTO addDiscount(@PathVariable UUID id, @RequestBody @Validated ProductDiscountDTO payload, BindingResult validation) {
        if (validation.hasErrors()) {
            String msg = validation.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining());
            throw new BadRequestException("Payload error: " + msg);
        } else {
            Product product = this.productsService.addDiscount(id, payload);
            return this.modelMapper.map(product, ProductRespDTO.class);
        }
    }

    //REMOVE DISCOUNT
    @DeleteMapping("/remove/discount/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ProductRespDTO removeDiscount(@PathVariable UUID id, @RequestBody @Validated ProductDiscountDTO payload, BindingResult validation) {
        if (validation.hasErrors()) {
            String msg = validation.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining());
            throw new BadRequestException("Payload error: " + msg);
        } else {
            Product product = this.productsService.removeDiscount(id, payload);
            return this.modelMapper.map(product, ProductRespDTO.class);
        }
    }

    @GetMapping("/filter")
    public Page<ProductRespDTO> filterProducts(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "24") int size,
                                               @RequestParam(defaultValue = "lastUpdate") String sortBy,
                                               @RequestParam(required = false) String name,
                                               @RequestParam(required = false) String categoryName,
                                               @RequestParam(required = false, defaultValue = "0.0") Double min,
                                               @RequestParam(required = false) Double max,
                                               @RequestParam(required = false) Boolean discountStatus
    ) {
        Page<Product> productPage = this.productsService.filterProducts(page, size, sortBy, name, categoryName, min, max, discountStatus);
        return productPage.map(product -> this.modelMapper.map(product, ProductRespDTO.class));
    }
}