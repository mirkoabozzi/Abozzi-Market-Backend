package mirkoabozzi.Abozzi.Market.services;

import jakarta.transaction.Transactional;
import mirkoabozzi.Abozzi.Market.dto.DiscountsDTO;
import mirkoabozzi.Abozzi.Market.entities.Discount;
import mirkoabozzi.Abozzi.Market.entities.Product;
import mirkoabozzi.Abozzi.Market.exceptions.BadRequestException;
import mirkoabozzi.Abozzi.Market.exceptions.NotFoundException;
import mirkoabozzi.Abozzi.Market.repositories.DiscountsRepository;
import mirkoabozzi.Abozzi.Market.repositories.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DiscountsService {
    @Autowired
    private DiscountsRepository discountsRepository;
    @Autowired
    private ProductsRepository productsRepository;

    //POST SAVE
    public Discount saveDiscount(DiscountsDTO payload) {
        if (this.discountsRepository.existsByDescription(payload.description()))
            throw new BadRequestException("Discount " + payload.description() + " already on DB");
        Discount newDiscount = new Discount(payload.description(), payload.percentage(), payload.startDate(), payload.endDate());
        return this.discountsRepository.save(newDiscount);
    }

    //FIND BY ID
    public Discount findById(UUID id) {
        return this.discountsRepository.findById(id).orElseThrow(() -> new NotFoundException("Discount whit ID " + id + " not found"));
    }

    //GET ALL
    public Page<Discount> getAllDiscounts(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.discountsRepository.findAll(pageable);
    }

    //UPDATE
    public Discount updateDiscount(UUID id, DiscountsDTO payload) {
        Discount discountFound = this.findById(id);
        discountFound.setDescription(payload.description());
        discountFound.setPercentage(payload.percentage());
        discountFound.setStartDate(payload.startDate());
        discountFound.setEndDate(payload.endDate());
        return this.discountsRepository.save(discountFound);
    }

    //DELETE
    @Transactional
    public void deleteDiscount(UUID discountId) {
        Discount discountFound = this.findById(discountId);
        List<Product> productsWithDiscount = this.productsRepository.findByDiscountListContaining(discountFound);
        for (Product product : productsWithDiscount) {
            product.getDiscountList().remove(discountFound);
            product.setDiscountStatus(false);
            productsRepository.save(product);
        }
        this.discountsRepository.delete(discountFound);
    }
}
