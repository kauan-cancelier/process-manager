package com.attus.processmanager.service;

import com.attus.processmanager.models.LegalProcess;
import com.attus.processmanager.models.enums.LegalProcessStatus;
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
import java.util.List;

@ExtendWith(MockitoExtension.class)
class LegalProcessServiceTest {

    @InjectMocks
    private LegalProcessService legalProcessService;

    @Mock
    private LegalProcessRepository legalProcessRepository;

    private LegalProcess createLegalProcess() {
        return LegalProcess.builder()
                .number(333L)
                .openingDate(LocalDateTime.now())
                .description("test")
                .id(1L)
                .build();
    }

    @Test
    @DisplayName("Teste: Inserir processo jurídico")
    void testSave() {
        LegalProcess legalProcess = createLegalProcess();
        Mockito.when(legalProcessRepository.save(Mockito.any(LegalProcess.class))).thenReturn(legalProcess);
        LegalProcess savedProcess = legalProcessService.save(legalProcess);

        Assertions.assertNotNull(savedProcess);
        Mockito.verify(legalProcessRepository).save(legalProcess);
    }

    @Test
    @DisplayName("Teste: Lança exceção ao tentar salvar um processo com o mesmo número")
    void testWithSameNumberSave() {
        LegalProcess legalProcess = createLegalProcess();
        legalProcess.setId(null);
        Mockito.when(legalProcessRepository.existsByNumber(legalProcess.getNumber())).thenReturn(true);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            legalProcessService.save(legalProcess);
        });

        Mockito.verify(legalProcessRepository, Mockito.never()).save(Mockito.any(LegalProcess.class));
    }

    @Test
    @DisplayName("Teste: Setar status ATIVO quando não especificado ao criar")
    void testSaveWithoutStatus() {
        LegalProcess legalProcess = createLegalProcess();
        legalProcess.setId(null);
        legalProcess.setStatus(null);

        Mockito.when(legalProcessRepository.save(Mockito.any(LegalProcess.class))).thenReturn(legalProcess);

        LegalProcess savedProcess = legalProcessService.save(legalProcess);

        Assertions.assertNotNull(savedProcess);
        Assertions.assertEquals(LegalProcessStatus.ATIVO, savedProcess.getStatus());
        Mockito.verify(legalProcessRepository).save(legalProcess);
    }



    @Test
    @DisplayName("Teste: Buscar processo jurídico por id")
    void testGetById() {
        LegalProcess legalProcess = createLegalProcess();
        Mockito.when(legalProcessRepository.findBy(legalProcess.getId())).thenReturn(legalProcess);
        LegalProcess findedProcess = legalProcessService.getById(legalProcess.getId());

        Assertions.assertNotNull(findedProcess.getId());
    }

    @Test
    @DisplayName("Teste: Lança exceção quando não foi possivel extrair processo jurídico por id")
    void testThrowExceptionWhenTryExtractActionById() {
        Assertions.assertThrows(NullPointerException.class, () -> legalProcessService.getById(-1L));
    }

    @Test
    @DisplayName("Teste: Remover processo jurídico")
    void testRemoveAction() {
        LegalProcess legalProcess = createLegalProcess();
        legalProcessService.remove(legalProcess);

        Mockito.verify(legalProcessRepository).delete(legalProcess);
    }

    @Test
    @DisplayName("Teste: Listar todos os processos jurídicos")
    void testList() {
        LegalProcess legalProcess = createLegalProcess();
        Mockito.when(legalProcessRepository.findAll()).thenReturn(List.of(legalProcess));
        List<LegalProcess> list = legalProcessService.list(null);

        Assertions.assertEquals(1, list.size());
    }

    @Test
    @DisplayName("Teste: Listar processos jurídicos pelo status")
    void testListByProcessStatus() {
        LegalProcess legalProcess = createLegalProcess();
        legalProcess.setStatus(LegalProcessStatus.ARQUIVADO);

        Mockito.when(legalProcessRepository.listBy(LegalProcessStatus.ARQUIVADO)).thenReturn(List.of(legalProcess));
        List<LegalProcess> list = legalProcessService.list(LegalProcessStatus.ARQUIVADO);

        Assertions.assertEquals(1, list.size());
    }

    @Test
    @DisplayName("Teste: Inativa processo ")
    void testInactivateProcess() {
        LegalProcess legalProcess = createLegalProcess();
        legalProcess.setStatus(LegalProcessStatus.ATIVO);

        Mockito.when(legalProcessRepository.findBy(legalProcess.getId()))
                .thenReturn(legalProcess);

        Mockito.when(legalProcessRepository.save(Mockito.any(LegalProcess.class)))
                .thenReturn(legalProcess);

        LegalProcess inactivatedProcess = legalProcessService.inactivateProcess(legalProcess.getId());

        Assertions.assertEquals(LegalProcessStatus.ARQUIVADO, inactivatedProcess.getStatus());
        Mockito.verify(legalProcessRepository).save(legalProcess);
    }


    @Test
    @DisplayName("Teste: Ativa processo ")
    void testActivateProcess() {
        LegalProcess legalProcess = createLegalProcess();
        legalProcess.setStatus(LegalProcessStatus.ARQUIVADO);

        Mockito.when(legalProcessRepository.findBy(legalProcess.getId()))
                .thenReturn(legalProcess);

        Mockito.when(legalProcessRepository.save(Mockito.any(LegalProcess.class)))
                .thenReturn(legalProcess);

        LegalProcess activatedProcess = legalProcessService.activateProcess(legalProcess.getId());

        Assertions.assertEquals(LegalProcessStatus.ATIVO, activatedProcess.getStatus());
        Mockito.verify(legalProcessRepository).save(legalProcess);
    }

    @Test
    @DisplayName("Teste: Não altera status caso seja o mesmo")
    void testChangeStatusToTheSame() {
        LegalProcess legalProcess = createLegalProcess();
        legalProcess.setStatus(LegalProcessStatus.ATIVO);

        Mockito.when(legalProcessRepository.findBy(legalProcess.getId()))
                .thenReturn(legalProcess);

        LegalProcess activatedProcess = legalProcessService.activateProcess(legalProcess.getId());
        Assertions.assertEquals(LegalProcessStatus.ATIVO, activatedProcess.getStatus());

        Mockito.verify(legalProcessRepository, Mockito.never()).save(Mockito.any(LegalProcess.class));
    }

}