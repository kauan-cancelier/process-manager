package com.attus.processmanager.controllers;

import com.attus.processmanager.controller.StakeholderController;
import com.attus.processmanager.models.Stakeholder;
import com.attus.processmanager.models.enums.StakeholderType;
import com.attus.processmanager.service.StakeholderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(controllers = StakeholderController.class)
class StakeholderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StakeholderService service;

    private static final String URL = "/stakeholders";

    private Stakeholder stakeholder;

    @BeforeEach
    void setUp() {
        stakeholder = createStakeholder();
    }

    private Stakeholder createStakeholder() {
        return Stakeholder.builder()
                .id(1L)
                .name("Stakeholder 1")
                .cnpj("12345678910234")
                .type(StakeholderType.AUTOR)
                .email("gmail@gmail.com")
                .phone("888888888")
                .build();
    }

    @Test
    void testCreate() throws Exception {
        Mockito.when(service.save(Mockito.any(Stakeholder.class))).thenReturn(stakeholder);
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stakeholder)))
                        .andExpect(status().isCreated());
    }

    @Test
    void testUpdate() throws Exception {
        Stakeholder updatedStakeholder = createStakeholder();
        updatedStakeholder.setName("Updated Stakeholder");

        Mockito.when(service.getById(stakeholder.getId())).thenReturn(stakeholder);
        Mockito.when(service.save(Mockito.any(Stakeholder.class))).thenReturn(updatedStakeholder);

        mockMvc.perform(put(URL + "/" + stakeholder.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedStakeholder)))
                        .andExpect(status().isOk());
    }

    @Test
    void testDelete() throws Exception {
        Mockito.doNothing().when(service).remove(stakeholder);
        mockMvc.perform(delete(URL + "/" + stakeholder.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void testFindById() throws Exception {
        Mockito.when(service.getById(stakeholder.getId())).thenReturn(stakeholder);
        mockMvc.perform(get(URL + "/" + stakeholder.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void testList() throws Exception {
        Mockito.when(service.list(stakeholder.getType())).thenReturn(Collections.singletonList(stakeholder));
        mockMvc.perform(get(URL + "?type=" + stakeholder.getType())).andExpect(status().isOk());
    }

}
