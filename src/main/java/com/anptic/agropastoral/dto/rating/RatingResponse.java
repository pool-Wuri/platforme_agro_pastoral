package com.anptic.agropastoral.dto.rating;

import com.anptic.agropastoral.dto.user.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RatingResponse {

    private UUID id;
    private UserResponse productor;
    private UserResponse buyer;
    private Integer score;
    private String comment;
    private LocalDateTime createdAt;
}
