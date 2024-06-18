package com.matchub.api.matchub_api.service;

import com.matchub.api.matchub_api.aws.S3Service;
import com.matchub.api.matchub_api.domain.HubUser;
import com.matchub.api.matchub_api.dto.HubUserDTOBase;
import com.matchub.api.matchub_api.dto.HubUserDTODetails;
import com.matchub.api.matchub_api.dto.HubUserDTOImage;
import com.matchub.api.matchub_api.dto.HubUserDTOLinks;
import com.matchub.api.matchub_api.repository.HubUserRepository;
import com.matchub.api.matchub_api.security.cookie.CookieService;
import com.matchub.api.matchub_api.security.token.domain.enums.Role;
import com.matchub.api.matchub_api.security.dto.ChangeBlockStateDTO;
import com.matchub.api.matchub_api.security.dto.ChangePasswordDTO;
import com.matchub.api.matchub_api.security.dto.ChangePositionDTO;
import com.matchub.api.matchub_api.security.dto.ResetPasswordDTO;
import com.matchub.api.matchub_api.security.token.service.TokenService;
import com.matchub.api.matchub_api.service.exceptions.ObjectNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class HubUserService{
    private final PasswordEncoder passwordEncoder;
    private final HubUserRepository hubUserRepository;
    private final ModelMapper modelMapper;
    private final CookieService cookieService;
    private final TokenService tokenService;
    private final S3Service s3Service;

    @Value("${api.data.default.img.path}")
    private String pathDefaultImg;

    private HubUser getAuthenticatedUser(Principal principal) {
        return (HubUser) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
    }

    private String maskUsername(String username) {
        // Verifica se a string tem menos de 4 caracteres
        if (username.length() < 4) {
            return username; // Retorna a string original se for muito curta para mascarar
        }

        // Pega o primeiro, segundo, penúltimo e último caractere
        char firstChar = username.charAt(0);
        char secondChar = username.charAt(1);
        char secondLastChar = username.charAt(username.length() - 2);
        char lastChar = username.charAt(username.length() - 1);

        // Cria uma string de asteriscos para substituir os caracteres entre o segundo e o penúltimo
        String maskedMiddle = "*".repeat(username.length() - 4);

        // Concatena os caracteres visíveis com a parte mascarada
        return firstChar + "" + secondChar + maskedMiddle + secondLastChar + lastChar;
    }

    public HubUserDTODetails findById(Long id) {
        // Get information and check existence
        Optional<HubUser> hubUser = hubUserRepository.findById(id);
        HubUser hubUserDomain = hubUser.orElseThrow( () -> new ObjectNotFoundException(
                "Object Not Found. " +
                "Id: "  + id + "." +
                "Type: " + HubUser.class.getName())
        );
        // Mask Username
        hubUserDomain.setUsername(maskUsername(hubUserDomain.getUsername()));
        // Return result
        return modelMapper.map(hubUserDomain, HubUserDTODetails.class);
    }

    public HubUser findDomainById(Long id) {
        // Get information and check existence
        Optional<HubUser> hubUser = hubUserRepository.findById(id);
        return hubUser.orElseThrow( () -> new ObjectNotFoundException(
                "Object Not Found. " +
                        "Id: "  + id + "." +
                        "Type: " + HubUser.class.getName())
        );
    }

    public HubUserDTOLinks findHubUser(Principal connectedHubUser){
        // Get old information by user logged
        HubUser logged = (HubUser) ((UsernamePasswordAuthenticationToken) connectedHubUser).getPrincipal();
        // Mask Username
        logged.setUsername(maskUsername(logged.getUsername()));
        // Save update and give response
        return modelMapper.map(logged, HubUserDTOLinks.class);
    }


    public HubUserDTOLinks updateHubUser(HubUserDTOBase hubUser,
                                         Principal connectedHubUser) {
        // Get old information by user logged
        HubUser updateHubUser = getAuthenticatedUser(connectedHubUser);
        // Update user
        modelMapper.map(hubUser, updateHubUser);
        // Save Update, Mask Username and Give Response
        return updateAndSave(updateHubUser);
    }

    public void confirmToRegister(Principal connectedHubUser, HttpServletResponse response){
        // Get old information by user logged
        HubUser updateHubUser = getAuthenticatedUser(connectedHubUser);
        // Update info
        updateHubUser.setChecked(true);
        updateHubUser.setRole(Role.HUBUSER);
        // Revoke tokens: It is not possible to do it here, because that would end the request
        // tokenService.revokeAllUserTokens(updateHubUser);
        // Create Refresh Token
        String refreshToken = tokenService.generateRefreshToken(updateHubUser);
        // Add Cookie
        cookieService.addCookie(response, refreshToken);
        // Save Update, Mask Username and Give Response
        updateAndSave(updateHubUser);
    }

    private HubUserDTOLinks updateAndSave(HubUser updateHubUser){
        // Save update
        HubUserDTOLinks savedHubUser = modelMapper.map(hubUserRepository.save(updateHubUser), HubUserDTOLinks.class);
        // Mask Username
        savedHubUser.setUsername(maskUsername(savedHubUser.getUsername()));
        // Give response
        return savedHubUser;
    }


    public void changePassword(ChangePasswordDTO request,
                               Principal connectedHubUser) {
        // Get old information by user logged
        HubUser logged = getAuthenticatedUser(connectedHubUser);

        // Check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), logged.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // Check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }
        // Update the password
        logged.setPassword(passwordEncoder.encode(request.getNewPassword()));
        // Save the new password
        hubUserRepository.save(logged);
    }

    public void resetPassword(ResetPasswordDTO request, Principal connectedHubUser){
        // Get old information by user logged
        HubUser logged = getAuthenticatedUser(connectedHubUser);
        // Update the password
        logged.setPassword(passwordEncoder.encode(request.getPassword()));
        // Check if the two new passwords are the same
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalStateException("Password are not the same");
        }
        // Save the new password
        hubUserRepository.save(logged);
    }

    public HubUserDTOLinks block(Long hubUserid, ChangeBlockStateDTO request) {
        // Get de new block user
        HubUser blocked = this.findDomainById(hubUserid);
        // Alter state
        blocked.setBlocked(request.getBlocked());
        // Save
        return modelMapper.map(hubUserRepository.save(blocked), HubUserDTOLinks.class);
    }

    public void delete(Long id) {
        hubUserRepository.deleteById(id);
    }

    public HubUserDTOLinks alterPosition(Long hubUserid, ChangePositionDTO request) {
        HubUser updatedHubUser = this.findDomainById(hubUserid);
        updatedHubUser.setRole(request.getRole());
        // Save update
        HubUserDTOLinks savedHubUser = modelMapper.map(hubUserRepository.save(updatedHubUser), HubUserDTOLinks.class);
        // Mask Username
        savedHubUser.setUsername(maskUsername(savedHubUser.getUsername()));
        // Give response
        return savedHubUser;
    }

    private String generateFileName(String userEmail) {
        String emailModified = userEmail.replace("@", "_");
        String extension = ".jpg";
        return emailModified + extension;
    }

    public void uploadDefaultImageS3(String email){
        File defaultImg = new File(pathDefaultImg);
        String fileName = generateFileName(email);

        s3Service.uploadImage(fileName, defaultImg);
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    public HubUserDTOImage uploadImageS3(MultipartFile multipartFile, Principal connectedHubUser) throws IOException {
        // Get old information by user logged
        HubUser logged = getAuthenticatedUser(connectedHubUser);

        /*
        MultipartFile: Spring interface that represents a file sent through a
        web form. It is specifically used to handle multipart/form-data requests
        */
        /*
        File: represents a file or directory path on the file system.
        It does not contain the content of the file itself, but is an abstract representation
        of the path in the file system
        */
        File file = convertMultiPartToFile(multipartFile);
        String fileName = generateFileName(logged.getEmail());

        return new HubUserDTOImage(s3Service.uploadImage(fileName, file));
    }

    @Scheduled(cron = "0 0 2 * * ?") // Run at 2 A.M.
    public void cleanHubUserGuest() {
        List<HubUser> guests = findGuests();
        hubUserRepository.deleteAll(guests);
    }

    private List<HubUser> findGuests() {
        List<HubUser> allTokens = hubUserRepository.findAll();
        return allTokens.stream()
                .filter(hubUser -> hubUser.getRole().equals(Role.GUEST))
                .collect(Collectors.toList());
    }
}
