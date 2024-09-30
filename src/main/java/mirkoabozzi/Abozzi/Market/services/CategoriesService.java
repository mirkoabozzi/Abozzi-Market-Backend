package mirkoabozzi.Abozzi.Market.services;

import mirkoabozzi.Abozzi.Market.dto.CategoriesDTO;
import mirkoabozzi.Abozzi.Market.entities.Category;
import mirkoabozzi.Abozzi.Market.exceptions.BadRequestException;
import mirkoabozzi.Abozzi.Market.exceptions.NotFoundException;
import mirkoabozzi.Abozzi.Market.repositories.CategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategoriesService {
    @Autowired
    private CategoriesRepository categoriesRepository;

    //FIND BY ID
    public Category findById(UUID id) {
        return this.categoriesRepository.findById(id).orElseThrow(() -> new NotFoundException("Category with id " + id + " not found on DB"));
    }

    //POST SAVE
    public Category saveCategory(CategoriesDTO payload) {
        if (categoriesRepository.existsByName(payload.name().toUpperCase()))
            throw new BadRequestException("Category " + payload.name() + " already on DB");
        Category newCategory = new Category(payload.name().toUpperCase());
        return this.categoriesRepository.save(newCategory);
    }

    //DELETE
    public void deleteCategory(UUID id) {
        this.categoriesRepository.delete(this.findById(id));
    }
}
