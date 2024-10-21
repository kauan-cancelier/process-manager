package com.attus.processmanager.controllers;

import com.attus.processmanager.controller.LegalProcessController;
import com.attus.processmanager.dto.ActionUpdateRequest;
import com.attus.processmanager.dto.LegalProcessUpdateRequest;
import com.attus.processmanager.models.LegalProcess;
import com.attus.processmanager.models.enums.LegalProcessStatus;
import com.attus.processmanager.service.ActionService;
import com.attus.processmanager.service.LegalProcessService;
import com.attus.processmanager.service.StakeholderLegalProcessService;
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

import java.time.LocalDateTime;
import java.util.Collections;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(controllers = LegalProcessController.class)
class LegalProcessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LegalProcessService service;

    @MockBean
    private StakeholderLegalProcessService stakeholderLegalProcessService;

    @MockBean
    private ActionService actionService;

    private LegalProcess legalProcess;
    
    private static final String URL = "/legal-processes";

    @BeforeEach
    void setUp() {
        legalProcess =createLegalProcess();
    }

    private LegalProcess createLegalProcess() {
        return LegalProcess.builder()
                .id(1L)
                .openingDate(LocalDateTime.now())
                .description("Test Process")
                .number(123L)
                .build();
    }

    @Test
    void testSave() throws Exception {
        Mockito.when(service.save(Mockito.any(LegalProcess.class))).thenReturn(legalProcess);
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(legalProcess)))
                        .andExpect(status().isCreated());
    }

    @Test
    void testUpdate() throws Exception {
        LegalProcessUpdateRequest updatedLegalProcess = new LegalProcessUpdateRequest();
        updatedLegalProcess.setStatus("ATIVO");
        updatedLegalProcess.setId(1L);
        updatedLegalProcess.setDescription("Updated Legal Process");
        updatedLegalProcess.setNumber(123L);

        Mockito.when(service.getById(legalProcess.getId())).thenReturn(legalProcess);
        Mockito.when(service.save(Mockito.any(LegalProcess.class))).thenReturn(legalProcess);

        mockMvc.perform(put(URL + "/" + legalProcess.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedLegalProcess)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateProcessNotFound() throws Exception {
        ActionUpdateRequest updateRequest = new ActionUpdateRequest();

        Mockito.when(service.getById(1L)).thenThrow(new NullPointerException());

        mockMvc.perform(put(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateWithInvalidType() throws Exception {
        LegalProcessUpdateRequest updatedLegalProcess = new LegalProcessUpdateRequest();
        updatedLegalProcess.setStatus("INVALID_STATUS");
        updatedLegalProcess.setId(1L);
        updatedLegalProcess.setDescription("Updated Legal Process");
        updatedLegalProcess.setNumber(123L);

        Mockito.when(service.getById(legalProcess.getId())).thenReturn(legalProcess);
        Mockito.when(service.save(Mockito.any(LegalProcess.class))).thenReturn(createLegalProcess());

        mockMvc.perform(put(URL + "/" + legalProcess.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedLegalProcess)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testInsertSameLegalProcessNumber() throws Exception {
       Mockito.when(service.save(Mockito.any(LegalProcess.class)))
                .thenReturn(legalProcess)
                .thenThrow(new IllegalArgumentException("Processo com este número já existe"));

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(legalProcess)))
                .andExpect(status().isCreated());

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(legalProcess)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDelete() throws Exception {
        Mockito.doNothing().when(service).remove(legalProcess);
        mockMvc.perform(delete(URL + "/" + legalProcess.getId()))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void testDeleteWithInvalidId() throws Exception {
        Mockito.doThrow(new NullPointerException()).when(service).remove(null);

        mockMvc.perform(delete(URL + "/" + -1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testListByStatus() throws Exception {
        Mockito.when(service.list(LegalProcessStatus.ATIVO)).thenReturn(Collections.singletonList(legalProcess));
        mockMvc.perform(get(URL + "?status=ATIVO", LegalProcessStatus.ATIVO))
                .andExpect(status().isOk());
    }

    @Test
    void testListWithNullType() throws Exception {
        Mockito.when(service.list(legalProcess.getStatus())).thenReturn(Collections.singletonList(legalProcess));
        mockMvc.perform(get(URL)).andExpect(status().isOk());
    }

    @Test
    void testListWithInvalidType() throws Exception {
        Mockito.when(service.list(legalProcess.getStatus())).thenReturn(Collections.singletonList(legalProcess));
        mockMvc.perform(get(URL + "?status=INVALID_TYPE")).andExpect(status().isBadRequest());
    }

    @Test
    void testInactivateProcess() throws Exception {
        Mockito.when(service.inactivateProcess(legalProcess.getId())).thenReturn(legalProcess);
        mockMvc.perform(put(URL + "/" + legalProcess.getId() + "/inactivate"))
                .andExpect(status().isOk());
    }

    @Test
    void testInactivateProcessWithInvalidId() throws Exception {
        Long processId = 1L;
        Mockito.when(service.inactivateProcess(processId)).thenThrow(new NullPointerException());
        mockMvc.perform(put(URL + "/" + processId + "/inactivate"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testActivateProcess() throws Exception {
        Mockito.when(service.inactivateProcess(legalProcess.getId())).thenReturn(legalProcess);
        mockMvc.perform(put(URL + "/" + legalProcess.getId() + "/activate"))
                .andExpect(status().isOk());
    }

    @Test
    void testActivateProcessWithNotFound() throws Exception {
        Long processId = 1L;
        Mockito.when(service.activateProcess(processId)).thenThrow(new NullPointerException());
        mockMvc.perform(put(URL + "/" + processId + "/activate"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetById() throws Exception {
        Mockito.when(service.getById(legalProcess.getId())).thenReturn(legalProcess);
        mockMvc.perform(get(URL + "/" + legalProcess.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void testFindByInvalidId() throws Exception {
        Mockito.when(service.getById(-1L)).thenThrow(NullPointerException.class);
        mockMvc.perform(get(URL + "/" + -1L))
                .andExpect(status().isBadRequest());
    }

}