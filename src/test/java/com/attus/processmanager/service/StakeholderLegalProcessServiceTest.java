package com.attus.processmanager.service;

import com.attus.processmanager.models.LegalProcess;
import com.attus.processmanager.models.Stakeholder;
import com.attus.processmanager.models.StakeholderLegalProcess;
import com.attus.processmanager.models.enums.StakeholderType;
import com.attus.processmanager.repository.StakeholderLegalProcessRepository;
import com.attus.processmanager.repository.StakeholderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class StakeholderLegalProcessServiceTest {

    @InjectMocks
    private StakeholderLegalProcessService service;

    @Mock
    private StakeholderLegalProcessRepository repository;

    @Mock
    private StakeholderService stakeholderService;

    @Mock
    private StakeholderRepository stakeholderRepository;

    @Mock
    private ActionService actionService;


    @Test
    @DisplayName("Teste: Inserção de partes envolvidas processo")
    void testSave() {
        StakeholderLegalProcess stakeholderLegalProcess = createStakeholderLegalProcess();
        Mockito.when(repository.save(Mockito.any(StakeholderLegalProcess.class))).thenReturn(stakeholderLegalProcess);
        StakeholderLegalProcess savedStakeholderProcess = service.save(stakeholderLegalProcess);
        Assertions.assertNotNull(savedStakeholderProcess);
        Mockito.verify(repository).save(savedStakeholderProcess);
    }

    @Test
    @DisplayName("Teste: Lança exceção quando stakeholder legal process é null")
    void testSaveThrowsExceptionForNullStakeholderLegalProcess() {
        NullPointerException exception = Assertions.assertThrows(NullPointerException.class, () -> {
            service.save(null);
        });
        Assertions.assertEquals("The stakeholder legal process must not be null", exception.getMessage());
    }

    @Test
    @DisplayName("Teste: Remover partes envolvidas processo")
    void testRemove() {
        StakeholderLegalProcess stakeholderLegalProcess = createStakeholderLegalProcess();
        service.remove(stakeholderLegalProcess);
        Mockito.verify(repository).delete(stakeholderLegalProcess);
    }

    @Test
    @DisplayName("Teste: Buscar partes envolvidas processo por id")
    void testGetById() {
        StakeholderLegalProcess stakeholderLegalProcess = createStakeholderLegalProcess();
        Mockito.when(repository.findBy(stakeholderLegalProcess.getId())).thenReturn(stakeholderLegalProcess);
        StakeholderLegalProcess findedStakeholderLegalProcess = service.getById(stakeholderLegalProcess.getId());

        Assertions.assertNotNull(findedStakeholderLegalProcess.getId());
    }

    @Test
    @DisplayName("Teste: Lança exceção quando não foi possivel extrair partes envolvidas processo por id")
    void testThrowExceptionWhenTryExtractActionById() {
        Assertions.assertThrows(NullPointerException.class, () -> service.getById(-1L));
    }

    @Test
    @DisplayName("Teste: Listar todos os stakeholders envolvidos pelo processo")
    void testList() {
        StakeholderLegalProcess stakeholderLegalProcess = createStakeholderLegalProcess();
        Stakeholder stakeholder = stakeholderLegalProcess.getStakeholder();

        Mockito.when(repository.listBy(stakeholderLegalProcess.getLegalProcess())).thenReturn(List.of(stakeholder));

        List<Stakeholder> list = service.listBy(stakeholderLegalProcess.getLegalProcess());

        Assertions.assertEquals(1, list.size());
        Assertions.assertEquals(stakeholder, list.get(0));
    }

    @Test
    @DisplayName("Teste: Lançar exceção ao tentar listar processos legais com stakeholder nulo")
    void testListByStakeholderNull() {
        Stakeholder stakeholder = null;
        NullPointerException ex = Assertions.assertThrows(NullPointerException.class, () -> {
            service.listBy(stakeholder);
        });
        Assertions.assertEquals("The stakeholder must not be null", ex.getMessage());
    }


    private StakeholderLegalProcess createStakeholderLegalProcess() {
        return StakeholderLegalProcess.builder()
                .legalProcess(createLegalProcess())
                .stakeholder(createStakeholder())
                .id(1L)
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

    private Stakeholder createStakeholder() {
        return Stakeholder.builder()
                .id(1L)
                .email("test@tes.com")
                .phone("48484848")
                .type(StakeholderType.AUTOR)
                .build();
    }

}