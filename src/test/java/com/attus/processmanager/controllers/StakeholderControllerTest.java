package com.attus.processmanager.controllers;

import com.attus.processmanager.controller.StakeholderController;
import com.attus.processmanager.dto.StakeholderSaveRequest;
import com.attus.processmanager.dto.StakeholderUpdateRequest;
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
    void testBadRequestWhenCreate() throws Exception {
        Mockito.when(service.save(null)).thenThrow(new NullPointerException());

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                        .andExpect(status().isBadRequest());
    }

    @Test
    void testReturnBadRequestWhenCpfAndCnpjAreBothProvided() throws Exception {
        StakeholderSaveRequest stakeholderRequest = new StakeholderSaveRequest();
        stakeholderRequest.setCpf("12345678901");
        stakeholderRequest.setCnpj("12345678000199");

        Mockito.when(service.save(Mockito.any(Stakeholder.class)))
                .thenThrow(new IllegalArgumentException());

        mockMvc.perform(post("/stakeholders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stakeholderRequest)))
                .andExpect(status().isBadRequest());
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
    void testUpdateWithInvalidId() throws Exception {
        StakeholderUpdateRequest updateRequest = new StakeholderUpdateRequest();

        Mockito.when(service.getById(-1L)).thenThrow(new IllegalArgumentException());

        mockMvc.perform(put(URL + "/" + -1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateWithStakeholderNotFound() throws Exception {
        StakeholderUpdateRequest updateRequest = new StakeholderUpdateRequest();

        Mockito.when(service.getById(1L)).thenThrow(new NullPointerException());

        mockMvc.perform(put(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDelete() throws Exception {
        Mockito.doNothing().when(service).remove(stakeholder);
        mockMvc.perform(delete(URL + "/" + stakeholder.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteWithInvalidId() throws Exception {
        Mockito.doThrow(new NullPointerException()).when(service).remove(null);

        mockMvc.perform(delete(URL + "/" + -1L))
                .andExpect(status().isBadRequest());
    }


    @Test
    void testFindById() throws Exception {
        Mockito.when(service.getById(stakeholder.getId())).thenReturn(stakeholder);
        mockMvc.perform(get(URL + "/" + stakeholder.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void testFindByIdWithException() throws Exception {
        Mockito.when(service.getById(-1L)).thenThrow(NullPointerException.class);
        mockMvc.perform(get(URL + "/" + -1L))
                .andExpect(status().isBadRequest());
    }


    @Test
    void testList() throws Exception {
        Mockito.when(service.list(stakeholder.getType())).thenReturn(Collections.singletonList(stakeholder));
        mockMvc.perform(get(URL + "?type=" + stakeholder.getType())).andExpect(status().isOk());
    }

    @Test
    void testListByNull() throws Exception {
        Mockito.when(service.list(null)).thenReturn(Collections.singletonList(stakeholder));
        mockMvc.perform(get(URL))
                .andExpect(status().isOk());
    }

    @Test
    void testListByInvalidType() throws Exception {
        mockMvc.perform(get(URL + "?type=akladhsf")).andExpect(status().isBadRequest());
    }

}
