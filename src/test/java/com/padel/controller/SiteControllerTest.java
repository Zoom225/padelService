package com.padel.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.padel.exception.GlobalExceptionHandler;
import com.padel.exception.ResourceNotFoundException;
import com.padel.model.entity.Site;
import com.padel.service.SiteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.LocalTime;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests SiteController")
class SiteControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private SiteService siteService;
    @InjectMocks
    private SiteController siteController;
    private Site site1;
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(siteController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        site1 = Site.builder()
                .id(1L).name("Brussels").address("Rue Test 1, Bruxelles")
                .numberOfCourts(4).openingTime(LocalTime.of(8, 0))
                .closingTime(LocalTime.of(22, 0)).active(true).build();
    }
    @Test
    @DisplayName("GET /sites - retourne liste vide")
    void getAllSites_empty() throws Exception {
        when(siteService.getAllActiveSites()).thenReturn(List.of());
        mockMvc.perform(get("/sites"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    @Test
    @DisplayName("GET /sites - retourne sites actifs")
    void getAllSites_withData() throws Exception {
        when(siteService.getAllActiveSites()).thenReturn(List.of(site1));
        mockMvc.perform(get("/sites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Brussels"));
    }
    @Test
    @DisplayName("GET /sites/{id} - site inexistant retourne 404")
    void getSiteById_notFound() throws Exception {
        when(siteService.getSiteById(99L))
                .thenThrow(new ResourceNotFoundException("Site non trouve"));
        mockMvc.perform(get("/sites/99"))
                .andExpect(status().isNotFound());
    }
    @Test
    @DisplayName("GET /sites/{id} - site trouve retourne 200")
    void getSiteById_found() throws Exception {
        when(siteService.getSiteById(1L)).thenReturn(site1);
        mockMvc.perform(get("/sites/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Brussels"));
    }
}
