package de.dhbw.trackingappbackend;

import de.dhbw.trackingappbackend.boundary.LocationControllerImpl;
import de.dhbw.trackingappbackend.control.AuthService;
import de.dhbw.trackingappbackend.control.CoordinateService;
import de.dhbw.trackingappbackend.entity.LocationRepository;
import de.dhbw.trackingappbackend.entity.UserRepository;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@AutoConfigureMockMvc
public class LocationControllerTest {

    @Autowired
    protected MockMvc mvc;

    @MockBean
    protected CoordinateService coordinateService;
    @MockBean
    protected AuthService authService;
    @MockBean
    protected UserRepository userRepository;
    @MockBean
    protected LocationRepository locationRepository;

    @InjectMocks
    protected LocationControllerImpl sut;

    /*  TODO fuck auth testing
    @Test
    public void testGetLocations() throws Exception {

        // Mock User
        UserDetailsImpl userDetails = new UserDetailsImpl("id", "mail", "pw");
        AppUser appUser = AppUser.builder().id("1").email("mail").password("pw").build();
        when(userRepository.findById("1")).thenReturn(Optional.of(appUser));

        // Mock Authentication
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/locations"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }*/
}
