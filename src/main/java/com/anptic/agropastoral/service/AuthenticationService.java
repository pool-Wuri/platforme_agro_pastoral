package com.anptic.agropastoral.service;

import com.anptic.agropastoral.dto.auth.AuthenticationRequest;
import com.anptic.agropastoral.dto.auth.AuthenticationResponse;
import com.anptic.agropastoral.dto.user.UserRequest;

public interface AuthenticationService {
    AuthenticationResponse register(UserRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
}
