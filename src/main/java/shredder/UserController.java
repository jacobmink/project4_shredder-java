package shredder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private TrailRepository trailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @GetMapping("/user")
    public Iterable<Users> allUsers(){
        Iterable<Users> userList = userRepository.findAll();
        return userList;
    }

//    @PostMapping("/user/login")
//    public HashMap login(@RequestBody Users user, HttpSession session) throws Exception{
//        bCryptPasswordEncoder = new BCryptPasswordEncoder();
//        Users userLoggingIn = userRepository.findByUsername(user.getUsername());
//        boolean validLogin = bCryptPasswordEncoder.matches(user.getPassword(), userLoggingIn.getPassword());
//        if(validLogin){
//            session.setAttribute("username", userLoggingIn.getUsername());
//            Iterable<Trail> favoriteTrails = trailRepository.findByUserId(userLoggingIn.getId());
//            HashMap<String, Object> result = new HashMap<String, Object>();
//            result.put("user", userLoggingIn);
//            result.put("trails", favoriteTrails);
//            return result;
//        }else{
//            throw new Exception("invalid credentials");
//        }
//
//    }

//    @PostMapping("/user/login")
//    public Users login(@RequestBody Users user, HttpSession session) throws Exception{
//        bCryptPasswordEncoder = new BCryptPasswordEncoder();
//        Users userLoggingIn = userRepository.findByUsername(user.getUsername());
//        boolean validLogin = bCryptPasswordEncoder.matches(user.getPassword(), userLoggingIn.getPassword());
//        if(validLogin){
//            session.setAttribute("username", userLoggingIn.getUsername());
//            return userLoggingIn;
//        }else{
//            throw new Exception("invalid credentials");
//        }
//
//    }

    @PostMapping("/user/login")
    public HashMap login(@RequestBody Users user, HttpSession session) throws Exception{
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Users userLoggingIn = userRepository.findByUsername(user.getUsername());
        boolean validLogin = bCryptPasswordEncoder.matches(user.getPassword(), userLoggingIn.getPassword());
        if(validLogin){
            session.setAttribute("username", userLoggingIn.getUsername());
            ArrayList<Object> favoriteTrails = new ArrayList<Object>();
            for(Object trail : userLoggingIn.getFavoriteTrails()){
                favoriteTrails.add(trail);
            }
            HashMap<String, Object> result = new HashMap<String, Object>();
            result.put("user", userLoggingIn);
            result.put("favoriteTrails", favoriteTrails);
            return result;
        }else{
            throw new Exception("invalid credentials");
        }

    }

    @PostMapping("/user/register")
    public Users register(@RequestBody Users user, HttpSession session) throws Exception{
        try{
            Users newUser = userService.saveUser(user);
            session.setAttribute("username", newUser.getUsername());
            return newUser;
        }catch(Exception err){
            throw new Exception(err.getMessage());
        }

    }

    @GetMapping("/user/{id}")
    public Users getUser(@PathVariable Long id) throws Exception{
        Optional<Users> foundUser = userRepository.findById(id);
        return foundUser.get();
    }

    @PutMapping("/user/{id}")
    public Users editUser(@PathVariable Long id, @RequestBody Users user) throws Exception{

            Optional<Users> foundUser = userRepository.findById(id);
            if(foundUser.isPresent()){
                Users userToEdit = foundUser.get();
                if(user.getUsername() != ""){
                    userToEdit.setUsername(user.getUsername());
                }
                if(user.getEmail() != ""){
                    userToEdit.setEmail(user.getEmail());
                }
                if(user.getFirstName() != "") {
                    userToEdit.setFirstName(user.getFirstName());
                }
                if(user.getLastName() != "") {
                    userToEdit.setLastName(user.getLastName());
                }
                if(user.getLocalMountain() != "") {
                    userToEdit.setLocalMountain(user.getLocalMountain());
                }
                return userRepository.save(userToEdit);
            }else{
                throw new Exception("Something went wrong!");
            }
    }

    @PostMapping("/user/trail/{trail_id}")
    public Trail addTrailToFavorites(@PathVariable Long trail_id, HttpSession session) throws Exception{
        try{
            Users foundUser = userService.findUserByUsername(session.getAttribute("username").toString());
            Optional<Trail> foundTrail = trailRepository.findById(trail_id);
            foundUser.setFavoriteTrails(foundTrail.get());
            userRepository.save(foundUser);
            return foundTrail.get();
        }catch(Exception err){
            throw new Exception(err.getMessage());
        }
    }


    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable Long id) throws Exception{
        try{
            userRepository.deleteById(id);
            return "user " + id + " deleted";
        }catch(Exception err){
            throw new Exception(err.getMessage());
        }
    }
}
