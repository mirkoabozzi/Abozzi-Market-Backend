package mirkoabozzi.Abozzi.Market.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import mirkoabozzi.Abozzi.Market.dto.request.ProductDiscountDTO;
import mirkoabozzi.Abozzi.Market.dto.request.ProductsDTO;
import mirkoabozzi.Abozzi.Market.entities.Category;
import mirkoabozzi.Abozzi.Market.entities.Discount;
import mirkoabozzi.Abozzi.Market.entities.Product;
import mirkoabozzi.Abozzi.Market.exceptions.BadRequestException;
import mirkoabozzi.Abozzi.Market.exceptions.NotFoundException;
import mirkoabozzi.Abozzi.Market.repositories.ProductsRepository;
import mirkoabozzi.Abozzi.Market.specification.ProductsSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class ProductsService {
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private CategoriesService categoriesService;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private DiscountsService discountsService;

    //POST SAVE
    public Product saveProduct(ProductsDTO payload) {
        Category categoryFound = this.categoriesService.findById(UUID.fromString(payload.category()));
        Product newProduct = new Product(
                payload.name(),
                payload.description(),
                payload.price(),
                payload.quantityAvailable(),
                LocalDateTime.now(),
                "https://ui-avatars.com/api/?name=" + payload.name() + "+" + payload.description(),
                false,
                categoryFound);
        log.info("[ProductsService] New product created: {}", newProduct.getName());
        return this.productsRepository.save(newProduct);
    }

    public Product findById(UUID id) {
        return this.productsRepository.findById(id).orElseThrow(() -> new NotFoundException("Product whit ID " + id + " not found"));
    }

    //GET ALL
    public Page<Product> getAllProducts(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return this.productsRepository.findAll(pageable);
    }

    //UPDATE
    public Product updateProduct(UUID id, ProductsDTO payload) {
        Product productFound = this.findById(id);
        Category categoryFound = this.categoriesService.findById(UUID.fromString(payload.category()));
        productFound.setName(payload.name());
        productFound.setDescription(payload.description());
        productFound.setPrice(payload.price());
        productFound.setQuantityAvailable(payload.quantityAvailable());
        productFound.setCategory(categoryFound);
        productFound.setLastUpdate(LocalDateTime.now());
        return this.productsRepository.save(productFound);
    }

    //DELETE
    public void delete(UUID id) {
        this.productsRepository.delete(this.findById(id));
    }

    //IMG UPLOAD
    public void imgUpload(MultipartFile file, UUID id) throws IOException, MaxUploadSizeExceededException {
        Product productFound = this.findById(id);
        String url = (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("secure_url");
        productFound.setImgUrl(url);
        this.productsRepository.save(productFound);
    }

    //FIND BY PRODUCT CONTAINS NAME
    public Page<Product> findByProductsContainsName(int pages, int size, String sortBy, String name) {
        Pageable pageable = PageRequest.of(pages, size, Sort.by(sortBy).descending());
        return this.productsRepository.findByNameContainingIgnoreCase(pageable, name);
    }

    //FIND PRODUCT BY CATEGORY
    public Page<Product> findProductsByCategoryContainingName(int pages, int size, String sortBy, String name) {
        Pageable pageable = PageRequest.of(pages, size, Sort.by(sortBy).descending());
        return this.productsRepository.findProductsByCategoryNameContaining(pageable, name);
    }

    //FIND PRODUCT BY PRICE RANGE
    public Page<Product> findByPriceRange(int pages, int size, String sortBy, int min, int max) {
        Pageable pageable = PageRequest.of(pages, size, Sort.by(sortBy).descending());
        return this.productsRepository.findByPriceRange(pageable, min, max);
    }

    //FIND PRODUCT ON DISCOUNT
    public Page<Product> findByDiscountStatus(int pages, int size, String sortBy) {
        Pageable pageable = PageRequest.of(pages, size, Sort.by(sortBy).descending());
        return this.productsRepository.findByDiscountStatus(pageable);
    }

    //ADD DISCOUNT TO PRODUCT
    public Product addDiscount(UUID productId, ProductDiscountDTO payload) {
        Product productFound = this.findById(productId);
        Discount discountFound = this.discountsService.findById(UUID.fromString(payload.discount()));
        if (!productFound.getDiscountList().contains(discountFound)) {
            productFound.getDiscountList().add(discountFound);
            productFound.setDiscountStatus(true);
        } else {
            throw new BadRequestException("This product already have this discount!");
        }
        return this.productsRepository.save(productFound);
    }

    //REMOVE DISCOUNT
    public Product removeDiscount(UUID productId, ProductDiscountDTO payload) {
        Product productFound = this.findById(productId);
        Discount discountFound = this.discountsService.findById(UUID.fromString(payload.discount()));
        if (productFound.getDiscountList().contains(discountFound)) {
            productFound.getDiscountList().remove(discountFound);
            if (productFound.getDiscountList().isEmpty())
                productFound.setDiscountStatus(false);
        } else {
            throw new BadRequestException("This product don't have this discount!");
        }
        return this.productsRepository.save(productFound);
    }

    public Page<Product> filterProducts(int page, int size, String sortBy, String name, String categoryName, Double min, Double max, Boolean discountStatus) {
        if (page > 100) page = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Specification<Product> specification = Specification.where(null);

        if (name != null)
            specification = specification.and(ProductsSpecification.hasName(name));

        if (categoryName != null)
            specification = specification.and(ProductsSpecification.hasCategoryName(categoryName));

        if (min != null && max != null)
            specification = specification.and(ProductsSpecification.hasPriceRange(min, max));

        if (discountStatus != null)
            specification = specification.and(ProductsSpecification.hasDiscount(discountStatus));

        return productsRepository.findAll(specification, pageable);
    }
}
