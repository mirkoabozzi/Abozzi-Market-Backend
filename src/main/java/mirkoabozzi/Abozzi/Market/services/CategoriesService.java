package mirkoabozzi.Abozzi.Market.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import mirkoabozzi.Abozzi.Market.dto.CategoriesDTO;
import mirkoabozzi.Abozzi.Market.entities.Category;
import mirkoabozzi.Abozzi.Market.exceptions.BadRequestException;
import mirkoabozzi.Abozzi.Market.exceptions.NotFoundException;
import mirkoabozzi.Abozzi.Market.repositories.CategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class CategoriesService {
    @Autowired
    private CategoriesRepository categoriesRepository;
    @Autowired
    private Cloudinary cloudinary;

    //FIND BY ID
    public Category findById(UUID id) {
        return this.categoriesRepository.findById(id).orElseThrow(() -> new NotFoundException("Category with id " + id + " not found on DB"));
    }

    //POST SAVE
    public Category saveCategory(CategoriesDTO payload) {
        if (categoriesRepository.existsByNameIgnoreCase(payload.name()))
            throw new BadRequestException("Category " + payload.name() + " already on DB");
        Category newCategory = new Category(payload.name(), "https://ui-avatars.com/api/?name=" + payload.name());
        return this.categoriesRepository.save(newCategory);
    }

    //GET ALL
    public Page<Category> findAll(int page, int size, String sortBy) {
        if (page > 50) page = 50;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.categoriesRepository.findAll(pageable);
    }

    //PUT UPDATE
    public Category updateCategory(UUID id, CategoriesDTO payload) {
        Category categoryFound = this.findById(id);
        if (!categoryFound.getName().equals(payload.name()) && this.categoriesRepository.existsByNameIgnoreCase(payload.name()))
            throw new BadRequestException("Category " + payload.name() + " already on DB");
        categoryFound.setName(payload.name());
        return this.categoriesRepository.save(categoryFound);
    }

    //IMG UPLOAD
    public void imgUpload(MultipartFile file, UUID id) throws IOException, MaxUploadSizeExceededException {
        Category categoryFound = this.findById(id);
        String url = (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("secure_url");
        categoryFound.setImage(url);
        this.categoriesRepository.save(categoryFound);
    }

    //DELETE
    public void deleteCategory(UUID id) {
        this.categoriesRepository.delete(this.findById(id));
    }
}
