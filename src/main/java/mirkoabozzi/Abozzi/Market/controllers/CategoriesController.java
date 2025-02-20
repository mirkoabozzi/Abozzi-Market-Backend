package mirkoabozzi.Abozzi.Market.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import mirkoabozzi.Abozzi.Market.dto.request.CategoriesDTO;
import mirkoabozzi.Abozzi.Market.dto.response.CategoryRespDTO;
import mirkoabozzi.Abozzi.Market.entities.Category;
import mirkoabozzi.Abozzi.Market.exceptions.BadRequestException;
import mirkoabozzi.Abozzi.Market.services.CategoriesService;
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
@RequestMapping("/categories")
@Tag(name = "Categories")
public class CategoriesController {
    @Autowired
    private CategoriesService categoriesService;
    @Autowired
    private ModelMapper modelMapper;

    //POST
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public CategoryRespDTO saveCategory(@RequestBody @Validated CategoriesDTO payload, BindingResult validation) {
        if (validation.hasErrors()) {
            String msg = validation.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining());
            throw new BadRequestException("Payload error: " + msg);
        } else {
            Category category = this.categoriesService.saveCategory(payload);
            return this.modelMapper.map(category, CategoryRespDTO.class);
        }
    }

    //GET ALL
    @GetMapping("/all")
    public Page<CategoryRespDTO> getAllCategory(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "20") int size,
                                                @RequestParam(defaultValue = "name") String sortBy
    ) {
        Page<Category> categoryPage = this.categoriesService.findAll(page, size, sortBy);
        return categoryPage.map(category -> modelMapper.map(category, CategoryRespDTO.class));
    }

    //PUT UPDATE
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public CategoryRespDTO updateCategory(@PathVariable UUID id, @RequestBody @Validated CategoriesDTO payload, BindingResult validation) {
        if (validation.hasErrors()) {
            String msg = validation.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining());
            throw new BadRequestException("Payload error: " + msg);
        } else {
            Category category = this.categoriesService.updateCategory(id, payload);
            return modelMapper.map(category, CategoryRespDTO.class);
        }
    }

    //IMG UPDATE
    @PostMapping("/image/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void imgUpload(@RequestParam("image") MultipartFile img, @PathVariable UUID id) throws IOException, MaxUploadSizeExceededException {
        this.categoriesService.imgUpload(img, id);
    }

    //DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteCategory(@PathVariable UUID id) {
        this.categoriesService.deleteCategory(id);
    }
}
