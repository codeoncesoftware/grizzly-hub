package fr.codeonce.grizzlyhub.auth.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import fr.codeonce.grizzlyhub.auth.domain.user.User;
import fr.codeonce.grizzlyhub.auth.domain.user.UserRepository;
import fr.codeonce.grizzlyhub.auth.service.auth.AuthService;
import fr.codeonce.grizzlyhub.auth.service.user.UserDto;
import fr.codeonce.grizzlyhub.auth.service.user.UserService;
import fr.codeonce.grizzlyhub.auth.service.util.SecurityContextUtil;
import fr.codeonce.grizzlyhub.team.domain.Team;
import fr.codeonce.grizzlyhub.team.service.ITeamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins ="localhost:4200")
@RequestMapping("/api/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ITeamService teamService;

    @GetMapping("/{email}")
    public UserDto getUser(@PathVariable String email){
        return userService.getUser(email);
    }


    @GetMapping("/logout")
    public void logout(HttpServletRequest req) {
        log.info("request logout");
        userService.logout(req);
    }

    /**
     * Update the user Password depending on the received Token and the new password
     *
     * @param token
     * @param password
     */
    @PostMapping("/reset/password/{token}")
    public void resetPassword(@PathVariable String token, @RequestBody Map<String, String> password) {
        log.info("request to reset password received");
        userService.resetPassword(token, password.get("password"));
    }

    /**
     * Send an email to the user to grant him access to the reset password page
     *
     * @param email
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    @GetMapping("/send/reset/password/{email}")
    public void sendResetPassword(@PathVariable String email, HttpServletRequest req) {
        log.info("request to send reset password email for {} ", email);
        userService.sendResetPassword(email, req.getHeader("Accept-Language"));
    }

    @PutMapping(value = "/update/user", consumes = "application/json", produces = "application/json")
    public UserDto updateUser(@RequestBody UserDto user) throws JSONException, IOException {

        log.info("Request params " + user.getEmail());
        try {
            return userService.updateUser(user);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/update/pwd")
    public boolean updateUserPwd(@RequestParam String oldPwd, @RequestParam String newPwd) {
        log.info("Request to update password");
        return userService.updateUserPwd(oldPwd, newPwd);
    }

    @GetMapping("/all")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping("/teams")
    public List<Team> getTeams() {
        return teamService.findAllTeams();
    }

    @PostMapping("/teams/{adminEmail}")
    public Team AddTeam(@RequestBody Team team, @PathVariable String adminEmail) {
        return teamService.addTeam(team, adminEmail);
    }

    @PutMapping("/updateTeam/{id}")
    public Team updateTeam(@RequestBody Team team , @PathVariable String id) {
        return teamService.updateTeam(team, id);
    }

    @GetMapping("/teammates")
    public List<User> getTeammates(){
        String currentEmail = SecurityContextUtil.getCurrentUserEmail();
        Team team = userRepository.findByEmail(currentEmail).get().getTeam();
        return userRepository.findByTeam(team);
    }

    @DeleteMapping("/teams/delete/{id}")
    public void deleteTeam(@PathVariable String id) {
        teamService.deleteTeam(id);
    }

    @DeleteMapping("/deleteMemberFromTeam/{id}/{email}")
    public void deleteMemberFromTeam(@PathVariable String id, @PathVariable String email) {
        teamService.deleteMemberFromTeam(id, email);
    }

    @GetMapping("/team/{id}")
    public Team getTeam(@PathVariable String id) {
        return teamService.findById(id);
    }

    @GetMapping("/teamsByOrganisationId/{organisationId}")
    public List<Team> getTeamsByOrganisationId(@PathVariable String organisationId) {
        return teamService.findByOrganisationId(organisationId);
    }

    @PostMapping("/invite")
    public void invite(@RequestBody List<String> userEmails, @RequestParam String orgId , @RequestParam String orgName , HttpServletRequest req) throws IOException {
        userService.sendInvitation(userEmails, req.getHeader("Accept-Language"), orgId, orgName);
    }
}
