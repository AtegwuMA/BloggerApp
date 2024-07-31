package com.blogger.blogger.services;




import com.blogger.blogger.dto.request.UserRequestDTO;
import com.blogger.blogger.dto.response.GenericResponse;
import com.blogger.blogger.dto.response.UserResponseDTO;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface UserService {
    GenericResponse login(String email, String password);


    GenericResponse create(UserRequestDTO userDto);


}
