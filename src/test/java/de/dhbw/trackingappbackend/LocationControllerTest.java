package de.dhbw.trackingappbackend;

import de.dhbw.trackingappbackend.boundary.LocationController;
import de.dhbw.trackingappbackend.control.AuthService;
import de.dhbw.trackingappbackend.control.CoordinateService;
import de.dhbw.trackingappbackend.entity.AppUser;
import de.dhbw.trackingappbackend.entity.LocationRepository;
import de.dhbw.trackingappbackend.entity.UserRepository;
import de.dhbw.trackingappbackend.security.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc
public class LocationControllerTest {

    @Autowired
    protected MockMvc mvc;

    @MockBean
    protected AuthService authService;
    @MockBean
    protected CoordinateService coordinateService;
    @MockBean
    protected UserRepository userRepository;
    @MockBean
    protected LocationRepository locationRepository;

    @InjectMocks
    protected LocationController sut;

    @Test
    public void testGetLocations() throws Exception {

        // Mock User
        UserDetailsImpl userDetails = new UserDetailsImpl("id", "mail", "pw");
        AppUser appUser = AppUser.builder().id("1").email("mail").password("pw").build();
        when(userRepository.findById("1")).thenReturn(Optional.of(appUser));

        // Mock Authentication
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/locations"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }
}
