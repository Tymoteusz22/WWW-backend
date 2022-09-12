package project.www.database;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import project.www.JWTUtil;
import project.www.database.model.UserCredentials;
import project.www.database.model.UserDetails;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class Controller {
    private final UserRepository userRepository;
    private final DirectoryRepository directoryRepository;
    private final JWTUtil jwtUtil;
    private final String rootPath = System.getProperty("user.dir");

    @CrossOrigin
    @PostMapping(path = "/createUser")
    public User addNewUser(@RequestBody UserDetails userDetails) {
        User n = new User();
        n.setUsername(userDetails.getUserCredentials().getUsername());
        n.setEmail(userDetails.getEmail());
        n.setPassword(userDetails.getUserCredentials().getPassword());
        n.setCreatedAt(Instant.now());
        return userRepository.save(n);
    }

    @CrossOrigin
    @PostMapping(path = "/generateToken")
    public String testCos(@RequestBody UserCredentials userCredentials) {
        return jwtUtil.generateToken(20);
    }


    @PostMapping(path = "/createFolder")
    public Directory createFolder(@RequestParam Integer userId, @RequestParam String name) {
        Directory d = new Directory();
        Optional<User> u = userRepository.findById(userId);
        d.setFolderName(name);
        d.setUser(u.get());
        return directoryRepository.save(d);
    }

    @GetMapping(path = "/getImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage(@RequestParam String folder, @RequestParam String imageName, @RequestParam Integer userId) throws IOException {
        String filePath = rootPath + String.format("\\images\\%s\\%s\\%s.jpg", userId, folder, imageName);
        return Files.readAllBytes(Path.of(filePath));
    }

    @PostMapping(path = "/saveImage")
    public void saveImage(@RequestParam String folder, @RequestParam String imageName, @RequestParam Integer userId, @RequestParam String image) throws IOException {
        String saveTo = rootPath + String.format("\\images\\%s\\%s\\%s.jpg", userId, folder, imageName);
        FileOutputStream fos = new FileOutputStream(saveTo);
        byte[] byteArray = Base64.getMimeDecoder().decode(image);
        fos.write(byteArray);
        fos.close();
    }



    @GetMapping(path = "/getClaims")
    public String getClaims(@RequestParam String token) {
        return jwtUtil.getAllClaimsFromToken(token).getSubject();
    }

    @GetMapping(path = "/userCount")
    public Long getUserCount() {
        return userRepository.findAll().spliterator().getExactSizeIfKnown();
    }

    @GetMapping(path = "/directoryCount")
    public Long getDirectoryCount() {
        return directoryRepository.findAll().spliterator().getExactSizeIfKnown();
    }
}
