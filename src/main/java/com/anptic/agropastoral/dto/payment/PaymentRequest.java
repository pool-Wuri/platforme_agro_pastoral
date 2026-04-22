package com.anptic.agropastoral.dto.payment;

import com.anptic.agropastoral.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    @NotNull(message = "L'ID de la réservation est obligatoire")
    private UUID reservationId;

    @NotNull(message = "Le moyen de paiement est obligatoire")
    private PaymentMethod paymentMethod;

    @NotBlank(message = "La référence de transaction est obligatoire")
    private String transactionId;
}
