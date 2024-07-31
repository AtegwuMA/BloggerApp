package com.blogger.blogger.services.serviceImpl;

import com.blogger.blogger.dto.request.UserRequestDTO;
import com.blogger.blogger.dto.response.GenericResponse;
import com.blogger.blogger.dto.response.UserDTOMapper;
import com.blogger.blogger.dto.response.UserResponseDTO;
import com.blogger.blogger.model.User;
import com.blogger.blogger.repository.UserRepository;
import com.blogger.blogger.services.UserService;
import lombok.Builder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Builder
@Slf4j
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private UserDTOMapper userDTOMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserDTOMapper userDTOMapper) {
        this.userRepository = userRepository;
        this.userDTOMapper = userDTOMapper;
    }

    @Override
    public GenericResponse login(String email, String password) {
        try {
            Optional<User> userFound = userRepository.findByEmailAndPassword(email, password);

            if (userFound.isEmpty()) {
                return new GenericResponse("01", "Invalid email or password", HttpStatus.UNAUTHORIZED);
            }

            UserResponseDTO userResponseDTO = userDTOMapper.apply(userFound.get());
            return new GenericResponse("00", "Successfully logged in", HttpStatus.ACCEPTED, userResponseDTO);

        } catch (Exception e) {
            log.error("Invalid email or password. ",e); // You can log the exception or handle it appropriately
            return new GenericResponse("02", "An error occurred during login", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public GenericResponse create(UserRequestDTO userDto) {
        try {
            User user = new User();
            BeanUtils.copyProperties(userDto, user);
            User saveUser = userRepository.save(user);
            UserResponseDTO userResponseDTO = userDTOMapper.apply(saveUser);
            return new GenericResponse("00", "Successfully created", HttpStatus.CREATED,
                        userResponseDTO);
        } catch (Exception e) {
            log.error("An error occurred during user creation. ",e);
            return new GenericResponse("02", "An error occurred during user creation",
                        HttpStatus.BAD_REQUEST, null);
        }
    }

}


//===============================================================original=====================

//@Service
//@Builder
//public class UserServiceImpl implements UserService {
//
//    private UserRepository userRepository;
//
//    private UserDTOMapper userDTOMapper;
//    @Autowired
//    public UserServiceImpl(UserRepository userRepository, UserDTOMapper userDTOMapper) {
//        this.userRepository = userRepository;
//        this.userDTOMapper = userDTOMapper;
//    }
//
//    @Override
//    public UserResponseDTO login(String email, String password) {
//        User userFound = userRepository.findByEmailAndPassword(email, password);
//        return userDTOMapper.apply(userFound);
//    }
//
//    @Override
//    public UserResponseDTO create(UserRequestDTO userDto) {
//        User user = new User();
//        BeanUtils.copyProperties(userDto, user);
//        User saveUser = userRepository.save(user);
//        return userDTOMapper.apply(saveUser);
//    }
//
//        @Override
//    public List<UserResponseDTO> getAllUsers() {
//        List<User> users = userRepository.findAll();
//        return users.stream()
//                .map(userDTOMapper::apply)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public  String loginImplimentation(HttpSession session, UserResponseDTO oauthUser) {
//        if (Objects.nonNull(oauthUser.getId())) {
//            session.setAttribute("loggedInUser", oauthUser);
//            Long userId = oauthUser.getId();
//            System.out.println("this is userid: "+userId);
//            return "redirect:/home";
//        } else {
//            return "redirect:/login";
//        }
//    }
//
//    public Long getUserIdByEmailAndPassword(String email, String password) {
//        Optional<User> userOptional = userRepository.findUserIdByEmailAndPassword(email, password);
//        // Handle case where user is not found
//        // You can return null or throw an exception
//        return userOptional.map(User::getId).orElse(null);
//    }
//}






//==========================================my new stuff======================================================

//    @Override
//    public List<UserResponseDTO> getAllUsers() {
//        List<User> users = userRepository.findAll();
//        return users.stream()
//                .map(userDTOMapper::apply)
//                .collect(Collectors.toList());
//    }




//    @Override
//    public List<UserResponseDTO> getAllUsers() {
//        List<User> users = userRepository.findAll();
//        List<UserResponseDTO> userResponseDTOs = users.stream()
//                .map(userDTOMapper::apply)
//                .collect(Collectors.toList());
//        return userResponseDTOs;
//    }


//@Override
//public UserResponseDTO create(UserRequestDTO userDto) {
//        User user = User.builder()
//                .firstName(userDto.getFirstName())
//                .lastName(userDto.getLastName())
//                .email(userDto.getEmail())
//                .password(userDto.getPassword())
//                .dob(userDto.getDob())
//                .build();
//    User user = new User();
//    BeanUtils.copyProperties(userDto, user);
//    User saveUser = userRepository.save(user);
//    return userDTOMapper.apply(saveUser);
//}

//    @Override
//    public GenericResponse create(UserRequestDTO userDto) {
//        User user = new User();
//        BeanUtils.copyProperties(userDto, user);
//        User saveUser = userRepository.save(user);
//        UserResponseDTO userResponseDTO = userDTOMapper.apply(saveUser);
//        return new GenericResponse("00", "Successfully created", HttpStatus.CREATED, userResponseDTO);
//    }

//        @Override
//    public List<UserResponseDTO> getAllUsers() {
//        List<User> users = userRepository.findAll();
//        return users.stream()
//                .map(userDTOMapper::apply)
//                .collect(Collectors.toList());
//    }


//    @Override
//    public GenericResponse login(String email, String password) {
//
//        Optional<User> userFound = userRepository.findByEmailAndPassword(email, password);
//
//        if (userFound.isEmpty()) {
//            log.error("Invalid email or password");
//            return new GenericResponse("01", "Invalid email or password", HttpStatus.UNAUTHORIZED);
//        }
//
//        UserResponseDTO userResponseDTO = userDTOMapper.apply(userFound.get());
//        return new GenericResponse("00", "Successfully logged in", HttpStatus.ACCEPTED, userResponseDTO);
//    }


//    public Long getUserIdByEmailAndPassword(String email, String password) {
//        Optional<User> userOptional = userRepository.findUserIdByEmailAndPassword(email, password);
//        // Handle case where user is not found
//        // You can return null or throw an exception
//        return userOptional.map(User::getId).orElse(null);
//    }
