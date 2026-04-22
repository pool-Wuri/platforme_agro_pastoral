package com.anptic.agropastoral.service;

import com.anptic.agropastoral.dto.payment.PaymentRequest;
import com.anptic.agropastoral.dto.payment.PaymentResponse;

public interface PaymentService {
    PaymentResponse processPayment(PaymentRequest paymentRequest);
}
