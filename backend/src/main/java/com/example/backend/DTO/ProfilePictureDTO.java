package com.example.backend.DTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProfilePictureDTO {
    private String username;
    private MultipartFile profilePicture;

    public ProfilePictureDTO(String username, MultipartFile profilePicture){
        this.username = username;
        this.profilePicture = profilePicture;
    }
}
