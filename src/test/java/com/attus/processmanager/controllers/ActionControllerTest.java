package com.attus.processmanager.controllers;

import com.attus.processmanager.controller.ActionController;
import com.attus.processmanager.dto.ActionSaveRequest;
import com.attus.processmanager.dto.ActionUpdateRequest;
import com.attus.processmanager.dto.StakeholderUpdateRequest;
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
    void testBadRequestWhenCreate() throws Exception {
        Mockito.when(service.save(null)).thenThrow(new NullPointerException());

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testIllegalCreate() throws Exception {
        ActionSaveRequest actionSaveRequest = new ActionSaveRequest();
        Mockito.when(service.save(null)).thenThrow(new IllegalArgumentException());

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actionSaveRequest)))
                .andExpect(status().isBadRequest());
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
    void testUpdateWithInvalidId() throws Exception {
        StakeholderUpdateRequest updateRequest = new StakeholderUpdateRequest();

        Mockito.when(service.getById(-1L)).thenThrow(new IllegalArgumentException());

        mockMvc.perform(put(URL + "/" + -1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateActionNotFound() throws Exception {
        ActionUpdateRequest updateRequest = new ActionUpdateRequest();

        Mockito.when(service.getById(1L)).thenThrow(new NullPointerException());

        mockMvc.perform(put(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDelete() throws Exception {
        Mockito.doNothing().when(service).remove(action);
        mockMvc.perform(delete(URL + "/" + action.getId()))
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
        Mockito.when(service.getById(action.getId())).thenReturn(action);
        mockMvc.perform(get(URL + "/" + action.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void testFindByInvalidId() throws Exception {
        Mockito.when(service.getById(-1L)).thenThrow(NullPointerException.class);
        mockMvc.perform(get(URL + "/" + -1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testList() throws Exception {
        Mockito.when(service.list(action.getType())).thenReturn(Collections.singletonList(action));
        mockMvc.perform(get(URL + "?type=" + action.getType())).andExpect(status().isOk());
    }

    @Test
    void testListWithNullType() throws Exception {
        Mockito.when(service.list(action.getType())).thenReturn(Collections.singletonList(action));
        mockMvc.perform(get(URL)).andExpect(status().isOk());
    }

    @Test
    void testListWithInvalidType() throws Exception {
        Mockito.when(service.list(action.getType())).thenReturn(Collections.singletonList(action));
        mockMvc.perform(get(URL + "?type=INVALID_TYPE")).andExpect(status().isBadRequest());
    }

}
