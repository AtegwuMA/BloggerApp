package com.blogger.blogger.services.serviceImpl;

import com.blogger.blogger.dto.request.PostRequestDto;
import com.blogger.blogger.dto.response.GenericResponse;
import com.blogger.blogger.dto.response.PostCreateDTOMapper;
import com.blogger.blogger.dto.response.PostResponseDTO;
import com.blogger.blogger.dto.response.PostUpdateDTOMapper;
import com.blogger.blogger.exceptions.AppRequestException;
import com.blogger.blogger.model.Post;
import com.blogger.blogger.model.User;
import com.blogger.blogger.repository.CommentRepository;
import com.blogger.blogger.repository.PostRepository;
import com.blogger.blogger.repository.UserRepository;
import com.blogger.blogger.services.PostService;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Builder
@Slf4j

public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final PostCreateDTOMapper postCreateDTOMapper;

    private final CommentRepository commentRepository;

    private final PostUpdateDTOMapper postUpdateDTOMapper;

    private final UserRepository userRepository; // Inject UserRepository to retrieve the User object

    @Autowired
    public PostServiceImpl(PostRepository postRepository, PostCreateDTOMapper postCreateDTOMapper, CommentRepository commentRepository, PostUpdateDTOMapper postUpdateDTOMapper, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.postCreateDTOMapper = postCreateDTOMapper;
        this.commentRepository = commentRepository;
        this.postUpdateDTOMapper = postUpdateDTOMapper;
        this.userRepository = userRepository;
    }

    @Override
    public GenericResponse getAllPosts(Long userId, Pageable pageable) {
        if (userId == null)  {
            throw new IllegalArgumentException("Invalid input");
        }
        Page<Post> posts = postRepository.findAllByUserId(userId, pageable);
        List<PostResponseDTO> postResponseDTOs = posts.stream()
                .map(new PostCreateDTOMapper())
                .collect(Collectors.toList());
        Page<PostResponseDTO> postResponseDTOS = new PageImpl<>(postResponseDTOs, pageable, posts.getTotalElements());
        return new GenericResponse("00", "Successful", HttpStatus.OK, postResponseDTOS);
    }
    @Override
    public GenericResponse createPost(PostRequestDto postRequestDto, Long userId) {
        if (userId == null)  {
            throw new IllegalArgumentException("Invalid input");
        }
        try {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppRequestException("UserId " + userId + " not found"));
        MultipartFile file = postRequestDto.getMedia();
        Date createdAt = new Date();
        String storageFileName = createdAt.getTime() + "_" + file.getOriginalFilename();


            String uploadDir = "public/dbImages";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);

            }
            try (InputStream inputStream = file.getInputStream()){
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (NullPointerException e){
                log.error("Exception: ",e);
            }

            if (postRequestDto.getTitle().isEmpty() & postRequestDto.getDescription().isEmpty()
                    & postRequestDto.getMedia().isEmpty()){
                throw new AppRequestException("Post cannot be empty");
            }
            Post post = Post.builder()
                    .title(postRequestDto.getTitle())
                    .description(postRequestDto.getDescription())
                    .media(storageFileName)
                    .user(user) // Set the User object to the user property
                    .build();
            Post savedPost = postRepository.save(post);

            PostResponseDTO responseDTO = postCreateDTOMapper.apply(savedPost);
            return new GenericResponse ("00", "Successfully created", HttpStatus.CREATED, responseDTO);
        } catch (NullPointerException npe) {
            log.error("NullPointerException occurred: ", npe);
            return new GenericResponse("02", "", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Exception occurred: ", e);
            return new GenericResponse("02", "", HttpStatus.BAD_REQUEST);
        }

    }



    @Override
    public GenericResponse updatePost(Long userId, Long postId, PostRequestDto postRequestDto) {
        if (postId == null || userId == null) {
            throw new IllegalArgumentException("UserId and PostId cannot be null");
        }

        try {
            PostResponseDTO postResponseDTO = (PostResponseDTO) getPostById(userId, postId).getData();
            if (postResponseDTO == null) {
                throw new AppRequestException("Post not found");
            }
            log.info("check this =======> {}", postRequestDto.getMedia());
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new AppRequestException("User with ID " + userId + " not found"));

            if (postRequestDto.getMedia() != null && !postRequestDto.getMedia().isEmpty()) {
                log.info("check this 67=======> {}", postRequestDto.getMedia());
                handleMediaUpdate(postRequestDto, postResponseDTO);
                log.info("check this not empty=======> {}", postResponseDTO.getMedia());
            }

            postResponseDTO.setTitle(postRequestDto.getTitle());
            postResponseDTO.setCategory(postRequestDto.getCategory());
            postResponseDTO.setDescription(postRequestDto.getDescription());

            Post updatedPost = mapPostResponseDtoToPost(postResponseDTO, user);
            Post savedPost = postRepository.save(updatedPost);

            PostResponseDTO updatedPostResponseDTO = postUpdateDTOMapper.apply(savedPost);

            return new GenericResponse("00", "Post updated successfully", HttpStatus.ACCEPTED, updatedPostResponseDTO);
        } catch (Exception e) {
            log.error("Error updating post: {}", e.getMessage());
            return new GenericResponse("02", "Failed to update post", HttpStatus.BAD_REQUEST);
        }
    }

    private void handleMediaUpdate(PostRequestDto postRequestDto, PostResponseDTO postResponseDTO) throws IOException {
        log.info("check this 12=======> {}", postResponseDTO.getMedia());
        String uploadDir = "public/dbImages";
        log.info("check this ht=======> {}", uploadDir);
        Path oldImagePath = Paths.get(uploadDir + postResponseDTO.getMedia());
        log.info("check this ht=======> {}", oldImagePath);
//        Files.delete(oldImagePath);
        log.info("check this ht34=======> {}", oldImagePath);
        MultipartFile file = postRequestDto.getMedia();
        log.info("check this ht=======> {}", file.getOriginalFilename());
        Date createdAt = new Date();
        String storageFileName = createdAt.getTime() + "_" + file.getOriginalFilename();
        log.info("check this ht=======> {}", storageFileName);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
        }

        postResponseDTO.setMedia(storageFileName);
        log.info("check this =======> {}", storageFileName);
    }

    private Post mapPostResponseDtoToPost(PostResponseDTO postResponseDTO, User user) {
        log.info("check this44 =======> {}", postResponseDTO.getMedia());
        return Post.builder()
                .id(postResponseDTO.getId())
                .title(postResponseDTO.getTitle())
                .category(postResponseDTO.getCategory())
                .description(postResponseDTO.getDescription())
                .media(postResponseDTO.getMedia())
                .user(user)
                .build();
    }



//    @Override
//    public GenericResponse updatePost(Long userId, Long postId, PostRequestDto postRequestDto)
//            {
//        if (postId == null || userId == null)  {
//            throw new IllegalArgumentException("UserId and PostId cannot be null");
//        }
//        try {
//            PostResponseDTO postResponseDTO = (PostResponseDTO) getPostById(userId, postId).getData();
//            User user = userRepository.findById(userId)
//                .orElseThrow(() -> new AppRequestException("UserId " + userId + " not found"));
//
//        if (!postRequestDto.getMedia().isEmpty()) {
//            //delete old image
//            log.info("this is the media1 ====> {}", postRequestDto.getMedia());
//            String uploadDir = "public/dbImages";
//            Path oldImagePath = Paths.get(uploadDir + postResponseDTO.getMedia());
//            Files.delete(oldImagePath);
//            MultipartFile file = postRequestDto.getMedia();
//            Date createdAt = new Date();
//            String storageFileName = createdAt.getTime() + "_" + file.getOriginalFilename();
//            try (InputStream inputStream = file.getInputStream()) {
//                Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
//                        StandardCopyOption.REPLACE_EXISTING);
//            }
//            log.info("this is the media23 ====> {}", postRequestDto.getMedia());
//            postResponseDTO.setMedia(storageFileName);
//            log.info("testing 50");
//
//        }
//
//        postResponseDTO.setTitle(postRequestDto.getTitle());
//        postResponseDTO.setCategory(postRequestDto.getCategory());
//        postResponseDTO.setDescription(postRequestDto.getDescription());
//        Post post = Post.builder()
//                .id(postResponseDTO.getId())
//                .title(postResponseDTO.getTitle())
//                .category(postResponseDTO.getCategory())
//                .description(postResponseDTO.getDescription())
//                .media(postResponseDTO.getMedia())
//                .user(user)
//                .build();
//        log.info(" final test");
//        Post savedPost = postRepository.save(post);
//        PostResponseDTO postResponseDTO1 = postUpdateDTOMapper.apply(savedPost);
//        return new GenericResponse ("00", "Updated successfully", HttpStatus.ACCEPTED, postResponseDTO1);
//        } catch (IOException e) {
//            log.error("Exception: {}" , e.getMessage());
//            return new GenericResponse ("02", "", HttpStatus.BAD_REQUEST);
//        }
//    }

    @Override
    public GenericResponse deletePost(Long userId, Long postId) {
        if (postId == null || userId == null)  {
            throw new IllegalArgumentException("UserId and PostId cannot be null");
        }
        return postRepository.findById(postId).map(post -> {
            Path oldImagePath = Paths.get("public/dbImages" + post.getMedia());
            try {
                Files.delete(oldImagePath);
            }
            catch (Exception ex){
                System.out.println("Exception: " + ex.getMessage());
            }
            postRepository.delete(post);
            return new GenericResponse("00", "Successfully deleted", HttpStatus.OK);
        }).orElseThrow(() -> new AppRequestException("PostId " + postId + " not found"));
    }
    @Override
    public GenericResponse getPostById(Long userId, Long postId) {
        if (postId == null || userId == null)  {
            throw new IllegalArgumentException("UserId and PostId cannot be null");
        }
        Post post = postRepository.findById(postId).get();
        PostResponseDTO postResponseDTO = postUpdateDTOMapper.apply(post);
        return new GenericResponse("00", "Successful", HttpStatus.OK, postResponseDTO);
    }



}



//    public Page<PostResponseDTO> getAllPosts(PostResponseDTO postResponseDTO, Long userId) {
////        Iterable<Long> userIdList = Collections.singletonList(userId);
////        Page<Post> posts = postRepository.findAllById(userIdList);
//
//        Page<Post> posts = (Page<Post>) postRepository.findAllById(Collections.singleton(userId));
//
//        List<PostResponseDTO> postResponseDTOS = new ArrayList<>();
//        posts.getContent().forEach(post -> {
//            byte[] fileContent = post.getMedia();
//            MultipartFile file =  ImageUtils.byteArrayToMultipartFile(fileContent);
//            postResponseDTO.setMedia(file);
//
//           // Set other properties of dto if needed
//
//            postResponseDTOS.add(postResponseDTO);
//        });
//
//        return new PageImpl<>(postResponseDTOS, pageable, posts.getTotalElements());
//    }

//    public Page<PostResponseDTO> getAllPosts(Long userId) {
//        Page<Post> posts = (Page<Post>) postRepository.findAllById(Collections.singleton(userId));
//
//        List<PostResponseDTO> postResponseDTOS = new ArrayList<>();
//        posts.getContent().forEach(post -> {
//            byte[] fileContent = post.getMedia();
//            MultipartFile file = ImageUtils.byteArrayToMultipartFile(fileContent);
//
//            PostResponseDTO postResponseDTO = new PostResponseDTO();
//            postResponseDTO.setId(post.getId());
//            postResponseDTO.setTitle(post.getTitle());
//            postResponseDTO.setDescription(post.getDescription());
//            postResponseDTO.setMedia(file);
//            // Set other properties of dto if needed
//
//            postResponseDTOS.add(postResponseDTO);
//        });
//
//        return new PageImpl<>(postResponseDTOS, posts.getPageable(), posts.getTotalElements());
//    }


//===========================================================================


//    public Page<PostRequestDto> getAllPosts(PostRequestDto postRequestDto, Pageable pageable) {
//
//        Page<Post> posts = postRepository.findAll(pageable);
//        posts.getContent().forEach(post -> {
//            byte[] fileContent = post.getMedia();
//            MultipartFile file = new MockMultipartFile("file", "filename", "application/octet-stream", ImageUtils.decompressImage(fileContent));
//            postRequestDto.setFile(file);
//        });
//        return
//    }


//public Page<Post> getAllPosts(Pageable pageable) {
//        return postRepository.findAll(pageable);
//    }

//    public Post createPost(Post post, MultipartFile file) {
//
//        try {
//            byte[] fileBytes = file.getBytes();
//
//            // Log or debug the byte array content before setting it to the post entity
//            System.out.println("Length of byte array data: " + fileBytes.length);
//            System.out.println("Byte array data content: " + Arrays.toString(Arrays.copyOfRange(fileBytes, 0, Math.min(fileBytes.length, 100))) + "...");
//
//            post.setMedia(fileBytes);
//
//            post.setMedia(fileBytes);
//        } catch (IOException e) {
//            // Handle the exception
//        }
//        return postRepository.save(post);
//    }


//public PostResponseDTO createPost(PostRequestDto postRequestDto, MultipartFile file) {
//    try {
//        byte[] fileBytes;
//
//        // Check the type of MultipartFile and convert accordingly
//        if (file != null) {
//            fileBytes = file.getBytes();
//        } else {
//            // Handle other types of MultipartFile if needed
//            throw new IllegalArgumentException("Unsupported MultipartFile type: " + file.getClass().getName());
//        }
//
//        // Log or debug the byte array content before setting it to the post entity
//        System.out.println("Length of byte array data: " + fileBytes.length);
//        System.out.println("Byte array data content: " + Arrays.toString(Arrays.copyOfRange(fileBytes, 0, Math.min(fileBytes.length, 100))) + "...");
//
//        Post post = Post.builder()
//                .title(postRequestDto.getTitle())
//                .description(postRequestDto.getDescription())
//                .media(ImageUtils.compressImage(fileBytes))
//                .build();
//        System.out.println("Length of byte array data: " + ImageUtils.compressImage(fileBytes).length);
//        System.out.println("Byte array data content: " + Arrays.toString(Arrays.copyOfRange(ImageUtils.compressImage(fileBytes), 0, Math.min(fileBytes.length, 100))) + "...");
//
//
//        Post savedpPost = postRepository.save(post);
//        System.out.println("Length of byte array data2: " + ImageUtils.compressImage(fileBytes).length);
//        System.out.println("Byte array data content2: " + Arrays.toString(Arrays.copyOfRange(ImageUtils.compressImage(fileBytes), 0, Math.min(fileBytes.length, 100))) + "...");
//        return PostResponseDTO.builder()
//                .id(savedpPost.getId())
//                .title(savedpPost.getTitle())
//                .description(savedpPost.getDescription())
//                .file(ImageUtils.byteArrayToMultipartFile(savedpPost.getMedia()))
//                .createdAt(savedpPost.getCreatedAt())
//                .build();
//
//    } catch (IOException e) {
//        // Handle the exception
//    }
//
//    return null;
//}
