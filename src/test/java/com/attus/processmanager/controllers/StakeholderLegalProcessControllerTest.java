package com.attus.processmanager.controllers;

import com.attus.processmanager.controller.StakeholderLegalProcessControler;
import com.attus.processmanager.models.LegalProcess;
import com.attus.processmanager.models.StakeholderLegalProcess;
import com.attus.processmanager.models.Stakeholder;
import com.attus.processmanager.models.enums.StakeholderType;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(controllers = StakeholderLegalProcessControler.class)
class StakeholderLegalProcessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StakeholderLegalProcessService service;

    private StakeholderLegalProcess stakeholderLegalProcess;

    private static final String URL = "/stakeholders-legal-process";

    @BeforeEach
    void setUp() {
        stakeholderLegalProcess = createStakeholderProcessController();
    }

    private StakeholderLegalProcess createStakeholderProcessController() {
        return StakeholderLegalProcess.builder()
                .id(1L)
                .stakeholder(createStakeholder())
                .legalProcess(createLegalProcess())
                .build();
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
        Mockito.when(service.save(Mockito.any(StakeholderLegalProcess.class))).thenReturn(stakeholderLegalProcess);
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stakeholderLegalProcess)))
                .andExpect(status().isCreated());
    }

    @Test
    void testDelete() throws Exception {
        Mockito.doNothing().when(service).remove(stakeholderLegalProcess);
        mockMvc.perform(delete(URL + "/" + stakeholderLegalProcess.getId()))
                .andExpect(status().isOk())
                .andReturn();
    }

}
