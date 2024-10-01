package mirkoabozzi.Abozzi.Market.services;

import mirkoabozzi.Abozzi.Market.dto.PaymentsDTO;
import mirkoabozzi.Abozzi.Market.entities.Payment;
import mirkoabozzi.Abozzi.Market.exceptions.BadRequestException;
import mirkoabozzi.Abozzi.Market.exceptions.NotFoundException;
import mirkoabozzi.Abozzi.Market.repositories.PaymentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class PaymentsService {
    @Autowired
    private PaymentsRepository paymentsRepository;

    //POST SAVE
    public Payment savePayments(PaymentsDTO payload) {
        if (this.paymentsRepository.existsByDescription(payload.description()))
            throw new BadRequestException("Payment " + payload.description() + " already on DB");
        Payment newPayment = new Payment(payload.description(), payload.total());
        return this.paymentsRepository.save(newPayment);
    }

    public Payment findById(UUID id) {
        return this.paymentsRepository.findById(id).orElseThrow(() -> new NotFoundException("Payment whit ID " + id + " not found"));
    }

    //GET ALL
    public Page<Payment> getAllPayments(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.paymentsRepository.findAll(pageable);
    }

    //UPDATE
    public Payment updatePayment(UUID id, PaymentsDTO payload) {
        Payment paymentFound = this.findById(id);
        paymentFound.setDescription(payload.description());
        paymentFound.setTotal(payload.total());
        paymentFound.setPaymentDate(LocalDate.now());
        return this.paymentsRepository.save(paymentFound);
    }

    //DELETE
    public void deletePayment(UUID id) {
        this.paymentsRepository.delete(this.findById(id));
    }

}
