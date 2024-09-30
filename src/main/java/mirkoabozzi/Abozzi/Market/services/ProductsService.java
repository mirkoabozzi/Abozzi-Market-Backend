package mirkoabozzi.Abozzi.Market.services;

import mirkoabozzi.Abozzi.Market.dto.ProductsDTO;
import mirkoabozzi.Abozzi.Market.entities.Category;
import mirkoabozzi.Abozzi.Market.entities.Product;
import mirkoabozzi.Abozzi.Market.exceptions.BadRequestException;
import mirkoabozzi.Abozzi.Market.repositories.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class ProductsService {
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private CategoriesService categoriesService;

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

}
