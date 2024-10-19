package com.attus.processmanager.controllers;

import com.attus.processmanager.controller.ActionController;
import com.attus.processmanager.models.Action;
import com.attus.processmanager.models.LegalProcess;
import com.attus.processmanager.models.enums.ActionType;
import com.attus.processmanager.service.ActionService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(controllers = ActionController.class)
class ActionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ActionService service;

    private static final String URL = "/actions";

    private Action action;

    @BeforeEach
    void setUp() {
        action = createAction();
    }

    private Action createAction() {
        return Action.builder()
                .id(1L)
                .legalProcess(createLegalProcess())
                .type(ActionType.AUDIENCIA)
                .description("Test description")
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
    void testCreate() throws Exception {
        Mockito.when(service.save(Mockito.any(Action.class))).thenReturn(action);
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(action)))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdate() throws Exception {
        Action updatedAction = createAction();
        updatedAction.setType(ActionType.PETICAO);

        Mockito.when(service.getById(action.getId())).thenReturn(action);
        Mockito.when(service.save(Mockito.any(Action.class))).thenReturn(updatedAction);

        mockMvc.perform(put(URL + "/" + action.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAction)))
                .andExpect(status().isOk());
    }

    @Test
    void testDelete() throws Exception {
        Mockito.doNothing().when(service).remove(action);
        mockMvc.perform(delete(URL + "/" + action.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void testFindById() throws Exception {
        Mockito.when(service.getById(action.getId())).thenReturn(action);
        mockMvc.perform(get(URL + "/" + action.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void testList() throws Exception {
        Mockito.when(service.list(action.getType())).thenReturn(Collections.singletonList(action));
        mockMvc.perform(get(URL + "?type=" + action.getType())).andExpect(status().isOk());
    }

}
