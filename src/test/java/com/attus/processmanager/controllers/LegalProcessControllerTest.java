package com.attus.processmanager.controllers;

import com.attus.processmanager.controller.LegalProcessController;
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
    
    private final String url = "/legal-processes";

    @BeforeEach
    void setUp() {
        legalProcess = new LegalProcess();
        legalProcess.setId(1L);
        legalProcess.setNumber(123L);
        legalProcess.setDescription("Test Process");
    }

    @Test
    void testSave() throws Exception {
        Mockito.when(service.save(Mockito.any(LegalProcess.class))).thenReturn(legalProcess);
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(legalProcess)))
                        .andExpect(status().isCreated());
    }

    @Test
    void testInsertSameLegalProcessNumber() throws Exception {
       Mockito.when(service.save(Mockito.any(LegalProcess.class)))
                .thenReturn(legalProcess)
                .thenThrow(new IllegalArgumentException("Processo com este número já existe"));

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(legalProcess)))
                .andExpect(status().isCreated());

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(legalProcess)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDelete() throws Exception {
        Mockito.doNothing().when(service).remove(legalProcess);
        mockMvc.perform(delete("/legal-processes/" + legalProcess.getId()))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void testFindAll() throws Exception {
        Mockito.when(service.list(LegalProcessStatus.ATIVO)).thenReturn(Collections.singletonList(legalProcess));
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void testFindByStatusAtivo() throws Exception {
        Mockito.when(service.list(LegalProcessStatus.ATIVO)).thenReturn(Collections.singletonList(legalProcess));
        mockMvc.perform(get("/legal-processes?legalProcessStatus=", LegalProcessStatus.ATIVO))
                .andExpect(status().isOk());
    }

    @Test
    void testInactivateProcess() throws Exception {
        Mockito.when(service.inactivateProcess(legalProcess.getId())).thenReturn(legalProcess);
        mockMvc.perform(put("/legal-processes/" + legalProcess.getId() + "/inactivate"))
                .andExpect(status().isOk());
    }

    @Test
    void testActivateProcess() throws Exception {
        Mockito.when(service.inactivateProcess(legalProcess.getId())).thenReturn(legalProcess);
        mockMvc.perform(put("/legal-processes/" + legalProcess.getId() + "/activate"))
                .andExpect(status().isOk());
    }

}