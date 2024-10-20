package com.attus.processmanager.service;

import com.attus.processmanager.models.Action;
import com.attus.processmanager.models.LegalProcess;
import com.attus.processmanager.models.enums.ActionType;
import com.attus.processmanager.repository.ActionRepository;
import com.attus.processmanager.repository.LegalProcessRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ActionServiceTest {

    @InjectMocks
    private ActionService actionService;

    @Mock
    private LegalProcessService legalProcessService;

    @Mock
    private LegalProcessRepository legalProcessRepository;

    @Mock
    private ActionRepository actionRepository;

    private Action createAction() {
        return Action.builder()
                .id(1L)
                .description("test")
                .type(ActionType.AUDIENCIA)
                .registrationDate(LocalDateTime.now())
                .legalProcess(createLegalProcess())
                .build();

    }

    private LegalProcess createLegalProcess() {
        return LegalProcess.builder()
                .number(333L)
                .openingDate(LocalDateTime.now())
                .description("test")
                .id(1L)
                .build();
    }


    @Test
    @DisplayName("Teste: Listagem de ações")
    void testFindAll() {
        Action action = createAction();
        Mockito.when(actionRepository.findAll()).thenReturn(Collections.singletonList(action));
        List<Action> list = actionService.list(null);
        Assertions.assertEquals(1, list.size());
    }

    @Test
    @DisplayName("Teste: Lista ações pelo tipo")
    void testListByActionType() {
        Action action = createAction();
        Mockito.when(actionRepository.listBy(ActionType.AUDIENCIA)).thenReturn(Collections.singletonList(action));
        List<Action> list = actionService.list(ActionType.AUDIENCIA);
        Assertions.assertEquals(1, list.size());
    }

    @Test
    @DisplayName("Teste: Lista ações pelo processo")
    void testListByLegalProcess() {
        Action action = createAction();
        Mockito.when(actionRepository.findBy(action.getLegalProcess())).thenReturn(Collections.singletonList(action));
        List<Action> list = actionService.listBy(action.getLegalProcess());
        Assertions.assertEquals(1, list.size());
    }

    @Test
    @DisplayName("Teste: Inserção de ação")
    void testSave() {
        Action action = createAction();
        Mockito.when(actionRepository.save(Mockito.any(Action.class))).thenReturn(action);
        Mockito.when(legalProcessService.getById(action.getLegalProcess().getId())).thenReturn(action.getLegalProcess());


        Action savedAction = actionService.save(action);
        Assertions.assertNotNull(savedAction);
        Mockito.verify(actionRepository).save(action);
    }

    @Test
    @DisplayName("Teste: Inserir sem data de abertura")
    void testSaveWithoutOpeningDate() {
        Action action = createAction();
        action.setRegistrationDate(null);
        Mockito.when(actionRepository.save(Mockito.any(Action.class))).thenReturn(action);
        Mockito.when(legalProcessService.getById(action.getLegalProcess().getId())).thenReturn(action.getLegalProcess());

        Action savedAction = actionService.save(action);
        Assertions.assertNotNull(savedAction);
        Mockito.verify(actionRepository).save(action);
    }

    @Test
    @DisplayName("Teste: Buscar ação por id")
    void testGetById() {
        Action action = createAction();
        Mockito.when(actionRepository.findBy(action.getId())).thenReturn(action);
        Action findedAction = actionService.getById(action.getId());
        Assertions.assertNotNull(findedAction.getId());
    }

    @Test
    @DisplayName("Teste: Lança exceção quando não foi possivel extrair ação por id")
    void testThrowExceptionWhenTryExtractActionById() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> actionService.getById(-1L));
    }

    @Test
    @DisplayName("Teste: Remover ação")
    void testRemoveAction() {
        Action action = createAction();
        actionService.remove(action);
        Mockito.verify(actionRepository).delete(action);
    }
  
}