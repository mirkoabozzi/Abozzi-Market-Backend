package mirkoabozzi.Abozzi.Market.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import mirkoabozzi.Abozzi.Market.dto.ProductDiscountDTO;
import mirkoabozzi.Abozzi.Market.dto.ProductsDTO;
import mirkoabozzi.Abozzi.Market.entities.Category;
import mirkoabozzi.Abozzi.Market.entities.Discount;
import mirkoabozzi.Abozzi.Market.entities.Product;
import mirkoabozzi.Abozzi.Market.exceptions.BadRequestException;
import mirkoabozzi.Abozzi.Market.exceptions.NotFoundException;
import mirkoabozzi.Abozzi.Market.repositories.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

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
        if (this.productsRepository.existsByName(payload.name()))
            throw new BadRequestException("Product " + payload.name() + " already on DB");
        Category categoryFound = this.categoriesService.findById(UUID.fromString(payload.category()));
        Product newProduct = new Product(
                payload.name(),
                payload.description(),
                payload.price(),
                payload.quantityAvailable(),
                LocalDate.now(),
                "https://ui-avatars.com/api/?name=" + payload.name() + "+" + payload.description(),
                categoryFound);
        return this.productsRepository.save(newProduct);
    }

    public Product findById(UUID id) {
        return this.productsRepository.findById(id).orElseThrow(() -> new NotFoundException("Product whit ID " + id + " not found"));
    }

    //GET ALL
    public Page<Product> getAllProducts(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
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
        productFound.setLastUpdate(LocalDate.now());
        return this.productsRepository.save(productFound);
    }

    //DELETE
    public void delete(UUID id) {
        this.productsRepository.delete(this.findById(id));
    }

    //IMG UPLOAD
    public void imgUpload(MultipartFile file, UUID id) throws IOException, MaxUploadSizeExceededException {
        Product productFound = this.findById(id);
        String url = (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        productFound.setImgUrl(url);
        this.productsRepository.save(productFound);
    }

    //FIND BY PRODUCT CONTAINS NAME
    public Page<Product> findByProductsContainsName(int pages, int size, String sortBy, String name) {
        Pageable pageable = PageRequest.of(pages, size, Sort.by(sortBy));
        return this.productsRepository.findByNameContainingIgnoreCase(pageable, name);
    }

    //FIND PRODUCT BY CATEGORY
    public Page<Product> findProductsByCategoryContainingName(int pages, int size, String sortBy, String name) {
        Pageable pageable = PageRequest.of(pages, size, Sort.by(sortBy));
        return this.productsRepository.findProductsByCategoryNameContaining(pageable, name);
    }

    //ADD DISCOUNT TO PRODUCT
    public Product addDiscount(UUID productId, ProductDiscountDTO payload) {
        Product productFound = this.findById(productId);
        Discount discountFound = this.discountsService.findById(UUID.fromString(payload.discount()));
        if (!productFound.getDiscountList().contains(discountFound)) {
            productFound.getDiscountList().add(discountFound);
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
        } else {
            throw new BadRequestException("This product don't have this discount!");
        }
        return this.productsRepository.save(productFound);
    }
}
